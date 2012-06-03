package Ass01.ML;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.List;

public class LState {
    private List<Integer> distToInt;                        // Distance of closet car to intersection from each road
    private List<Integer> lightState;                       // What the lights are on at each road
    private int lightDelay;                                 // Light delay
    private int reward;

    public LState (List<Integer> distToInt, List<Integer> lightState, int lightDelay) {
        this.distToInt = distToInt;
        this.lightState = lightState;
        this.lightDelay = lightDelay;
        reward = initReward();
    }

    public List<Integer> getDistToInt() {
        return distToInt;
    }

    public List<Integer> getLightState() {
        return lightState;
    }

    public int getLightDelay() {
        return lightDelay;
    }

    public int getReward() {
        return reward;
    }

    // Set the inital reward based on assignment criteria for being in this state
    private int initReward() {
        int reward = 0;
        for (int i = 0; i < lightState.size(); i++) {
            if (lightState.get(i) == Intersection.red && distToInt.get(i) <= 1) {
                reward = -1;
            }
        }
        return reward;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LState) {
            LState tmp = (LState) o;
            return (this.distToInt.equals(tmp.distToInt) && this.lightState.equals(tmp.lightState)
                    && this.lightDelay == tmp.lightDelay);
        }
        return false;
    }
}
