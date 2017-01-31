package vsoc.camps;

import java.io.Serializable;

import vsoc.VsocInvalidConfigurationException;
import vsoc.behaviour.Behaviour;
import vsoc.behaviour.BehaviourController;
import vsoc.nn.Net;
import vsoc.nn.feedforward.AbstractFFNetConnector;
import vsoc.nn.feedforward.DefaultFFConnector;

/**
 * Atan control system that controls the robots by means of a neuronal network.
 */
public class BehaviourNeuroControlSystem extends BehaviourController implements
        Serializable {

    private static final long serialVersionUID = 0L;

    private NetBehaviour netBehaviour = null;

    public BehaviourNeuroControlSystem() {
        super();
    }
    
    public BehaviourNeuroControlSystem(Behaviour behaviour) {
        super(behaviour);
        this.netBehaviour = getNetBehaviour(behaviour);
    }

    private NetBehaviour getNetBehaviour(Behaviour behav) {
        if (behav == null) {
            throw new VsocInvalidConfigurationException(
                    "The Behaviour of a NeuroControlSystem must contain exactly one net NetBehaviour.");
        } else if (behav instanceof NetBehaviour) {
            return (NetBehaviour) behav;
        } else {
            return getNetBehaviour(behav.getChild());
        }
    }

    public Net getNet() {
        return this.netBehaviour.getNet();
    }

    public void setNet(Net net) {
        this.netBehaviour.setNet(net);
    }

    public AbstractFFNetConnector createNetConnector() {
        return new DefaultFFConnector();
    }

}
