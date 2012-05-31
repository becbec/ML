package Ass01.ML;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Rebecca
 * Date: 28/05/12
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class Action {
    private boolean nextMove;

    public Action(int delayFactor) {
        Random rand = new Random();
        int delay = 3;

        if (rand.nextDouble() < 0.5 && delayFactor >= delay) {
            nextMove = true;
        } else {
            nextMove = false;
        }
    }

    public boolean getNextMove() {
        return nextMove;
    }
}
