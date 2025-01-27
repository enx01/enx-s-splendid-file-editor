import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("enx's splendid file editor");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setSize(screenSize.width, screenSize.height);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            FileEditorPane fePane = new FileEditorPane();
            UtilsPane uPane = new UtilsPane();
            JSplitPane midPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fePane, uPane);
            midPane.setDividerLocation(400);
            midPane.setResizeWeight(0.1); // Set weight for resizing

            EnxTree tree = new EnxTree(fePane);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tree, midPane);
            splitPane.setDividerLocation(250); // Set initial divider location
            splitPane.setResizeWeight(0.1); // Set weight for resizing

            frame.add(splitPane);

            EnxMenuBar menuBar = new EnxMenuBar(tree, splitPane, frame);
            frame.setJMenuBar(menuBar);

            frame.setVisible(true);
        });
    }
}
