package Ass01.ML;

import java.lang.*;
import java.util.*;

public class Main {
    static int time;
    static int speed = 1;

    public static void main(String [] args){
        System.out.println("Hello, World");
        time = 0;
        //Create intersection
        Intersection intersection = new Intersection(); //pass in number of roads later? //for now just generates 1

        while(true){

            if (time%10 == 0) {  //if time multiple of 10, change all lights TODO: update this later to use ML
                for (int i=0; i < intersection.getNumRoads(); i++){
                    intersection.setLightState(i, (intersection.getLightState(i)+1)%2); //toggle light state
                }
            }

            ListIterator roadItr = intersection.getRoads().listIterator();
            ListIterator lightItr = intersection.getLightState().listIterator();
            Position obstacle;
            if (lightItr.equals(Intersection.red)) {
                obstacle = intersection.getPos();
            }
            while(roadItr.hasNext() && lightItr.hasNext()  )
            {
                obstacle = new Position(-1,-1);
                if (lightItr.equals(Intersection.red)) {
                    obstacle = intersection.getPos();
                }
                ListIterator carItr = ((Road) roadItr).getCars().listIterator();

                while (carItr.hasNext()){

                    //move forward  //need to double check, not queued at light
                    ((Car) carItr).moveCar(obstacle, ((Road) roadItr).getDirection());

                    //remove car if necessary
                    if (((Car) carItr).removeCar(intersection.getPos(),((Road) roadItr).getDirection())) {
                        ((Road) roadItr).removeCar();

                    } else {
                        obstacle = ((Car) carItr).getPos();
                    }

                }
                //TODO:potentially add car(s)
            }

            time++;

        }
    }

}
