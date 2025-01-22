import javax.swing.*;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("enx's splendid file editor");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setSize(screenSize.width, screenSize.height);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            JDesktopPane desktopPane = new JDesktopPane();
            EnxTree tree = new EnxTree();

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tree, desktopPane);
            splitPane.setDividerLocation(250); // Set initial divider location
            splitPane.setResizeWeight(0.2); // Set weight for resizing

            frame.add(splitPane, BorderLayout.CENTER);

            EnxMenuBar menuBar = new EnxMenuBar(desktopPane, tree, splitPane);
            frame.setJMenuBar(menuBar);

            frame.setVisible(true);
        });
    }
}
