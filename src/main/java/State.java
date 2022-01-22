import Interfaces.IState;

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
}
