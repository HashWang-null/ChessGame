package client;


import overt.ChessStep;

import java.util.Arrays;
import java.util.Vector;

public class ChessBoard {
    public static final int SIDE_NUM    = 15;
    public static final int BLACK_TYPE  = 1;
    public static final int WHITE_TYPE  = 2;
    public static final int BLANK       = 0;

    private int[][] boardArray;
    private Vector<ChessStep> historySteps;

    public ChessBoard() {
        this.boardArray = new int[SIDE_NUM][SIDE_NUM];
        this.historySteps = new Vector<>();
    }

    //-----------------公开方法-----------

    //落子
    public void dropChess(ChessStep step) {
        if (!isAvailable(step.getX(), step.getY())) {
            throw new RuntimeException("落子到不能落子的地方！ point: x:" + step.getX() + " y:" + step.getY());
        }
        if (step.getColor() != BLACK_TYPE && step.getColor() != WHITE_TYPE) {
            throw new RuntimeException("错误的颜色！ color:" + step.getColor());
        }
        this.boardArray[step.getY()][step.getX()] = step.getColor();
        this.historySteps.add(step);
    }
    public void dropChess(int color, int index_x, int index_y) {
        if (!isAvailable(index_x, index_y)) {
            throw new RuntimeException("落子到不能落子的地方！ point: x:" + index_x + " y:" + index_y);
        }
        if (color != BLACK_TYPE && color != WHITE_TYPE) {
            throw new RuntimeException("错误的颜色！ color:" + color);
        }
        this.boardArray[index_y][index_x] = color;
        this.historySteps.add(new ChessStep(color, index_x, index_y));
    }

    //撤销落子,返回最后的ChessStep
    public ChessStep undoChessDrop() {
        if (this.historySteps == null) {
            throw  new RuntimeException("未初始化historySteps！");
        }
        if (historySteps.size() > 0) {
            ChessStep lastStep = historySteps.get(historySteps.size()-1);
            historySteps.remove(lastStep);
            return lastStep;
        } else {
            return null;
        }
    }

    //坐标是否可用
    public boolean isAvailable(int index_x, int index_y) {
        if (index_x >= 0 && index_y >= 0 && index_x < SIDE_NUM && index_y < SIDE_NUM) {
            return boardArray[index_y][index_x] == BLANK;
        }
        return false;
    }

    //重置棋盘
    public void refreshChessBoard() {
        for (int i = 0; i < SIDE_NUM; i++) {
            Arrays.fill(boardArray[i], BLANK);
        }
        this.historySteps.clear();
    }

    //是否赢
    public boolean isWin(ChessStep chessStep) {
        int[] dx = {0, 1, 1, -1};
        int[] dy = {1, 0, 1, 1};
        for (int di = 0; di < 4; ++di) {
            int sum = 1, x = chessStep.getX() - dx[di], y = chessStep.getY() - dy[di];
            while (x > -1 && y > -1 && x < SIDE_NUM && y < SIDE_NUM && boardArray[y][x] == chessStep.getColor()) {
                ++ sum;
                x -= dx[di];
                y -= dy[di];
            }
            x = chessStep.getX() + dx[di];
            y = chessStep.getY() + dy[di];
            while (x > -1 && y > -1 && x < SIDE_NUM && y < SIDE_NUM && boardArray[y][x] == chessStep.getColor()) {
                ++ sum;
                x += dx[di];
                y += dy[di];
            }
            if (sum >= 5) {
                return true;
            }
        }
        return false;
    }

    //获取历史记录
    public Vector<ChessStep> getHistorySteps() {
        return historySteps;
    }

    //打印数组
    public void printArray() {
        System.out.println("--------------------------------------");
        for (int y = 0; y < SIDE_NUM; ++y) {
            for (int x = 0; x < SIDE_NUM; ++x) {
                System.out.print(boardArray[y][x] + " ");
            }
            System.out.println();
        }
        System.out.println("--------------------------------------");
    }

    //打印历史记录
    public void printHistorySteps() {
        System.out.println(historySteps.toString());
    }
}
