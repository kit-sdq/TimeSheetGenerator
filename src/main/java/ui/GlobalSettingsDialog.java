/* Licensed under MIT 2024-2025. */
package ui;

import mail.MailInformation;
import ui.json.Global;
import ui.json.JSONHandler;
import ui.json.UISettings;
import ui.json.api.Preset;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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

	private static final int TEXTBOXES_COUNT = 7;
	private static final int TEXTFIELD_INDEX_DEPARTMENT_FORMAT = 2;
	private static final int TEXTFIELD_INDEX_PDF_FORMAT = 5;
	private static final int TEXTFIELD_INDEX_MAIL_SUBJ_FORMAT = 6;

	public static void showGlobalSettingsDialog(UserInterface parentUI) {
		JDialog dialog = new JDialog();
		dialog.setTitle("Global Settings");
		dialog.setSize(690, 555);
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
		JTextField[] fields = new JTextField[TEXTBOXES_COUNT];
		JComboBox<String> workAreaSelector = new JComboBox<>();
		workAreaSelector.addItem(WORK_AREA_UB);
		workAreaSelector.addItem(WORK_AREA_GF);
		workAreaSelector.setSelectedIndex(getIndexValue(globalSettings.getWorkingArea()));

		// Checkboxes
		JCheckBox addSignatureBox = new JCheckBox();
		JCheckBox useYYYYBox = new JCheckBox();
		JCheckBox addVacationEntryBox = new JCheckBox();
		JCheckBox useGermanMonthNameBox = new JCheckBox();
		JCheckBox warnHoursMismatchBox = new JCheckBox();
		addSignatureBox.setSelected(uiSettings.isAddSignature());
		useYYYYBox.setSelected(uiSettings.isUseYYYY());
		addVacationEntryBox.setSelected(uiSettings.isAddVacationEntry());
		useGermanMonthNameBox.setSelected(uiSettings.isUseGermanMonths());
		warnHoursMismatchBox.setSelected(uiSettings.isWarnOnHoursMismatch());

		String[] labels = { "Name:", "Staff ID:", "Department:", "Working Time:", "Wage:", "PDF Name Format:", "Email Subject Format:", "Working Area:",
				"Add Signature at Bottom:", "Explicitly add Vacation Entry:", "Use 4-digit year in the day column:", "Use German months in Sheet header",
				"Warn when too few/ too many hours:" };
		String[] placeholders = { "Enter your name", "Enter your staff ID", "Enter your department", "Enter working time (HH:MM)", "Enter your wage",
				uiSettings.getExportPdfNameFormat(), uiSettings.getMailSubjectFormat() };
		String[] initialValues = { globalSettings.getName(), String.valueOf(globalSettings.getStaffId()), globalSettings.getDepartment(),
				globalSettings.getWorkingTime(), String.valueOf(globalSettings.getWage()), uiSettings.getExportPdfNameFormat(),
				uiSettings.getMailSubjectFormat() };
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
				errorLabel.setForeground(TextColors.ERROR.color());
				errorLabels[i] = errorLabel;
				panel.add(errorLabel, gbc);
			}

			if (i < TEXTBOXES_COUNT) {
				JTextField textField = new JTextField(20);
				textField.setCaretColor(TextColors.DEFAULT.color());
				DialogHelper.addPlaceholderText(textField, placeholders[i], initialValues[i]);
				textField.setCaretPosition(0);
				fields[i] = textField;
				panel.add(textField, gbc);
				final int index = i;

				// Add focus listener for validation when focus is lost
				textField.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						textField.selectAll();
					}

					@Override
					public void focusLost(FocusEvent e) {
						validateField(fields[index], errorLabels[index], index);
					}
				});
				if (i == TEXTFIELD_INDEX_PDF_FORMAT) {
					JButton helpButton = new JButton("Format Help");
					gbc.gridx = 3;
					gbc.gridy = row;
					gbc.weightx = 0;
					helpButton.addActionListener(l -> showPdfFormatHelp(dialog));
					panel.add(helpButton, gbc);
				}
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

		// Preset Switch at the bottom
		JComboBox<Preset> presetSelector = makePresetSelector();
		addPresetSelectionLogic(presetSelector, uiSettings, fields[TEXTFIELD_INDEX_PDF_FORMAT], fields[TEXTFIELD_INDEX_DEPARTMENT_FORMAT], fields[TEXTFIELD_INDEX_MAIL_SUBJ_FORMAT]);

		JPanel presetSelectorPaddingPanel = new JPanel(new BorderLayout(15, 5));
		JLabel choosePresetLabel = new JLabel("\t\tOr choose Preset:");	// sorry
		choosePresetLabel.setFont(choosePresetLabel.getFont().deriveFont(Font.BOLD));
		presetSelectorPaddingPanel.add(choosePresetLabel, BorderLayout.CENTER);
		presetSelectorPaddingPanel.add(presetSelector, BorderLayout.EAST);

		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = 5;
		gbc.anchor = GridBagConstraints.SOUTHWEST;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(presetSelectorPaddingPanel, gbc);

		gbc.anchor = GridBagConstraints.SOUTHEAST;
		panel.add(buttonPanel, gbc);

		// Action listeners for buttons
		saveButton.addActionListener(e -> {
			boolean saved = trySaveNewGlobalSettings(parentUI, globalSettings, uiSettings, fields, errorLabels, workAreaSelector, checkBoxes);
			if (saved) {
				dialog.dispose();
			} else {
				// Show error message
				JOptionPane.showMessageDialog(dialog, "Please fix the errors before saving.", "Validation Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		cancelButton.addActionListener(e -> dialog.dispose());

		JScrollPane scrollPanel = new JScrollPane(panel);
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.getVerticalScrollBar().setUnitIncrement(SCROLL_SENSITIVITY);
		dialog.add(scrollPanel);
		dialog.setVisible(true);
		DialogHelper.addEnterEscapeHotkeys(dialog, saveButton, cancelButton);
		dialog.pack();
		dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
	}

	/**
	 * Validates and saves the new settings. If validation fails, the method will
	 * not override the existing settings and instead return false. If validation
	 * succeeds, returns true.
	 * 
	 * @param parentUI         the parent user interface, used for updating the time
	 *                         UI
	 * @param globalSettings   the new global settings, containing the updated
	 *                         settings
	 * @param uiSettings       the new ui settings, containing the updated settings
	 * @param fields           the text fields for text values like name or format.
	 *                         Must have size exactly 5, otherwise will be ignored.
	 * @param errorLabels      the error labels for displaying validation errors.
	 *                         Must have size exactly 5, otherwise will be ignored.
	 * @param workAreaSelector The combo box for selecting the working area (UB /
	 *                         GF)
	 * @param checkBoxes       the checkboxes for boolean values like addSignature
	 *                         or addVacationEntry. Must have size exactly 5,
	 *                         otherwise will be ignored.
	 * @return true if settings are valid and saved successfully, false otherwise
	 */
	private static boolean trySaveNewGlobalSettings(UserInterface parentUI, Global globalSettings, UISettings uiSettings, JTextField[] fields,
			JLabel[] errorLabels, JComboBox<String> workAreaSelector, JCheckBox[] checkBoxes) {
		boolean hasError = false;
		// Validate all fields
		for (int i = 0; i < Math.min(fields.length, errorLabels.length); i++) {
			validateField(fields[i], errorLabels[i], i);
			if (!errorLabels[i].getText().isBlank()) {
				hasError = true;
			}
		}
		if (hasError)
			return false;

		// Update globalSettings object
		if (fields.length >= 5) {
			globalSettings.setName(fields[0].getText());
			globalSettings.setStaffId(Integer.parseInt(fields[1].getText()));
			globalSettings.setDepartment(fields[2].getText());
			globalSettings.setWorkingTime(fields[3].getText());
			globalSettings.setWage(Double.parseDouble(fields[4].getText()));
		}
		globalSettings.setWorkingArea(getConfigValue(workAreaSelector.getSelectedItem()));

		if (checkBoxes.length == 5) {
			uiSettings.setAddSignature(checkBoxes[0].isSelected());
			uiSettings.setAddVacationEntry(checkBoxes[1].isSelected());
			uiSettings.setUseYYYY(checkBoxes[2].isSelected());
			uiSettings.setUseGermanMonths(checkBoxes[3].isSelected());
			uiSettings.setWarnOnHoursMismatch(checkBoxes[4].isSelected());
		}
		if (fields.length > TEXTFIELD_INDEX_PDF_FORMAT)
			uiSettings.setExportPdfNameFormat(fields[TEXTFIELD_INDEX_PDF_FORMAT].getText());
		if (fields.length > TEXTFIELD_INDEX_MAIL_SUBJ_FORMAT)
			uiSettings.setMailSubjectFormat(fields[TEXTFIELD_INDEX_MAIL_SUBJ_FORMAT].getText());

		// Save globalSettings to file or database as needed
		JSONHandler.saveGlobal(globalSettings);
		JSONHandler.saveUISettings(uiSettings);

		parentUI.updateTotalTimeWorkedUI();
		return true;
	}

	private static JComboBox<Preset> makePresetSelector() {
		JComboBox<Preset> presetSelector = new JComboBox<>();
		presetSelector.addItem(Preset.NO_PRESET);
		JSONHandler.getPresets().getPresets().forEach(preset -> {
			if (preset.isVisible()) presetSelector.addItem(preset);
		});
		// Now, figure out which preset it selected by field values
		return presetSelector;
	}

	private static void addPresetSelectionLogic(
			JComboBox<Preset> presetSelector,
			UISettings uiSettings,
			JTextField fileNameTextField,
			JTextField departmentField,
			JTextField emailSubjectFormatField
	) {
		DocumentListener presetEditedListener = new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				textfieldEdited();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				textfieldEdited();
			}

			private void textfieldEdited() {
				presetSelector.setSelectedIndex(0);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		};
		fileNameTextField.getDocument().addDocumentListener(presetEditedListener);
		departmentField.getDocument().addDocumentListener(presetEditedListener);
		emailSubjectFormatField.getDocument().addDocumentListener(presetEditedListener);

		presetSelector.addActionListener(e -> {
			final int selectedIndex = presetSelector.getSelectedIndex();
			Preset selected = presetSelector.getItemAt(selectedIndex);
			if (selected.equals(Preset.NO_PRESET)) return;
			// Update values according to the preset:
			fileNameTextField.setText(selected.getFileFormat());
			departmentField.setText(selected.getDepartment());
			emailSubjectFormatField.setText(selected.getMailSubject());
			uiSettings.setMailInformation(new MailInformation(selected.getMailRecipient(), selected.getMailRecipientsCC()), false);
			// Update colors just in case:
			fileNameTextField.setForeground(TextColors.DEFAULT.color());
			departmentField.setForeground(TextColors.DEFAULT.color());
			emailSubjectFormatField.setForeground(TextColors.DEFAULT.color());
			// Set selected index as it was overwritten by the document listener:
			presetSelector.setSelectedIndex(selectedIndex);
		});

		for (int i = 1; i < presetSelector.getItemCount(); i++) {
			Preset preset = presetSelector.getItemAt(i);
			// Compare and see if we currently have this preset:
			if (!fileNameTextField.getText().equals(preset.getFileFormat())) continue;
			if (!departmentField.getText().equals(preset.getDepartment())) continue;
			if (!emailSubjectFormatField.getText().equals(preset.getMailSubject())) continue;
			if (!uiSettings.getMailRecipient().equals(preset.getMailRecipient())) continue;
			if (!uiSettings.getMailRecipientsCC().equals(preset.getMailRecipientsCC())) continue;
			presetSelector.setSelectedIndex(i);
			break;
		}
	}

	private static void showPdfFormatHelp(JDialog parentDialog) {
		String message = """
				You can use the following placeholders in TimesheetGenerator formats:

				- %FIRST%: First- and middle names, separated by space
				- %FIRST_U%: First- and middle names, separated by underscores
				- %LAST%: Lastname
				- %MM%: Month number from 01-12
				- %MM_GER%: German Month Name
				- %MM_ENG%: English Month Name
				- %YY%: Year (2 digits)
				- %YYYY%: Year (4 digits)
				""";
		JOptionPane.showMessageDialog(parentDialog, message, "PDF Name Format Help", JOptionPane.INFORMATION_MESSAGE);
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
		if (textField.getForeground().equals(TextColors.DEFAULT.color())) {
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
