package vsoc.genetic;import vsoc.nn.Net;/*** Interface for Objects that have the capability to genereate Objects of the same* type by applying genetic operations to parent objects.*/public interface Crossable<T extends Crossable<T>> {    /**    * Creates a new instance of a new crossable object by applying    * crossover and mutation on two parent objects.    */    T newChild(T otherParent, double mutationRate);    void setParametersRandom(long seed);        double distance(Net n);    }