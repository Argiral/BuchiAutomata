package Model;

import Interfaces.IState;
import Interfaces.ITransition;

import java.util.Objects;

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
     * @return Model.Transition symbol
     */
    @Override
    public char getSymbol() {
        return this.symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;
        return symbol == that.symbol && from.equals(that.from) && to.equals(that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, symbol);
    }
}
