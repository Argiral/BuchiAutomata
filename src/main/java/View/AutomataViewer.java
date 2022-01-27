package View;

import Interfaces.IAutomata;

import Interfaces.IState;
import Interfaces.ITransition;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

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
        return createGraph(automata, false);
    }

    public static Graph createGraph(IAutomata automata, boolean htmlNodes) {
        HashMap<IState, Node> dict = new HashMap<>();

        // Create nodes (map from state to node)
        if (htmlNodes) {
            // Interpret node names as html
            // WARNING: it can throw an exception!
            for (IState s: automata.getStates()) {
                if (s.isFinal()) {
                    if (s.equals(automata.getInitialState())) {
                        dict.put(s, node(Label.html(s.getKey())).with(Style.lineWidth(3), Shape.SEPTAGON));
                    } else {
                        dict.put(s, node(Label.html(s.getKey())).with(Style.lineWidth(3)));
                    }
                } else {
                    if (s.equals(automata.getInitialState())) {
                        dict.put(s, node(Label.html(s.getKey())).with(Shape.SEPTAGON));
                    } else {
                        dict.put(s, node(Label.html(s.getKey())));
                    }
                }
            }
        } else {
            // Interpret node names as pure text
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

    public static void saveAutomata(IAutomata automata, String filename) {
        saveAutomata(automata, filename, false);
    }

    public static void saveAutomata(IAutomata automata, String filename, boolean htmlNodes) {
         try {
            Graphviz.fromGraph(createGraph(automata, htmlNodes)).width(2500).render(Format.SVG).toFile(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showAutomata(IAutomata automata) {
        showAutomata(automata, false);
    }
    public static void showAutomata(IAutomata automata, boolean htmlNodes) {
        display(Graphviz.fromGraph(createGraph(automata, htmlNodes)).render(Format.SVG).toImage());
    }

    public static void display(BufferedImage image){
        JFrame frame = new JFrame();
        frame.setTitle("Automata");
        frame.setSize(image.getWidth(), image.getHeight());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(image));
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
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
}
