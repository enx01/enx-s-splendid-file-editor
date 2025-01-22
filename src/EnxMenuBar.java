import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class EnxMenuBar extends JMenuBar {

    private JDesktopPane desktopPane;
    private EnxTree tree;
    private JSplitPane splitPane;

    public EnxMenuBar(JDesktopPane desktopPane, EnxTree tree, JSplitPane splitPane) {
        this.desktopPane = desktopPane;
        this.tree = tree;
        this.splitPane = splitPane;

        // "File" menu
        JMenu fileMenu = new JMenu("file");

        // New JMenuItem
        JMenuItem newItem = new JMenuItem("new");

        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewProjectFrame newProjFrame = new NewProjectFrame(tree, splitPane);
                desktopPane.add(newProjFrame);
                newProjFrame.setVisible(true);
            }
        });

        fileMenu.add(newItem);

        JMenuItem openItem = new JMenuItem("open");

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fileChooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedDir = fileChooser.getSelectedFile();
                    tree.loadDirectory(selectedDir);
                    tree.setVisible(true);

                    splitPane.setDividerLocation(250);
                }

            }
        });

        fileMenu.add(openItem);
        fileMenu.add(new JMenuItem("save"));

        JMenuItem exitItem = new JMenuItem("exit");

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        });

        fileMenu.add(exitItem);
        this.add(fileMenu);

        // "Edit" menu
        JMenu editMenu = new JMenu("edit");
        editMenu.add(new JMenuItem("undo"));
        editMenu.add(new JMenuItem("redo"));
        editMenu.add(new JMenuItem("cut"));
        editMenu.add(new JMenuItem("copy"));
        editMenu.add(new JMenuItem("paste"));
        this.add(editMenu);

        // "Settings" menu
        JMenu settingsMenu = new JMenu("settings");

        JMenuItem preferencesItem = new JMenuItem("preferences");

        preferencesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PreferencesFrame prefFrame = new PreferencesFrame();
                desktopPane.add(prefFrame);
                prefFrame.setVisible(true);
            }
        });

        settingsMenu.add(preferencesItem);
        this.add(settingsMenu);

        // "Help" menu
        JMenu helpMenu = new JMenu("help");
        helpMenu.add(new JMenuItem("documentation"));
        helpMenu.add(new JMenuItem("about"));
        this.add(helpMenu);
    }

}
