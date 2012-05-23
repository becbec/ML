package Ass01.ML;

import java.lang.*;
import java.util.*;

public class Road {
    private LinkedList<Car> cars;
    private int direction; //0 is horizontal, 1 is vertical
    private int offset;
    public static int horizontal = 0;
    public static int vertical = 1;

    public Road(int direction, int offset){
        cars = new LinkedList<Car>();
        this.direction = direction;
        this.offset = offset;
    }

    public LinkedList<Car> getCars() {
        return cars;
    }

    public void addCar(Car c) {
        this.cars.addLast(c); //add car to end
    }

    public void removeCar(){
        this.cars.removeFirst(); //remove car from beginning
    }

    public int getDirection() {
        return direction;
    }

    public int getOffset() {
        return offset;
    }

}
