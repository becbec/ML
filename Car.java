package Ass01.ML;

import java.awt.Color;

import javax.media.opengl.GL;

public class Car {
    private Position pos;
    private int speed;
    public double r;
    public double g;
    public double b;

    public Car(Position p, int s) {
        pos = p;
        speed = s;
        r = Math.random();
        g = Math.random();
        b = Math.random();
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
    
    public boolean canMoveCar(Position closestObstacle, int roadDirection) {
        Position temp = null;
        if (roadDirection == Road.horizontal) {
            temp = new Position(this.getPos().getX()+this.speed, this.getPos().getY());
        } else if (roadDirection == Road.vertical) {
            temp = new Position(this.getPos().getX(), this.getPos().getY()+this.speed);
        }

        if (!(temp.getX() == closestObstacle.getX() && temp.getY() == closestObstacle.getY())) {
            return true;
        }
        return false;
    }

    //for now cars only travel in increasing direction.
    // If we decide to add two-way roads then we will need to change road to have 4 directions rather than 2
    public void moveCar(int roadDirection) { //obstacle could refer to car in front or red light in front (set to -1,-1 if no obstacle in front)
        Position temp = null;
        if (roadDirection == Road.horizontal) {
            this.setPos(new Position(this.getPos().getX()+this.speed, this.getPos().getY()));
        } else if (roadDirection == Road.vertical) {
            this.setPos(new Position(this.getPos().getX(), this.getPos().getY()+this.speed));
        }
    }

    public boolean removeCar(Position edgeBoader, int roadDirection) {
        if (roadDirection == Road.horizontal) {
            if (this.getPos().getX() > 100) {
                return true;
            }
        } else if (roadDirection == Road.vertical) {
            if (this.getPos().getY() > 100) {
                return true;
            }
        }

        return false;
    }

    /*public boolean removeCar(Position intersectionPos, int roadDirection) {
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
    }*/

}
