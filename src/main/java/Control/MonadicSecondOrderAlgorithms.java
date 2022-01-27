package Control;

import Interfaces.IAutomata;
import Interfaces.IState;
import Interfaces.ITransition;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MonadicSecondOrderAlgorithms {
    public static String automataToS1S(IAutomata automata) {
        List<IState> states = automata.getStates();
        String myTab = "    ";
        // It asserts the states names are only integers
        StringBuilder output = new StringBuilder();
        for (IState s : states) {
            output.append("∃Y").append(subscriptNumber(Integer.parseInt(s.getKey())));
        }
        output.append("(\n");

        // Initial state
        output.append(myTab).append("0 ∈ Y").append(subscriptNumber(Integer.parseInt(automata.getInitialState().getKey()))).append(" ∧\n");

        // Disjoint states
        for (int i = 0; i < states.size(); i++) {
            for (int j = i+1; j < states.size(); j++) {
                output.append(myTab).append("(¬∃y(y ∈ Y").append(subscriptNumber(states.get(i).getKey()));
                output.append(" ∧ y ∈ Y").append(subscriptNumber(states.get(j).getKey())).append(") ∧\n");
            }
        }

        // Transitions
        output.append(myTab).append("∀x(\n");

        List<String> transitions = new LinkedList<>();
        for (ITransition t : automata.getTransitionsList()) {
            transitions.add(myTab + myTab + "(x ∈ Y" + subscriptNumber(t.getSource().getKey()) +
                    " ∧ x ∈ Q" + smallChar(t.getSymbol()) +
                    " ∧ x+1 ∈ Y" + subscriptNumber(t.getDestination().getKey()) + ")");
        }
        output.append(String.join(" ∨\n", transitions));
        output.append("\n");

        // Close transitions parenthesis
        output.append(myTab).append(") ∧\n");

        // Final states
        output.append(myTab).append("(\n");
        List<String> finals = new LinkedList<>();
        for (IState s : automata.getFinalStates()) {
            finals.add(myTab + myTab + "(∀x∃y(x < y ∧ y ∈ Y" + subscriptNumber(s.getKey()) + "))");
        }
        output.append(String.join(" ∨\n", finals));
        output.append("\n");

        output.append(myTab).append(")\n");

        // Close initial parenthesis
        output.append(")");

//        System.out.println("q" + subscriptNumber(100));
//        System.out.println("q" + subscriptNumber(567));
//        System.out.println("q" + subscriptNumber(10567));
//        System.out.println("q" + subscriptNumber(1));
//        System.out.println("q" + subscriptNumber(0));
        return output.toString();
    }

    private static String subscriptNumber(String integerString) {
        return subscriptNumber(Integer.parseInt(integerString));
    }

    private static String subscriptNumber(int number) {
        // Special case
        if (number == 0) {
            return "₀";
        }

        String vals = "₀₁₂₃₄₅₆₇₈₉";
        StringBuilder txt = new StringBuilder();

        int i = number;
        while (i >= 1) {
            txt.append(vals.charAt(i % 10));
            i = i / 10;
        }

        return txt.reverse().toString();
    }

    private static char smallChar(char symbol) {
        // Would probably be better to work directly with char codes
        // But I'm lazy and I don't want to look for them
        String normal = "abcdefghijklmnopqrstuvwxyz";
        String small = "ᴀʙᴄᴅᴇғɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ";
        int index = normal.indexOf(symbol);
        if (index != -1) {
            return small.charAt(index);
        } else {
            return symbol;
        }
    }
}
