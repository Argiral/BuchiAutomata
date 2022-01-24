package Model;

import Interfaces.IAutomata;
import Interfaces.IState;
import Interfaces.ITransition;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutomataFactory {
    private static final AutomataFactory uniqueInstance = new AutomataFactory();

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

    public IAutomata loadFromXML(String filePath) {
        Document config;

        SAXBuilder builder = new SAXBuilder();
        try {
            config = builder.build(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // Create new automata
        IAutomata newAutomata = new BuchiAutomata();

        // Configure states and get map
        Map<String, IState> nameStateMap = configureStates(config, newAutomata);

        // Configure transitions
        configureTransitions(config, newAutomata, nameStateMap);

        return newAutomata;
    }

    private Map<String, IState> configureStates(Document config, IAutomata newAutomata) {
        Map<String, IState> map = new HashMap<>();

        // Assertion: the initial state is the first state of the list
        List<Element> states = config.getRootElement().getChild("states").getChildren("state");
        for (Element e : states) {
            // Get the name of the state
            String name = e.getAttributeValue("name");

            // Get type of state (final/not final)
            boolean isFinal = e.getAttributeValue("final").equals("true");


            // Add the new state to the automata
            IState newState = new State(name, isFinal);
            newAutomata.addState(newState);

            // Create map
            map.put(name, newState);
        }

        return map;
    }

    private void configureTransitions(Document config, IAutomata newAutomata, Map<String, IState> nameStateMap) {
        List<Element> transitions = config.getRootElement().getChild("transitions").getChildren("transition");
        for (Element e : transitions) {

            // Get the name of the source state
            String source = e.getAttributeValue("source");

            // Get the name of the destination state
            String destination = e.getAttributeValue("destination");

            // Get the transition symbol
            char symbol = e.getAttributeValue("symbol").charAt(0);


            // First method much less efficient, needs to iterate over all states twice for every transition
//            newAutomata.addTransition(newAutomata.getStateByKey(source), symbol, newAutomata.getStateByKey(destination));
            newAutomata.addTransition(nameStateMap.get(source), symbol, nameStateMap.get(destination));
        }
    }

    public boolean saveToXml(IAutomata automata, String savePath) {

        // Create the document and add the root
        Element ba = new Element("automata");
        Document doc = new Document(ba);

        // Add the list of states to the document
        Element states = createStates(automata);
        doc.getRootElement().addContent(states);

        // Add the list of transitions to the document
        Element transitions = createTransitions(automata);
        doc.getRootElement().addContent(transitions);


        XMLOutputter xmlOutput = new XMLOutputter();

        // display nice
        xmlOutput.setFormat(Format.getPrettyFormat());
        try {
            xmlOutput.output(doc, new FileWriter(savePath));
        } catch (IOException e) {
            System.err.println("Error in writing the file!");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private Element createStates(IAutomata automata) {
        Element states = new Element("states");

        // Add initial state as first state
        Element initialState = new Element("state");
        initialState.setAttribute("name", automata.getInitialState().getKey());
        initialState.setAttribute("final", "" + automata.getInitialState().isFinal());

        states.addContent(initialState);

        // Add other states (except initial state, already added)
        for (IState s : automata.getStates()) {
            if (!s.equals(automata.getInitialState())) {
                // Create new state element
                Element state = new Element("state");

                // Add name
                state.setAttribute("name", s.getKey());

                // Add final
                state.setAttribute("final", "" + s.isFinal());


                // Add the new state to the list of states
                states.addContent(state);
            }
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
