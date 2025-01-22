import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class PreferencesFrame extends JInternalFrame {

    public PreferencesFrame() {
        super("preferences", false, true, false, false);
        setSize(500, 300);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JComboBox<String> lookAndFeelComboBox = new JComboBox<>();
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            lookAndFeelComboBox.addItem(info.getName());
        }

        panel.add(new JLabel("themes :"));
        panel.add(lookAndFeelComboBox);

        JButton applyButton = new JButton("apply");
        panel.add(applyButton);

        add(panel, BorderLayout.CENTER);

        // pack();

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLookAndFeel = (String) lookAndFeelComboBox.getSelectedItem();
                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if (info.getName().equals(selectedLookAndFeel)) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }

                    SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(PreferencesFrame.this));
                    // pack();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

}
