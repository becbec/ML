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
    private HashMap<LState, HashMap<String, Integer>> SAPairs;  // Maping of actions to pair to rewards
    private final double gamma;                                 // Discount factor
    private final double alpha;                                 // Learning rate
    private String nextMove;

    public QLearning () {
        SAPairs = new HashMap<LState, HashMap<String, Integer>>();
        gamma = 0.9;
        alpha = 0.1;
    }


    //hash for states mapping actions to rewards

    public void performLearning(int[] distToInt, int[] lightSetting) {
        LState s = new LState(distToInt, lightSetting);
        String nextMove = nextMove();

        if (SAPairs.containsKey(s)) {
            // Update the reward of a current state-action pair
            if (SAPairs.get(s).containsKey(nextMove)) {
                int currentReward = SAPairs.get(s).get(nextMove);
                SAPairs.get(s).put(nextMove, updateReward(currentReward));
            // Add the new action and give it a reward
            } else {
                SAPairs.get(s).put(nextMove, updateReward(0));
            }
        // Add the new state, action and reward
        }  else {
            SAPairs.put(s, new HashMap());
            SAPairs.get(s).put(nextMove, updateReward(0));
        }


    }

    public String optimalPlay(int[] distToInt, int[] lightSetting) {
        LState s = new LState(distToInt, lightSetting);
        HashMap<String, Integer> tmp = SAPairs.get(s);
        String optimalMove = "";
        int highestReward = -1;

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



        return move;
    }

    private int updateReward(double currentReward) {
        // Somehow need to write how to update the reward...

        return 0;
    }




}
