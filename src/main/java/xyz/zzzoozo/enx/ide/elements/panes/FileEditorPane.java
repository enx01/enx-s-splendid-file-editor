package xyz.zzzoozo.enx.ide.elements.panes;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.JTextComponent;

import xyz.zzzoozo.enx.ide.exceptions.NoFileOpenedException;
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

                // System.out
                // .println("Checking visibleRect :" + visibleRect.toString() + "\nWith nloc :"
                // + nloc.toString());

                if (!visibleRect.contains(nloc)) {
                    super.adjustVisibility(nloc);
                }

            }
        }

        private final JTextArea textArea;

        public JTextArea getTextArea() {
            return textArea;
        }

        private final JTextArea lineNumbers;

        public LineNumberedTextArea(JTextArea textArea) {
            setLayout(new BorderLayout());
            this.textArea = textArea;

            textArea.setTabSize(4);
            // textArea.setLineWrap(true);
            // textArea.setWrapStyleWord(true);

            JPanel textAndLines = new JPanel();
            textAndLines.setLayout(new BorderLayout());

            JScrollPane scrollPane = new JScrollPane(textAndLines);

            textAndLines.add(textArea, BorderLayout.CENTER);

            lineNumbers = new JTextArea();
            lineNumbers.setBackground(Color.LIGHT_GRAY);
            lineNumbers.setEditable(false);
            lineNumbers.setMargin(new Insets(3, 5, 0, 5));

            textAndLines.add(lineNumbers, BorderLayout.WEST);

            DefaultCaret caret = (DefaultCaret) lineNumbers.getCaret();
            caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

            textArea.setCaret(new NoBehaviourCaret());
            lineNumbers.setHighlighter(null);

            textArea.addCaretListener(e -> updateHightlighter(/* highlighter, painter */));

            textArea.addCaretListener(e -> updateLineNumbers());

            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            this.add(scrollPane);

            updateLineNumbers();
        }

        private void updateHightlighter(/* DefaultHighlighter highlighter, DefaultHighlightPainter painter */) {
            try {

                DefaultHighlighter highlighter = (DefaultHighlighter) textArea.getHighlighter();
                Color high = new Color(66, 66, 66, 60);
                DefaultHighlighter.DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(
                        high);
                highlighter.setDrawsLayeredHighlights(false);

                highlighter.removeAllHighlights();

                int caretPosition = textArea.getCaretPosition();
                int lineStart = textArea.getLineStartOffset(textArea.getLineOfOffset(caretPosition));
                int lineEnd = textArea.getLineEndOffset(textArea.getLineOfOffset(caretPosition));

                highlighter.addHighlight(lineStart, lineEnd, painter);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
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
    private JTextArea textArea = new JTextArea();

    public FileEditorPane() {
        openedFiles = new ArrayList<>();

        setLayout(new BorderLayout());

        content = new JTabbedPane();

        add(content);
    }

    public String getCurrentText() throws NoFileOpenedException {
        int index = content.getSelectedIndex();

        if (index == -1)
            throw new NoFileOpenedException("Couldn't fetch content : No file opened");

        JPanel j = (JPanel) content.getComponentAt(index);
        LineNumberedTextArea lnta = (LineNumberedTextArea) j.getComponent(0);
        return lnta.getTextArea().getText();
    }

    public String getCurrentPath() throws NoFileOpenedException {
        int index = content.getSelectedIndex();

        if (openedFiles.isEmpty() || index == -1)
            throw new NoFileOpenedException("Couldn't get current file : No file opened");

        for (File f : openedFiles) {
            if (content.getTitleAt(index).equals(f.getName()))
                return f.getPath();
        }

        return "";
    }

    private void setFocusTo(File file) {
        // System.out.println("Iterating through tabs. Length : " +
        // content.getTabCount());
        for (int i = 0; i < content.getTabCount(); i++) {
            // System.out
            // .println("Checking element at : " + i + " , " + content.getTitleAt(i));

            if (content.getTitleAt(i).equals(file.getName())) {
                // System.out.println("found match! setting selected index to : " + i);
                content.setSelectedIndex(i);
            }
        }
        // System.out.println("content:" + getTextArea().getText());
    }

    public void openFile(File file) {
        if (file.isDirectory())
            return;

        if (openedFiles.contains(file)) {
            setFocusTo(file);
            return;
        }

        textArea = new JTextArea();

        openedFiles.add(file);

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

        // System.out.println("content:" + getTextArea().getText());

    }

    public JTextArea getTextArea() {
        return textArea;
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

    public JTabbedPane getContent() {
        return content;
    }

    public ArrayList<File> getOpenedFiles() {
        return openedFiles;
    }

}
