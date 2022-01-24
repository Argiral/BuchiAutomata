package View;

import Interfaces.IAutomata;

import Interfaces.IState;
import Interfaces.ITransition;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.*;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.*;


// Use code from https://github.com/nidi3/graphviz-java
// Maven: guru.nidi.graphviz-java:0.8.3
//        ch.qos.logback:logback-classic:0.9.30

public class AutomataViewer {
    public static Graph createExampleGraph() {
        Node
                q0 = node("q0").with(Style.FILLED, Color.AQUAMARINE4),
                q1 = node("q1").with(Shape.SEPTAGON),
                q2 = node("q2").with(Style.DIAGONALS, Style.lineWidth(3), Color.LIGHTGRAY),
                q3 = node("q3").with(Style.lineWidth(3)),
                q4 = node("q4").with(Style.DASHED);

        return graph("automata_example").directed().with(
                q0.link(
                        to(q1).with(Label.of("a")),
                        to(q2).with(Label.of("b")),
                        to(q3).with(Label.of("b"))
                ),
                q1.link(
                        to(q1).with(Label.of("b")),
                        to(q3).with(Label.of("a"))
                ),
                q2.link(
                        to(q2).with(Label.of("a")),
                        to(q4).with(Label.of("b"))
                ),
                q3.link(
                        to(q3).with(Label.of("b")),
                        to(q4).with(Label.of("a,b"))
                ),
                q4.link(
                        to(q4).with(Label.of("a,b"))
                )
                );
    }

    public static Graph createGraph(IAutomata automata) {
        HashMap<IState, Node> dict = new HashMap<>();

        // Create nodes (map from state to node)
        for (IState s: automata.getStates()) {
            if (s.isFinal()) {
                if (s.equals(automata.getInitialState())) {
                    dict.put(s, node(s.getKey()).with(Style.lineWidth(3), Shape.SEPTAGON));
                } else {
                    dict.put(s, node(s.getKey()).with(Style.lineWidth(3)));
                }
            } else {
                if (s.equals(automata.getInitialState())) {
                    dict.put(s, node(s.getKey()).with(Shape.SEPTAGON));
                } else {
                    dict.put(s, node(s.getKey()));
                }
            }
        }

        List<LinkSource> lksources = new LinkedList<>();

        // Create links
        Map<IState, List<ITransition>> transitionMap = automata.getTransitionsMap();
        for (IState s : transitionMap.keySet()) {
//            LinkTarget[] lktarget = new LinkTarget[transitionMap.get(s).size()];
//            int i = 0;
//            for (ITransition t : transitionMap.get(s)) {
//                lktarget[i] =  to(dict.get(t.getDestination())).with(Label.of("" + t.getSymbol()));
//                i++;
//            }
            // Merge transitions with same source and destination
            List<MyPair> newTransitionsList = mergeTransitions(transitionMap.get(s));
            LinkTarget[] lktarget = new LinkTarget[newTransitionsList.size()];
            int i = 0;
            for (MyPair p : newTransitionsList) {
                lktarget[i++] =  to(dict.get(p.state)).with(Label.of("" + p.transitionValue));
            }
            lksources.add(dict.get(s).link(lktarget));
        }

        // For some reason, this draws only one transition per symbol -> always wrong with non-deterministic automata
//        for (ITransition t : automata.getTransitionsList()) {
//            links.add(dict.get(t.getSource()).link(to(dict.get(t.getDestination())).with(Label.of("" + t.getSymbol()))));
//            System.out.println("" + t.getSource().getKey() + " - " + t.getSymbol() + " - " + t.getDestination().getKey());
//        }

        return graph().directed().with(lksources);

    }

    public static void printAutomata(IAutomata automata, String filename) {
        try {
            Graphviz.fromGraph(createGraph(automata)).width(2500).render(Format.SVG).toFile(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<MyPair> mergeTransitions(List<ITransition> transitions) {
        List<MyPair> list = new LinkedList<>();
        if (transitions == null) {
            return list;
        }
        for (ITransition t : transitions) {
            // Create new pair
            MyPair newPair = new MyPair(t.getDestination(), t.getSymbol() + "");
            // Check if a transition to the same state exist
            boolean found = false;
            for (MyPair listPair : list) {
                if (listPair.state.equals(newPair.state)) {
                    listPair.transitionValue += "," + newPair.transitionValue;
                    found = true;
                    break;
                }
            }
            if (!found) {
                list.add(newPair);
            }
        }
        return list;
    }

    private static class MyPair {
        IState state;
        String transitionValue;
        MyPair(IState state, String transitionValue) {
            this.state = state;
            this.transitionValue = transitionValue;
        }
    }

//    public static void main(String[] args) throws IOException {
////        Graphviz.fromGraph(createExampleGraph()).width(900).render(Format.PNG).toFile(new File("graph/example_automata.png"));
//        Graphviz.fromGraph(createGraph(createTestNBA())).width(1500).render(Format.PNG).toFile(new File("graph/example_NBA.png"));
////        Graphviz.fromGraph(createGraph(createTestDBA())).width(2500).render(Format.SVG).toFile(new File("graph/example_DBA.svg"));
//    }
////
////
//    static IAutomata createTestNBA() {
//        // Create non-deterministic automata
//        IAutomata automataNBA = new BuchiAutomata();
//
//        // Add states
//        State s0 = new State("q0");
//        automataNBA.addState(s0);
//        State s1 = new State("q1");
//        automataNBA.addState(s1);
//        State s2 = new State("q2");
//        automataNBA.addState(s2);
//        State s3 = new State("q3");
//        automataNBA.addState(s3);
//        State s4 = new State("q4", true);
//        automataNBA.addState(s4);
//
//        // Set initial state
//        automataNBA.setInitialState(s0);
//
//        // Add transitions
//        automataNBA.addTransition(new Transition(s0, 'a', s1));
//        automataNBA.addTransition(new Transition(s0, 'b', s2));
//        automataNBA.addTransition(new Transition(s1, 'b', s1));
//        automataNBA.addTransition(new Transition(s1, 'a', s3));
//        automataNBA.addTransition(new Transition(s2, 'a', s2));
//        automataNBA.addTransition(new Transition(s2, 'b', s4));
//        automataNBA.addTransition(new Transition(s3, 'b', s3));
//        automataNBA.addTransition(new Transition(s3, 'a', s4));
//        automataNBA.addTransition(new Transition(s3, 'b', s4));
//        automataNBA.addTransition(new Transition(s4, 'a', s4));
//        automataNBA.addTransition(new Transition(s4, 'b', s4));
//
//        return automataNBA;
//    }
//
//    static IAutomata createTestDBA() {
//        // Create deterministic automata
//        IAutomata automataDBA = new BuchiAutomata();
//
//        // Add states
//        State s0 = new State("q0");
//        automataDBA.addState(s0);
//        State s1 = new State("q1");
//        automataDBA.addState(s1);
//        State s2 = new State("q2", true);
//        automataDBA.addState(s2);
//        State s3 = State.createFinalState("q3");
////        Model.State s3 = new Model.State("q3", true);
//        automataDBA.addState(s3);
//
//        // Do not set initial state, q0 should be set automatically since it is the first one added
//
//        // Add transitions (complete except    (q1,'b',q2)   )
//        automataDBA.addTransition(new Transition(s0, 'a', s1));
//        automataDBA.addTransition(new Transition(s0, 'b', s2));
//        automataDBA.addTransition(new Transition(s1, 'a', s3));
//        // With transition Model.Transition(s1, 'b', s2) would be complete
//        automataDBA.addTransition(new Transition(s2, 'a', s3));
//        automataDBA.addTransition(new Transition(s2, 'b', s3));
//        automataDBA.addTransition(new Transition(s3, 'a', s1));
//        automataDBA.addTransition(new Transition(s3, 'b', s3));
//
//        return automataDBA;
//    }
}
