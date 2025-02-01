package xyz.zzzoozo.enx.ide.elements.panes;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

import java.io.*;
import java.awt.*;
import java.util.ArrayList;

public class FileEditorPane extends JPanel {

    private static class LineNumberedTextArea extends JPanel {

        private static class NoBehaviourCaret extends DefaultCaret {
            @Override
            protected void adjustVisibility(Rectangle nloc) {
                JTextComponent component = getComponent();
                if (component == null) {
                    return;
                }

                Rectangle visibleRect = component.getVisibleRect();

                System.out
                        .println("Checking visibleRect :" + visibleRect.toString() + "\nWith nloc :" + nloc.toString());

                if (!visibleRect.contains(nloc)) {
                    super.adjustVisibility(nloc);
                } else {
                    // do nothing
                }

            }
        }

        private final JTextArea textArea;
        private final JTextArea lineNumbers;

        public LineNumberedTextArea(JTextArea textArea) {
            setLayout(new BorderLayout());
            this.textArea = textArea;

            JPanel textAndLines = new JPanel();
            textAndLines.setLayout(new BorderLayout());

            JScrollPane scrollPane = new JScrollPane(textAndLines);
            // textAndLines.add(scrollPane, BorderLayout.CENTER);

            textAndLines.add(textArea, BorderLayout.CENTER);

            lineNumbers = new JTextArea();
            lineNumbers.setBackground(Color.LIGHT_GRAY);
            lineNumbers.setEditable(false);
            lineNumbers.setMargin(new Insets(3, 5, 0, 5));
            // scrollPane.setRowHeaderView(lineNumbers);

            // JScrollPane lineNumbersScrollPane = new JScrollPane(lineNumbers);

            // scrollPane.add(lineNumbers);

            textAndLines.add(lineNumbers, BorderLayout.WEST);

            // lineNumbersScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            // lineNumbersScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            // add(lineNumbersScrollPane, BorderLayout.WEST);

            // DefaultCaret caret = (DefaultCaret) textArea.getCaret();
            // caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

            DefaultCaret caret = (DefaultCaret) lineNumbers.getCaret();
            caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

            // textArea.setCaretPosition(0);
            // lineNumbers.setCaretPosition(0);

            textArea.setCaret(new NoBehaviourCaret());
            // lineNumbers.setCaret(new NoBehaviourCaret());
            lineNumbers.setHighlighter(null);

            textArea.addCaretListener(e -> updateLineNumbers());

            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            this.add(scrollPane);

            updateLineNumbers();
        }

        private void updateLineNumbers() {
            int lines = textArea.getLineCount();
            StringBuilder numbers = new StringBuilder();
            for (int i = 1; i <= lines; i++) {
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
