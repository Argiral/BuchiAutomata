package Control;

import Interfaces.IAutomata;
import Interfaces.IState;
import Interfaces.ITransition;
import Model.BuchiAutomata;
import Model.State;

import java.util.HashMap;
import java.util.Map;

public class BuchiAlgorithms {

    public static IAutomata reduceDegOfNonDeterminism(IAutomata automata) {
        // TODO implement reduceDegOfNonDeterminism
        return null;
    }


    public static IAutomata union(IAutomata first, IAutomata second) {
        // TODO implement union

        // Generate new automata (and new initial state)
        IAutomata newAutomata = new BuchiAutomata();
        IState start = new State("q0");
        newAutomata.addState(start);

        // Save the initial states of the original automata
        IState startA1 = first.getInitialState();
        IState startA2 = second.getInitialState();

        // Copy first automata (changing names)
        Map<String, IState> firstStates = new HashMap<>();
        for (IState s : first.getStates()) {
            IState nState = new State(s.getKey() + "_first", s.isFinal());
            newAutomata.addState(nState);
            // Keep track of changes (to easier add transitions)
            firstStates.put(s.getKey(), nState);
        }

        for (ITransition t : first.getTransitionsList()) {
            // Use firstStates dictionary to retrive new state from old label
            newAutomata.addTransition(firstStates.get(t.getSource().getKey()), t.getSymbol(), firstStates.get(t.getDestination().getKey()));

            // Add transitions from new starting state
            if (t.getSource().equals(startA1)) {
                // Add transition from the new starting state
                newAutomata.addTransition(start, t.getSymbol(), firstStates.get(t.getDestination().getKey()));
            }
        }

        // Copy second automata (changing names)
        Map<String, IState> secondStates = new HashMap<>();
        for (IState s : second.getStates()) {
            IState nState = new State(s.getKey() + "_second", s.isFinal());
            newAutomata.addState(nState);
            // Keep track of changes (to easier add transitions)
            secondStates.put(s.getKey(), nState);
        }

        for (ITransition t : second.getTransitionsList()) {
            // Use firstStates dictionary to retrive new state from old label
            newAutomata.addTransition(secondStates.get(t.getSource().getKey()), t.getSymbol(), secondStates.get(t.getDestination().getKey()));

            // Add transitions from new starting state
            if (t.getSource().equals(startA2)) {
                // Add transition from the new starting state
                newAutomata.addTransition(start, t.getSymbol(), secondStates.get(t.getDestination().getKey()));
            }
        }

        return newAutomata;
    }

    public static IAutomata intersection(IAutomata first, IAutomata second) {
        // TODO implement intersection
        return null;
    }

    public static IAutomata complement(IAutomata automata) {
        // For deterministic BA, complement is much easier
        if (automata.isDeterministic()) {
            return complementDBA(automata);
        } else {
            return complementNBA(automata);
        }
    }

    private static IAutomata complementDBA(IAutomata automata) {
        // TODO implement
        assert automata.isDeterministic();
        return null;
    }

    private static IAutomata complementNBA(IAutomata automata) {
        // TODO implement
        return null;
    }
}
