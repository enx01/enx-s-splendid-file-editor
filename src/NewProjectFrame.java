import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class NewProjectFrame extends JInternalFrame {

    private EnxTree tree;
    private JSplitPane splitPane;

    public NewProjectFrame(EnxTree tree, JSplitPane splitPane) {
        super("New Project", false, true, false, false);
        this.tree = tree;
        this.splitPane = splitPane;

        setSize(400, 150);
        setMinimumSize(new Dimension(400, 150));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 5, 5); // Add padding around components

        JLabel nameLabel = new JLabel("name:");
        JTextField nameField = new JTextField(20); // Set width to 15 columns
        JLabel locationLabel = new JLabel("location:");
        JTextField locationField = new JTextField(20); // Set width to 15 columns
        JButton browseButton = new JButton("...");
        JButton createButton = new JButton("create");

        // Add components to the layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST; // Align to the left
        add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(locationLabel, gbc);

        gbc.gridx = 1;
        add(locationField, gbc);

        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        add(browseButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        add(createButton, gbc);

        // Browse button action
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fileChooser.showOpenDialog(NewProjectFrame.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedDirectory = fileChooser.getSelectedFile();
                    locationField.setText(selectedDirectory.getAbsolutePath());
                }
            }
        });

        // Create button action
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String projectName = nameField.getText().trim();
                String projectLocation = locationField.getText().trim();

                if (!projectName.isEmpty() && !projectLocation.isEmpty()) {
                    File projectDir = new File(projectLocation, projectName);

                    if (projectDir.mkdir()) {
                        tree.loadDirectory(projectDir);
                        tree.setVisible(true);

                        splitPane.setDividerLocation(250);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(NewProjectFrame.this, "Failed to create project directory.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(NewProjectFrame.this,
                            "Please provide valid project name and location.",
                            "Error", JOptionPane.ERROR_MESSAGE);

                }

            }
        });
    }

}
