package xyz.zzzoozo.enx.ide.elements.panes;

import java.awt.*;
import java.io.*;

import javax.swing.*;


public class UtilsPane extends JPanel {

    private static class SplendidTerminal extends JPanel {
        private final JTextArea textArea;
        private final JTextField commandField;
        private PrintWriter printWriter;

        public SplendidTerminal() {
            setLayout(new BorderLayout());

            textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setBackground(Color.BLACK);
            textArea.setForeground(Color.WHITE);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            add(new JScrollPane(textArea), BorderLayout.CENTER);

            // Create the text field for command input
            commandField = new JTextField();
            commandField.setBackground(Color.BLACK);
            commandField.setForeground(Color.WHITE);
            commandField.setFont(new Font("Monospaced", Font.PLAIN, 20));

            // Set preferred size for the command field
            commandField.setPreferredSize(new Dimension(800, 30)); // Width, Height
            add(commandField, BorderLayout.SOUTH);



            // Add action listener for the command field
            commandField.addActionListener(e -> {
                String command = commandField.getText();
                sendCommand(command);
                commandField.setText(""); // Clear the input field
            });

            startLocalShell();
        }


        private void startLocalShell() {
            try {
                // Determine the command based on the operating system
                String os = System.getProperty("os.name").toLowerCase();
                String command = os.contains("win") ? "cmd.exe" : "zsh";

                // Start the process
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                Process process = processBuilder.redirectErrorStream(true).start();

                // Set up input and output streams
                printWriter = new PrintWriter(process.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // Read output in a separate thread
                new Thread(() -> {
                    String line;
                    try {
                        while ((line = reader.readLine()) != null) {
                            textArea.append(line + "\n");
                            textArea.setCaretPosition(textArea.getDocument().getLength());
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error starting thread: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }).start();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error starting local shell: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        public void sendCommand(String command) {
            if (printWriter != null) {
                printWriter.println(command);
            }
        }

    }

    public UtilsPane() {
        setLayout(new BorderLayout());
        JTabbedPane content = new JTabbedPane();

        SplendidTerminal terminal = new SplendidTerminal();

        content.addTab("terminal", terminal);
        content.addTab("debugger", new JPanel());
        content.addTab("logs", new JPanel());

        add(content);
    }

}
