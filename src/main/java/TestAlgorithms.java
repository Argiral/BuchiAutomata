import Control.BuchiAlgorithms;
import Interfaces.IAutomata;
import Interfaces.IState;
import Model.BuchiAutomata;
import Model.State;
import View.AutomataViewer;

public class TestAlgorithms {
    public static void main(String[] args) {
//        AutomataViewer.printAutomata(s2ex4a1(), "graph/s2/4a.svg");
//        AutomataViewer.printAutomata(s2ex4a2(), "graph/s2/4b.svg");

        System.out.println(s2ex4a1().getStatistics());
        IAutomata union = BuchiAlgorithms.union(s2ex4a1(), s2ex4a2());
        System.out.println(union.getStatistics());
        AutomataViewer.printAutomata(union, "graph/s2/union.svg");
    }

    static IAutomata s2ex4a1() {
        IAutomata automata = new BuchiAutomata();

        IState q0 = new State("q0");
        automata.addState(q0);
        IState q1 = new State("q1", true);
        automata.addState(q1);

        automata.addTransition(q0, 'a', q0);
        automata.addTransition(q0, 'b', q0);
        automata.addTransition(q0, 'b', q1);
        automata.addTransition(q1, 'b', q1);

        return automata;
    }

    static IAutomata s2ex4a2() {
        IAutomata automata = new BuchiAutomata();

        IState q0 = new State("q0");
        automata.addState(q0);
        IState q1 = new State("q1");
        automata.addState(q1);
        IState q2 = new State("q2", true);
        automata.addState(q2);

        automata.addTransition(q0, 'a', q0);
        automata.addTransition(q0, 'b', q0);
        automata.addTransition(q0, 'a', q1);
        automata.addTransition(q1, 'b', q1);
        automata.addTransition(q1, 'a', q2);
        automata.addTransition(q2, 'a', q2);
        automata.addTransition(q2, 'a', q2);

        return automata;
    }
}
