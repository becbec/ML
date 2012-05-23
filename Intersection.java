package Ass01.ML;

import java.lang.*;
import java.util.*;

public class Intersection {
    private List<Road> roads;
    private List<Integer> lightState; //0 = red, 1 = green
    private Position pos;

    public static int red = 0;
    public static int green = 1;


    public Intersection(Position p){    //for now it just creates 2 roads, we can change this later
        this.pos = p;
        roads = new LinkedList<Road>();
        lightState = new ArrayList<Integer>();
        Road r = new Road(Road.horizontal,pos.getY());
        roads.add(r);
        Road s = new Road(Road.vertical, pos.getX());
        roads.add(s);
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
}
