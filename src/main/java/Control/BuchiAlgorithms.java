package Control;

import Interfaces.IAutomata;
import Interfaces.IState;
import Interfaces.ITransition;
import Model.BuchiAutomata;
import Model.State;

import java.util.*;
import java.util.stream.Collectors;

public class BuchiAlgorithms {

    public static IAutomata reduceDegOfNonDeterminism(IAutomata automata) {
        IAutomata newAutomata = new BuchiAutomata();

        // Keep track of the nodes to complete
        Set<String> toCheck = new HashSet<>();
        Set<String> alreadyChecked = new HashSet<>();
        // Keep track of the lists of states represented by the new states
        Map<String, List<IState>> nameToList = new HashMap<>();

        // Initial state of the new automata
        IState newq0 = new State("{" + automata.getInitialState().getKey() + "}", automata.getInitialState().isFinal());
        newAutomata.addState(newq0);

        // Add the initial state to the list of states to check
        toCheck.add(newq0.getKey());
        nameToList.put(newq0.getKey(), automata.getInitialState().toList());

        while (!toCheck.isEmpty()) {
            // Get next element
            String stateToCheck = toCheck.iterator().next();
            // Get state. Should have been created when found to check
            IState newSource = newAutomata.getStateByKey(stateToCheck);
            if (newSource == null) {
                System.err.println("State " + stateToCheck + " not found...");
            }

            // Get transitions for each symbol
            for (char symbol : automata.getAlphabet()) {
                Set<IState> reachable = new HashSet<>();
                // Get all reachable state from the given state and reading the given symbol
                // Iterate over each state
                for (IState state : nameToList.get(stateToCheck)) {
                    reachable.addAll(automata.run(symbol, state));
                }

                // Split into final and non-final
                Set<IState>
                        reachable_final = new HashSet<>(),
                        reachable_normal = new HashSet<>();
                for (IState state : reachable) {
                    if (state.isFinal()) {
                        reachable_final.add(state);
                    } else {
                        reachable_normal.add(state);
                    }
                }


                String  // names
                        new_final_state_key = "{" + reachable_final.stream().map(IState::getKey).sorted().collect(Collectors.joining(",")) + "}",
                        new_normal_state_key = "{" + reachable_normal.stream().map(IState::getKey).sorted().collect(Collectors.joining(",")) + "}";
                IState  // states
                        new_final_state = automata.getStateByKey(new_final_state_key),
                        new_normal_state = automata.getStateByKey(new_normal_state_key);

                // Ev create new states
                if (new_final_state == null) {
                    new_final_state = new State(new_final_state_key, true);
                }

                if (new_normal_state == null) {
                    new_normal_state = new State(new_normal_state_key);
                }

                create_new_state(newAutomata, toCheck, alreadyChecked, nameToList, newSource, symbol, reachable_final, new_final_state_key, new_final_state);
                create_new_state(newAutomata, toCheck, alreadyChecked, nameToList, newSource, symbol, reachable_normal, new_normal_state_key, new_normal_state);
            }

            toCheck.remove(stateToCheck);
            alreadyChecked.add(stateToCheck);
        }

        return newAutomata;
    }

    private static void create_new_state(IAutomata newAutomata, Set<String> toCheck, Set<String> alreadyChecked, Map<String, List<IState>> nameToList, IState newSource, char symbol, Set<IState> reachable_states, String new_state_key, IState new_state) {
        if (!reachable_states.isEmpty()) {
            // Add state to automata
            if (!newAutomata.getStates().contains(new_state)) {
                newAutomata.addState(new_state);
            }

            // Update map
            nameToList.put(new_state_key, new LinkedList<>(reachable_states));

            // Add to set to check
            if (!alreadyChecked.contains(new_state_key)) {
                toCheck.add(new_state_key);
            }

            // Add new transition
            newAutomata.addTransition(newSource, symbol, new_state);
        }
    }


    public static IAutomata union(IAutomata first, IAutomata second) {
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
        System.err.println("Not yet implemented!");
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
        System.err.println("Not yet implemented!");
        return null;
    }

    private static IAutomata complementNBA(IAutomata automata) {
        // TODO implement
        System.err.println("Not yet implemented!");
        return null;
    }
}
