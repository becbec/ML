package Ass01.ML;

import java.util.*;

// init qvalue as this? + 0.0000000000000000001 * Math.random() ) );

public class QLearning {
    private HashMap<LState, HashMap<Boolean, Double>> SAPairs;  // Maping of actions to pair to rewards
    private LState state;                                      // Current state we are looking at
    private boolean nextMove;                                   // Store the move that was made
    private int beenDelayedFor;                                   // Delay ?

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

    public QLearning () {
        SAPairs = new HashMap<LState, HashMap<Boolean, Double>>();
        state = null;
        nextMove = false;
        beenDelayedFor = 0;
    }

    public boolean getNextMove(List<Integer> distToInt, List<Integer> lightState) {
        nextMove = chooseNextMove(distToInt, lightState);

        updateSAPair(distToInt, lightState, nextMove);

        for (int i = 0; i < lightState.size(); i++)  {
            if (lightState.get(i) == Intersection.red && distToInt.get(i) == 1 && !nextMove) {
                Controller.learningCount++;
            }
        }

        if (nextMove == true) {
            System.out.println("nextMove is true");
        }

        return nextMove;
    }

    public void updateQValue(List<Integer> distToInt, List<Integer> lightState) {
        LState s = null;

        for (LState tmp: SAPairs.keySet()) {
            if (tmp.getDistToInt().equals(distToInt) && tmp.getLightState().equals(lightState)
                   ) {//&& tmp.getLightDelay() == (beenDelayedFor-1)%3) {
                s = tmp; // TODO: add light delay as a check also?
            }
        }

        // Update the qValue;
        int reward = state.getReward();
        double currentQ = SAPairs.get(state).get(nextMove);
        //double newQ = currentQ + alpha * (reward + gamma * maxQ(s) - currentQ);
        double newQ = (1-alpha)*currentQ + alpha * (reward + gamma *maxQ(s));

        System.out.println("newQ = "+newQ);
        // Update in the hash
        SAPairs.get(state).put(nextMove, newQ);
    }

    public boolean getBestAction(List<Integer> distToInt, List<Integer> lightState) {
        double maxQ = -Double.MAX_VALUE;
        boolean selectedAction = false;
        boolean nextBestMove = false;
        LState s = null;
        int maxDV = 0;
        List<Boolean> doubleValues = new ArrayList<Boolean>();
        Random rand = new Random();

        for (LState tmp: SAPairs.keySet()) {
            if (tmp.getDistToInt().equals(distToInt) && tmp.getLightState().equals(lightState)
                    ){//&& tmp.getLightDelay() == beenDelayedFor) {
                s = tmp;
            }
        }

        if (s != null) {
            if (rand.nextDouble() < epsilon) {
                selectedAction = true;
                HashMap<Boolean, Double> actions = SAPairs.get(s);
                for (Boolean key : actions.keySet()) {
                    System.out.println("actions.get(key) = "+actions.get(key)+ "move = "+key);
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

        if(!selectedAction) {
            System.out.println("RANDOM Choice !" );
            nextBestMove = chooseNextMove(distToInt, lightState);
        }

        for (int i = 0; i < lightState.size(); i++)  {
            if (lightState.get(i) == Intersection.red && distToInt.get(i) == 1 && !nextBestMove) {
                Controller.playCount++;
            }
        }

        return nextBestMove;
    }

    private void updateSAPair(List<Integer> distToInt, List<Integer> lightState, Boolean nextMove) {
        state = null;

        System.out.println("next move is "+nextMove);

        // Check to see if this state already exists
        for (LState tmp: SAPairs.keySet()) {
            if (tmp.getDistToInt().equals(distToInt) && tmp.getLightState().equals(lightState)
                    ){ //&& tmp.getLightDelay() == beenDelayedFor) {
                state = tmp;   // TODO: added lightdelay also?
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
                SAPairs.get(state).put(nextMove, 0.0);
            }
            // State does not exist
        }   else {
            System.out.println("s is null");
            state = new LState(distToInt, lightState);
            if (beenDelayedFor > 3) {
                beenDelayedFor = 3;
            }
            state.setLightDelay(beenDelayedFor);
            SAPairs.put(state, new HashMap());
            SAPairs.get(state).put(nextMove, 0.0);
        }

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

    private Boolean chooseNextMove(List<Integer> distToInt, List<Integer> lightState) {
        //TODO: include light delay in this function...
        Random random = new Random();
        double probability = random.nextDouble();
        Boolean nextMove = false;
        System.out.println("light delay = "+beenDelayedFor);

        if (probability < epsilon) {
            System.out.println("choosing optimally");
            for (int i = 0; i < distToInt.size(); i++) {
                // If there is a car queued at the intersection then switch the lights
                if (distToInt.get(i) == 1 && lightState.get(i) == Intersection.red
                        && beenDelayedFor >= delayTime) {
                    nextMove = true;
                }
            }
        } else {
            System.out.println("NOTTT");
            if (random.nextDouble() < 0.5 && beenDelayedFor >= delayTime) {
                nextMove = true;
            }
        }

        // Set the beenDelayedFor counter
        if (nextMove) {
            beenDelayedFor = 0;
        } else {
            beenDelayedFor++;
        }

        return nextMove;
    }
}
