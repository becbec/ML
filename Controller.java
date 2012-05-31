package Ass01.ML;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.*;
import java.util.*;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;

import com.sun.opengl.util.FPSAnimator;

public class Controller implements GLEventListener {
    static int time;
    static int speed = 1;
    Intersection intersection = new Intersection(new Position(50,50));
    QLearning ql = new QLearning();
    int scale = 6;
    boolean nextMove;

    public static void main(String [] args){
    	Controller c = new Controller();
    	c.run();
    }

	public Controller() {
        // Animation setup
        GLCanvas canvas = new GLCanvas();
		canvas.setBackground(Color.white);
		canvas.setSize(100*scale, 100*scale);
		Frame frame = new Frame("Simulation");
		frame.add(canvas);
		frame.setSize(100*scale, 100*scale);
		frame.setBackground(Color.white);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		canvas.addGLEventListener(this);
		FPSAnimator animator = new FPSAnimator(canvas, 30);
        animator.start();
	}
	
	private void run() {
	    System.out.println("Hello, World");
        int k = 0;
        Random rnd = new Random();

        while(k < 300){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            k++;

            // Get the next move and execute it
            if (k < 200) {
                nextMove = ql.getNextMove(getClosetPos(intersection), intersection.getLightState());
            } else {
                nextMove = ql.getBestAction(getClosetPos(intersection), intersection.getLightState());
                System.out.println("YEAHHHH LEARNINNGG AND STUFF");
            }


            if (nextMove) {
            //if (time%10 == 0) {  //if time multiple of 10, change all lights TODO: update this later to use ML
                for (int i=0; i < intersection.getNumRoads(); i++){
                    intersection.setLightState(i, (intersection.getLightState(i)+1)%2); //toggle light state
                }
            }

            ListIterator<Road> roadItr = intersection.getRoads().listIterator();
            ListIterator<Integer> lightItr = intersection.getLightState().listIterator();
            Position obstacle;

            while(roadItr.hasNext() && lightItr.hasNext())
            {
                Road nextRoad = roadItr.next();

                obstacle = new Position(-1,-1);
                Integer nextLight = lightItr.next();
                if (nextLight.equals(Intersection.red)) {
                    obstacle = intersection.getPos();
                    System.out.println("the intersection is red obstacle is at x "+obstacle.getX()+ " y "+obstacle.getY());
                } else {
                    System.out.println("the lights are green");
                }

                ListIterator<Car> carItr = nextRoad.getCars().listIterator();

                while (carItr.hasNext()){
                    Car nextCar = carItr.next();

                    //move forward - double check, not queued at light
                    nextCar.moveCar(obstacle, nextRoad.getDirection());
                    if (nextLight.equals(Intersection.red)) {
                        obstacle = intersection.getPos();
                    } else if (nextCar.removeCar(intersection.getPos(), nextRoad.getDirection())) {
                        obstacle = new Position(-1, -1);
                    } else {
                        obstacle = nextCar.getPos();
                    }
                }

                if (time%(rnd.nextInt(10)+5)==0) {   //IS THIS CORRECT?
                    Position p = new Position(0,0);
                    if (nextRoad.getDirection() == Road.horizontal){
                       p.setY(nextRoad.getOffset());
                    } else if (nextRoad.getDirection() == Road.vertical) {
                        p.setX(nextRoad.getOffset());
                    }

                    Car c = new Car(p,speed);
                    nextRoad.addCar(c);
                }
            }

           // Remove cars from the intersection
           List<Road> roads = intersection.getRoads();
            for(int j = 0; j < intersection.getNumRoads(); j++) {
                Road nextRoad = roads.get(j);
                for (int i = 0; i < nextRoad.getCars().size(); i++) {
                    Car nextCar = nextRoad.getCars().get(i);
                    if (nextCar.removeCar(intersection.getPos(),nextRoad.getDirection())) {
                        nextRoad.removeCar();
                    }
                }
            }

            // Update the qValues
            if (k < 200) {
                ql.updateQValue(getClosetPos(intersection), intersection.getLightState());
            }

            time++;

        }
    }

    public static List<Integer> getClosetPos(Intersection intersection) {
        ListIterator<Position> closetPosItr = intersection.getClosestCarsInt().listIterator();
        List<Integer> closetCars = new ArrayList<Integer>();
        Position intPos = intersection.getPos();

        while (closetPosItr.hasNext()) {
            Position nextPos = closetPosItr.next();
            System.out.println("clost pos is x " +nextPos.getX()+ " y "+nextPos.getY());
            if (nextPos.equals(intPos)) {
                closetCars.add(9);
            } else {
                int x =  (Math.abs(nextPos.getX() - intPos.getX()) +
                        Math.abs(nextPos.getY() - intPos.getY()));
                if (x > 9) {
                    x = 9;
                }
                closetCars.add(x);
            }
        }
        return closetCars;
    }
    
	@Override
	public void display(GLAutoDrawable draw) {
		GL gl = draw.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		//Scale to the screen
		gl.glPushMatrix();
		gl.glTranslated(-1.0,-1.0,0.0);
		gl.glScaled(1.0/500, 1.0/500, 0.0);
		
		// Draw grass
		gl.glBegin( GL.GL_POLYGON );
		gl.glColor3f(0.1f,0.7f,0.1f);
		gl.glVertex2d(0, 0);
		gl.glVertex2d(0, 1000);
		gl.glVertex2d(1000, 1000);
		gl.glVertex2d(1000, 0);
	    gl.glEnd();
		
	    // Draw Road
	    ListIterator<Road> roadItr = intersection.getRoads().listIterator();
	    while(roadItr.hasNext()) {
	    	gl.glPushMatrix();
	    	drawRoad(gl, roadItr.next());
	    	gl.glPopMatrix();
	    }
	    
	    // Draw Car
	    roadItr = intersection.getRoads().listIterator();
	    while(roadItr.hasNext()) {
	    	ListIterator<Car> carsItr = roadItr.next().getCars().listIterator();
	    	while (carsItr.hasNext()) {
	    		gl.glPushMatrix();
	    		drawCar(gl, carsItr.next());
	    		gl.glPopMatrix();
	    	}
	    }
	    
	    gl.glPushMatrix();
	    //drawIntersection(gl, intersection);
	    gl.glPopMatrix();
	    
	    gl.glPopMatrix(); //Scaling to screen popped
		gl.glFlush();
	}

	@Override
	public void displayChanged(GLAutoDrawable draw, boolean mode, boolean device) {		
	}

	@Override
	public void init(GLAutoDrawable draw) {
		GL gl = draw.getGL();
		gl.glClearColor(0, 0, 0, 0);
	}

	@Override
	public void reshape(GLAutoDrawable draw, int x, int y, int width, int height) {
	}
    
    public void drawRoad(GL gl, Road r) {
    	if (r.getDirection() == Road.horizontal) {
    		gl.glTranslated(0,r.getOffset()*10,0);
    		gl.glRotated(-90.0, 0.0, 0.0, 1.0);
    		gl.glTranslated(-10,0,0);
    	} else {
    		gl.glTranslated(r.getOffset()*10,0,0);
    	}
    	
		gl.glBegin( GL.GL_POLYGON );
		gl.glColor3f(0.2f,0.2f,0.2f);
		gl.glVertex2d(0, 0);
		gl.glVertex2d(0, 1000);
		gl.glVertex2d(20, 1000);
		gl.glVertex2d(20, 0);
	    gl.glEnd();
    }
    
    public void drawCar(GL gl, Car c) {
    	Position p = c.getPos();
    	gl.glTranslated(p.getX()*10,p.getY()*10,0);
    	
		gl.glBegin( GL.GL_POLYGON );
		gl.glColor3f(0.2f,0.2f,1f);
		gl.glVertex2d(0, 0);
		gl.glVertex2d(0, 10);
		gl.glVertex2d(10, 10);
		gl.glVertex2d(10, 0);
	    gl.glEnd();
    }
    
    public void drawIntersection(GL gl, Intersection i) {
    	gl.glTranslated(i.getPos().getX()*10,i.getPos().getY()*10-10,0);
    	gl.glBegin( GL.GL_POLYGON );
    	//if (i.getLightState(). == Intersection.green) {
    		gl.glColor3f(0.2f,1f,0.2f);
    	//}
		gl.glVertex2d(0, 0);
		gl.glVertex2d(0, 20);
		gl.glVertex2d(20, 20);
		gl.glVertex2d(20, 0);
	    gl.glEnd();
    }

}
