import Control.BuchiAlgorithms;
import Interfaces.IAutomata;
import Interfaces.IState;
import Model.AutomataFactory;
import Model.BuchiAutomata;
import Model.State;
import View.AutomataViewer;

import Model.SeriesAutomata;

public class TestAlgorithms {
    public static void main(String[] args) {
//        testFactorySave();
//        testFactoryLoad();
//        testColoredLabels();

//        testReductionDegOfNonDet();
//        testUnion();
//        testIntersection();
//        testComplementDBA();
//        testGreedySubsetConstruction();
        testComplementBA();
//        testRemoveUnreachableStates();
//        testRemoveDeadEnds();
//        testSimplifyAutomata();
//        testInvertTransitions();

//        System.out.println(BuchiAlgorithms.extractNodesColor("<font color=\"green\">{q1}</font>"));
//        System.out.println(BuchiAlgorithms.extractNodesNames("{q0,q1}, {q1}, {q2} (Java), (.Net)"));
//        System.out.println(BuchiAlgorithms.statesFromName("{q0}, {q1}, (Java), (.Net)", SeriesAutomata.s2ex4a1()));

    }

    private static void testReductionDegOfNonDet() {
        // Degree of non-determinism reduction
        IAutomata degReduction = BuchiAlgorithms.reduceDegOfNonDeterminism(SeriesAutomata.s2e2());

        // Save images
        AutomataViewer.saveAutomata(SeriesAutomata.s2e2(), "data/graph/s2/2.svg");
        AutomataViewer.saveAutomata(degReduction, "data/graph/s2/degReduction.svg");

        // Print statistics
        System.out.println("DEGREE OF NON-DETERMINISM REDUCTION");
        System.out.println(SeriesAutomata.s2e2().getStatistics());
        System.out.println(degReduction.getStatistics());
    }

    private static void testUnion() {
        // Union of two BA
        IAutomata union = BuchiAlgorithms.union(SeriesAutomata.s2ex4a1(), SeriesAutomata.s2ex4a2());

        // Save images
        AutomataViewer.saveAutomata(SeriesAutomata.s2ex4a1(), "data/graph/s2/4a.svg");
        AutomataViewer.saveAutomata(SeriesAutomata.s2ex4a2(), "data/graph/s2/4b.svg");
        AutomataViewer.saveAutomata(union, "data/graph/s2/4_union.svg");
    }

    private static void testIntersection() {
        // Intersection of two BA
        IAutomata intersection = BuchiAlgorithms.intersection(SeriesAutomata.s2ex4a1(), SeriesAutomata.s2ex4a2());

        // Save images
        AutomataViewer.saveAutomata(SeriesAutomata.s2ex4a1(), "data/graph/s2/4a.svg");
        AutomataViewer.saveAutomata(SeriesAutomata.s2ex4a2(), "data/graph/s2/4b.svg");
        AutomataViewer.saveAutomata(intersection, "data/graph/s2/4_intersection.svg");
    }

    private static void testComplementDBA() {
        IAutomata a1 = SeriesAutomata.s3e1a1();
        IAutomata a2 = SeriesAutomata.s3e1a2();

        IAutomata a1c = BuchiAlgorithms.complementDBA(a1);
        IAutomata a2c = BuchiAlgorithms.complementDBA(a2);

        AutomataViewer.saveAutomata(a1, "data/graph/s3/1a.svg");
        AutomataViewer.saveAutomata(a2, "data/graph/s3/1b.svg");
//
        AutomataViewer.saveAutomata(a1c, "data/graph/s3/1a_complement.svg");
        AutomataViewer.saveAutomata(a2c, "data/graph/s3/1b_complement.svg");
    }

    private static void testFactorySave() {
        IAutomata automata = SeriesAutomata.s2e2();

        AutomataFactory.instance().saveToXml(automata, "data/automata/s2e2.xml");
    }

    private static void testFactoryLoad() {
        IAutomata automata = AutomataFactory.instance().loadFromXML("data/automata/s2e2.xml");

        AutomataViewer.saveAutomata(automata, "data/graph/s2/2_loaded.svg");
    }

    private static void testGreedySubsetConstruction() {
        IAutomata automata1 = SeriesAutomata.s4e1a1();

        IAutomata subset1 = BuchiAlgorithms.greedySubsetConstruction(automata1);
        System.out.println("GREEDY SUBSET CONSTRUCTION");
        System.out.println("A1");
        System.out.println(automata1.getStatistics());
        System.out.println();
        System.out.println(subset1.getStatistics());

        AutomataViewer.saveAutomata(automata1, "data/graph/s4/1a1.svg");
        AutomataViewer.saveAutomata(subset1, "data/graph/s4/subset1.svg");


        IAutomata automata2 = SeriesAutomata.s4e1a2();

        IAutomata subset2 = BuchiAlgorithms.greedySubsetConstruction(automata2);

        System.out.println();
        System.out.println();
        System.out.println(automata2.getStatistics());
        System.out.println();
        System.out.println(subset2.getStatistics());

        AutomataViewer.saveAutomata(automata2, "data/graph/s4/1a2.svg");
        AutomataViewer.saveAutomata(subset2, "data/graph/s4/subset2.svg");


        IAutomata automata3 = SeriesAutomata.s5e1();

        IAutomata subset3 = BuchiAlgorithms.greedySubsetConstruction(automata3);

        System.out.println();
        System.out.println();
        System.out.println(automata3.getStatistics());
        System.out.println();
        System.out.println(subset3.getStatistics());

        AutomataViewer.saveAutomata(automata3, "data/graph/s5/1.svg");
        AutomataViewer.saveAutomata(subset3, "data/graph/s5/subset.svg");
    }

    private static void testComplementBA() {
//        IAutomata automata1 = SeriesAutomata.s4e1a1();
//        IAutomata automata5 = SeriesAutomata.s5e2();
//
//        IAutomata complement5 = BuchiAlgorithms.complementNBA(automata5);
//
//        AutomataViewer.showAutomata(complement5, true);
//        AutomataViewer.saveAutomata(automata5, "data/graph/s5/2.svg");
//        AutomataViewer.saveAutomata(complement5, "data/graph/s5/complement.svg", true);
//
//
//        IAutomata automata6a = SeriesAutomata.s6e1a();
//
//        IAutomata complement6a = BuchiAlgorithms.complementNBA(automata6a);
//
//        AutomataViewer.showAutomata(automata6a);
//        AutomataViewer.showAutomata(complement6a, true);
//        AutomataViewer.saveAutomata(automata6a,"data/graph/s6/1a.svg");
//        AutomataViewer.saveAutomata(complement6a, "data/graph/s6/complement1a.svg", true);


        IAutomata automata6b = SeriesAutomata.s6e1b();

        IAutomata complement6b = BuchiAlgorithms.complementNBA(automata6b);

        AutomataViewer.showAutomata(automata6b);
        AutomataViewer.showAutomata(complement6b, true);
        AutomataViewer.saveAutomata(automata6b,"data/graph/s6/1b.svg");
        AutomataViewer.saveAutomata(complement6b, "data/graph/s6/complement1b.svg", true);
    }

    private static void testColoredLabels() {
        IAutomata automata = new BuchiAutomata();
        IState q0 = new State("<b>{q0}</b>");
        automata.addState(q0);
        IState q1 = new State("{q1}");
        automata.addState(q1);
        IState q2 = new State( "<font color=\"red\">{q0}</font>, <font color=\"green\">{q1}</font>", true);
        automata.addState(q2);

        automata.addTransition(q0, 'a', q1);
        automata.addTransition(q1, 'a', q2);


        AutomataViewer.saveAutomata(automata, "data/graph/test_color.svg", true);
        AutomataViewer.showAutomata(automata, true);
    }

    public static void testInvertTransitions() {
        IAutomata automata = SeriesAutomata.s2e2();

        IAutomata inverted = BuchiAlgorithms.invertTransitions(automata);

        AutomataViewer.showAutomata(inverted);
    }


    private static void testRemoveUnreachableStates() {
        IAutomata automata = SeriesAutomata.s2e2();

        IState newState = new State("new");
        automata.addState(newState);
        automata.addTransition(newState, 'z', automata.getStateByKey("0"));
        automata.addTransition(newState, 'z', automata.getStateByKey("1"));

        AutomataViewer.showAutomata(automata);

        IAutomata smaller = BuchiAlgorithms.removeUnreachableStates(automata);

        System.out.println(automata.getStatistics());
        System.out.println(smaller.getStatistics());

        AutomataViewer.showAutomata(smaller);

    }

    private static void testRemoveDeadEnds() {
        IAutomata automata = new BuchiAutomata();

        IState q0 = new State("q0");
        automata.addState(q0);
        IState q1 = new State("q1");
        automata.addState(q1);
        IState q2 = new State("q2");
        automata.addState(q2);
        IState q3 = new State("q3", true);
        automata.addState(q3);
        IState q4 = new State("q4", true);
        automata.addState(q4);
        IState q5 = new State("q5", true);
        automata.addState(q5);
        IState q6 = new State("q6", true);
        automata.addState(q6);
        IState q7 = new State("q7");
        automata.addState(q7);
        IState q8 = new State("q8", true);
        automata.addState(q8);
        IState q9 = new State("q9");
        automata.addState(q9);
        IState q10 = new State("q10");
        automata.addState(q10);

        automata.addTransition(q0, 'a', q1);
        automata.addTransition(q1, 'a', q2);
        automata.addTransition(q1, 'a', q4);
        automata.addTransition(q1, 'a', q5);
        automata.addTransition(q1, 'a', q6);
        automata.addTransition(q1, 'a', q7);
        automata.addTransition(q1, 'a', q9);
        automata.addTransition(q2, 'a', q3);
        automata.addTransition(q5, 'a', q1);
        automata.addTransition(q6, 'a', q6);
        automata.addTransition(q7, 'a', q8);
        automata.addTransition(q8, 'a', q7);
        automata.addTransition(q10, 'a', q1);

        IAutomata reduced = BuchiAlgorithms.removeDeadEnds(automata);

        AutomataViewer.showAutomata(automata);
        AutomataViewer.showAutomata(reduced);
    }

    private static void testSimplifyAutomata() {
        IAutomata automata = SeriesAutomata.s2e2();

        AutomataViewer.showAutomata(automata);

        IAutomata simplified = BuchiAlgorithms.simplifyAutomata(automata);

        AutomataViewer.showAutomata(simplified);
    }
}
