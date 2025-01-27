import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;

public class FileEditorPane extends JPanel {

    private class LineNumberedTextArea extends JPanel {

        private JTextArea textArea;
        private JTextPane lineNumbers;

        public LineNumberedTextArea(JTextArea textArea) {
            setLayout(new BorderLayout());
            this.textArea = textArea;
            lineNumbers = new JTextPane();
            lineNumbers.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setRowHeaderView(lineNumbers);
            textArea.addCaretListener(e -> updateLineNumbers());
            this.add(scrollPane, BorderLayout.CENTER);
            updateLineNumbers();
        }

        private void updateLineNumbers() {
            int lines = textArea.getLineCount();
            StringBuilder numbers = new StringBuilder();
            for (int i = 1; i < lines; i++) {
                numbers.append(i).append("\n");
            }
            lineNumbers.setText(numbers.toString());
        }

    }

    private JTabbedPane content;
    private ArrayList<File> openedFiles;

    public FileEditorPane() {
        openedFiles = new ArrayList<>();

        setLayout(new BorderLayout());

        content = new JTabbedPane();

        // content.addTab("dummy tab 1", new JPanel().add(new JTextArea()));
        // content.addTab("dummy tab 2", new JPanel().add(new JTextArea()));
        // content.addTab("dummy tab 3", new JPanel().add(new JTextArea()));

        add(content);
    }

    private void setFocusTo(File file) {
        System.out.println("Iterating through tabs. Length : " + content.getTabCount());
        for (int i = 0; i < content.getTabCount(); i++) {
            System.out
                    .println("Checking element at : " + i + " , " + content.getTitleAt(i));

            if (content.getTitleAt(i).equals(file.getName())) {
                System.out.println("found match! setting selected index to : " + i);
                content.setSelectedIndex(i);
            }
        }
    }

    public void openFile(File file) {
        if (file.isDirectory())
            return;

        if (openedFiles.contains(file)) {
            setFocusTo(file);
            return;
        }

        openedFiles.add(file);
        JTextArea textArea = new JTextArea();

        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }

            textArea.setText(fileContent.toString());

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        // JScrollPane scrollPane = new JScrollPane(textArea);

        LineNumberedTextArea lineNumberedTextArea = new LineNumberedTextArea(textArea);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(lineNumberedTextArea);

        // content.addTab(file.getName(), panel);

        // content.setSelectedIndex(content.getTabCount() - 1);
        addTab(file, panel);
    }

    private void addTab(File file, Component component) {
        String title = file.getName();
        content.addTab(title, component);
        int index = content.indexOfTab(title);

        // Create a custom tab component
        JPanel tabPanel = new JPanel(new GridBagLayout());
        tabPanel.setOpaque(false);
        JLabel tabLabel = new JLabel(title);
        JButton closeButton = new JButton("x");

        // closeButton.setPreferredSize(new Dimension(20, 20));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        tabPanel.add(tabLabel, gbc);

        gbc.gridx++;
        gbc.weightx = 0;

        tabPanel.add(closeButton, gbc);

        // Add action listener to the close button
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = content.indexOfTabComponent(tabPanel);
                if (index != -1) {
                    openedFiles.remove(file);
                    content.remove(index);
                }
            }
        });

        content.setTabComponentAt(index, tabPanel);
        content.setSelectedIndex(content.getTabCount() - 1); // Select the newly added tab
    }

}
