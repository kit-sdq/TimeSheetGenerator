package net.justonedev.kit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SaveOnClosePrompt {

    static void showDialog() {
        // Create the dialog
        JDialog dialog = new JDialog((Frame) null, "Save Changes?", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);

        // Add message
        JLabel messageLabel = new JLabel("You have unsaved changes. Save?", SwingConstants.CENTER);
        dialog.add(messageLabel, BorderLayout.CENTER);

        // Create buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton saveButton = new JButton("Save");
        JButton saveAsButton = new JButton("Save as");
        JButton discardButton = new JButton("Discard");

        // Button actions
        saveButton.addActionListener((ActionEvent e) -> {
            System.out.println("Save button pressed");
            dialog.dispose();
        });

        saveAsButton.addActionListener((ActionEvent e) -> {
            System.out.println("Save as button pressed");
            dialog.dispose();
        });

        discardButton.addActionListener((ActionEvent e) -> {
            dialog.dispose();
        });

        // Add buttons to panel
        buttonPanel.add(saveButton);
        buttonPanel.add(saveAsButton);
        buttonPanel.add(discardButton);

        // Add button panel to dialog
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Display dialog
        dialog.setVisible(true);
    }

}
