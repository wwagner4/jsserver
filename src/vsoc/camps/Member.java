package vsoc.camps;

import java.io.Serializable;

public class Member implements Serializable {

    private static final long serialVersionUID = 0L;

    private int matchCount = 0;

    private int ownGoalsCount = 0;

    private int otherGoalsCount = 0;

    private int ballKickCount = 0;

    private int kickOutCount = 0;

    private int receivedGoalsCount = 0;
    
    private BehaviourNeuroControlSystem controlSystem = null;

    public Member() {
        super();
    }

    public void setNeuroControlSystem(BehaviourNeuroControlSystem cs) {
        this.controlSystem = cs;
    }

    public void reset() {
        this.matchCount = 0;
        this.ownGoalsCount = 0;
        this.otherGoalsCount = 0;
        this.ballKickCount = 0;
        this.kickOutCount = 0;
        this.receivedGoalsCount = 0;
    }

    public double kickPerMatch() {
        double re = 0.0;
        if (this.matchCount != 0) {
            re = (double) this.ballKickCount / this.matchCount;
        }
        return re;
    }

    public double kickOutPerMatch() {
        double re = 0.0;
        if (this.matchCount != 0) {
            return (double) this.kickOutCount / this.matchCount;
        }
        return re;
    }

    public double goalsPerMatch() {
        double re = 0.0;
        if (this.matchCount != 0) {
            return (double) this.otherGoalsCount / this.matchCount;
        }
        return re;
    }

    public double ownGoalsPerMatch() {
        double re = 0.0;
        if (this.matchCount != 0) {
            return (double) this.ownGoalsCount / this.matchCount;
        }
        return re;
    }

    public double receivedGoalsPerMatch() {
        double re = 0.0;
        if (this.matchCount != 0) {
            return (double) this.receivedGoalsCount / this.matchCount;
        }
        return re;
    }


    
    public BehaviourNeuroControlSystem getNeuroControlSystem() {
        return this.controlSystem;
    }

    public void increaseMatchCount() {
        this.matchCount++;
    }

    public void increaseKickCount(int val) {
        this.ballKickCount += val;
    }

    public void increaseKickOutCount(int val) {
        this.kickOutCount += val;
    }

    public void increaseOwnGoalsCount(int val) {
        this.ownGoalsCount += val;
    }

    public void increaseOtherGoalsCount(int val) {
        this.otherGoalsCount += val;
    }

    public void increaseReceivedGoalsCount(int val) {
        this.receivedGoalsCount += val;
    }

    public void setWeightsRandom(long seed) {
        this.controlSystem.getNet().setWeightsRandom(seed);
    }

}
