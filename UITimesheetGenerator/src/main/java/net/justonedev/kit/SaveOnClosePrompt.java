/*
 * Licensed under the MIT License (MIT)
 * Copyright (c) 2024 Benjamin Claus
 */
package net.justonedev.kit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class SaveOnClosePrompt {

    static boolean showDialog() {
        final AtomicBoolean proceed = new AtomicBoolean(false);

        // Create the dialog
        JDialog dialog = new JDialog((Frame) null, "Save Changes?", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 150);
        dialog.setLocationRelativeTo(null);

        // Add message
        JLabel messageLabel = new JLabel("You have unsaved changes. Save?", SwingConstants.CENTER);
        dialog.add(messageLabel, BorderLayout.CENTER);

        // Create buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton saveButton = new JButton("Save");
        JButton saveAsButton = new JButton("Save as");
        JButton discardButton = new JButton("Discard");
        JButton cancelButton = new JButton("Cancel");

        // Button actions
        saveButton.addActionListener((ActionEvent e) -> {
            Main.saveFile(null);    // Gets the current open file to save
            proceed.set(true);
            dialog.dispose();
        });

        saveAsButton.addActionListener((ActionEvent e) -> {
            Main.saveFileAs();
            proceed.set(true);
            dialog.dispose();
        });

        discardButton.addActionListener((ActionEvent e) -> {
            proceed.set(true);
            dialog.dispose();
        });

        cancelButton.addActionListener((ActionEvent e) -> {
            proceed.set(false);
            dialog.dispose();
        });

        // Add buttons to panel
        buttonPanel.add(saveButton);
        buttonPanel.add(saveAsButton);
        buttonPanel.add(discardButton);
        buttonPanel.add(cancelButton);

        // Add button panel to dialog
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Display dialog
        dialog.setVisible(true);

        return proceed.get();
    }

}
