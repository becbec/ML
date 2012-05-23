package Ass01.ML;

import java.lang.*;
import java.util.*;

public class Intersection {
    private List<Road> roads;
    private List<Integer> lightState; //0 = red, 1 = green
    private Position pos;

    public static int red = 0;
    public static int green = 1;


    public Intersection(){    //for now it just creates 1 road, we can change this later
        roads = new LinkedList<Road>();
        lightState = new ArrayList<Integer>();
        Road r = new Road(0,10);
        roads.add(r);
        pos = new Position(12,10); //TODO: fix this to work out position
        //Road s = new Road(1, 12);
        //roads.add(s);
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
