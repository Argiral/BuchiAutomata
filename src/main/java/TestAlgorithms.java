import Control.BuchiAlgorithms;
import Interfaces.IAutomata;
import Model.AutomataFactory;
import View.AutomataViewer;

import Model.SeriesAutomata;

public class TestAlgorithms {
    public static void main(String[] args) {
//        testFactorySave();
//        testFactoryLoad();
//        testReductionDegOfNonDet();
//        testUnion();
//        testIntersection();
        testComplementDBA();
//        testGreedySubsetConstruction();

    }

    private static void testReductionDegOfNonDet() {
        // Degree of non-determinism reduction
        IAutomata degReduction = BuchiAlgorithms.reduceDegOfNonDeterminism(SeriesAutomata.s2e2());

        // Save images
        AutomataViewer.printAutomata(SeriesAutomata.s2e2(), "data/graph/s2/2.svg");
        AutomataViewer.printAutomata(degReduction, "data/graph/s2/degReduction.svg");

        // Print statistics
        System.out.println("DEGREE OF NON-DETERMINISM REDUCTION");
        System.out.println(SeriesAutomata.s2e2().getStatistics());
        System.out.println(degReduction.getStatistics());
    }

    private static void testUnion() {
        // Union of two BA
        IAutomata union = BuchiAlgorithms.union(SeriesAutomata.s2ex4a1(), SeriesAutomata.s2ex4a2());

        // Save images
        AutomataViewer.printAutomata(SeriesAutomata.s2ex4a1(), "data/graph/s2/4a.svg");
        AutomataViewer.printAutomata(SeriesAutomata.s2ex4a2(), "data/graph/s2/4b.svg");
        AutomataViewer.printAutomata(union, "data/graph/s2/4_union.svg");
    }

    private static void testIntersection() {
        // Intersection of two BA
        IAutomata intersection = BuchiAlgorithms.intersection(SeriesAutomata.s2ex4a1(), SeriesAutomata.s2ex4a2());

        // Save images
        AutomataViewer.printAutomata(SeriesAutomata.s2ex4a1(), "data/graph/s2/4a.svg");
        AutomataViewer.printAutomata(SeriesAutomata.s2ex4a2(), "data/graph/s2/4b.svg");
        AutomataViewer.printAutomata(intersection, "data/graph/s2/4_intersection.svg");
    }

    private static void testComplementDBA() {
        IAutomata a1 = SeriesAutomata.s3e1a1();
        IAutomata a2 = SeriesAutomata.s3e1a2();

        IAutomata a1c = BuchiAlgorithms.complementDBA(a1);
        IAutomata a2c = BuchiAlgorithms.complementDBA(a2);

        AutomataViewer.printAutomata(a1, "data/graph/s3/1a.svg");
        AutomataViewer.printAutomata(a2, "data/graph/s3/1b.svg");

        AutomataViewer.printAutomata(a1c, "data/graph/s3/1a_complement.svg");
        AutomataViewer.printAutomata(a2c, "data/graph/s3/1b_complement.svg");
    }

    private static void testFactorySave() {
        IAutomata automata = SeriesAutomata.s2e2();

        AutomataFactory.instance().saveToXml(automata, "data/automata/s2e2.xml");
    }

    private static void testFactoryLoad() {
        IAutomata automata = AutomataFactory.instance().loadFromXML("data/automata/s2e2.xml");

        AutomataViewer.printAutomata(automata, "data/graph/s2/2_loaded.svg");
    }

    private static void testGreedySubsetConstruction() {
        IAutomata automata1 = SeriesAutomata.s4e1a1();

        IAutomata subset1 = BuchiAlgorithms.greedySubsetConstruction(automata1);
        System.out.println("GREEDY SUBSET CONSTRUCTION");
        System.out.println("A1");
        System.out.println(automata1.getStatistics());
        System.out.println();
        System.out.println(subset1.getStatistics());

        AutomataViewer.printAutomata(automata1, "data/graph/s4/1a1.svg");
        AutomataViewer.printAutomata(subset1, "data/graph/s4/subset1.svg");


        IAutomata automata2 = SeriesAutomata.s4e1a2();

        IAutomata subset2 = BuchiAlgorithms.greedySubsetConstruction(automata2);

        System.out.println();
        System.out.println();
        System.out.println(automata2.getStatistics());
        System.out.println();
        System.out.println(subset2.getStatistics());

        AutomataViewer.printAutomata(automata2, "data/graph/s4/1a2.svg");
        AutomataViewer.printAutomata(subset2, "data/graph/s4/subset2.svg");
    }
}
