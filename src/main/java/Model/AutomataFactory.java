package Model;

import Interfaces.IAutomata;
import Interfaces.IState;
import Interfaces.ITransition;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// org.jdom:jdom:2.0.2
public class AutomataFactory {
    private static AutomataFactory uniqueInstance = new AutomataFactory();

    /**
     * Avoid instantiation of other instances
     */
    private AutomataFactory() {}

    /**
     * Give access to the instance
     * @return the singleton instance
     */
    public static AutomataFactory instance() {
        return uniqueInstance;
    }


    public boolean save(IAutomata automata, String savePath) {
        String outputPath = savePath;


        // Create the document and add the root
        Element ba = new Element("automata");
        Document doc = new Document(ba);

        // Add the list of states to the document
        Element states = createStates(automata);
        doc.getRootElement().addContent(states);

        // Add the list of transitions to the document
        Element transitions = createTransitions(automata);
        doc.getRootElement().addContent(transitions);


        // new XMLOutputter().output(doc, System.out);
        XMLOutputter xmlOutput = new XMLOutputter();

        // display nice nice
        xmlOutput.setFormat(Format.getPrettyFormat());
        try {
            xmlOutput.output(doc, new FileWriter(outputPath));
        } catch (IOException e) {
            System.err.println("Error in writing the file!");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private Element createStates(IAutomata automata) {
        Element states = new Element("states");
        for (IState s : automata.getStates()) {
            // Create new state element
            Element state = new Element("state");

            // Add name
            state.setAttribute("name", s.getKey());

            // Add final
            state.setAttribute("final", "" + s.isFinal());


            // Add the new state to the list of states
            states.addContent(state);
        }

        return states;
    }

    private Element createTransitions(IAutomata automata) {
        Element transitions = new Element("transitions");
        for (ITransition t : automata.getTransitionsList()) {
            // Create new state element
            Element transition = new Element("transition");

            // Add source
            transition.setAttribute("source", t.getSource().getKey());

            // Add destination
            transition.setAttribute("destination", t.getDestination().getKey());

            // Add symbol
            transition.setAttribute("symbol", "" + t.getSymbol());


            // Add the new transition to the list of transitions
            transitions.addContent(transition);
        }

        return transitions;
    }
}
