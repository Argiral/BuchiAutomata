import Interfaces.ITransition;
import Model.BuchiAutomata;
import Model.State;
import Model.Transition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuchiAutomataTest {
    BuchiAutomata automataNBA, automataDBA;
    ITransition transitionToAddLater;

    @BeforeEach
    void setUp() {
        createTestNBA();
        createTestDBA();
    }

    @Test
    void getInitialState() {
        assertEquals(automataNBA.getInitialState().getKey(), "q0");
        assertEquals(automataDBA.getInitialState().getKey(), "q0");
    }

    @Test
    void getStates() {
        assertEquals(automataNBA.getStates().size(), 5);
        assertEquals(automataDBA.getStates().size(), 4);
    }

    @Test
    void getFinalStates() {
        assertEquals(automataNBA.getFinalStates().size(), 1);
        assertEquals(automataNBA.getFinalStates().get(0).getKey(), "q4");

        assertEquals(automataDBA.getFinalStates().size(), 2);
        assert (automataDBA.getFinalStates().get(0).getKey().equals("q2")
                || automataDBA.getFinalStates().get(0).getKey().equals("q3"));
        assert (automataDBA.getFinalStates().get(1).getKey().equals("q2")
                || automataDBA.getFinalStates().get(1).getKey().equals("q3"));
    }

    @Test
    void getTransitions() {
        List<ITransition> lNBA = new LinkedList<>();
        List<ITransition> lDBA = new LinkedList<>();

        for (List<ITransition> l : automataNBA.getTransitionsMap().values()) {
            lNBA.addAll(l);
        }
        assertEquals(lNBA.size(), 11);
        assertEquals(automataNBA.getTransitionsList().size(), 11);

        for (List<ITransition> l : automataDBA.getTransitionsMap().values()) {
            lDBA.addAll(l);
        }
        assertEquals(lDBA.size(), 7);
        assertEquals(automataDBA.getTransitionsList().size(), 7);
    }

    @Test
    void degreeOfNonDeterminism() {
        assertEquals(automataNBA.degreeOfNonDeterminism(), 2);
        assertEquals(automataDBA.degreeOfNonDeterminism(), 1);
    }

    @Test
    void isDeterministic() {
        assertFalse(automataNBA.isDeterministic());
        assertTrue(automataDBA.isDeterministic());
    }

    @Test
    void isComplete() {
        assertFalse(automataNBA.isComplete());
        assertFalse(automataDBA.isComplete());

        automataDBA.addTransition(transitionToAddLater);
        assertTrue(automataDBA.isComplete());
    }

    @Test
    void getAlphabet() {
        Collection<Character> alph = new HashSet<>();
        alph.add('a');
        alph.add('b');

        assert automataNBA.getAlphabet().size() == 2;
        assert automataNBA.getAlphabet().containsAll(alph);

        automataNBA.addSymbolToAlphabet('c');
        assert automataNBA.getAlphabet().size() == 3;

        assert automataDBA.getAlphabet().size() == 2;
        assert automataDBA.getAlphabet().containsAll(alph);
    }

    @Test
    void numberOfStates() {
        assertEquals(automataNBA.numberOfStates(), 5);
        assertEquals(automataDBA.numberOfStates(), 4);
    }

    @Test
    void numberOfTransitions() {
        assertEquals(automataNBA.numberOfTransitions(), 11);
        assertEquals(automataDBA.numberOfTransitions(), 7);
    }


    void createTestNBA() {
        // Create non-deterministic automata
        automataNBA = new BuchiAutomata();

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
    }

    void createTestDBA() {
        // Create deterministic automata
        automataDBA = new BuchiAutomata();

        // Add states
        State s0 = new State("q0");
        automataDBA.addState(s0);
        State s1 = new State("q1");
        automataDBA.addState(s1);
        State s2 = new State("q2", true);
        automataDBA.addState(s2);
        State s3 = State.createFinalState("q3");
//        Model.State s3 = new Model.State("q3", true);
        automataDBA.addState(s3);

        // Do not set initial state, q0 should be set automatically since it is the first one added

        // Add transitions (complete except    (q1,'b',q2)   )
        automataDBA.addTransition(new Transition(s0, 'a', s1));
        automataDBA.addTransition(new Transition(s0, 'b', s2));
        automataDBA.addTransition(new Transition(s1, 'a', s3));
            // With transition Model.Transition(s1, 'b', s2) would be complete
        automataDBA.addTransition(new Transition(s2, 'a', s3));
        automataDBA.addTransition(new Transition(s2, 'b', s3));
        automataDBA.addTransition(new Transition(s3, 'a', s1));
        automataDBA.addTransition(new Transition(s3, 'b', s3));

        transitionToAddLater = new Transition(s1, 'b', s2);
    }
}