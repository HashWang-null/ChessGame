package client;

import overt.ChessStep;

import javax.swing.*;
import java.net.Socket;
import java.net.SocketException;

public class Kernel {
    public static final int BLACK_TYPE      = ChessBoard.BLACK_TYPE;
    public static final int WHITE_TYPE      = ChessBoard.WHITE_TYPE;
    public static final int BLANK           = ChessBoard.BLANK;

    //注册组件
    private MainFrame mainFrame;
    private GamePanel gamePanel;
    private SouthPanel southPanel;
    private EastPanel eastPanel;
    //自己的组件
    private Socket socket;
    private ChessBoard chessBoard;
    private Sender sender;
    private Recipient recipient;
    //状态标志
    private boolean stand_alone = false;        //单机模式
    private boolean isMatch     = false;        //是否已经匹配
    private boolean GP_access   = false;        //GamePanel是否可以点击
    private boolean is_host     = false;        //是否为Host
    private boolean is_waiting  = false;        //点击显示应该等待
    private int     color       = BLANK;        //棋的颜色
    private int waiting_color   = BLANK;        //当前等待的棋的颜色
//    private boolean EP_status;      //EastPanel(主控面板)状态

    public Kernel() {
        this.chessBoard = new ChessBoard();
    }

    //-------------------------------set方法-------------------------------------
    public void setSouthPanel(SouthPanel southPanel) {
        this.southPanel = southPanel;
    }

    public void setEastPanel(EastPanel eastPanel) {
        this.eastPanel = eastPanel;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    //--------------------------------------公共方法----------------------------

    //连接到服务器
    public void connectionToServer() {
        try {
            socket = new Socket("101.132.73.96", 9876);
            this.sender = new Sender(socket);
            this.recipient = new Recipient(socket, this);
            recipient.start();
        } catch (SocketException se) {
            showServerError("无法连接到服务器");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //请求匹配
    public void request_partner() {
        if (this.sender == null || this.socket.isClosed()) {
            throw new RuntimeException("Socket未初始化!" + this.socket.isClosed());
        }
        setMatchingPartnerStatus();
        sender.sendRequestPartner();
    }

    //等待匹配
    public void waiting_partner() {
        setMatchingPartnerStatus();
    }

    //匹配对手
    public void match_partner(String partner_info, boolean is_host) {
        setPartnerStatus(partner_info);
        this.is_host = is_host;
        if (is_host) {
            //本客户端为Host
            initSession(BLACK_TYPE);
            sender.sendStartSession(WHITE_TYPE);
        } else {
        }
    }

    //设置新开局, 当本客户端不为Host时
    public void receiveNewSession(int color) {
        if (!is_host) {
            initSession(color);
        } else {
            System.err.println("主持人收到了开局信息");
        }
    }

    //本客户端落子
    public void s_dropChess(int index_x, int index_y) {
        //单机模式
        if (stand_alone) {
            if (!is_host) {
                is_host = true;
                this.color = BLACK_TYPE;
                this.waiting_color = BLACK_TYPE;
            }
            ChessStep step = new ChessStep(color, index_x, index_y);
            if (chessBoard.isAvailable(index_x, index_y)) {
                chessBoard.dropChess(step);
                gamePanel.drawChess(step);
//                changeWaitingColor();
                this.color = getChangeColor(color);
            }
            boolean isWin = chessBoard.isWin(step);
            if (isWin) {
                showWinnerDialog(step.getColor());
                startAnotherSession(step.getColor());
            }
            return;
        }
        //联机模式
        if (is_waiting) {
            showWaitingDialog();
        } else if (GP_access && checkDropChess(color, index_x, index_y)) {
            ChessStep step = new ChessStep(color, index_x, index_y);
            chessBoard.dropChess(step);
            gamePanel.drawChess(step);
            changeWaitingColor();
            sender.sendDropChess(color, index_x, index_y);

            boolean isWin = chessBoard.isWin(step);
            if (isWin) {
                startAnotherSession(step.getColor());
            }
        }
    }

    //对方客户端落子
    public void p_dropChess(int color, int index_x, int index_y) {
        if (color < 1 || color > 2 || color == this.color) {
            throw new RuntimeException("对方Color错误：" + color);
        }
        if (checkDropChess(color, index_x, index_y)) {
            ChessStep step = new ChessStep(color, index_x, index_y);
            chessBoard.dropChess(step);
            gamePanel.drawChess(step);
            changeWaitingColor();

            boolean isWin = chessBoard.isWin(step);
            if (isWin) {
                startAnotherSession(step.getColor());
            }
        }
    }

    //撤销画棋子,ifSend 为True发送消息，否则不发送
    public void undoDropChess(Boolean ifSend) {
        ChessStep step = chessBoard.undoChessDrop();
        if (step != null) {
            gamePanel.undoDrawChess(step.getX(), step.getY());
            changeWaitingColor();
            if (ifSend) {
                sender.sendUndoChess();
            }
        }
    }

    public void doChat(String line, boolean ifSend) {
        if (ifSend) {
            southPanel.setTextArea("我方：" + line);
            sender.sendChat(line);
        } else {
            southPanel.setTextArea("对方：" + line);
        }
    }

    //----------------------------------弹出对话框-------------------------------

    public void showServerError(String info) {
        if (mainFrame == null) {
            throw new RuntimeException("未初始化!");
        }
        Object[] options = {"重新连接", "退出"};
        int option = JOptionPane.showOptionDialog(
                mainFrame,
                info,
                "服务器连接错误",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        if (option == 0) {
            connectionToServer();
        } else {
            System.exit(0);
        }
    }

    public void showPartnerError(String info) {
        if (mainFrame == null) {
            throw new RuntimeException("未初始化!");
        }
        Object[] options = {"重新匹配", "退出"};
        int option = JOptionPane.showOptionDialog(mainFrame,
                info,
                "警告",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        if (option == 0) {
            request_partner();
        } else {
            System.exit(0);
        }
    }

    public void showWinnerDialog(int color) {
        String info = null;
        if (color == BLACK_TYPE) {
            info = "黑棋赢了！";
        } else if (color == WHITE_TYPE) {
            info = "白棋赢了!";
        } else {
            throw new RuntimeException("颜色错误");
        }
        JOptionPane.showMessageDialog(mainFrame, info, "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWaitingDialog() {
        JOptionPane.showMessageDialog(mainFrame, "等待对方", "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    //----------------------------------私有方法------------------------------------

    //检测是否能进行落子操作
    private boolean checkDropChess(int color, int index_x, int index_y) {
        chessBoard.printHistorySteps();
        return chessBoard.isAvailable(index_x, index_y) && waiting_color == color;
    }

    //更改Waiting_Color
    private void changeWaitingColor() {
        waiting_color = getChangeColor(waiting_color);
    }

    //获取相反的color
    private int getChangeColor(int color) {
        if (color == BLACK_TYPE) {
            return WHITE_TYPE;
        } else if (color == WHITE_TYPE) {
            return BLACK_TYPE;
        } else {
            throw new RuntimeException("颜色错误");
        }
    }

    //初始化战局
    private void initSession(int color) {
        this.is_waiting = false;
        this.color = color;
        this.waiting_color = BLACK_TYPE;
        this.chessBoard.refreshChessBoard();
        this.gamePanel.repaint();
        this.GP_access = true;
    }

    //开始新的一局
    private void startAnotherSession(int color) {
        if (stand_alone) {
            initSession(BLACK_TYPE);
            return;
        }
        this.is_waiting = true;
        this.GP_access = false;
        showWinnerDialog(color);
        this.chessBoard.refreshChessBoard();
        this.gamePanel.repaint();
        if (is_host) {
            sender.sendStartSession(this.color);
            initSession(getChangeColor(color));
        } else {
        }
    }

    // -----------更改状态---------

    //未匹配状态
    private void setNoPartnerStatus() {
        this.isMatch    = false;
        this.GP_access  = false;
        eastPanel.setNoPartnerStatus();
    }
    //匹配中状态
    private void setMatchingPartnerStatus() {
        this.isMatch    = false;
        this.GP_access  = false;
        eastPanel.setMatchingPartnerStatus();
    }
    //已匹配状态
    private void setPartnerStatus(String partner_info) {
        this.isMatch    = true;
        this.GP_access  = true;
        eastPanel.setPartnerStatus(partner_info);
    }

}
