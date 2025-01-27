package xyz.zzzoozo.elements.panes;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class UtilsPane extends JPanel {

    public UtilsPane() {
        setLayout(new BorderLayout());
        JTabbedPane content = new JTabbedPane();

        content.addTab("Terminal", new JPanel());
        content.addTab("Debugger", new JPanel());
        content.addTab("Logs", new JPanel());

        add(content);
    }

}