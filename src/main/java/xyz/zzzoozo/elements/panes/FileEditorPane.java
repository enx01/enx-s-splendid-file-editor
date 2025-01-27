package xyz.zzzoozo.elements.panes;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.util.ArrayList;

public class FileEditorPane extends JPanel {

    private static class LineNumberedTextArea extends JPanel {

        private final JTextArea textArea;
        private final JTextPane lineNumbers;

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

    private final JTabbedPane content;
    private final ArrayList<File> openedFiles;

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

        LineNumberedTextArea lineNumberedTextArea = new LineNumberedTextArea(textArea);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(lineNumberedTextArea);

        addTab(file, panel);
    }

    private void addTab(File file, Component component) {
        String title = file.getName();
        content.addTab(title, component);
        int index = content.indexOfTab(title);

        JPanel tabPanel = new JPanel(new GridBagLayout());
        tabPanel.setOpaque(false);
        JLabel tabLabel = new JLabel(title);
        JButton closeButton = new JButton("x");

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

        closeButton.addActionListener(e -> {
            int index1 = content.indexOfTabComponent(tabPanel);
            if (index1 != -1) {
                openedFiles.remove(file);
                content.remove(index1);
            }
        });

        content.setTabComponentAt(index, tabPanel);
        content.setSelectedIndex(content.getTabCount() - 1); // Select the newly added tab
    }

}