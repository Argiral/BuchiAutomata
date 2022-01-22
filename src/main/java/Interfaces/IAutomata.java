package Interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IAutomata {

    /**
     * Add a new state to the automata
     * @param state The new state to add
     */
    void addState(IState state);

    /**
     * Add a new transition to the automata
     * @param from The origin state
     * @param symbol The transition symbol
     * @param to The destination state
     */
    void addTransition(IState from, char symbol, IState to);

    /**
     * Add the initial state. If there is already an initial state, it will be replaced
     * If never specified, the first state added is set as the initial state
     * @param state The new initial state
     */
    void setInitialState(IState state);

    /**
     * Return the initial state of the automata
     * @return The initial state
     */
    IState getInitialState();

    /**
     * Add a new transition to the automata
     */
    void addTransition(ITransition transition);

    /**
     *
     * @return The list of states
     */
    List<IState> getStates();

    /**
     * Get the number of states in the automata
     * @return The number of states
     */
    int numberOfStates();

    /**
     * Get the number of transitions in the automata
     * @return The number of transitions
     */
    int numberOfTransitions();

    /**
     *
     * @return The list of final (accepting) states
     */
    List<IState> getFinalStates();

    /**
     * Get the list of transitions as a map
     * The map is in form of states -> transition starting from that state
     * @return The map of transitions
     */
    Map<IState, List<ITransition>> getTransitionsMap();

    /**
     * Get the list of transitions as a list
     * @return The list of transitions
     */
    List<ITransition> getTransitionsList();


    /**
     * Run the automata on a given string starting from the initial state
     * and return the list of reachable states
     * @param word The word to read
     * @return The list of states that can be reached
     */
    List<IState> run(String word);

    /**
     * Run the automata on a given string starting from a given state
     * and return the list of reachable states
     * @param word The word to read
     * @param initialState The current state
     * @return The list of states that can be reached
     */
    List<IState> run(String word, IState initialState);

    /**
     * Read a symbol (char) starting from the initial state
     * and return the list of reachable states
     * @param symbol The char to read
     * @return The list of states that can be reached
     */
    List<IState> run(char symbol);

    /**
     * Read a symbol (char) starting from a given state
     * and return the list of reachable states
     * @param symbol The char to read
     * @param initialState The current state
     * @return The list of states that can be reached
     */
    List<IState> run(char symbol, IState initialState);

    /**
     * Whether the automata is deterministic
     * @return true if deterministic, false if not
     */
    boolean isDeterministic();

    /**
     * Check if the automata is complete
     * Only make sens for deterministic automata
     * @return true if the automata is complete, else not
     */
    boolean isComplete();

    /**
     * Add a new symbol to the alphabet considered by the automata
     * @param c The new valid symbol
     */
    void addSymbolToAlphabet(char c);

    /**
     * Get the alphabet considered from the automata
     * @return the list of valid chars
     */
    Set<Character> getAlphabet();
}
