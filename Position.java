package Ass01.ML;


public class Position {
    private int x;
    private int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPos(Position p){
        this.x = p.x;
        this.y = p.y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Position) {
            Position tmp = (Position) o;
            return (this.x == tmp.x && this.y == tmp.y);
        }

        return false;
    }

}
