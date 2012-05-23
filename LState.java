package Ass01.ML;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * Created with IntelliJ IDEA.
 * User: Rebecca
 * Date: 22/05/12
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class LState {
    private int[] distToInt;                        // Distance of closet car to intersection from each road
    private int[] lightSetting;                     // What the lights are on at each road
    private int lightDelay;                         // Light delay?
    private int reward;

    public LState (int[] distToInt, int[] lightSetting) {
        this.distToInt = distToInt;
        this.lightSetting = lightSetting;
        lightDelay = 0;
        reward = initReward();
    }

    public int[] getDistToInt() {
        return distToInt;
    }

    public int getReward() {
        return reward;
    }

    // Set the inital reward based on assignment criteria for being in this state
    private int initReward() {
        int reward = 0;
        for (int i = 0; i < lightSetting.length; i++) {
            if (lightSetting[i] == 1 && distToInt[i] != 9) {
                reward = -1;
            }
        }
        return reward;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LState) {
            LState tmp = (LState) o;
            return (this.distToInt.equals(tmp.distToInt) && this.lightSetting.equals(tmp.lightSetting)
                    && this.lightDelay == tmp.lightDelay);
        }
        return false;
    }




}
