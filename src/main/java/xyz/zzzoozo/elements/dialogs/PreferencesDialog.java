package xyz.zzzoozo.elements.dialogs;

import javax.swing.*;
import java.awt.*;

public class PreferencesDialog extends JDialog {

    public PreferencesDialog(Frame owner) {
        setSize(500, 300);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JComboBox<String> lookAndFeelComboBox = new JComboBox<>();
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            lookAndFeelComboBox.addItem(info.getName());
        }

        panel.add(new JLabel("look & feel :"));
        panel.add(lookAndFeelComboBox);

        JButton applyButton = new JButton("apply");
        panel.add(applyButton);

        add(panel, BorderLayout.CENTER);

        applyButton.addActionListener(e -> {
            String selectedLookAndFeel = (String) lookAndFeelComboBox.getSelectedItem();
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if (info.getName().equals(selectedLookAndFeel)) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }

                SwingUtilities.updateComponentTreeUI(owner);
                SwingUtilities.updateComponentTreeUI(PreferencesDialog.this);
                // pack();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PreferencesDialog.this, ex.getMessage(), "error!",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner); // Center the dialog relative to the owner
    }

}
