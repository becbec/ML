package Ass01.ML;

import java.util.HashMap;
import java.util.List;

// init qvalue as this? + 0.0000000000000000001 * Math.random() ) );

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

    public void performLearning(int[] distToInt, List<Integer> lightState) {
        LState s = null;
        String nextMove = nextMove();

        // Check to see if this state already exists
        for (LState tmp: SAPairs.keySet()) {
            if (tmp.getDistToInt().equals(distToInt) && tmp.getLightState().equals(lightState)) {
                s = tmp;
            }
        }

        // State exists
        if (s != null) {
            System.out.println("s is not null");
            // Update the reward of a current state-action pair if it exists
            if (SAPairs.get(s).containsKey(nextMove)) {
                System.out.println("s-a pair exists");
                double currentValue = SAPairs.get(s).get(nextMove);
                SAPairs.get(s).put(nextMove, updateQValue(currentValue, s.getReward()));
            // State-action pair doesn't exist so add the new action and give it a qValue
            } else {
                System.out.println("s-a pair does not exist");
                SAPairs.get(s).put(nextMove, 0.0);  // TODO: figure out what the q-value starts at
            }
        // Add the new state, action and reward
        }  else {
            System.out.println("s is null");
            s = new LState(distToInt, lightState);
            SAPairs.put(s, new HashMap());
            SAPairs.get(s).put(nextMove, 0.0);  // TODO: figure out what the q-value starts at
        }
    }

    public String optimalPlay(int[] distToInt, List<Integer> lightState) {
        LState s = new LState(distToInt, lightState);
        HashMap<String, Double> tmp = SAPairs.get(s);
        String optimalMove = "";
        double highestQValue = -2;

        for (String key : tmp.keySet()) {
            if (tmp.get(key) > highestQValue) {
                highestQValue = tmp.get(key)+s.getReward();
                optimalMove = key;
            }
        }

        return optimalMove;
    }

    public String getNextMove() {
        return nextMove;
    }



    private String nextMove() {
        //TODO: need to actually decide how to make the next move...

        return null;
    }


    private double updateQValue(double currentQ, int reward) {
        //TODO: Somehow need to write how to update the reward...
        //TODO: need to write getting maxQ/figure out what that is.

        //double newQ = currentQ + alpha * (reward + gamma * maxQ - currentQ);

        /*action = selectAction( state );
		    		newstate = thisWorld.getNextState( action );
				    reward = thisWorld.getReward();

				    this_Q = policy.getQValue( state, action );
				    max_Q = policy.getMaxQValue( newstate );

				    // Calculate new Value for Q
				    new_Q = this_Q + alpha * ( reward + gamma * max_Q - this_Q );
				    policy.setQValue( state, action, new_Q );

				    // Set state to the new state.
				    state = newstate;*/


        return 0;
        //return newQ;
    }




}
