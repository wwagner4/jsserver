package vsoc.server;

import java.util.*;

import vsoc.util.*;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;

import atan.model.*;

/**
 * A player.
 */

public abstract class VsocPlayer extends MoveObject implements Player {

    private static final long serialVersionUID = 1L;

    private Controller control;

    private int ownGoalCount = 0;

    private int otherGoalCount = 0;

    private int kickOutCount = 0;

    private int kickCount = 0;

    private int number = -1;

    protected final double shapeWidth = 3.5;
    protected final double sw = shapeWidth;
    protected final double swh = shapeWidth / 2.0;

    VsocPlayer(double x, double y, double angle) {
        super(x, y, angle);
        setController(new DummyControlSystem());
    }

    public void setController(Controller c) {
        this.control = c;
        c.setPlayer(this);
        this.reset();
    }

    public Controller getController() {
        return this.control;
    }

    protected void moveFromVelo() {
        setVelo(getVelo() * getServer().getPlayerDecay());
        moveRelative(getVeloVector());
    }

    public void dash(int power) {
        setVelo(getVelo() + getServer().getDashPowerRate() * (double) power
                / getServer().getPlayerDecay());
    }

    public void kick(int power, double dir) {
        getServer().getBall().kicked(this, power, dir);
    }

    boolean canSee(Vec2D pos) {
        return pos.isInSec();
    }

    abstract Vision createVision(SimObject o);

    public Collection<Vision> see() {
        Collection<Vision> coll = new ArrayList<>();

        for (SimObject o : getServer().getSimObjects()) {
            if (!o.equals(this)) {
                Vec2D pos = o.getPosition()
                        .trans(getPosition(), getDirection());
                if (canSee(pos)) {
                    Vision v = createVision(o);
                    v.setPosition(pos);
                    coll.add(v);
                }
            }
        }
        return coll;
    }

    public void paint(Graphics2D g) {
        double x = getPosition().getX();
        double y = getPosition().getY();
        Shape s = new Ellipse2D.Double(x - this.swh, y - this.swh, this.sw, this.sw);
        g.setColor(color());
        g.fill(s);
        g.setColor(Color.black);
        g.draw(s);
        double a = this.swh * Math.cos(getDirection());
        double b = this.swh * Math.sin(getDirection());
        Line2D l = new Line2D.Double(x, y, x + a, y + b);
        g.draw(l);
    }

    abstract protected Color color();

    abstract void increaseWestGoalCount();

    abstract void increaseEastGoalCount();

    void increaseOwnGoalCount() {
        this.ownGoalCount++;
    }

    void increaseKickOutCount() {
        this.kickOutCount++;
    }

    void increaseKickCount() {
        this.kickCount++;
    }

    void increaseOtherGoalCount() {
        this.otherGoalCount++;
    }

    public int getOwnGoalCount() {
        return this.ownGoalCount;
    }

    public int getKickCount() {
        return this.kickCount;
    }

    public int getKickOutCount() {
        return this.kickOutCount;
    }

    public int getOtherGoalCount() {
        return this.otherGoalCount;
    }

    void reset() {
        this.ownGoalCount = 0;
        this.otherGoalCount = 0;
        this.kickCount = 0;
        this.kickOutCount = 0;
    }

    public abstract String getType();

    public void write(PrintWriter w) {
        Vec2D pos = getPosition();
        w.print("pos:");
        w.print(pos);
        w.print("\tType:");
        w.println(getType());
        w.print("\tOwnGoalCount:");
        w.print(getOwnGoalCount());
        w.print(" OtherGoalCount:");
        w.println(getOtherGoalCount());
        w.print("\tKickCount:");
        w.print(getKickCount());
        w.print("\tKickOutCount:");
        w.println(getKickOutCount());
    }

    public void say(String msg) {
        // Player ignores if Controller says something
    }

    public void senseBody() {
        // Player ignores if Controller wants to sense its body
    }

    public void turnNeck(double angle) {
        // Player ignores if Controller wants to turn its neck
    }

    public void catchBall(double angle) {
        // Player ignores if Controller wants to catch the ball
    }

    public void changeViewMode(ViewQuality a, ViewAngle b) {
        // Player ignores if Controller wants to change the view mode
    }

    public void bye() {
        // Player ignores if Controller wants to say good bye
    }

    public void setTeamEast(boolean b) {
        // Player ignores if Controller wants to set it east or west
    }

    public void setNumber(int num) {
        this.number = num;
    }

    public int getNumber() {
        return this.number;
    }

    public abstract void move(int x, int y);

}