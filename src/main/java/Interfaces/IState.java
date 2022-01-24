package Interfaces;

import java.util.List;

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

    /**
     * Return a list containing the state as element
     * @return A list with the state
     */
    List<IState> toList();

    /**
     * Rename the state
     * @param name The new name of the state
     */
    void rename(String name);

    /**
     * Set state as final/non-final
     * @param isFinal True if the state is to be set final, false if non-final
     */
    void setFinal(boolean isFinal);

}
