package Interfaces;

public interface ITransition {

    /**
     * Get the starting state
     * @return Staring state ("from")
     */
    IState getSource();

    /**
     * Get the destination state
     * @return Destination state ("to")
     */
    IState getDestination();

    /**
     * Get the transition symbol
     * @return Model.Transition symbol
     */
    char getSymbol();
}
