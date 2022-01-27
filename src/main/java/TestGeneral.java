import Control.MonadicSecondOrderAlgorithms;
import Interfaces.IAutomata;
import Interfaces.IState;
import Model.BuchiAutomata;
import Model.State;
import View.TextViewer;


public class TestGeneral {
    public static void main(String[] args) {
        String s1s = MonadicSecondOrderAlgorithms.automataToS1S(testAutomata());
        System.out.println(s1s);
        TextViewer.showInTextArea(s1s);
    }

    private static IAutomata testAutomata() {
        IAutomata automata = new BuchiAutomata();

        IState q0 = new State("0");
        automata.addState(q0);
        IState q1 = new State("1", true);
        automata.addState(q1);
        IState q2 = new State("2", true);
        automata.addState(q2);

        automata.addTransition(q0, 'a', q0);
        automata.addTransition(q0, 'b', q0);
        automata.addTransition(q0, 'b', q1);
        automata.addTransition(q1, 'b', q1);
        automata.addTransition(q1, 'b', q2);

        return automata;
    }
}
