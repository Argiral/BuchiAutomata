package Model;

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
     * Remove a state from the automata, and
     * all the transitions concerning this state
     *
     * @param state The state to remove
     * @return True if the state existed in the automata, else false
     */
    @Override
    public boolean removeState(IState state) {
        boolean exist = this.states.remove(state);

        // Remove all the related transitions
        for (ITransition t : this.getTransitionsList()) {
            if (t.getSource().equals(state) || t.getDestination().equals(state)) {
                removeTransition(t);
            }
        }

        return exist;
    }

    /**
     * Remove a state from the automata, and
     * all the transitions concerning this state
     *
     * @param stateKey The key (name) of the state
     * @return True if the state existed in the automata, else false
     */
    @Override
    public boolean removeState(String stateKey) {
        IState state = getStateByKey(stateKey);
        if (state == null) {
            return false;
        }
        return removeState(state);
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
     * Remove a transition from the automata
     *
     * @param transition The transition to remove
     * @return True if the transition existed in the automata, else false
     */
    @Override
    public boolean removeTransition(ITransition transition) {
        List<ITransition> ts = this.getTransitionsMap().get(transition.getSource());
        if (ts != null) {
            boolean removed =  ts.remove(transition);
            // Remove map entry if necessary
            if (this.getTransitionsMap().get(transition.getSource()).isEmpty()) {
                this.transitions.remove(transition.getSource());
            }
            return removed;
        }
        return false;
    }

    /**
     * Remove a transition from the automata
     *
     * @param source      The source state
     * @param symbol      The transition symbol
     * @param destination The destination state
     * @return True if the transition existed in the automata, else false
     */
    @Override
    public boolean removeTransition(IState source, char symbol, IState destination) {
        return removeTransition(new Transition(source, symbol, destination));
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
     * Get the state with a given name
     *
     * @param name the name of the state to retrive
     * @return The requested state, or null if no state with the given name exist in the automata
     */
    @Override
    public IState getStateByKey(String name) {
        for (IState s : this.states) {
            if (s.getKey().equals(name)) {
                return s;
            }
        }
        return null;
    }

    /**
     * @return The list of states
     */
    @Override
    public List<IState> getStates() {
        return this.states;
    }

    /**
     * Get the number of states in the automata
     *
     * @return The number of states
     */
    @Override
    public int numberOfStates() {
        return this.states.size();
    }

    /**
     * Get the number of transitions in the automata
     *
     * @return The number of transitions
     */
    @Override
    public int numberOfTransitions() {
        return this.getTransitionsList().size();
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
        return run(symbol, this.initialState);
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
        Set<IState> reachable = new HashSet<>();
        // Return empty list if no transitions available from given state
        if (this.transitions.get(initialState) == null) {
            return new LinkedList<>();
        }
        // Get all the transition from the given state
        for (ITransition t : this.transitions.get(initialState)) {
            // Check if the symbol is correct
            if (t.getSymbol() == symbol) {
                reachable.add(t.getDestination());
            }
        }
        return new LinkedList<>(reachable);
    }

    /**
     * Indicate the degree of non-determinism
     * If for each state and symbol there is at most 1 transition -> 1 (is deterministic)
     * If at most 2 -> 2
     * ...
     *
     * @return Degree of non-determinism of the automata
     */
    @Override
    public long degreeOfNonDeterminism() {
        long max = 0;
        for (List<ITransition> l : this.transitions.values()) {
            // Check for each possible symbol if there are 2 or more transitions
            for (char c : this.alphabet) {
                max = Math.max(max, l.stream().filter(t -> t.getSymbol() == c).count());
            }
        }
        return max;
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

        return this.degreeOfNonDeterminism() <= 1;

//        // Iterate over each starting state
//        for (List<ITransition> l : this.transitions.values()) {
//            // Check for each possible symbol if there are 2 or more transitions
//            for (char c : this.alphabet) {
//                if (l.stream().filter(t -> t.getSymbol() == c).count() >= 2) {
//                    return false;
//                }
//            }
//        }
//        return true;
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

    /**
     * Return statistics about the automata
     * e.g. number of states, transitions
     *
     * @return a string describing the automata
     */
    @Override
    public String getStatistics() {
        StringBuilder finalStatesString = new StringBuilder("[");
        for (IState iState : this.getFinalStates()) {
            finalStatesString.append(iState.getKey());
            finalStatesString.append(";");
        }
        finalStatesString.deleteCharAt(finalStatesString.length() - 1); // remove last ';'
        finalStatesString.append("]");


        return "Buchi automata\n" +
                "\tnumber of states: " + this.states.size() + "\n" +
                "\t\tinitial state: " + this.initialState.getKey() + "\n" +
                "\t\tfinal states:" + finalStatesString + "\n" +
                "\tnumber of transitions: " + this.getTransitionsList().size() + "\n" +
                "\tdeterministic: " + this.isDeterministic() + " (deg: " + this.degreeOfNonDeterminism() + ")\n" +
                "\tcomplete: " + this.isComplete() + "\n";
    }
}
