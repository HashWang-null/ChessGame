package overt;


public class ChessStep {
    private int color;
    private int index_x;
    private int index_y;

    public ChessStep(int color, int index_x, int index_y) {
        this.color = color;
        this.index_x = index_x;
        this.index_y = index_y;
    }


    public int getColor() {
        return color;
    }

    public int getX() {
        return index_x;
    }

    public int getY() {
        return index_y;
    }

    @Override
    public String toString() {
        return "ChessStep{" +
                "color=" + color +
                ", index_x=" + index_x +
                ", index_y=" + index_y +
                '}';
    }
}
