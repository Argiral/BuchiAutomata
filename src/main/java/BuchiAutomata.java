import Interfaces.IAutomata;
import Interfaces.IState;
import Interfaces.ITransition;

import java.util.*;
import java.util.stream.Collectors;

public class BuchiAutomata implements IAutomata {
    private IState initialState = null;
    private final List<IState> states = new LinkedList<>();
    private final Map<IState, List<ITransition>> transitions  = new HashMap<>();
    private final Set<Character> alphabet;

    public BuchiAutomata() {
        this.alphabet = new HashSet<>();
    }

    public BuchiAutomata(Set<Character> alphabet) {
        this.alphabet = alphabet;
    }


    /**
     * Add a new state to the automata
     *
     * @param state The new state to add
     */
    @Override
    public void addState(IState state) {
        // If this is the first state added, mark as initial state
        if (this.initialState == null) {
            this.initialState = state;
        }
        this.states.add(state);
    }

    /**
     * Add a new transition to the automata
     *
     * @param from   The origin state
     * @param symbol The transition symbol
     * @param to     The destination state
     */
    @Override
    public void addTransition(IState from, char symbol, IState to) {
        addTransition(new Transition(from, symbol, to));
    }

    /**
     * Add a new transition to the automata
     */
    public void addTransition(ITransition transition) {
        IState from = transition.getSource();

        // If the key is already present, update the list
        if (this.transitions.containsKey(from)) {
            this.transitions.get(from).add(transition);
        // If the key is absent, insert new (with new list)
        } else {
            List<ITransition> newList = new LinkedList<>();
            newList.add(transition);
            this.transitions.put(from, newList);
        }

        // If necessary, add transition symbol to the alphabet
        // Since it is a set, no need to check if already present -> set discards duplicates
        this.alphabet.add(transition.getSymbol());

    }

    /**
     * Add the initial state. If there is already an initial state, it will be replaced
     * If never specified, the first state added is set as the initial state
     *
     * @param state The new initial state
     */
    @Override
    public void setInitialState(IState state) {
        this.initialState = state;
    }

    /**
     * Return the initial state of the automata
     *
     * @return The initial state
     */
    @Override
    public IState getInitialState() {
        return this.initialState;
    }

    /**
     * @return The list of states
     */
    @Override
    public List<IState> getStates() {
        return this.states;
    }

    /**
     * @return The list of final (accepting) states
     */
    @Override
    public List<IState> getFinalStates() {
        return this.states.stream().filter(IState::isFinal).collect(Collectors.toList());
    }

    /**
     * Get the list of transitions as a map
     * The map is in form of states -> transition starting from that state
     *
     * @return The map of transitions
     */
    @Override
    public Map<IState, List<ITransition>> getTransitionsMap() {
        return this.transitions;
    }

    /**
     * Get the list of transitions as a list
     *
     * @return The list of transitions
     */
    @Override
    public List<ITransition> getTransitionsList() {
        List<ITransition> list = new LinkedList<>();

        for (List<ITransition> l : this.transitions.values()) {
            list.addAll(l);
        }

        return list;
    }



    /**
     * Run the automata on a given string starting from the initial state
     * and return the list of reachable states
     *
     * @param word The word to read
     * @return The list of states that can be reached
     */
    @Override
    public List<IState> run(String word) {
        // TODO implement run
        return null;
    }

    /**
     * Run the automata on a given string starting from a given state
     * and return the list of reachable states
     *
     * @param word         The word to read
     * @param initialState The current state
     * @return The list of states that can be reached
     */
    @Override
    public List<IState> run(String word, IState initialState) {
        // TODO implement run
        return null;
    }

    /**
     * Read a symbol (char) starting from the initial state
     * and return the list of reachable states
     *
     * @param symbol The char to read
     * @return The list of states that can be reached
     */
    @Override
    public List<IState> run(char symbol) {
        // TODO implement run
        return null;
    }

    /**
     * Read a symbol (char) starting from a given state
     * and return the list of reachable states
     *
     * @param symbol       The char to read
     * @param initialState The current state
     * @return The list of states that can be reached
     */
    @Override
    public List<IState> run(char symbol, IState initialState) {
        // TODO implement run
        return null;
    }

    /**
     * Whether the automata is deterministic
     *
     * @return true if deterministic, false if not
     */
    @Override
    public boolean isDeterministic() {
        // The automata is NOT deterministic if there is at least a state with
        // 2 outgoing transitions with the same symbol

        // Iterate over each starting state
        for (List<ITransition> l : this.transitions.values()) {
            // Check for each possible symbol if there are 2 or more transitions
            for (char c : this.alphabet) {
                if (l.stream().filter(t -> t.getSymbol() == c).count() >= 2) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if the automata is complete
     * Only make sens for deterministic automata
     *
     * @return true if the automata is complete, else not
     */
    @Override
    public boolean isComplete() {
        // Make sens only for deterministic automata
        if (!this.isDeterministic()) {
            return false;
        }

        // Iterate over each starting state
        for (List<ITransition> l : this.transitions.values()) {
            // Check for each possible symbol if there is exactly 1 transition
            for (char c : this.alphabet) {
                if (l.stream().filter(t -> t.getSymbol() == c).count() != 1) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Add a new symbol to the alphabet considered by the automata
     *
     * @param c The new valid symbol
     */
    @Override
    public void addSymbolToAlphabet(char c) {
        if (!this.alphabet.add(c)) {
            System.out.println("The alphabet already contain " + c);
        }
    }

    /**
     * Get the alphabet considered from the automata
     *
     * @return the list of valid chars
     */
    @Override
    public Set<Character> getAlphabet() {
        return this.alphabet;
    }
}
