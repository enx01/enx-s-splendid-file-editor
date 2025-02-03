package xyz.zzzoozo.enx.ide;

import xyz.zzzoozo.enx.ide.elements.SplendidMenuBar;
import xyz.zzzoozo.enx.ide.elements.SplendidTree;
import xyz.zzzoozo.enx.ide.elements.panes.FileEditorPane;
import xyz.zzzoozo.enx.ide.elements.panes.UtilsPane;

import javax.swing.*;

import java.awt.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::run);
    }

    private static void run() {
        JFrame frame = new JFrame("enx's splendid ide");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // try {
        // UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        // } catch (Exception e) {
        // JOptionPane.showMessageDialog(frame, e.getMessage(), "error!",
        // JOptionPane.ERROR_MESSAGE);
        // }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        FileEditorPane fePane = new FileEditorPane();
        UtilsPane uPane = new UtilsPane();

        JSplitPane midPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fePane, uPane);
        midPane.setDividerLocation(400);
        midPane.setResizeWeight(0.1); // Set weight for resizing

        SplendidTree tree = new SplendidTree(fePane);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tree, midPane);
        splitPane.setDividerLocation(250); // Set initial divider location
        splitPane.setResizeWeight(0.1); // Set weight for resizing

        // frame.add(term);
        frame.add(splitPane);

        SplendidMenuBar menuBar = new SplendidMenuBar(tree, splitPane, frame, fePane);
        frame.setJMenuBar(menuBar);

        frame.setVisible(true);
    }
}
