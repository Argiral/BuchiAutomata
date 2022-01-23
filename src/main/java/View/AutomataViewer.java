package View;

import Interfaces.IAutomata;

import Interfaces.IState;
import Interfaces.ITransition;
import Model.BuchiAutomata;
import Model.State;
import Model.Transition;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.*;


// Use code from https://github.com/nidi3/graphviz-java
// Maven: guru.nidi.graphviz-java:0.8.3
//  //        guru.nidi.graphviz-rough:0.18.1
//        ch.qos.logback:logback-classic:0.9.30

public class AutomataViewer {
    public static Graph createExampleGraph() {
        Node
                q0 = node("q0"),
                q1 = node("q1"),
                q2 = node("q2"),
                q3 = node("q3"),
                q4 = node("q4");

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
            dict.put(s, node(s.getKey()));
        }

        List<LinkSource> lksources = new LinkedList<>();

        // Create links
        Map<IState, List<ITransition>> transitionMap = automata.getTransitionsMap();
        for (IState s : transitionMap.keySet()) {
            LinkTarget[] lktarget = new LinkTarget[transitionMap.get(s).size()];
            int i = 0;
            for (ITransition t : transitionMap.get(s)) {
                lktarget[i] =  to(dict.get(t.getDestination())).with(Label.of("" + t.getSymbol()));
                i++;
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

    public static void main(String[] args) throws IOException {
        Graphviz.fromGraph(createExampleGraph()).width(900).render(Format.PNG).toFile(new File("graph/example_automata.png"));
        Graphviz.fromGraph(createGraph(createTestNBA())).width(1500).render(Format.PNG).toFile(new File("graph/example_NBA2.png"));
//        Graphviz.fromGraph(createGraph(createTestDBA())).width(2500).render(Format.SVG).toFile(new File("graph/example_DBA.svg"));
    }
//
//
    static IAutomata createTestNBA() {
        // Create non-deterministic automata
        IAutomata automataNBA = new BuchiAutomata();

        // Add states
        State s0 = new State("q0");
        automataNBA.addState(s0);
        State s1 = new State("q1");
        automataNBA.addState(s1);
        State s2 = new State("q2");
        automataNBA.addState(s2);
        State s3 = new State("q3");
        automataNBA.addState(s3);
        State s4 = new State("q4", true);
        automataNBA.addState(s4);

        // Set initial state
        automataNBA.setInitialState(s0);

        // Add transitions
        automataNBA.addTransition(new Transition(s0, 'a', s1));
        automataNBA.addTransition(new Transition(s0, 'b', s2));
        automataNBA.addTransition(new Transition(s1, 'b', s1));
        automataNBA.addTransition(new Transition(s1, 'a', s3));
        automataNBA.addTransition(new Transition(s2, 'a', s2));
        automataNBA.addTransition(new Transition(s2, 'b', s4));
        automataNBA.addTransition(new Transition(s3, 'b', s3));
        automataNBA.addTransition(new Transition(s3, 'a', s4));
        automataNBA.addTransition(new Transition(s3, 'b', s4));
        automataNBA.addTransition(new Transition(s4, 'a', s4));
        automataNBA.addTransition(new Transition(s4, 'b', s4));

        return automataNBA;
    }
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
