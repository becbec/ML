package Ass01.ML;

import java.util.*;

// init qvalue as this? + 0.0000000000000000001 * Math.random() ) );

public class QLearning {
    private HashMap<LState, HashMap<Boolean, Double>> SAPairs;  // Maping of actions to pair to rewards
    private LState state;                                      // Current state we are looking at
    private boolean nextMove;                                   // Store the move that was made
    private int delayFactor;                                   // Delay ?

    private final int delay = 3;
    private final double gamma;                                // Discount factor
    private final double alpha;                                // Learning rate

    /*
    how to qLearn
    select an action and execute it
    receive a reward for it
    observe new state s'
    update table entry for q(s,a)
     */

    public QLearning () {
        SAPairs = new HashMap<LState, HashMap<Boolean, Double>>();
        gamma = 0.9;
        alpha = 0.1;
        state = null;
        nextMove = false;
        delayFactor = 0;
    }

    public boolean getNextMove(List<Integer> distToInt, List<Integer> lightState) {
        nextMove = chooseNextMove();

        updateSAPair(distToInt, lightState, nextMove);

        return nextMove;
    }

    public void updateQValue(List<Integer> distToInt, List<Integer> lightState) {
        LState s = null;

        for (LState tmp: SAPairs.keySet()) {
            if (tmp.getDistToInt().equals(distToInt) && tmp.getLightState().equals(lightState)) {
                s = tmp; // TODO: add light delay as a check also?
            }
        }

        // Update the qValue;
        int reward = state.getReward();
        double currentQ = SAPairs.get(state).get(nextMove);
        double newQ = currentQ + alpha * (reward + gamma * maxQ(s) - currentQ);

        // Update in the hash
        SAPairs.get(state).put(nextMove, newQ);
    }

    public boolean getBestAction(List<Integer> distToInt, List<Integer> lightState) {
        double maxQ = -Double.MAX_VALUE;
        int selectedAction = -1;
        boolean nextBestMove = false;
        LState s = null;
        //HashMap<Boolean, Double> actions = SAPairs.get(s);
        int maxDV = 0;
        List<Boolean> doubleValues = new ArrayList<Boolean>();
        double epsilon = 0.1;
        Random rand = new Random();

        for (LState tmp: SAPairs.keySet()) {
            if (tmp.getDistToInt().equals(distToInt) && tmp.getLightState().equals(lightState)) {
                s = tmp;
            }
        }

        if (s != null) {
            if (1-epsilon > rand.nextDouble()) {
                HashMap<Boolean, Double> actions = SAPairs.get(s);
                for (Boolean key : actions.keySet()) {
                    if (actions.get(key) > maxQ) {
                        maxQ = actions.get(key)+s.getReward();
                        nextBestMove = key;
                    } else if(actions.get(key) == maxQ ) {
                        maxDV++;
                        doubleValues.add(key);
                    }
                }

                if(maxDV > 0) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(doubleValues.size());
                    nextBestMove = doubleValues.get(randomIndex);
                }
           }
        }

        if(selectedAction == -1) {
            System.out.println("RANDOM Choice !" );
            nextBestMove = chooseNextMove();
        }

        return nextBestMove;
    }

    private double maxQ(LState s) {
        List<Double> nums = new ArrayList<Double>();

        if (SAPairs.get(s) == null) {
            return 0.0;
        } else {
            for (Double tmp : SAPairs.get(s).values()) {
                nums.add(tmp);
            }

            // Get the max
            Arrays.sort(nums.toArray());
            return nums.get(nums.size()-1);
        }

    }

    private Boolean chooseNextMove() {
        //TODO: include light delay in this function...
        Random rand = new Random();
        Boolean nextMove;

        if (rand.nextDouble() < 0.5 && delayFactor >= delay) {
            nextMove = true;
            System.out.println("Switch lights = true");
            delayFactor = 0;
        } else {
            nextMove = false;
            delayFactor++;
            System.out.println("Switch light = false");
        }

        return nextMove;
    }

    private void updateSAPair(List<Integer> distToInt, List<Integer> lightState, Boolean nextMove) {
        state = null;

        for (Integer aDistToInt : distToInt) {
            System.out.println("number for the state is: " + aDistToInt);
        }

        // Check to see if this state already exists
        for (LState tmp: SAPairs.keySet()) {
            if (tmp.getDistToInt().equals(distToInt) && tmp.getLightState().equals(lightState)) {
                state = tmp;   // TODO: add lightdelay also?
            }
        }

        // State exists
        if (state != null) {
            // If the move exists
            if (SAPairs.get(state).containsKey(nextMove)) {
                System.out.println("s-a pair exists");
            // State-action pair doesn't exist so add the new action and give it a start qValue
            } else {
                System.out.println("s-a pair does not exists");
                SAPairs.get(state).put(nextMove, 0.0);  // TODO: figure out what the q-value starts at
            }
        // State does not exist
        }   else {
            System.out.println("s is null");
            state = new LState(distToInt, lightState);
            state.setLightDelay(delayFactor);
            SAPairs.put(state, new HashMap());
            SAPairs.get(state).put(nextMove, 0.0);
        }

    }

    /*
    optimal find the maxq else if there are n with same maxq pick random out of the n move

     */

    /*public void performLearning(List<Integer> distToInt, List<Integer> lightState) {
        LState s = null;
        String nextMove = nextMove();

        for (int i = 0; i < distToInt.size(); i++) {
            System.out.println("number for state is: " +distToInt.get(i));
        }

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
                SAPairs.get(s).put(nextMove, 0.0);
            }
        // Add the new state, action and reward
        }  else {
            System.out.println("s is null");
            s = new LState(distToInt, lightState);
            SAPairs.put(s, new HashMap());
            SAPairs.get(s).put(nextMove, 0.0);
        }
    }  */

   /* public String optimalPlay(List<Integer> distToInt, List<Integer> lightState) {
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


        return null;
    } */


   // private double updateQValue(double currentQ, int reward) {


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


        //return 0;
        //return newQ;
   // }




}
