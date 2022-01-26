package Control;

import Interfaces.IAutomata;
import Interfaces.IState;
import Interfaces.ITransition;
import Model.BuchiAutomata;
import Model.State;
import javafx.util.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public static IAutomata union(IAutomata first, IAutomata second) {
        // Generate new automata (and new initial state)
        IAutomata newAutomata = new BuchiAutomata();
        IState start = new State("q0");
        newAutomata.addState(start);

        copyIntoNew(newAutomata, start, first, "_first");
        copyIntoNew(newAutomata, start, second, "_second");

        return newAutomata;
    }

    public static IAutomata intersection(IAutomata first, IAutomata second) {
        IAutomata newAutomata = new BuchiAutomata();

        // Keep track of the nodes to complete
        Set<String> toCheck = new HashSet<>();

        // Keep track of the lists of states represented by the new states
        Map<String, Pair<IState, IState>> nameToList = new HashMap<>();

        // Initial state of the new automata
        IState newq0 = new State("{" + first.getInitialState().getKey() + "," + second.getInitialState().getKey() + ",0}", false);
        newAutomata.addState(newq0);

        // Add the initial state to the list of states to check
        toCheck.add(newq0.getKey());
        nameToList.put(newq0.getKey(), new Pair<>(first.getInitialState(), second.getInitialState()));

        // Alphabets union
        Set<Character> newAlphabet = first.getAlphabet();
        newAlphabet.addAll(second.getAlphabet());

        // Construct every new state
        while (!toCheck.isEmpty()) {
            // Get next state to construct
            String stateToCheckName = toCheck.iterator().next();
            IState stateToCheck = newAutomata.getStateByKey(stateToCheckName);
            assert stateToCheck != null;
            // Get label
            int label = Integer.parseInt(stateToCheckName.split(",")[2].charAt(0) + "");

            // Get currents states in old automatas
            IState s1 = nameToList.get(stateToCheckName).getKey();
            IState s2 = nameToList.get(stateToCheckName).getValue();

            // For every symbol
            for (char symbol : newAlphabet) {
                // Get all the transitions from the given states and with the current symbol
                List<ITransition> t1 = first.getTransitionsMap().get(s1).stream().filter(s -> s.getSymbol() == symbol).collect(Collectors.toList());
                List<ITransition> t2 = second.getTransitionsMap().get(s2).stream().filter(s -> s.getSymbol() == symbol).collect(Collectors.toList());

                // For all combinations of transitions
                for (Pair<ITransition, ITransition> tr : getCombinations(t1, t2)) {
                    // Get the destination states
                    IState newState1 = tr.getKey().getDestination();
                    IState newState2 = tr.getValue().getDestination();

                    // Determine new label
                    int newLabel;
                    switch (label) {
                        case 0:
                        case 2:
                            if (newState1.isFinal()) {
                                // Shortcuts: in the original algorithm, here -> 1
                                // But make sens to check if both are already final
                                if (newState2.isFinal()) {
                                    newLabel = 2;
                                } else {
                                    newLabel = 1;
                                }
                            } else {
                                newLabel = 0;
                            }
                            break;
                        case 1:
                            if (newState2.isFinal()) {
                                newLabel = 2;
                            } else {
                                newLabel = 1;
                            }
                            break;
                        default:
                            newLabel = 9;
                            System.err.println("ERROR: Label not correctly identified, problems are to be expected\n (intersection algorithm)");
                            break;
                    }
                    // Create state in the new automata if missing
                    String destStateName = "{" + newState1.getKey() + "," + newState2.getKey() + "," + newLabel + "}";
                    IState newState = newAutomata.getStateByKey(destStateName);
                    if (newState == null) {
                        newState = new State(destStateName, newLabel == 2);
                        // Add to automata
                        newAutomata.addState(newState);
                        // Add state to list toCheck
                        toCheck.add(destStateName);
                        // Update dictionary
                        nameToList.put(destStateName, new Pair<>(newState1, newState2));
                    }
                    // Add transition
                    newAutomata.addTransition(stateToCheck, symbol, newState);
                }
            }

            // Remove state from list to check
            toCheck.remove(stateToCheckName);
        }

        return newAutomata;
    }

    public static IAutomata complement(IAutomata automata) {
        // For deterministic BA, complement is much easier
        if (automata.isDeterministic()) {
            return complementDBA(automata);
        } else {
            return complementNBA(automata);
        }
    }

    public static IAutomata complementDBA(IAutomata automata) {
        assert automata.isDeterministic();

        // Create new automata
        IAutomata newAutomata = new BuchiAutomata();

        // Keep a map of the new states for faster retrieval
        Map<String, IState> newStatesMap = new HashMap<>();

        // Create new states
        for (IState s : automata.getStates()) {
            // Copy all the states and make not final
            IState origState = new State(s.getKey(), false);
            newAutomata.addState(origState);
            newStatesMap.put(s.getKey(), origState);

            // Copy non-final states and make final
            if (!s.isFinal()) {
                IState normalState = new State(s.getKey() + "_new", true);
                newAutomata.addState(normalState);
                newStatesMap.put(s.getKey() + "_new", normalState);
            }
        }

        // Create new transitions
        for (ITransition t : automata.getTransitionsList()) {
            // Copy all the original transitions
            newAutomata.addTransition(newStatesMap.get(t.getSource().getKey()), t.getSymbol(), newStatesMap.get(t.getDestination().getKey()));

            // Create new transitions to the new final states (previous non-final)
            if (!t.getDestination().isFinal()) {
                newAutomata.addTransition(newStatesMap.get(t.getSource().getKey()), t.getSymbol(), newStatesMap.get(t.getDestination().getKey() + "_new"));

                // Create transitions between 2 final states (in the new automata)
                if (!t.getSource().isFinal()) {
                    newAutomata.addTransition(newStatesMap.get(t.getSource().getKey() + "_new"), t.getSymbol(), newStatesMap.get(t.getDestination().getKey() + "_new"));
                }
            }
        }

        // Ev. add trap state
        IState trapState = new State("trap", true);
        newAutomata.addState(trapState);

        boolean usedTrap = false;
        // Add only transitions from original states
        // Transitions from new states would not be wrong but are not necessary
        for (IState s : automata.getStates()) {
            IState newState = newStatesMap.get(s.getKey());
            // Check that every state has a transition for every symbol
            for (char symbol : newAutomata.getAlphabet()) {
                // If no transition with the given symbol available, add it (to trap state)
                if (newAutomata.getTransitionsMap().get(newState).stream().noneMatch(t -> t.getSymbol() == symbol)) {
                    usedTrap = true;
                    newAutomata.addTransition(newState, symbol, trapState);
                }
            }
        }

        // Remove trap state if not necessary, else add loops
        if (!usedTrap) {
            newAutomata.removeState(trapState);
        } else {
            for (char symbol : newAutomata.getAlphabet()) {
                newAutomata.addTransition(trapState, symbol, trapState);
            }
        }

        return newAutomata;
    }

    public static IAutomata complementNBA(IAutomata automata) {
        // "<font color=\"red\">{q0}</font>, <font color=\"green\">{q1}</font>"

        //############## Construct "upper part" ##############
        IAutomata subset = greedySubsetConstruction(automata);

        IAutomata newAutomata = new BuchiAutomata();
        for (IState s : subset.getStates()) {
            newAutomata.addState(s);
        }
        for (ITransition t : subset.getTransitionsList()) {
            newAutomata.addTransition(t);
        }

        // Keep track of the nodes to complete
        Set<String> toCheck = new HashSet<>();

        //############## Add missing transitions to final trap state ##############
        // TODO complement NBA trap state

        // ############## Construct lower part and initialize coloring ##############
        Map<IState, IState> upToDown = new HashMap<>();
        Map<String, List<List<IState>>> nameToLists = new HashMap<>();
        Map<String, Boolean> containsRed = new HashMap<>();
        Map<Pair<String, List<IState>>, String> stateSubStateToColor = new HashMap<>();  // state and listOfStats to color (e.g. state {q0},{q1,q2} and substate [q1,q2] -> "red")

        // Copy states
        for (IState upperState : subset.getStates()) {
            List<List<IState>> lOfStates = statesFromName(upperState.getKey(), automata);
            // New state is final if none of those it represents was final
            boolean isFinal = true;
            StringBuilder temp = new StringBuilder();
            List<String> tempListOfColor = new LinkedList<>(); // can not already add to dictionary since in the loop we don't have yet the name of the final state
            for (List<IState> s : lOfStates) {
                String name = "{" + s.stream().map(IState::getKey).collect(Collectors.joining(",")) + "}";
                if (s.get(0).isFinal()) {
                    // Paint in red
                    temp.append("<font color=\"red\">").append(name).append("</font>");
                    isFinal = false;
                    tempListOfColor.add("red");
                } else {
                    // Paint in green
                    temp.append("<font color=\"green\">").append(name).append("</font>");
                    tempListOfColor.add("green");
                }
                temp.append(",");
            }
            // Remove last ","
            temp.deleteCharAt(temp.length() - 1);

            String newName = "(" + temp.toString() + ")";

            assert lOfStates.size() == tempListOfColor.size();
            for (int i = 0; i < lOfStates.size(); i++) {
                // Update color dictionary
                stateSubStateToColor.put(new Pair<>(newName.toString(), lOfStates.get(i)), tempListOfColor.get(i));
            }

            // Create new state
            IState lowerState = new State(newName.toString(), isFinal);
            // Keep track of the states represented in the new state
            nameToLists.put(newName.toString(), lOfStates);
            // Keep track of whether the new state contains red or not
            containsRed.put(newName.toString(), !isFinal);
            // Add the new state to the automata
            newAutomata.addState(lowerState);
            // Add to the list to check
            toCheck.add(newName.toString());
            // Save reference from upper state to lower
            upToDown.put(upperState, lowerState);
            // Save reference from new state to list of represented states
//            nameToLists.put(newName.toString(), states);
        }

        // Copy transitions
        for (ITransition t : subset.getTransitionsList()) {
            newAutomata.addTransition(t.getSource(), t.getSymbol(), upToDown.get(t.getDestination()));
        }


        // ############## Check all the possible transitions ##############
        // Construct every new state
        // Construct every new state
        while (!toCheck.isEmpty()) {
            // Get next state to construct
            String stateToCheckName = toCheck.iterator().next();
            boolean hasRed = containsRed.get(stateToCheckName);
            IState stateToCheck = newAutomata.getStateByKey(stateToCheckName);
            assert stateToCheck != null;

            // States ({}) represented in the state
            List<List<IState>> currentStates = nameToLists.get(stateToCheckName);
            // Reverse list --> start from right-most element
            Collections.reverse(currentStates);

            // Do for each symbol
            for (char symbol : automata.getAlphabet()) {
                // Get successors
                List<List<IState>> successors = new LinkedList<>();
                Map<List<IState>, List<IState>> statesToOrigin = new HashMap<>(); // Keep track of which states generated which states (important for coloring)
                Set<IState> alreadyAdded = new HashSet<>(); // Keep track of states already considered ("remove" part in the algorithm)

                for (List<IState> ls : currentStates) {
                    // Get successors of each element
                    List<IState> reachable = automata.run(symbol, ls);

                    // Split into final and non-final (with finals on the right)
                    List<IState> reachable_final = reachable.stream().filter(IState::isFinal).collect(Collectors.toList());
                    List<IState> reachable_normal = reachable.stream().filter(s -> !s.isFinal()).collect(Collectors.toList());

                    // Remove states already reached for elements more on the right and add to list
                    reachable_final = new LinkedList<>(new HashSet<>(reachable_final)); // remove duplicates inside list
                    reachable_final.removeAll(alreadyAdded);    // remove duplicates from other states
                    alreadyAdded.addAll(reachable_final);       // keep track for future duplicates
                    successors.add(reachable_final);            // add to list as successors
                    statesToOrigin.put(reachable_final, ls);    // Keep track of origin of the new states
                    reachable_normal = new LinkedList<>(new HashSet<>(reachable_normal)); // remove duplicates inside list
                    reachable_normal.removeAll(alreadyAdded);   // remove duplicates
                    alreadyAdded.addAll(reachable_normal);      // keep track for future duplicates
                    successors.add(reachable_normal);           // add to list as successors
                    statesToOrigin.put(reachable_normal, ls);   // Keep track of origin of the new states
                }

                // Remove empty sets
                successors.removeIf(List::isEmpty);

                // Reverse again
                Collections.reverse(successors);

                // Do not add empty states
                if (successors.isEmpty()) {
                    break;
                }


                List<String> tempColors = new LinkedList<>();
                boolean newStateContainsRed = false;

                // If necessary, create new state and add to lists (toCheck, nameToLists)
                StringBuilder temp = new StringBuilder();
                for (List<IState> list : successors) {
                    String originColor = stateSubStateToColor.get(new Pair<>(stateToCheckName, statesToOrigin.get(list)));
                    if (!originColor.equals("red") && !originColor.equals("green") && !originColor.equals("blue")) {
                        System.err.println("Problem identifying color! ");
                        System.exit(465);
                    }
                    // Decide depending on whether original state had red
                    String name = "{" + list.stream().map(IState::getKey).collect(Collectors.joining(",")) + "}";
                    if (hasRed) {
                        switch (originColor) {
                            // A successor component of a red component is red
                            case "red":
                                temp.append("<font color=\"red\">").append(name).append("</font>,");
                                tempColors.add("red");
                                newStateContainsRed = true;
                                break;
                            // A successor component of a blue component is blue
                            case "blue":
                                temp.append("<font color=\"blue\">").append(name).append("</font>,");
                                tempColors.add("blue");
                                break;
                            case "green":
                                // An accepting successor component of a green component is blue
                                if (list.get(0).isFinal()) {
                                    temp.append("<font color=\"blue\">").append(name).append("</font>,");
                                    tempColors.add("blue");
                                } else {
                                    temp.append("<font color=\"green\">").append(name).append("</font>,");
                                    tempColors.add("green");
                                }
                                break;
                        }
                    } else {
                        switch (originColor) {
                            // A successor component of a blue component is red
                            case "blue":
                                temp.append("<font color=\"red\">").append(name).append("</font>,");
                                tempColors.add("red");
                                newStateContainsRed = true;
                                break;
                            case "green":
                                // An accepting successor component of a green component is red
                                if (list.get(0).isFinal()) {
                                    temp.append("<font color=\"red\">").append(name).append("</font>,");
                                    tempColors.add("red");
                                    newStateContainsRed = true;
                                } else {
                                    temp.append("<font color=\"green\">").append(name).append("</font>,");
                                    tempColors.add("green");
                                }
                                break;
                            // Since predecessor has not red, this case should never happen!
                            case "red":
                                System.err.println("This case should have happened!...");
                                System.exit(798);
                                break;
                        }
                    }
                }
                temp.deleteCharAt(temp.length() - 1);  // Delete last ','


                String newStateName = "(" + temp + ")";
                IState newState = newAutomata.getStateByKey(newStateName);
                if (newState == null) {
                    // Create new state
                    newState = new State(newStateName);

                    // Add to automata
                    newAutomata.addState(newState);

                    // Add to list to check
                    toCheck.add(newStateName);

                    // Update dictionary
                    nameToLists.put(newStateName, successors);
                }
                // Add transition
                newAutomata.addTransition(stateToCheck, symbol, newState);

                // Update color dictionary
                assert successors.size() == tempColors.size();
                for (int i = 0; i < successors.size(); i++) {
                    // Update color dictionary
                    stateSubStateToColor.put(new Pair<>(newStateName, successors.get(i)), tempColors.get(i));
                }
                // Update contains red
                containsRed.put(newStateName, newStateContainsRed);

            }

            // Remove state from list to check
            toCheck.remove(stateToCheckName);
        }



        return newAutomata;
    }

    public static IAutomata removeDeadEnds(IAutomata automata) {
        // TODO implement remove dead ends
        return automata;
    }

    public static IAutomata greedySubsetConstruction(IAutomata automata) {
        IAutomata newAutomata = new BuchiAutomata();

        // Keep track of the nodes to complete
        Set<String> toCheck = new HashSet<>();

        // Keep track of the lists of states represented by the new states
        // Each state represents multiple lists (in the algorighm: {}), each containing potentially multiple states
        Map<String, List<List<IState>>> nameToLists = new HashMap<>();

        // Initial state of the new automata
        IState newq0 = new State("({" + automata.getInitialState().getKey() + "})");
        newAutomata.addState(newq0);

        // Add the initial state to the list of states to check
        toCheck.add(newq0.getKey());
        List<List<IState>> listNewQ0 = new LinkedList<>();
        listNewQ0.add(automata.getInitialState().toList());
        nameToLists.put(newq0.getKey(), listNewQ0);

        // Construct every new state
        while (!toCheck.isEmpty()) {
            // Get next state to construct
            String stateToCheckName = toCheck.iterator().next();
            IState stateToCheck = newAutomata.getStateByKey(stateToCheckName);
            assert stateToCheck != null;

            // States ({}) represented in the state
            List<List<IState>> currentStates = nameToLists.get(stateToCheckName);
            // Reverse list --> start from right-most element
            Collections.reverse(currentStates);

            // Do for each symbol
            for (char symbol : automata.getAlphabet()) {
                // Get successors
                List<List<IState>> successors = new LinkedList<>();
                Set<IState> alreadyAdded = new HashSet<>(); // Keep track of states already considered ("remove" part in the algorithm)

                for (List<IState> ls : currentStates) {
                    // Get successors of each element
                    List<IState> reachable = automata.run(symbol, ls);

                    // Split into final and non-final (with finals on the right)
                    List<IState> reachable_final = reachable.stream().filter(IState::isFinal).collect(Collectors.toList());
                    List<IState> reachable_normal = reachable.stream().filter(s -> !s.isFinal()).collect(Collectors.toList());

                    // Remove states already reached for elements more on the right and add to list
                    reachable_final = new LinkedList<>(new HashSet<>(reachable_final)); // remove duplicates inside list
                    reachable_final.removeAll(alreadyAdded);    // remove duplicates from other states
                    alreadyAdded.addAll(reachable_final);       // keep track for future duplicates
                    successors.add(reachable_final);            // add to list as successors
                    reachable_normal = new LinkedList<>(new HashSet<>(reachable_normal)); // remove duplicates inside list
                    reachable_normal.removeAll(alreadyAdded);   // remove duplicates
                    alreadyAdded.addAll(reachable_normal);      // keep track for future duplicates
                    successors.add(reachable_normal);           // add to list as successors
                }

                // Remove empty sets
                successors.removeIf(List::isEmpty);

                // Reverse again
                Collections.reverse(successors);

                // Do not add empty states
                if (successors.isEmpty()) {
                    break;
                }


                // If necessary, create new state and add to lists (toCheck, nameToLists)
                StringBuilder temp = new StringBuilder();
                for (List<IState> list : successors) {
                    temp.append("{");
                    temp.append(list.stream().map(IState::getKey).sorted().collect(Collectors.joining(",")));
                    temp.append("},");
                }

                temp.deleteCharAt(temp.length() - 1);  // Delete last ','

                String newStateName = "(" + temp + ")";
                IState newState = newAutomata.getStateByKey(newStateName);
                if (newState == null) {
                    // Create new state
                    newState = new State(newStateName);

                    // Add to automata
                    newAutomata.addState(newState);

                    // Add to list to check
                    toCheck.add(newStateName);

                    // Update dictionary
                    nameToLists.put(newStateName, successors);
                }
                // Add transition
                newAutomata.addTransition(stateToCheck, symbol, newState);
            }

            // Remove state from list to check
            toCheck.remove(stateToCheckName);
        }

        return newAutomata;
    }


    // Helper method for reduceDegOfNonDeterminism()
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

    // Helper method for union()
    private static void copyIntoNew(IAutomata destAutomata, IState newStart, IAutomata toCopy, String nameAddition) {

        // Save the initial states of the original automata
        IState startOld = toCopy.getInitialState();

        // Copy first automata (changing names)
        Map<String, IState> firstStates = new HashMap<>();
        for (IState s : toCopy.getStates()) {
            IState nState = new State(s.getKey() + nameAddition, s.isFinal());
            destAutomata.addState(nState);
            // Keep track of changes (to easier add transitions)
            firstStates.put(s.getKey(), nState);
        }

        for (ITransition t : toCopy.getTransitionsList()) {
            // Use firstStates dictionary to retrive new state from old label
            destAutomata.addTransition(firstStates.get(t.getSource().getKey()), t.getSymbol(), firstStates.get(t.getDestination().getKey()));

            // Add transitions from new starting state
            if (t.getSource().equals(startOld)) {
                // Add transition from the new starting state
                destAutomata.addTransition(newStart, t.getSymbol(), firstStates.get(t.getDestination().getKey()));
            }
        }
    }

    // Helper method for intersection()
    private static List<Pair<ITransition, ITransition>> getCombinations(List<ITransition> firstTransitions, List<ITransition> secondTransitions) {
        List<Pair<ITransition, ITransition>> lst = new LinkedList<>();
        for (ITransition t1 : firstTransitions) {
            for (ITransition t2 : secondTransitions) {
                lst.add(new Pair<>(t1, t2));
            }
        }

        return lst;
    }

    private static void printListOfStates(List<IState> list) {
        for (IState s : list) {
            System.out.print(s.getKey() + " - ");
        }
        System.out.println();
    }

    public static List<List<IState>> statesFromName(String name, IAutomata sourceAutomata) {
        List<List<IState>> lOfStates = new LinkedList<>();

        for (List<String> l : extractNodesNames(name)) {
            List<IState> states = new LinkedList<>();
            for (String s : l) {
                states.add(sourceAutomata.getStateByKey(s));
            }
            lOfStates.add(states);
        }

        return lOfStates;
    }

    public static List<List<String>> extractNodesNames(String nodeName) {
        List<List<String>> matchList = new LinkedList<>();

        Pattern regex = Pattern.compile("\\{([a-zA-Z0-9,]*)\\}");
        Matcher regexMatcher = regex.matcher(nodeName);

        while (regexMatcher.find()) {//Finds Matching Pattern in String
            String states = regexMatcher.group(1); // Might be more comma-separated states names
            matchList.add(Arrays.asList(states.split(",")));
        }

        return matchList;
    }

    public static List<String> extractNodesColor(String nodeName) {
        Pattern regex = Pattern.compile(".*color=\"(green|red|blue)\".*");
        Matcher regexMatcher = regex.matcher(nodeName);

        List<String> matchList = new LinkedList<>();
        while (regexMatcher.find()) {//Finds Matching Pattern in String
            matchList.add(regexMatcher.group(1));
        }

        return matchList;
    }
}
