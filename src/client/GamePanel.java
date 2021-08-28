package client;
import com.google.gson.Gson;
import message.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.util.HashMap;


public class GamePanel extends JPanel {
    public static final int LATTICE_SIDE    = 40;
    public static final int SIDE_NUM        = ChessBoard.SIDE_NUM;
    public static final int BLACK_TYPE      = ChessBoard.BLACK_TYPE;
    public static final int WHITE_TYPE      = ChessBoard.WHITE_TYPE;
    public static final int BLANK           = ChessBoard.BLANK;

    private int panelSide = (SIDE_NUM+1) * LATTICE_SIDE;;
    public int color = BLANK;
    private ChessBoard chessBoard;
    public Client client;
    private Gson gson = new Gson();
    private int cID = 0;

    private int waiting_color;
//    private boolean isAvailable;

    public GamePanel() {
        initPanel();
    }

    private int getCID() {
        return cID ++;
    }

    private void initPanel() {
        this.chessBoard = ChessBoard.newInstance();
        this.setFocusable(true);
        initMouseListener();
        initKeyBoardListener();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawChessBoard(g);
        drawAllChess();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void initSession(int color) {
        if (color != BLACK_TYPE && color != WHITE_TYPE) {
            throw new RuntimeException("颜色匹配错误");
        }
        this.color = color;
        this.waiting_color = BLACK_TYPE;
        repaint();
    }

    public void dropChess(ChessBoard.ChessStep step) {
//        if (dropChessAvailable(step)) {
        if (true) {
            this.chessBoard.dropChess(step);
            drawChess(step);
            if (this.waiting_color == BLACK_TYPE) {
                this.waiting_color = WHITE_TYPE;
            } else if (this.waiting_color == WHITE_TYPE) {
                this.waiting_color = BLACK_TYPE;
            }
        }
    }

//    public boolean checkAvailable(int index_x, int index_y) {
//        return chessBoard.checkAvailable(index_x, index_y);
//    }

    private void drawChessBoard(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(0, 0, panelSide, panelSide);
        g.setColor(Color.black);
        for(int i = 1; i <= SIDE_NUM; i++) {
            g.drawLine(LATTICE_SIDE, i*LATTICE_SIDE, panelSide-LATTICE_SIDE, i*LATTICE_SIDE);
            g.drawLine(i*LATTICE_SIDE, LATTICE_SIDE, i*LATTICE_SIDE, panelSide-LATTICE_SIDE);
        }
    }

    private void initMouseListener() {
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
//                System.out.println("----------------------------------------");
//                System.out.println("This color  " + color);
//                System.out.println("Waiting color  " + waiting_color);
//                System.out.println("++++++++++++++++++++++++++++++++++++++++");
                if (client.isMatched) {
                    Point mousePoint = getMousePosition();
                    if (mousePoint.x > -1 && mousePoint.y > -1 && mousePoint.x < panelSide && mousePoint.y < panelSide) {
                        int index_x = (mousePoint.x + (LATTICE_SIDE >> 1)) / LATTICE_SIDE - 1;
                        int index_y = (mousePoint.y + (LATTICE_SIDE >> 1)) / LATTICE_SIDE - 1;
//                        if (index_x < 0 || index_y < 0 || index_x >= SIDE_NUM || index_y >= SIDE_NUM) {
//                            return;
//                        }
                        if (checkDropChess(index_x, index_y)) {
                            ChessBoard.ChessStep step = new ChessBoard.ChessStep(color, new ChessBoard.ChessPoint(index_x, index_y));
                            dropChess(step);
                            int cID = getCID();
                            Message msg = new Message(
                                    "ClientCommand",
                                    "DropChess",
                                    new HashMap<String, String>() {
                                        {
                                            put("cid", String.valueOf(cID));
                                            put("x", String.valueOf(step.getChessPoint().getX()));
                                            put("y", String.valueOf(step.getChessPoint().getY()));
                                            put("color", String.valueOf(color));
                                        }
                                    });
                            client.sendMessage(gson.toJson(msg));
                        }
                    }
                }
//                System.out.println("This color  " + color);
//                System.out.println("Waiting color  " + waiting_color);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

//    private boolean dropChessAvailable(ChessBoard.ChessStep step) {
//        int color = step.getChessType();
//        return color == waiting_color;
//    }

    private void initKeyBoardListener() {
        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyChar() == 'r') {
                    System.out.println("R");
                }
                if(e.getKeyChar() == 'a') {
                    System.out.println("A");
                }
            }
        });
    }

    private void drawAllChess() {
        for(ChessBoard.ChessStep step : this.chessBoard.getHistorySteps()) {
            drawChess(step);
        }
    }

    private void drawChess(ChessBoard.ChessStep step) {
        int color = step.getChessType();
        int index_x = step.getChessPoint().getX();
        int index_y = step.getChessPoint().getY();
        if ((index_x+1) * (index_y+1) * (SIDE_NUM-index_x) * (SIDE_NUM-index_y) == 0) {
            return;
        }
        Graphics g = this.getGraphics();
        if(color == BLACK_TYPE) {
            g.setColor(Color.black);
        } else if(color == WHITE_TYPE) {
            g.setColor(Color.white);
        } else {
            throw new RuntimeException();
        }
        int x = index_x * LATTICE_SIDE + (LATTICE_SIDE >> 1);
        int y = index_y * LATTICE_SIDE + (LATTICE_SIDE >> 1);
        g.fillOval(x, y, LATTICE_SIDE, LATTICE_SIDE);
    }

    private void undoDrawChess(int index_x, int index_y) {
        int x = index_x * LATTICE_SIDE + (LATTICE_SIDE >> 1);
        int y = index_y * LATTICE_SIDE + (LATTICE_SIDE >> 1);
        Graphics g = this.getGraphics();
        g.setColor(Color.orange);
        g.fillOval(x, y, LATTICE_SIDE, LATTICE_SIDE);
        g.setColor(Color.black);
        if(index_x == 0) {
            g.drawLine(x + (LATTICE_SIDE >> 1), y + (LATTICE_SIDE >> 1),x + LATTICE_SIDE, y + (LATTICE_SIDE >> 1));
        } else if(index_x == 14) {
            g.drawLine(x - (LATTICE_SIDE >> 1), y + (LATTICE_SIDE >> 1),x + (LATTICE_SIDE >> 1), y + (LATTICE_SIDE >> 1));
        } else {
            g.drawLine(x, y + (LATTICE_SIDE >> 1), x + LATTICE_SIDE, y + (LATTICE_SIDE >> 1));
        }
        if(index_y == 0) {
            g.drawLine(x + (LATTICE_SIDE >> 1), y + (LATTICE_SIDE >> 1), x + (LATTICE_SIDE >> 1), y + LATTICE_SIDE);
        } else if(index_y == 14) {
            g.drawLine(x + (LATTICE_SIDE >> 1), y, x + (LATTICE_SIDE >> 1), y + (LATTICE_SIDE >> 1));
        } else {
            g.drawLine(x + (LATTICE_SIDE >> 1), y, x + (LATTICE_SIDE >> 1), y + LATTICE_SIDE);
        }
    }

    private boolean checkCommand(int cID) {
        BufferedReader br = client.getBr();
        try {
            String line = br.readLine();
            Message msg = gson.fromJson(line, Message.class);
            if (msg.type.equals("ClientReply")) {
                int cid = Integer.parseInt(msg.info.get("cid"));
                if (cid == cID) {
                    return msg.title.equals("DealSuccessfully");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkDropChess(int index_x, int index_y) {
        return index_x >= 0 && index_y >= 0 && index_x < SIDE_NUM && index_y < SIDE_NUM && color == waiting_color;
    }
}
