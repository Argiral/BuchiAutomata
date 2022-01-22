package Interfaces;

public interface IState {

    /**
     * Whether the state is an accepting state
     * @return true if the state is a final state, false if not
     */
    boolean isFinal();

    /**
     * Get a key ("name") of the state
     * @return The name of the state
     */
    String getKey();
}
