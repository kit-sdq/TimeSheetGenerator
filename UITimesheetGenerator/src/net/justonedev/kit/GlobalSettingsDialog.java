/*
 * Licensed under the MIT License (MIT)
 * Copyright (c) 2024 Benjamin Claus
 */
package net.justonedev.kit;

import net.justonedev.kit.json.Global;
import net.justonedev.kit.json.JSONHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class GlobalSettingsDialog {

    private static final String WORK_AREA_UB = "Unibereich (ub)";
    private static final String WORK_AREA_GF = "Großforschung (gf)";

    public static void showGlobalSettingsDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Global Settings");
        dialog.setSize(600, 400);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null); // Center the dialog

        Global globalSettings = JSONHandler.globalSettings;

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding of 10

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10); // Spacing between components

        int row = 0;

        // Error labels array for validation messages
        JLabel[] errorLabels = new JLabel[6];

        // Fields array for text fields
        JTextField[] fields = new JTextField[5];
        JComboBox<String> workAreaSelector = new JComboBox<>();
        workAreaSelector.addItem(WORK_AREA_UB);
        workAreaSelector.addItem(WORK_AREA_GF);
        workAreaSelector.setSelectedIndex(getIndexValue(globalSettings.getWorkingArea()));

        String[] labels = {"Name:", "Staff ID:", "Department:", "Working Time:", "Wage:", "Working Area:"};
        String[] placeholders = {"Enter your name", "Enter your staff ID", "Enter your department",
                "Enter working time (HH:MM)", "Enter your wage", "Enter working area"};
        String[] initialValues = {
                globalSettings.getName(),
                String.valueOf(globalSettings.getStaffId()),
                globalSettings.getDepartment(),
                globalSettings.getWorkingTime(),
                String.valueOf(globalSettings.getWage())
        };

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setHorizontalAlignment(JLabel.RIGHT);
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(label, gbc);

            gbc.gridx = 1;
            gbc.gridy = row;
            gbc.weightx = 1;

            // Error label for validation messages
            JLabel errorLabel = new JLabel(" ");
            errorLabel.setForeground(Color.RED);
            errorLabels[i] = errorLabel;
            gbc.gridx = 2;
            gbc.gridy = row;
            gbc.weightx = 0;
            panel.add(errorLabel, gbc);

            if (i < labels.length - 1) {
                JTextField textField = new JTextField(20);
                DialogHelper.addPlaceholderText(textField, placeholders[i], initialValues[i]);
                fields[i] = textField;
                panel.add(textField, gbc);
                final int index = i;

                // Add focus listener for validation when focus is lost
                textField.addFocusListener(new FocusAdapter() {
                    public void focusLost(FocusEvent e) {
                        validateField(fields[index], errorLabels[index], index);
                    }
                });
            } else {
                panel.add(workAreaSelector, gbc);
            }

            row++;
        }

        // Buttons at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(buttonPanel, gbc);

        // Action listeners for buttons
        saveButton.addActionListener(e -> {
            boolean hasError = false;
            // Validate all fields
            for (int i = 0; i < fields.length; i++) {
                validateField(fields[i], errorLabels[i], i);
                if (!errorLabels[i].getText().isBlank()) {
                    hasError = true;
                }
            }

            if (hasError) {
                // Show error message
                JOptionPane.showMessageDialog(dialog, "Please fix the errors before saving.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update globalSettings object
            globalSettings.setName(fields[0].getText());
            globalSettings.setStaffId(Integer.parseInt(fields[1].getText()));
            globalSettings.setDepartment(fields[2].getText());
            globalSettings.setWorkingTime(fields[3].getText());
            globalSettings.setWage(Double.parseDouble(fields[4].getText()));
            globalSettings.setWorkingArea(getConfigValue(workAreaSelector.getSelectedItem()));

            // Save globalSettings to file or database as needed
            JSONHandler.saveGlobal(globalSettings);

            dialog.dispose();
        });

        cancelButton.addActionListener(e -> {
            dialog.dispose();
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private static int getIndexValue(String configValue) {
        if (configValue == null) return 0;
        if (configValue.equals("ub")) return 0;
        return 1;
    }

    private static String getConfigValue(Object selectorValue) {
        if (selectorValue == null) return "ub";
        return selectorValue.toString().equals(WORK_AREA_UB) ? "ub" : "gf";
    }

    // Helper method to add placeholder text
    private static void validateField(JTextField textField, JLabel errorLabel, int index) {
        String text = textField.getText().trim();
        // Ignore placeholder text during validation
        if (textField.getForeground().equals(Color.GRAY)) {
            errorLabel.setText(" ");
            return;
        }

        switch (index) {
            case 0: // Name
                if (text.isEmpty()) {
                    errorLabel.setText("Name cannot be empty");
                } else {
                    errorLabel.setText(" ");
                }
                break;
            case 1: // Staff ID
                try {
                    Integer.parseInt(text);
                    errorLabel.setText(" ");
                } catch (NumberFormatException e) {
                    errorLabel.setText("Invalid staff ID");
                }
                break;
            case 2: // Department
                if (text.isEmpty()) {
                    errorLabel.setText("Department cannot be empty");
                } else {
                    errorLabel.setText(" ");
                }
                break;
            case 3: // Working Time
                if (!DialogHelper.isValidTimeFormat(text)) {
                    errorLabel.setText("Invalid time format (HH:MM)");
                } else {
                    errorLabel.setText(" ");
                }
                break;
            case 4: // Wage
                try {
                    Double.parseDouble(text);
                    errorLabel.setText(" ");
                } catch (NumberFormatException e) {
                    errorLabel.setText("Invalid wage");
                }
                break;
            case 5: // Working Area
                if (text.isEmpty()) {
                    errorLabel.setText("Working area cannot be empty");
                } else {
                    errorLabel.setText(" ");
                }
                break;
        }
    }

}
