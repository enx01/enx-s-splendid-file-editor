import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class UtilsPane extends JPanel {

    private JTabbedPane content;

    public UtilsPane() {
        setLayout(new BorderLayout());
        content = new JTabbedPane();

        content.addTab("Terminal", new JPanel());
        content.addTab("Debugger", new JPanel());
        content.addTab("Logs", new JPanel());

        add(content);
    }

}
