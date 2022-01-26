package Interfaces;

import java.util.Collection;
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
     * Remove a state from the automata, and
     * all the transitions concerning this state
     * @param state The state to remove
     * @return True if the state existed in the automata, else false
     */
    boolean removeState(IState state);

    /**
     * Remove a state from the automata, and
     * all the transitions concerning this state
     * @param stateKey The key (name) of the state
     * @return True if the state existed in the automata, else false
     */
    boolean removeState(String stateKey);

    /**
     * Add a new transition to the automata
     * @param from The origin state
     * @param symbol The transition symbol
     * @param to The destination state
     */
    void addTransition(IState from, char symbol, IState to);

    /**
     * Remove a transition from the automata
     * @param transition The transition to remove
     * @return True if the transition existed in the automata, else false
     */
    boolean removeTransition(ITransition transition);

    /**
     * Remove a transition from the automata
     * @param source The source state
     * @param symbol The transition symbol
     * @param destination The destination state
     * @return True if the transition existed in the automata, else false
     */
    boolean removeTransition(IState source, char symbol, IState destination);

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
     * Get the state with a given name
     * @param name the name of the state to retrive
     * @return The requested state, or null if no state with the given name exist in the automata
     */
    IState getStateByKey(String name);

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
     * Run the automata on a given string starting from a list of initial states
     * and return the list of reachable states
     * @param word The word to read
     * @param initialStates The list of initial states
     * @return The list of states that can be reached
     */
    List<IState> run(String word, Collection<IState> initialStates);

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
     * Read a symbol (char) starting from a list of given states
     * and return the list of reachable states
     * @param symbol The char to read
     * @param initialStates The list of initial states
     * @return The list of states that can be reached
     */
    List<IState> run(char symbol, Collection<IState> initialStates);

    /**
     * Indicate the degree of non-determinism
     * If for each state and symbol there is at most 1 transition -> 1 (is deterministic)
     * If at most 2 -> 2
     * ...
     * @return Degree of non-determinism of the automata
     */
    long degreeOfNonDeterminism();

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

    /**
     * Return statistics about the automata
     * e.g. number of states, transitions
     * @return a string describing the automata
     */
    String getStatistics();

    /**
     * Clone the automata and return an exact copy
     * @return A clone of the automata
     */
    IAutomata clone();
}
