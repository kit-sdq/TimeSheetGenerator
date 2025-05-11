/* Licensed under MIT 2024-2025. */
package ui;

import ui.json.JSONHandler;
import ui.json.Month;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;

public class MonthlySettingsBar extends JPanel {

	private static final String[] MONTHS = new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
			"November", "December" };

	private static final int YEAR_OFFSET = 2000;

	private final transient UserInterface parentUi;

	private final JComboBox<String> monthSelector;
	private final JComboBox<String> semesterSelector;
	private final JTextField semesterTextField;
	private final JLabel semesterTextFieldLabel;
	private final JTimeField predTimeField;
	private final JLabel succTimeLabel;
	private final JLabel succTimeValue;

	private final Font FONT_NORMAL;
	private final Font FONT_BOLD;

	public MonthlySettingsBar(UserInterface parentUi) {
		super(new BorderLayout());
		this.parentUi = parentUi;
		// Top Panel with Selectors and Button
		JPanel selectorsPanel = new JPanel();

		// Month Selector
		monthSelector = new JComboBox<>(MONTHS);
		selectorsPanel.add(monthSelector);

		// SS/WS Selector
		semesterSelector = new JComboBox<>(new String[] { "SS", "WS" });
		selectorsPanel.add(semesterSelector);

		semesterTextField = new JTextField(2);
		selectorsPanel.add(semesterTextField);

		semesterTextFieldLabel = new JLabel();
		semesterTextFieldLabel.setText("/25");
		selectorsPanel.add(semesterTextFieldLabel);

		this.add(selectorsPanel, BorderLayout.WEST);

		JPanel timeCarryPanel = new JPanel();
		timeCarryPanel.setLayout(new FlowLayout());

		// Pred. Time
		JLabel predTimeLabel = new JLabel();
		predTimeLabel.setText("Predecessor Time:");
		timeCarryPanel.add(predTimeLabel);

		predTimeField = new JTimeField(this.parentUi);
		predTimeField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if ((e.getKeyChar() < '0' || e.getKeyChar() > '9') && e.getKeyChar() != ':')
					return;
				parentUi.setHasUnsavedChanges(true);
			}
		});
		timeCarryPanel.add(predTimeField);

		// Pred. Time
		succTimeLabel = new JLabel();
		succTimeLabel.setText("    Successor Time: ");
		timeCarryPanel.add(succTimeLabel);

		// Succ. Time
		succTimeValue = new JLabel();
		succTimeValue.setText("00:00");
		timeCarryPanel.add(succTimeValue);

		FONT_NORMAL = succTimeValue.getFont();
		FONT_BOLD = FONT_NORMAL.deriveFont(Font.BOLD);

		this.add(timeCarryPanel, BorderLayout.CENTER);

		// Right Side Button
		JButton settingsButton = new JButton("Global Settings");
		this.add(settingsButton, BorderLayout.EAST);

		reset();

		// Add Events

		semesterSelector.addActionListener(l -> updateSemesterView());

		semesterTextField.addActionListener(l -> updateSemesterView());

		semesterTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updateSemesterView();
			}
		});

		monthSelector.addActionListener(l -> {
			int month = monthSelector.getSelectedIndex();
			if (month >= 3 && month <= 8) {
				semesterSelector.setSelectedIndex(0);
			} else {
				semesterSelector.setSelectedIndex(1);
			}
			updateSemesterView();
		});

		settingsButton.addActionListener(l -> GlobalSettingsDialog.showGlobalSettingsDialog(parentUi));
	}

	public String getSelectedMonthName() {
		int index = monthSelector.getSelectedIndex();
		if (index < 0 || index >= monthSelector.getItemCount())
			return "";
		return monthSelector.getItemAt(index);
	}

	public int getSelectedMonthNumber() {
		return monthSelector.getSelectedIndex() + 1;
	}

	private int getFullYear() {
		return getYear() + YEAR_OFFSET;
	}

	private int getYear() {
		int year;
		try {
			year = Integer.parseInt(semesterTextField.getText());
		} catch (NumberFormatException e) {
			year = LocalDateTime.now().getYear() % 2000;
		}
		if (getSelectedMonthNumber() < 4) {
			// Winter semester, new year
			year++;
		}
		return year;
	}

	public String getYearStr() {
		return String.valueOf(getYear());
	}

	public Time getPredTime() {
		return predTimeField.isValid() ? Time.parseTime(predTimeField.getText()) : new Time(0, 0);
	}

	public void setSuccTime(Time time) {
		succTimeValue.setText(time.toString());
		if (JSONHandler.getUISettings().isWarnOnHoursMismatch()) {
			if (time.isNotZero()) {
				succTimeValue.setForeground(Color.RED);
				succTimeValue.setFont(FONT_BOLD);
				succTimeLabel.setForeground(Color.RED);
				succTimeLabel.setFont(FONT_BOLD);
			} else {
				succTimeValue.setForeground(Color.BLACK);
				succTimeValue.setFont(FONT_NORMAL);
				succTimeLabel.setForeground(Color.BLACK);
				succTimeLabel.setFont(FONT_NORMAL);
			}
		}
	}

	public void reset() {
		setTimeFromCurrentDate();
		setSuccTime(new Time());
		predTimeField.clear();
	}

	public void importMonthSettings(Month month) {
		String yearString = String.valueOf(month.getYear());
		semesterTextField.setText(yearString.substring(yearString.length() - 2));
		monthSelector.setSelectedIndex(month.getMonth() - 1);
		if (month.getMonth() >= 4 && month.getMonth() <= 9) {
			semesterSelector.setSelectedIndex(0);
		} else {
			semesterSelector.setSelectedIndex(1);
		}
		predTimeField.setText(month.getPredTransfer());
		// Succ Transfer is Calculated
		updateSemesterView();
	}

	public void fillMonth(Month month) {
		month.setYear(getFullYear());
		month.setMonth(monthSelector.getSelectedIndex() + 1);
		month.setPredTransfer(predTimeField.getText());
		month.setSuccTransfer(succTimeValue.getText());
	}

	private void setTimeFromCurrentDate() {
		LocalDateTime time = LocalDateTime.now();
		int month = time.getMonthValue();
		monthSelector.setSelectedIndex(month - 1);
		int year = time.getYear();
		if (month >= 4 && month <= 9) {
			semesterSelector.setSelectedIndex(0);
		} else {
			semesterSelector.setSelectedIndex(1);
			if (month < 4)
				year--;
		}
		String yearStr = String.valueOf(year);
		semesterTextField.setText(yearStr.substring(yearStr.length() - 2));
		updateSemesterView();
	}

	private void updateSemesterView() {

		// Catch invalid year on both occasions
		int year;
		try {
			year = Integer.parseInt(semesterTextField.getText().trim());
		} catch (NumberFormatException ignored) {
			semesterTextFieldLabel.setText("Invalid year");
			return;
		}

		if (semesterSelector.getSelectedIndex() == 0) {
			semesterTextFieldLabel.setText("");
		} else {
			semesterTextFieldLabel.setText("/%d".formatted(year + 1));
		}
		this.parentUi.setHasUnsavedChanges(true);
	}

}
