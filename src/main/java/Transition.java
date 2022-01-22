import Interfaces.IState;
import Interfaces.ITransition;

public class Transition implements ITransition {
    IState from, to;
    char symbol;

    public Transition(IState from, char symbol, IState to) {
        this.from = from;
        this.to = to;
        this.symbol = symbol;
    }

    /**
     * Get the starting state
     *
     * @return Staring state ("from")
     */
    @Override
    public IState getSource() {
        return this.from;
    }

    /**
     * Get the destination state
     *
     * @return Destination state ("to")
     */
    @Override
    public IState getDestination() {
        return this.to;
    }

    /**
     * Get the transition symbol
     *
     * @return Transition symbol
     */
    @Override
    public char getSymbol() {
        return this.symbol;
    }
}
