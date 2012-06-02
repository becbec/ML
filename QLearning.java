package Ass01.ML;

import java.util.*;


// init qvalue as this? + 0.0000000000000000001 * Math.random() ) );

public class QLearning {
    private HashMap<LState, HashMap<Boolean, Double>> SAPairs; // Maping of actions to pair to rewards
    public LState state;                                      // Current state we are looking at
    private boolean nextMove;                                  // Store the move that was made
    private int beenDelayedFor;                                // How long you have been delayed for

    private final int delayTime = 3;                           // Length of time lights must stay red
    private final double gamma = 0.9;                          // Discount factor
    private final double alpha = 0.1;                          // Learning rate
    private final double epsilon = 0.9;                        // Epsilon-greedy exploration percent

    /*
    how to qLearn
    select an action and execute it
    receive a reward for it
    observe new state s'
    update table entry for q(s,a)
     */

    public QLearning() {
        SAPairs = new HashMap<LState, HashMap<Boolean, Double>>();
        state = null;
        nextMove = false;
        beenDelayedFor = 0;
    }

    public boolean getNextMove(List<Integer> distToInt, List<Integer> lightState) {
        nextMove = chooseNextMove(distToInt, lightState);
        updateSAPair(distToInt, lightState, nextMove);
        setBeenDelayedFor(nextMove);

        return nextMove;
    }

    public void updateQValue(List<Integer> distToInt, List<Integer> lightState) {
        LState s = null;

        for (LState tmp : SAPairs.keySet()) {
            if (tmp.getDistToInt().equals(distToInt) && tmp.getLightState().equals(lightState)
                   && tmp.getLightDelay() == beenDelayedFor) {
                s = tmp;
            }
        }

        if (s == null) {
            s = new LState(distToInt, lightState, beenDelayedFor);
        }

        // Update the qValue in the hash;
        int reward = s.getReward();
        double currentQ = SAPairs.get(state).get(nextMove);
        double newQ = currentQ + alpha * (reward + gamma * maxQ(s) - currentQ);
        //double newQ = (1 - alpha) * currentQ + alpha * (reward + gamma * maxQ(s));
        System.out.println("s     dist = " + s.getDistToInt() + " lights     = " +
        					s.getLightState() + "delay = " + s.getLightDelay());
        System.out.println("state dist = " + state.getDistToInt() + " lightstate = " +
							state.getLightState() + "delay = " + state.getLightDelay());
        System.out.println("reward = " + reward + " currentQ = " + currentQ + " newQ = " + newQ);
        SAPairs.get(state).put(nextMove, newQ);
    }

    public boolean getBestAction(List<Integer> distToInt, List<Integer> lightState) {
        double maxQ = -Double.MAX_VALUE;
        boolean nextBestMove = false;
        LState s = null;
        int maxDV = 0;
        List<Boolean> doubleValues = new ArrayList<Boolean>();

        for (LState tmp : SAPairs.keySet()) {
            if (tmp.getDistToInt().equals(distToInt) && tmp.getLightState().equals(lightState)
                    && tmp.getLightDelay() == beenDelayedFor) {
                s = tmp;
            }
        }

        if (s != null) {
            HashMap<Boolean, Double> actions = SAPairs.get(s);
            for (Boolean key : actions.keySet()) {
                //System.out.println("actions.get(key) = " + actions.get(key) + "move = " + key);
                if (actions.get(key) > maxQ) {
                    maxQ = actions.get(key);// + s.getReward();
                    nextBestMove = key;
                } else if (actions.get(key) == maxQ) {
                    maxDV++;
                    doubleValues.add(key);
                }
            }

            if (maxDV > 0) {
                Random random = new Random();
                int randomIndex = random.nextInt(doubleValues.size());
                nextBestMove = doubleValues.get(randomIndex);
            }
        } else {
            nextBestMove = chooseNextMove(distToInt, lightState);
        }

        if (beenDelayedFor < delayTime && nextBestMove) {
            nextBestMove = false;
        }

        setBeenDelayedFor(nextBestMove);
        //state = s;

        return nextBestMove;
    }

    private Boolean chooseNextMove(List<Integer> distToInt, List<Integer> lightState) {
        Random random = new Random();
        double probability = random.nextDouble();
        Boolean nextMove = false;

        if (probability > epsilon) {
            for (int i = 0; i < distToInt.size(); i++) {
                // If there is a car queued at the intersection then switch the lights
                if (distToInt.get(i) == 1 && lightState.get(i) == Intersection.red
                        && beenDelayedFor >= delayTime) {
                    nextMove = true;
                }
            }
        } else {
            if (random.nextDouble() < 0.5 && beenDelayedFor >= delayTime) {
                nextMove = true;
            }
        }

        return nextMove;
    }

    private void updateSAPair(List<Integer> distToInt, List<Integer> lightState, Boolean nextMove) {
        state = null;

        //System.out.println("next move is "+nextMove);

        // Check to see if this state already exists
        for (LState tmp : SAPairs.keySet()) {
            if (tmp.getDistToInt().equals(distToInt) && tmp.getLightState().equals(lightState)
                    && tmp.getLightDelay() == beenDelayedFor) {
                state = tmp;
            }
        }

        // State exists
        if (state != null) {
            // If the move exists
            if (SAPairs.get(state).containsKey(nextMove)) {
                //System.out.println("s-a pair exists");
            // State-action pair doesn't exist so add the new action and give it a start qValue
            } else {
                //System.out.println("s-a pair does not exists");
                SAPairs.get(state).put(nextMove, 0.0);
            }
        // State does not exist
        } else {
            //System.out.println("s is null");
            state = new LState(distToInt, lightState, beenDelayedFor);
            SAPairs.put(state, new HashMap<Boolean, Double>());
            SAPairs.get(state).put(nextMove, 0.0);
        }

    }

    private double maxQ(LState s) {
        LinkedList<Double> nums = new LinkedList<Double>();
        //double maxQ = 0.0;

        if (SAPairs.get(s) == null) {
            return 0.0;
        } else {
            for (Double tmp : SAPairs.get(s).values()) {
                nums.add(tmp);
            }

            // Get the max
            java.util.Collections.sort(nums);
            
            return nums.get(nums.size()-1);
        }
    }

    private void setBeenDelayedFor(boolean nextMove) {
        // Set the beenDelayedFor counter
        if (nextMove) {
            beenDelayedFor = 0;
        } else if (beenDelayedFor < delayTime) {
            beenDelayedFor++;
        }
    }
}
