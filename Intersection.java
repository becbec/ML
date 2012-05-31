package Ass01.ML;

import java.lang.*;
import java.util.*;

public class Intersection {
    private List<Road> roads;
    private List<Integer> lightState; //0 = red, 1 = green
    private Position pos;
    private List<Car> closetCarInt; // Closest car of each road to the intersection

    public static int red = 1;
    public static int green = 0;


    public Intersection(Position p){    //for now it just creates 2 roads, we can change this later
        this.pos = p;
        roads = new LinkedList<Road>();
        lightState = new ArrayList<Integer>();
        Road r = new Road(Road.horizontal,pos.getY());
        roads.add(r);
        Road s = new Road(Road.vertical, pos.getX());
        roads.add(s);
        closetCarInt = new ArrayList<Car>();
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

        while(roadItr.hasNext()){
            boolean exit = false;
            ListIterator<Car> carItr = roadItr.next().getCars().listIterator();
            while (carItr.hasNext() && !exit) {
                carPos = carItr.next().getPos();
                if (!carPos.equals(this.getPos())) {
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
