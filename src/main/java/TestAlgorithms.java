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
        testGreedySubsetConstruction();
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
        // Union of two BA
        IAutomata union = BuchiAlgorithms.intersection(SeriesAutomata.s2ex4a1(), SeriesAutomata.s2ex4a2());

        // Save images
        AutomataViewer.printAutomata(SeriesAutomata.s2ex4a1(), "data/graph/s2/4a.svg");
        AutomataViewer.printAutomata(SeriesAutomata.s2ex4a2(), "data/graph/s2/4b.svg");
        AutomataViewer.printAutomata(union, "data/graph/s2/4_intersection.svg");
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
        IAutomata automata = SeriesAutomata.s4e1a1();

        IAutomata subset = BuchiAlgorithms.greedySubsetConstruction(automata);
        System.out.println("GREEDY SUBSET CONSTRUCTION");
        System.out.println(automata.getStatistics());
        System.out.println();
        System.out.println(subset.getStatistics());

        AutomataViewer.printAutomata(automata, "data/graph/s4/1a1.svg");
        AutomataViewer.printAutomata(subset, "data/graph/s4/subset.svg");
    }
}
