package client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class ChessBoard {
    private int[][] boardArray;
    private ChessStepStack historySteps;
    private int color;
    public static final int SIDE_NUM = 15;
    public static final int BLACK_TYPE = 1;
    public static final int WHITE_TYPE = 2;
    public static final int BLANK = 0;

    private static ChessBoard chessBoard;

    private ChessBoard() {
        this.historySteps = new ChessStepStack();
        this.boardArray = new int[SIDE_NUM][SIDE_NUM];
    }

    public static ChessBoard newInstance() {
        if(chessBoard == null) {
            chessBoard = new ChessBoard();
        }
        return chessBoard;
    }

    public void refreshChessBoard() {
        for (int i = 0; i < SIDE_NUM; i++) {
            Arrays.fill(boardArray[i], BLANK);
        }
        this.historySteps.clear();
    }

    public boolean checkAvailable(ChessPoint point) {
        return checkAvailable(point.getX(), point.getY());
    }

    public boolean checkAvailable(int index_x, int index_y) {
        if (index_x >= 0 && index_y >= 0 && index_x < SIDE_NUM && index_y <SIDE_NUM) {
            return boardArray[index_x][index_y] == BLANK;
        }
        return false;
    }

    public ArrayList<ChessStep> getHistorySteps() {
        return this.historySteps.steps;
    }

    public boolean isWin(ChessStep newStep) {
        int[] dx = {0, 1, 1, -1};
        int[] dy = {1, 0, 1, 1};
        for(int di = 0; di < 4; di++) {
            int sum = 1, x, y;
            x = newStep.getChessPoint().getX() - dx[di];
            y = newStep.getChessPoint().getY() - dy[di];
            while(x > -1 && y > -1 && x < SIDE_NUM && y < SIDE_NUM && boardArray[y][x] == newStep.getChessType()) {
                x -= dx[di];
                y -= dy[di];
                sum ++;
            }
            x = newStep.getChessPoint().getX() + dx[di];
            y = newStep.getChessPoint().getY() + dy[di];
            while(x > -1 && y > -1 && x < SIDE_NUM && y < SIDE_NUM && boardArray[y][x] == newStep.getChessType()) {
                x += dx[di];
                y += dy[di];
                sum ++;
            }
            if(sum > 4) {
                System.out.println(historySteps.getSteps());
                return true;
            }
        }
        return false;
    }

    public void dropChess(ChessStep newStep) {
        this.boardArray[newStep.getChessPoint().getY()][newStep.getChessPoint().getX()] = newStep.getChessType();
        this.historySteps.addNewStep(newStep);
    }

    public boolean undoChessDrop() {
        ChessStep step = this.historySteps.getLastStep();
        if(step != null) {
            this.boardArray[step.getChessPoint().getY()][step.getChessPoint().getX()] = 0;
            this.historySteps.removeLastStep();
            return true;  
        }
        return false;
    }

    public static class ChessPoint {
        private int x;
        private int y;

        public ChessPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChessPoint that = (ChessPoint) o;
            return x == that.x &&
                    y == that.y;
        }

        public boolean equals(int x, int y) {
            return this.x == x && this.y == y;
        }

        @Override
        public String toString() {
            return "[x = " + this.x + ", y = " + this.y + " ]";
        }
    }

    public static class ChessStep {
        private int chessType;
        private ChessPoint chessPoint;

        public ChessStep(int chessType, ChessPoint chessPoint) {
            this.chessType = chessType;
            this.chessPoint = chessPoint;
        }

        public int getChessType() {
            return chessType;
        }

        public ChessPoint getChessPoint() {
            return chessPoint;
        }

        @Override
        public String toString() {
            if(this.chessType == BLACK_TYPE) {
                return "[type: black, x: " +
                        this.chessPoint.getX() + ", y: " + this.chessPoint.getY() + "]";
            } else if(this.chessType == WHITE_TYPE) {
                return "[type: white, x: " +
                        this.chessPoint.getX() + ", y: " + this.chessPoint.getY() + "]";
            }
            throw new RuntimeException("未知类型的棋子！");
        }
    }

    private static class ChessStepStack {
        private ArrayList<ChessStep> steps;
        private ChessStep lastStep;

        private ChessStepStack() {
            this.steps = new ArrayList<>();
            this.lastStep = null;
        }

        private void addNewStep(ChessStep newStep) {
            for(ChessStep step : steps) {
                if(step.getChessPoint().equals(newStep.getChessPoint())) {
                    return;
                }
            }
            this.steps.add(newStep);
            this.lastStep = newStep;
        }

        private void removeLastStep() {
            if(this.steps.size() == 0) {
                return;
            }
            if(this.steps.size() == 1) {
                this.steps.clear();
                this.lastStep = null;
                return;
            }
            this.steps.remove(this.lastStep);
            this.lastStep = steps.get(steps.size()-1);
        }

        private void removeLastSteps(int time) {
            for(int i = 0; i < time; i++) {
                removeLastStep();
            }
        }

        private ArrayList<ChessStep> getSteps() {
            return this.steps;
        }

        private ChessStep getLastStep() {
            if(this.steps.size() == 0) {
                return null;
            }
            return this.steps.get(this.steps.size()-1);
        }

        private boolean isContainPoint(int x, int y) {
            for(ChessStep step : steps) {
                if(step.chessPoint.equals(x, y)) {
                    return true;
                }
            }
            return false;
        }

        private void clear() {
            this.lastStep = null;
            this.steps.clear();
        }
    }

}

