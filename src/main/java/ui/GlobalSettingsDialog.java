/* Licensed under MIT 2024-2025. */
package ui;

import ui.json.Global;
import ui.json.JSONHandler;
import ui.json.UISettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public final class GlobalSettingsDialog {

	private GlobalSettingsDialog() {
		// Don't allow instances of this class
	}

	private static final String WORK_AREA_UB = "Unibereich (ub)";
	private static final String WORK_AREA_GF = "Großforschung (gf)";

	private static final int SCROLL_SENSITIVITY = 16;

	public static void showGlobalSettingsDialog(UserInterface parentUI) {
		JDialog dialog = new JDialog();
		dialog.setTitle("Global Settings");
		dialog.setSize(600, 485);
		dialog.setModal(true);
		dialog.setLocationRelativeTo(null); // Center the dialog

		Global globalSettings = JSONHandler.getGlobalSettings();
		UISettings uiSettings = JSONHandler.getUISettings();

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding of 10

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 10, 5, 10); // Spacing between components

		int row = 0;

		// Error labels array for validation messages
		JLabel[] errorLabels = new JLabel[7];

		// Fields array for text fields
		JTextField[] fields = new JTextField[5];
		JComboBox<String> workAreaSelector = new JComboBox<>();
		workAreaSelector.addItem(WORK_AREA_UB);
		workAreaSelector.addItem(WORK_AREA_GF);
		workAreaSelector.setSelectedIndex(getIndexValue(globalSettings.getWorkingArea()));

		// Checkboxes
		JCheckBox addSignatureBox = new JCheckBox();
		JCheckBox addVacationEntryBox = new JCheckBox();
		JCheckBox useYYYYBox = new JCheckBox();
		JCheckBox useGermanMonthNameBox = new JCheckBox();
		JCheckBox warnHoursMismatchBox = new JCheckBox();
		addSignatureBox.setSelected(uiSettings.isAddSignature());
		addVacationEntryBox.setSelected(uiSettings.isAddVacationEntry());
		useYYYYBox.setSelected(uiSettings.isUseYYYY());
		useGermanMonthNameBox.setSelected(uiSettings.isUseGermanMonths());
		warnHoursMismatchBox.setSelected(uiSettings.isWarnOnHoursMismatch());

		final int TEXTBOXES_COUNT = 5;

		String[] labels = { "Name:", "Staff ID:", "Department:", "Working Time:", "Wage:", "Working Area:", "Add Signature at Bottom:",
				"Explicitly add Vacation Entry:", "Use 4-digit year in the day column:", "Use German month names", "Warn when too few/ too many hours:" };
		String[] placeholders = { "Enter your name", "Enter your staff ID", "Enter your department", "Enter working time (HH:MM)", "Enter your wage" };
		String[] initialValues = { globalSettings.getName(), String.valueOf(globalSettings.getStaffId()), globalSettings.getDepartment(),
				globalSettings.getWorkingTime(), String.valueOf(globalSettings.getWage()) };
		JCheckBox[] checkBoxes = { addSignatureBox, addVacationEntryBox, useYYYYBox, useGermanMonthNameBox, warnHoursMismatchBox };

		for (int i = 0; i < labels.length; i++) {
			JLabel label = new JLabel(labels[i]);
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			gbc.gridx = 0;
			gbc.gridy = row;
			gbc.weightx = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			panel.add(label, gbc);

			gbc.gridx = 2;
			gbc.gridy = row;
			gbc.weightx = 0;

			// Error label for validation messages
			if (i < errorLabels.length) {
				JLabel errorLabel = new JLabel(" ");
				errorLabel.setForeground(Color.RED);
				errorLabels[i] = errorLabel;
				panel.add(errorLabel, gbc);
			}

			if (i < TEXTBOXES_COUNT) {
				JTextField textField = new JTextField(20);
				DialogHelper.addPlaceholderText(textField, placeholders[i], initialValues[i]);
				fields[i] = textField;
				panel.add(textField, gbc);
				final int index = i;

				// Add focus listener for validation when focus is lost
				textField.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent e) {
						validateField(fields[index], errorLabels[index], index);
					}
				});
			} else if (i == TEXTBOXES_COUNT) {
				panel.add(workAreaSelector, gbc);
			} else {
				panel.add(checkBoxes[i - TEXTBOXES_COUNT - 1], gbc);
			}

			row++;
		}

		// Buttons at the bottom
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton saveButton = new JButton("Save");
		JButton cancelButton = new JButton("Cancel");
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		// Buttons at the bottom
		JPanel presetButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton presetProggenButton = new JButton("Proggen I Preset");
		JButton presetAlgoButton = new JButton("Algo I Preset");
		presetProggenButton.addActionListener((e) -> {
			addVacationEntryBox.setSelected(false);
			useYYYYBox.setSelected(false);
			useGermanMonthNameBox.setSelected(false);
		});
		presetAlgoButton.addActionListener((e) -> {
			addVacationEntryBox.setSelected(true);
			useYYYYBox.setSelected(true);
			useGermanMonthNameBox.setSelected(true);
		});
		presetButtonPanel.add(presetProggenButton);
		presetButtonPanel.add(presetAlgoButton);

		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = 3;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.SOUTHWEST;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(presetButtonPanel, gbc);

		gbc.anchor = GridBagConstraints.SOUTHEAST;
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

			uiSettings.setAddSignature(addSignatureBox.isSelected());
			uiSettings.setAddVacationEntry(addVacationEntryBox.isSelected());
			uiSettings.setUseYYYY(useYYYYBox.isSelected());
			uiSettings.setUseGermanMonths(useGermanMonthNameBox.isSelected());
			uiSettings.setWarnOnHoursMismatch(warnHoursMismatchBox.isSelected());

			// Save globalSettings to file or database as needed
			JSONHandler.saveGlobal(globalSettings);
			JSONHandler.saveUISettings(uiSettings);

			parentUI.updateTotalTimeWorkedUI();

			dialog.dispose();
		});

		cancelButton.addActionListener(e -> dialog.dispose());

		JScrollPane scrollPanel = new JScrollPane(panel);
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.getVerticalScrollBar().setUnitIncrement(SCROLL_SENSITIVITY);
		dialog.add(scrollPanel);
		dialog.setVisible(true);
	}

	private static int getIndexValue(String configValue) {
		if (configValue == null)
			return 0;
		if (configValue.equals("ub"))
			return 0;
		return 1;
	}

	private static String getConfigValue(Object selectorValue) {
		if (selectorValue == null)
			return "ub";
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
		case 0:
			validateNotEmpty(text, errorLabel, "Name");
			break;
		case 1:
			try {
				Integer.parseInt(text);
				errorLabel.setText(" ");
			} catch (NumberFormatException e) {
				errorLabel.setText("Invalid staff ID");
			}
			break;
		case 2:
			validateNotEmpty(text, errorLabel, "Department");
			break;
		case 3:
			if (!DialogHelper.isValidTimeFormat(text)) {
				errorLabel.setText("Invalid time format (HH:MM)");
			} else {
				errorLabel.setText(" ");
			}
			break;
		case 4:
			try {
				Double.parseDouble(text);
				errorLabel.setText(" ");
			} catch (NumberFormatException e) {
				errorLabel.setText("Invalid wage");
			}
			break;
		case 5:
			validateNotEmpty(text, errorLabel, "Working area");
			break;
		default:
			// Unknown field, nothing to validate
			break;
		}
	}

	private static void validateNotEmpty(String text, JLabel errorLabel, String setting) {
		if (text.isEmpty()) {
			errorLabel.setText("%s cannot be empty".formatted(setting));
		} else {
			errorLabel.setText(" ");
		}
	}

}
