package vsoc.camps.neuroevolution;

import java.util.Iterator;

import org.apache.log4j.Logger;

import vsoc.behaviour.*;
import vsoc.nn.Net;
import vsoc.util.*;
import atan.model.Player;

public class NetBehaviour<N extends Net> implements Behaviour {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(NetBehaviour.class);

	private N net = null;

	private Retina retinaFlagLeft = new Retina();

	private Retina retinaFlagRight = new Retina();

	private Retina retinaFlagOwn = new Retina();

	private Retina retinaGoalOther = new Retina();

	private Retina retinaPlayerOwn = new Retina();

	private Retina retinaPlayerOther = new Retina();

	private Retina retinaFlagPenaltyOther = new Retina();

	private Retina retinaBall = new Retina();

	public NetBehaviour(N net) {
		super();
		this.net = net;
	}

	public boolean shouldBeApplied(Sensors sens) {
		return true;
	}

	public void apply(Sensors sens, Player player) {
		if (log.isDebugEnabled()) {
			debugSensors(sens);
		}
		initRetinas(sens);
		setInputLayer();
		this.net.calculate();
		addCommandsFromOutputLayer(sens, player);
	}

	private void debugSensors(Sensors sens) {
		log.debug("Sensors: --- BEGIN ---");
		log.debug("sens.ball:" + sens.getBall());
		log.debug("sens.bodyState:" + sens.getBodyState());
		log.debug("sens.flagsCenter:" + sens.getFlagsCenter());
		log.debug("sens.flagsCornerOther:" + sens.getFlagsCornerOther());
		log.debug("sens.flagsCornerOwn:" + sens.getFlagsCornerOwn());
		log.debug("sens.flagsGoalOther:" + sens.getFlagsGoalOther());
		log.debug("sens.flagsGoalOwn:" + sens.getFlagsGoalOwn());
		log.debug("sens.flagsLeft:" + sens.getFlagsLeft());
		log.debug("sens.flagsOther:" + sens.getFlagsOther());
		log.debug("sens.flagsOwn:" + sens.getFlagsOwn());
		log.debug("sens.flagsPenaltyOther:" + sens.getFlagsPenaltyOther());
		log.debug("sens.flagsPenaltyOwn:" + sens.getFlagsPenaltyOwn());
		log.debug("sens.flagsRight:" + sens.getFlagsRight());
		log.debug("sens.lines:" + sens.getLines());
		log.debug("sens.messages:" + sens.getMessages());
		log.debug("sens.playersOther:" + sens.getPlayersOther());
		log.debug("sens.playersOwn:" + sens.getPlayersOwn());
		log.debug("sens.playMode:" + sens.getPlayMode());
		log.debug("sens.refereeMessage:" + sens.getRefereeMessage());
		log.debug("Sensors: --- END ---");
	}

	private void initRetinas(Sensors sens) {
		this.retinaBall.reset();
		DistDirVision ball = sens.getBall();
		if (ball != null) {
			this.retinaBall.addVision(ball.getDistance(), -ball.getDirection());
		}
		this.retinaFlagLeft.reset();
		if (sens.sawFlagLeft()) {
			Iterator<DistDirVision> iter = sens.getFlagsLeft().values().iterator();
			while (iter.hasNext()) {
				DistDirVision vis = iter.next();
				this.retinaFlagLeft.addVision(vis.getDistance(), -vis.getDirection());

			}
		}
		this.retinaFlagOwn.reset();
		if (sens.sawFlagOwn()) {
			Iterator<DistDirVision> iter = sens.getFlagsOwn().values().iterator();
			while (iter.hasNext()) {
				DistDirVision vis = iter.next();
				this.retinaFlagOwn.addVision(vis.getDistance(), -vis.getDirection());

			}
		}
		this.retinaFlagPenaltyOther.reset();
		if (sens.sawFlagPenaltyOther()) {
			Iterator<DistDirVision> iter = sens.getFlagsOwn().values().iterator();
			while (iter.hasNext()) {
				DistDirVision vis = iter.next();
				this.retinaFlagPenaltyOther.addVision(vis.getDistance(), -vis.getDirection());

			}
		}
		this.retinaFlagRight.reset();
		if (sens.sawFlagRight()) {
			Iterator<DistDirVision> iter = sens.getFlagsOwn().values().iterator();
			while (iter.hasNext()) {
				DistDirVision vis = iter.next();
				this.retinaFlagRight.addVision(vis.getDistance(), -vis.getDirection());
			}
		}
		this.retinaGoalOther.reset();
		if (sens.sawFlagGoalOther()) {
			Iterator<DistDirVision> iter = sens.getFlagsOwn().values().iterator();
			while (iter.hasNext()) {
				DistDirVision vis = iter.next();
				this.retinaGoalOther.addVision(vis.getDistance(), -vis.getDirection());
			}
		}
		this.retinaPlayerOther.reset();
		if (sens.sawPlayerOther()) {
			Iterator<DistDirVision> iter = sens.getFlagsOwn().values().iterator();
			while (iter.hasNext()) {
				DistDirVision vis = iter.next();
				this.retinaPlayerOther.addVision(vis.getDistance(), -vis.getDirection());

			}
		}
		this.retinaPlayerOwn.reset();
		if (sens.sawPlayerOwn()) {
			Iterator<DistDirVision> iter = sens.getFlagsOwn().values().iterator();
			while (iter.hasNext()) {
				DistDirVision vis = iter.next();
				this.retinaPlayerOwn.addVision(vis.getDistance(), -vis.getDirection());

			}
		}
	}

	public Behaviour getChild() {
		return null;
	}

	private void addCommandsFromOutputLayer(Sensors sens, Player player) {
		addTurnCommandFromOutputLayer(player);
		player.dash(powerValueFromNeuroActivation(this.net.getOutputValue(8)));
		if ((sawBall(sens)) && (distBall(sens) < 0.7)) {
			addKickCommandFromOutputLayer(player);
		}
	}

	private boolean sawBall(Sensors sens) {
		return sens.getBall() != null;
	}

	private double distBall(Sensors sens) {
		return sens.getBall().getDistance();
	}

	private void addKickCommandFromOutputLayer(Player player) {
		short maxVal = (short) 0;
		int maxIndex = 9;
		for (int i = 9; i < 16; i++) {
			short val = this.net.getOutputValue(i);
			if (val > maxVal) {
				maxIndex = i;
				maxVal = val;
			}
		}
		int power = powerValueFromNeuroActivation(this.net.getOutputValue(17));
		switch (maxIndex) {
		case 9:
			player.kick(power, 60);
			break;
		case 10:
			player.kick(power, 40);
			break;
		case 11:
			player.kick(power, 20);
			break;
		case 12:
		case 13:
			player.kick(power, 0);
			break;
		case 14:
			player.kick(power, -20);
			break;
		case 15:
			player.kick(power, -40);
			break;
		case 16:
			player.kick(power, -60);
			break;
		default:
			// Nothing to do
		}
	}

	private int powerValueFromNeuroActivation(short val) {
		int index = val / 5;
		switch (index) {
		case 0:
			return -100;
		case 1:
			return -58;
		case 2:
			return -25;
		case 3:
			return 1;
		case 4:
			return 22;
		case 5:
			return 38;
		case 6:
			return 51;
		case 7:
			return 62;
		case 8:
			return 70;
		case 9:
			return 77;
		case 10:
			return 82;
		case 11:
			return 86;
		case 12:
			return 89;
		case 13:
			return 92;
		case 14:
			return 94;
		case 15:
			return 96;
		case 16:
			return 97;
		case 17:
			return 98;
		case 18:
		case 19:
			return 99;
		case 20:
			return 100;
		default: 
			throw new IllegalStateException("invalid activation value " + val);
		}
	}

	private void addTurnCommandFromOutputLayer(Player player) {
		short maxVal = (short) 0;
		int maxIndex = 0;
		for (int i = 0; i < 8; i++) {
			short val = this.net.getOutputValue(i);
			if (val > maxVal) {
				maxIndex = i;
				maxVal = val;
			}
		}
		switch (maxIndex) {
		case 0:
			player.turn(50);
			break;
		case 1:
			player.turn(30);
			break;
		case 2:
			player.turn(10);
			break;
		case 3:
		case 4:
			player.turn(0);
			break;
		case 5:
			player.turn(-10);
			break;
		case 6:
			player.turn(-30);
			break;
		case 7:
			player.turn(-50);
			break;
		default:
			// Nothing to do
		}
	}

	private void setInputLayer() {
		this.net.setInputValue(0, this.retinaFlagLeft.getA());
		this.net.setInputValue(1, this.retinaFlagLeft.getB());
		this.net.setInputValue(2, this.retinaFlagLeft.getC());
		this.net.setInputValue(3, this.retinaFlagLeft.getD());
		this.net.setInputValue(4, this.retinaFlagRight.getA());
		this.net.setInputValue(5, this.retinaFlagRight.getB());
		this.net.setInputValue(6, this.retinaFlagRight.getC());
		this.net.setInputValue(7, this.retinaFlagRight.getD());
		this.net.setInputValue(8, this.retinaFlagOwn.getA());
		this.net.setInputValue(9, this.retinaFlagOwn.getB());
		this.net.setInputValue(10, this.retinaFlagOwn.getC());
		this.net.setInputValue(11, this.retinaFlagOwn.getD());
		this.net.setInputValue(12, this.retinaFlagPenaltyOther.getA());
		this.net.setInputValue(13, this.retinaFlagPenaltyOther.getB());
		this.net.setInputValue(14, this.retinaFlagPenaltyOther.getC());
		this.net.setInputValue(15, this.retinaFlagPenaltyOther.getD());
		this.net.setInputValue(16, this.retinaGoalOther.getA());
		this.net.setInputValue(17, this.retinaGoalOther.getB());
		this.net.setInputValue(18, this.retinaGoalOther.getC());
		this.net.setInputValue(19, this.retinaGoalOther.getD());
		this.net.setInputValue(20, this.retinaPlayerOther.getA());
		this.net.setInputValue(21, this.retinaPlayerOther.getB());
		this.net.setInputValue(22, this.retinaPlayerOther.getC());
		this.net.setInputValue(23, this.retinaPlayerOther.getD());
		this.net.setInputValue(24, this.retinaPlayerOwn.getA());
		this.net.setInputValue(25, this.retinaPlayerOwn.getB());
		this.net.setInputValue(26, this.retinaPlayerOwn.getC());
		this.net.setInputValue(27, this.retinaPlayerOwn.getD());
		this.net.setInputValue(28, this.retinaBall.getA());
		this.net.setInputValue(29, this.retinaBall.getB());
		this.net.setInputValue(30, this.retinaBall.getC());
		this.net.setInputValue(31, this.retinaBall.getD());
	}

	public N getNet() {
		return this.net;
	}

	public void setNet(N net) {
		this.net = net;
	}
}
