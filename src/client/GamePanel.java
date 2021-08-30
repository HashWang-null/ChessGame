package client;


import overt.ChessStep;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GamePanel extends JPanel {
    public static final int SIDE_NUM        = ChessBoard.SIDE_NUM;
    public static final int BLACK_TYPE      = ChessBoard.BLACK_TYPE;
    public static final int WHITE_TYPE      = ChessBoard.WHITE_TYPE;
    public static final int BLANK           = ChessBoard.BLANK;
    public static final int LATTICE_SIDE    = 40;
    public static final int panelSide       = (SIDE_NUM+1) * LATTICE_SIDE;

    private ChessBoard chessBoard = new ChessBoard();
    private Kernel kernel;

    public GamePanel(Kernel kernel) {
        this.kernel = kernel;
        initMouseListener();
        initKeyBoardListener();
    }

    //------------------------公有方法-------------------------------

    //画图
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawChessBoard(g);
        drawAllChess();
    }

    //画棋子
    public void drawChess(ChessStep step) {
        int color = step.getColor();
        int index_x = step.getX();
        int index_y = step.getY();
        if ((index_x+1) * (index_y+1) * (SIDE_NUM-index_x) * (SIDE_NUM-index_y) == 0) {
            throw new RuntimeException("超界！");
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
        drawAllChess();
    }

    //撤销画棋子
    public void undoDrawChess(int index_x, int index_y) {
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

    //------------------------私有方法-------------------------------


    //画棋盘
    private void drawChessBoard(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(0, 0, panelSide, panelSide);
        g.setColor(Color.black);
        for(int i = 1; i <= SIDE_NUM; i++) {
            g.drawLine(LATTICE_SIDE, i*LATTICE_SIDE, panelSide-LATTICE_SIDE, i*LATTICE_SIDE);
            g.drawLine(i*LATTICE_SIDE, LATTICE_SIDE, i*LATTICE_SIDE, panelSide-LATTICE_SIDE);
        }
    }

    //画历史记录所有的棋子
    private void drawAllChess() {
        for(ChessStep step : this.chessBoard.getHistorySteps()) {
            drawChess(step);
        }
    }

    //初始化鼠标监听器
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
                Point mousePoint = getMousePosition();
                if (mousePoint.x >= 0 && mousePoint.y >= 0 && mousePoint.x < panelSide && mousePoint.y < panelSide) {
                    int index_x = (mousePoint.x + (LATTICE_SIDE >> 1)) / LATTICE_SIDE - 1;
                    int index_y = (mousePoint.y + (LATTICE_SIDE >> 1)) / LATTICE_SIDE - 1;
                    kernel.s_dropChess(index_x, index_y);
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    //初始化键盘监听器
    private void initKeyBoardListener() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println(KeyEvent.getKeyText(e.getKeyCode()));
            }
        });
    }

}
