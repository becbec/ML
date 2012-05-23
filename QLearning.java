package Ass01.ML;

import java.util.HashMap;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Rebecca
 * Date: 22/05/12
 * Time: 7:05 PM
 * To change this template use File | Settings | File Templates.
 */


public class QLearning {
    private HashMap<LState, HashMap<String, Double>> SAPairs;  // Maping of actions to pair to rewards
    private final double gamma;                                 // Discount factor
    private final double alpha;                                 // Learning rate
    private String nextMove;

    public QLearning () {
        SAPairs = new HashMap<LState, HashMap<String, Double>>();
        gamma = 0.9;
        alpha = 0.1;
    }

    public void performLearning(int[] distToInt, int[] lightSetting) {
        LState s = new LState(distToInt, lightSetting);
        String nextMove = nextMove();

        if (SAPairs.containsKey(s)) {
            // Update the reward of a current state-action pair
            if (SAPairs.get(s).containsKey(nextMove)) {
                double currentValue = SAPairs.get(s).get(nextMove);
                SAPairs.get(s).put(nextMove, updateQValue(currentValue, s.getReward()));
            // Add the new action and give it a reward
            } else {
                SAPairs.get(s).put(nextMove, 0.0);
            }
        // Add the new state, action and reward
        }  else {
            SAPairs.put(s, new HashMap());
            SAPairs.get(s).put(nextMove, 0.0);
        }
    }

    public String optimalPlay(int[] distToInt, int[] lightSetting) {
        LState s = new LState(distToInt, lightSetting);
        HashMap<String, Double> tmp = SAPairs.get(s);
        String optimalMove = "";
        double highestReward = -1;

        for (String key : tmp.keySet()) {
            if (tmp.get(key) > highestReward) {
                highestReward = tmp.get(key);
                optimalMove = key;
            }
        }

        return optimalMove;
    }

    public String getNextMove() {
        return nextMove;
    }



    private String nextMove() {
        Random rand = new Random();
        String move = "";

        // need to actually decide how to make the next move...

        return move;
    }


    private double updateQValue(double currentValue, int reward) {
        // Somehow need to write how to update the reward...

        /* this_Q = policy.getQValue( state, action );
		    next_Q = policy.getQValue( newstate, newaction );

		    new_Q = this_Q + alpha * ( reward + gamma * next_Q - this_Q );

		    policy.setQValue( state, action, new_Q );*/


        return 0;
    }




}
