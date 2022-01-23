package Model;

import Interfaces.IState;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class State implements IState {
    private String name;
    private boolean isFinal;

    public State(String name) {
        this.name = name;
        this.isFinal = false;
    }

    public State(String name, boolean isFinal) {
        this.name = name;
        this.isFinal = isFinal;
    }

    public static State createFinalState(String name) {
        return new State(name, true);
    }

    /**
     * Whether the state is an accepting state
     *
     * @return true if the state is a final state, false if not
     */
    @Override
    public boolean isFinal() {
        return this.isFinal;
    }

    /**
     * Get a key ("name") of the state
     *
     * @return The name of the state
     */
    @Override
    public String getKey() {
        return this.name;
    }

    /**
     * Return a list containing the state as element
     *
     * @return A list with the state
     */
    @Override
    public List<IState> toList() {
        List<IState> l = new LinkedList<>();
        l.add(this);
        return l;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return isFinal == state.isFinal && name.equals(state.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isFinal);
    }
}
