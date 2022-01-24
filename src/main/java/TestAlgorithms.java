import Control.BuchiAlgorithms;
import Interfaces.IAutomata;
import View.AutomataViewer;

import Model.SeriesAutomata;

public class TestAlgorithms {
    public static void main(String[] args) {
        testReductionDegOfNonDet();
        testUnion();
    }

    private static void testReductionDegOfNonDet() {
        // Degree of non-determinism reduction
        IAutomata degReduction = BuchiAlgorithms.reduceDegOfNonDeterminism(SeriesAutomata.s2e2());

        // Save images
        AutomataViewer.printAutomata(SeriesAutomata.s2e2(), "graph/s2/2.svg");
        AutomataViewer.printAutomata(degReduction, "graph/s2/degReduction.svg");

        // Print statistics
        System.out.println("DEGREE OF NON-DETERMINISM REDUCTION");
        System.out.println(SeriesAutomata.s2e2().getStatistics());
        System.out.println(degReduction.getStatistics());
    }

    private static void testUnion() {
        // Union of two BA
        IAutomata union = BuchiAlgorithms.union(SeriesAutomata.s2ex4a1(), SeriesAutomata.s2ex4a2());

        // Save images
        AutomataViewer.printAutomata(SeriesAutomata.s2ex4a1(), "graph/s2/4a.svg");
        AutomataViewer.printAutomata(SeriesAutomata.s2ex4a2(), "graph/s2/4b.svg");
        AutomataViewer.printAutomata(union, "graph/s2/4_union.svg");
    }
}
