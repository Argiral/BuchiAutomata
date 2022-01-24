package Model;

import Interfaces.IAutomata;
import Interfaces.IState;

// Automata from the "Automata on Infinite Structures" course
public class SeriesAutomata {

    public static IAutomata s2e2() {
        IAutomata automata = new BuchiAutomata();

        IState q0 = new State("0", true);
        automata.addState(q0);
        IState q1 = new State("1");
        automata.addState(q1);
        IState q2 = new State("2");
        automata.addState(q2);
        IState q3 = new State("3");
        automata.addState(q3);
        IState q4 = new State("4");
        automata.addState(q4);
        IState q5 = new State("5");
        automata.addState(q5);
        IState q6 = new State("6", true);
        automata.addState(q6);

        automata.addTransition(q0, 'a', q0);
        automata.addTransition(q0, 'a', q1);
        automata.addTransition(q0, 'b', q2);
        automata.addTransition(q0, 'a', q3);
        automata.addTransition(q0, 'b', q4);
        automata.addTransition(q0, 'a', q5);
        automata.addTransition(q1, 'b', q2);
        automata.addTransition(q2, 'a', q3);
        automata.addTransition(q2, 'a', q6);
        automata.addTransition(q3, 'b', q4);
        automata.addTransition(q4, 'a', q5);
        automata.addTransition(q4, 'a', q6);
        automata.addTransition(q6, 'b', q2);
        automata.addTransition(q6, 'b', q4);


        return automata;
    }

    public static IAutomata s2ex4a1() {
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

    public static IAutomata s2ex4a2() {
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
        automata.addTransition(q2, 'b', q2);

        return automata;
    }

    public static IAutomata s3e1a1() {
        IAutomata automata = new BuchiAutomata();

        IState q0 = new State("q0");
        automata.addState(q0);
        IState q1 = new State("q1", true);
        automata.addState(q1);
        IState q2 = new State("q2");
        automata.addState(q2);
        IState q3 = new State("q3", true);
        automata.addState(q3);

        automata.addTransition(q0, 'b', q0);
        automata.addTransition(q0, 'a', q1);
        automata.addTransition(q1, 'b', q1);
        automata.addTransition(q1, 'a', q2);
        automata.addTransition(q2, 'b', q2);
        automata.addTransition(q2, 'a', q3);
        automata.addTransition(q3, 'b', q3);
        automata.addTransition(q3, 'a', q3);

        return automata;
    }

    public static IAutomata s3e1a2() {
        IAutomata automata = new BuchiAutomata();

        IState q0 = new State("q0");
        automata.addState(q0);
        IState q1 = new State("q1", true);
        automata.addState(q1);
        IState q2 = new State("q2");
        automata.addState(q2);
        IState q3 = new State("q3", true);
        automata.addState(q3);

        automata.addTransition(q0, 'a', q1);
        automata.addTransition(q0, 'b', q3);
        automata.addTransition(q0, 'c', q2);
        automata.addTransition(q1, 'a', q1);
        automata.addTransition(q1, 'b', q2);
        automata.addTransition(q2, 'a', q0);
        automata.addTransition(q2, 'b', q3);
        automata.addTransition(q2, 'c', q1);
        automata.addTransition(q3, 'a', q3);
        automata.addTransition(q3, 'c', q0);

        return automata;
    }

    public static IAutomata s4e1a1() {
        IAutomata automata = new BuchiAutomata();

        IState q0 = new State("q0");
        automata.addState(q0);
        IState q1 = new State("q1", true);
        automata.addState(q1);
        IState q2 = new State("q2");
        automata.addState(q2);

        automata.addTransition(q0, 'a', q0);
        automata.addTransition(q0, 'b', q0);
        automata.addTransition(q0, 'c', q0);
        automata.addTransition(q0, 'a', q1);
        automata.addTransition(q1, 'b', q1);
        automata.addTransition(q1, 'a', q2);
        automata.addTransition(q2, 'a', q2);
        automata.addTransition(q2, 'b', q2);
        automata.addTransition(q2, 'c', q2);

        return automata;
    }

    public static IAutomata s4e1a2() {
        IAutomata automata = new BuchiAutomata();

        IState q0 = new State("q0");
        automata.addState(q0);
        IState q1 = new State("q1");
        automata.addState(q1);
        IState q2 = new State("q2", true);
        automata.addState(q2);

        automata.addTransition(q0, 'a', q0);
        automata.addTransition(q0, 'b', q0);
        automata.addTransition(q0, 'b', q1);
        automata.addTransition(q1, 'a', q0);
        automata.addTransition(q1, 'b', q1);
        automata.addTransition(q1, 'b', q2);
        automata.addTransition(q2, 'a', q2);
        automata.addTransition(q2, 'a', q0);
        automata.addTransition(q2, 'b', q0);

        return automata;
    }
}
