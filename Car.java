package Ass01.ML;

public class Car {
    private Position pos;
    private int speed;

    public Car(Position p, int s) {
        pos = p;
        speed = s;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    //for now cars only travel in increasing direction.
    // If we decide to add two-way roads then we will need to change road to have 4 directions rather than 2
    public void moveCar(Position closestObstacle, int roadDirection) { //obstacle could refer to car in front or red light in front (set to -1,-1 if no obstacle in front)
        Position temp = this.pos;
        if (roadDirection == Road.horizontal) {
           temp.setX(temp.getX() + this.speed);
        } else if (roadDirection == Road.vertical) {
           temp.setY(temp.getY() + this.speed);
        }
        if (!(temp.getX() == closestObstacle.getX() && temp.getY() == closestObstacle.getY())) {
            this.setPos(temp);
        }

    }

    public boolean removeCar(Position intersectionPos, int roadDirection) {
        if (roadDirection == Road.horizontal) {
          if (this.pos.getX() > intersectionPos.getX()) {
           return true;
          }
        } else if (roadDirection == Road.vertical) {
            if (this.pos.getY() > intersectionPos.getY()) {
              return true;
            }
        }

        return false;
    }

}
