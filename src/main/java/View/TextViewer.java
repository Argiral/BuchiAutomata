package View;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class TextViewer {
    public static void showInTextArea(String text) {
        JPanel middlePanel = new JPanel ();
        middlePanel.setBorder(new TitledBorder(new EtchedBorder(), "Display Area"));

        // create the middle panel components
        JTextArea display = new JTextArea(16,58);
        display.setText(text);
        display.setEditable (false); // set textArea non-editable
        JScrollPane scroll = new JScrollPane(display);
        scroll.setBorder(new TitledBorder(new EtchedBorder(),"My border"));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //Add Textarea in to middle panel
        middlePanel.add(scroll);

        // My code
        JFrame frame = new JFrame();
//        frame.add(middlePanel);
        frame.add(scroll);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo (null);
        frame.setVisible(true);

//        System.out.println("§ͮɅVɆ∀∃∄¬");
//        display.append("§ͮɅVɆ∀∃∄¬∈∧\nᴀʙᴄ");
//        System.out.println("ᴀʙᴄᴅᴇғɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ");
//        System.out.println("aᵃᵇᶜᵈᵉᶠᵍʰᶦʲᵏˡᵐⁿᵒᵖᵠʳˢᵗᵘᵛʷˣʸᶻ");
//        display.append("    newline?");
    }
}
