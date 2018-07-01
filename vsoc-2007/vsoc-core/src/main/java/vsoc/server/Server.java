package vsoc.server;

import atan.model.Controller;
import atan.model.Player;
import vsoc.server.gui.Paintable;
import vsoc.server.gui.Simulation;
import vsoc.server.gui.SimulationChangeListener;
import vsoc.util.Vec2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Performs soccer specific actions.
 *
 */
public class Server implements Serializable, Simulation, Paintable {

	private static final long serialVersionUID = 1L;

	private static final Color FIELD_BACKGROUND = initFieldBackground();

	private static final Color BACKGROUND = initBackground();

	public static final int HEIGHT = 68;

	public static final int WIDTH = 106;

	private long time = 0;

	private Collection<SimulationChangeListener> listeners = new ArrayList<>();

	private Collection<SimObject> simObjects = new ArrayList<>();

	private Collection<MoveObject> playersAndBall = new ArrayList<>();

	private List<VsocPlayer> players = new ArrayList<>();

	private Ball ball = null;

	private GoalEast goalEast = null;

	private GoalWest goalWest = null;

	private int goalWestCount = 0;

	private int goalEastCount = 0;

	private boolean informListeners = true;

	private static Stroke stroke = new BasicStroke((float) 0.1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

	public Server() {
		super();
	}

	private static Color initFieldBackground() {
		return new Color(150, 170, 160);
	}

	private static Color initBackground() {
		return new Color(160, 170, 170);
	}

	protected double getBallDecay() {
		return 0.94;
	}

	public boolean isInformListeners() {
		return informListeners;
	}

	public void setInformListeners(boolean informListeners) {
		this.informListeners = informListeners;
	}

	protected double getPlayerDecay() {
		return 0.4;
	}

	protected double getDashPowerRate() {
		return 0.006;
	}

	protected double getKickPowerRate() {
		return 0.016;
	}

	public int getEastGoalCount() {
		return this.goalEastCount;
	}

	public int getWestGoalCount() {
		return this.goalWestCount;
	}

	public void reset() {
		this.goalWestCount = 0;
		this.goalEastCount = 0;
		this.time = 0;
	}

	public long getTime() {
		return this.time;
	}

	public void setTime(long t) {
		this.time = t;
	}

	public String getName() {
		return "Vsoc Server";
	}

	@Override
	public void addListener(SimulationChangeListener l) {
		this.listeners.add(l);
	}

	public void takeStep() {
		this.time++;
		if (informListeners) {
			for(SimulationChangeListener listener: this.listeners) {
				listener.simulationChangePerformed(this);
			}
		}
		takeStepOfAllControlSystems();
		moveAll();
		correctBall();
	}

	private void takeStepOfAllControlSystems() {
        for (VsocPlayer p : this.players) {
            informController(p, p.getController());
        }
	}

	void informController(VsocPlayer p, Controller c) {
		c.preInfo();
        for (Vision v : p.see()) {
            v.informControlSystem(c);
        }

		c.postInfo();

	}

	private void moveAll() {
        for (MoveObject o : this.playersAndBall) {
            o.moveFromVelo();
        }
	}

	private void correctBall() {

		if (this.ball.getPosition().isNorthOfHorizontalLine(HEIGHT / 2))
			correctBallNorth();

		else if (this.ball.getPosition().isSouthOfHorizontalLine(-HEIGHT / 2))
			correctBallSouth();

		else if (this.ball.getPosition().isEastOfVerticalLine(WIDTH / 2))
			correctBallEast();

		else if (this.ball.getPosition().isWestOfVerticalLine(-WIDTH / 2))
			correctBallWest();

	}

	private void correctBallNorth() {
		if (this.ball.getPosition().isEastOfVerticalLine(WIDTH / 2))
			this.ball.setPosition(new Vec2D(WIDTH / 2, HEIGHT / 2));
		else if (this.ball.getPosition().isWestOfVerticalLine(-WIDTH / 2))
			this.ball.setPosition(new Vec2D(-WIDTH / 2, HEIGHT / 2));
		else
			this.ball.setPosition(new Vec2D(this.ball.getPosition().getX(), HEIGHT / 2));
		this.ball.getKicker().increaseKickOutCount();
		this.ball.setVelo(0);
	}

	private void correctBallSouth() {
		if (this.ball.getPosition().isEastOfVerticalLine(WIDTH / 2))
			this.ball.setPosition(new Vec2D(WIDTH / 2, -HEIGHT / 2));
		else if (this.ball.getPosition().isWestOfVerticalLine(-WIDTH / 2))
			this.ball.setPosition(new Vec2D(-WIDTH / 2, -HEIGHT / 2));
		else
			this.ball.setPosition(new Vec2D(this.ball.getPosition().getX(), -HEIGHT / 2));
        this.ball.getKicker().increaseKickOutCount();
		this.ball.setVelo(0);
	}

	private void correctBallEast() {
		if (isBallEastGoal()) {
			this.ball.setPosition(new Vec2D(0, 0));
			this.goalEastCount++;
			this.ball.getKicker().increaseEastGoalCount();
		} else {
			this.ball.setPosition(new Vec2D(WIDTH / 2, this.ball.getPosition().getY()));
			this.ball.getKicker().increaseKickOutCount();
		}
		this.ball.setVelo(0);
	}

	private void correctBallWest() {
		if (isBallWestGoal()) {
			this.ball.setPosition(new Vec2D(0, 0));
			this.goalWestCount++;
			this.ball.getKicker().increaseWestGoalCount();
		} else {
            this.ball.setPosition(new Vec2D(-WIDTH / 2, this.ball.getPosition().getY()));
            this.ball.getKicker().increaseKickOutCount();
        }
		this.ball.setVelo(0);
	}

	private boolean isBallWestGoal() {
		return this.ball.getPosition().isConnectionLineSouthOfPoint(this.ball.getPreviousPosition(),
		    this.goalWest.getNorthPole())
		    && this.ball.getPosition().isConnectionLineNorthOfPoint(this.ball.getPreviousPosition(),
		        this.goalWest.getSouthPole());
	}

	private boolean isBallEastGoal() {
		return this.ball.getPosition().isConnectionLineSouthOfPoint(this.ball.getPreviousPosition(),
		    this.goalEast.getNorthPole())
		    && this.ball.getPosition().isConnectionLineNorthOfPoint(this.ball.getPreviousPosition(),
		        this.goalEast.getSouthPole());
	}

	public Ball getBall() {
		return this.ball;
	}

	public List<VsocPlayer> getPlayersWest() {
		ArrayList<VsocPlayer> c = new ArrayList<>();
        for (VsocPlayer o : Server.this.players) {
            if (o instanceof VsocPlayerWest)
                c.add(o);
        }
		return c;
	}

	public List<VsocPlayer> getPlayersEast() {
		ArrayList<VsocPlayer> c = new ArrayList<>();
        for (VsocPlayer o : Server.this.players) {
            if (o instanceof VsocPlayerEast)
                c.add(o);
        }
		return c;
	}

	public List<VsocPlayer> getPlayers() {
		return this.players;
	}

	public void paint(Graphics2D g) {
		g.setStroke(stroke);
		g.setColor(BACKGROUND);
		g.fill(new Rectangle2D.Double(-100, -100, 200, 200));
		double x = -WIDTH / 2.0;
		double y = -HEIGHT / 2.0;
		Rectangle2D rect = new Rectangle2D.Double(x, y, WIDTH, HEIGHT);
		g.setColor(FIELD_BACKGROUND);
		g.fill(rect);
		g.setColor(Color.black);
		g.draw(rect);
		for (SimObject so : this.simObjects) {
			so.paint(g);
		}
	}

	public void setGoalEast(GoalEast goalEast) {
		this.goalEast = goalEast;
	}

	public void setGoalWest(GoalWest goalWest) {
		this.goalWest = goalWest;
	}

	public GoalEast getGoalEast() {
		return this.goalEast;
	}

	public GoalWest getGoalWest() {
		return this.goalWest;
	}

	public Collection<SimObject> getSimObjects() {
		return this.simObjects;
	}

	public int getActualPlayerWestCount() {
		int count = 0;
        for (MoveObject mo : this.playersAndBall) {
            if (mo instanceof Player) {
                Player p = (Player) mo;
                if (!p.isTeamEast()) {
                    count++;
                }
            }
        }
		return count;
	}

	public int getActualPlayerEastCount() {
		int count = 0;
        for (MoveObject mo : this.playersAndBall) {
            if (mo instanceof Player) {
                Player p = (Player) mo;
                if (p.isTeamEast()) {
                    count++;
                }
            }
        }
		return count;
	}

	public void addSimObject(SimObject simObj) {
		this.simObjects.add(simObj);
		if (simObj instanceof Ball) {
			if (this.ball != null) {
				throw new IllegalStateException("Only one ball may be added.");
			}
			Ball ballObj = (Ball) simObj;
			this.ball = ballObj;
			ball.setServer(this);
			this.playersAndBall.add(ballObj);
		} else if (simObj instanceof VsocPlayer) {
			VsocPlayer p = (VsocPlayer) simObj;
			p.setServer(this);
			this.playersAndBall.add(p);
			this.players.add(p);
		}
	}

	public int getPlayersCount() {
		return this.players.size();
	}

	public int getPlayersEastCount() {
		int re = 0;
        for (VsocPlayer player : this.players) {
            if (player.isTeamEast()) {
                re++;
            }
        }
		return re;
	}

	public int getPlayersWestCount() {
		int re = 0;
        for (VsocPlayer player : this.players) {
            if (!player.isTeamEast()) {
                re++;
            }
        }
		return re;
	}

}
