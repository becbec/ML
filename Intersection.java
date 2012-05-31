package Ass01.ML;

import java.lang.*;
import java.util.*;

public class Intersection {
    private List<Road> roads;
    private List<Integer> lightState; //0 = red, 1 = green
    private Position pos;

    public static int red = 1;
    public static int green = 0;


    public Intersection(Position p){    //for now it just creates 2 roads, we can change this later
        this.pos = p;
        roads = new LinkedList<Road>();
        lightState = new Vector<Integer>();
        Road r = new Road(Road.horizontal,pos.getY());
        roads.add(r);
        Road s = new Road(Road.vertical, pos.getX());
        roads.add(s);
        //set lights one to red, one to green
        lightState.add(green);
        lightState.add(red);

    }

    public List<Road> getRoads() {
        return roads;
    }

    public Position getPos() {
        return pos;
    }

    public int getLightState(int index){
        return this.lightState.get(index);
    }

    public List<Integer> getLightState() {
        return lightState;
    }

    public void setLightState(int index, int state) {
        this.lightState.set(index, state);
    }

    public int getNumRoads(){
        return roads.size();
    }

    public List<Position> getClosestCarsInt() {   //Returns closest car to intersection on each road
        List<Position> closest = new ArrayList<Position>();
        ListIterator<Road> roadItr = this.roads.listIterator();
        Position carPos = null;

        // For if we continue cars moving
        while (roadItr.hasNext()) {
            boolean exit = false;
            Road nextRoad = roadItr.next();
            ListIterator<Car> carItr = nextRoad.getCars().listIterator();
            while (carItr.hasNext() && !exit) {
                carPos = carItr.next().getPos();
                if (nextRoad.getDirection() == Road.horizontal && carPos.getX() < this.getPos().getX()) {
                    exit = true;
                } else if (nextRoad.getDirection() == Road.vertical && carPos.getY() < this.getPos().getY()) {
                    exit = true;
                }
            }

            if (carPos == null) {
                carPos = this.pos;
            }
            closest.add(carPos);
        }

        return closest;
    }

}
