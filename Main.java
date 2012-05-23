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
        Intersection intersection = new Intersection();
        Random rnd = new Random();
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

                    //move forward - double check, not queued at light
                    ((Car) carItr).moveCar(obstacle, ((Road) roadItr).getDirection());

                    //remove car if necessary
                    if (((Car) carItr).removeCar(intersection.getPos(),((Road) roadItr).getDirection())) {
                        ((Road) roadItr).removeCar();
                        obstacle = new Position(-1,-1);
                    } else {
                        obstacle = ((Car) carItr).getPos();
                    }

                }
                if (time%(rnd.nextInt(10)+5)==0) {   //IS THIS CORRECT?
                    Position p = new Position(0,0);
                    if (((Road) roadItr).getDirection() == Road.horizontal){
                       p.setY(((Road) roadItr).getOffset());
                    } else if (((Road) roadItr).getDirection() == Road.vertical) {
                        p.setX(((Road) roadItr).getOffset());
                    }

                    Car c = new Car(p,speed);
                    ((Road) roadItr).addCar(c);
                }

            }

            time++;

        }
    }

}
