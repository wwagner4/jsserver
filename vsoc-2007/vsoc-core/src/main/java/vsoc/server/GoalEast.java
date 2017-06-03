package vsoc.server;

import vsoc.util.*;
import java.awt.*;
import java.awt.geom.*;

import atan.model.Flag;

/**
 * A Flag
 */
public class GoalEast extends SimObject {

	private static final long serialVersionUID = 1L;
	
    GoalEast(double x, double y) {
        super(x, y);
    }

    Vision createVisionForWestPlayer() {
        return new VisionGoalOther(Flag.FLAG_CENTER);
    }

    Vision createVisionForEastPlayer() {
        return new VisionGoalOwn(Flag.FLAG_CENTER);
    }

    public double getGoalWidth() {
        return 14.02;
    }

    public Vec2D getSouthPole() {
        Vec2D p = getPosition();
        return new Vec2D(p.getX(), p.getY() - this.getGoalWidth() / 2);
    }

    public Vec2D getNorthPole() {
        Vec2D p = getPosition();
        return new Vec2D(p.getX(), p.getY() + this.getGoalWidth() / 2);
    }

    public void paint(Graphics2D g) {
        double x = getPosition().getX();
        double y = -getGoalWidth() / 2;
        double w = 2;
        double h = getGoalWidth();
        Shape rect = new Rectangle2D.Double(x, y, w, h);
        g.setColor(Color.RED);
        g.fill(rect);
        g.setColor(Color.BLACK);
        g.draw(rect);
    }
}