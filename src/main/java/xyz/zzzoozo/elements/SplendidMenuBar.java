package xyz.zzzoozo.elements;

import xyz.zzzoozo.elements.dialogs.NewProjectDialog;
import xyz.zzzoozo.elements.dialogs.PreferencesDialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SplendidMenuBar extends JMenuBar {

    public SplendidMenuBar(SplendidTree tree, JSplitPane splitPane, Frame owner) {
        // "File" menu
        JMenu fileMenu = new JMenu("file");

        // New JMenuItem
        JMenuItem newItem = new JMenuItem("new");

        newItem.addActionListener(e -> {
            NewProjectDialog newProjDialog = new NewProjectDialog(tree, splitPane);
            newProjDialog.setVisible(true);
        });

        fileMenu.add(newItem);

        JMenuItem openItem = new JMenuItem("open");

        openItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fileChooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedDir = fileChooser.getSelectedFile();
                tree.loadDirectory(selectedDir);
                tree.setVisible(true);

                splitPane.setDividerLocation(250);
            }

        });

        fileMenu.add(openItem);
        fileMenu.add(new JMenuItem("save"));

        JMenuItem exitItem = new JMenuItem("exit");

        exitItem.addActionListener(e -> System.exit(0));

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

        preferencesItem.addActionListener(e -> {
            PreferencesDialog prefDialog = new PreferencesDialog(owner);
            prefDialog.setVisible(true);
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