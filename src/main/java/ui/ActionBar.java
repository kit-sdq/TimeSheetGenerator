/* Licensed under MIT 2024-2025. */
package ui;

import ui.export.FileExporter;
import ui.json.JSONHandler;
import ui.json.UISettings;

import javax.swing.*;
import java.awt.*;

public class ActionBar extends JPanel {

	private static final String HOURS_FORMAT = "Total Time: %s/%s          ";

	private final transient UserInterface parentUi;

	private final JLabel hoursWorkedLabel;
	private final Font fontNormal;
	private final Font fontBold;

	public ActionBar(UserInterface parentUi) {
		this.parentUi = parentUi;
		this.setPreferredSize(new Dimension(this.parentUi.getWidth(), 70));
		this.setLayout(new BorderLayout());

		JPanel buttonPanel = new JPanel();

		JButton addButton = new JButton("+");
		addButton.setPreferredSize(new Dimension(50, 50));
		buttonPanel.add(addButton);

		JButton duplicateButton = new JButton("Duplicate");
		duplicateButton.setPreferredSize(new Dimension(115, 50));
		buttonPanel.add(duplicateButton);

		JButton editButton = new JButton("Edit");
		editButton.setPreferredSize(new Dimension(75, 50));
		buttonPanel.add(editButton);

		JButton removeButton = new JButton("Remove");
		removeButton.setPreferredSize(new Dimension(110, 50));
		buttonPanel.add(removeButton);

		JButton compileButton = new JButton("Compile to Tex");
		compileButton.setPreferredSize(new Dimension(140, 50));
		buttonPanel.add(compileButton);

		JButton printButton = new JButton("Print to PDF");
		printButton.setPreferredSize(new Dimension(135, 50));
		buttonPanel.add(printButton);

		JButton createEmailButton = new JButton("Draft E-Mail");
		createEmailButton.setPreferredSize(new Dimension(125, 50));
		buttonPanel.add(createEmailButton);

		this.add(buttonPanel, BorderLayout.WEST);

		addButton.addActionListener(e -> addEntryButtonClicked());
		duplicateButton.addActionListener(l -> this.parentUi.duplicateSelectedListEntry());
		removeButton.addActionListener(l -> this.parentUi.removeSelectedListEntry());
		editButton.addActionListener(l -> this.parentUi.editSelectedListEntry());

		compileButton.addActionListener(l -> compileTexButtonClicked());
		printButton.addActionListener(l -> exportPdfButtonClicked());
		createEmailButton.addActionListener(l -> this.parentUi.createEmailClicked());

		hoursWorkedLabel = new JLabel();
		fontNormal = hoursWorkedLabel.getFont().deriveFont(18f);
		fontBold = fontNormal.deriveFont(Font.BOLD);
		hoursWorkedLabel.setFont(fontNormal);
		this.add(hoursWorkedLabel, BorderLayout.EAST);
		updateHours(new Time());
	}

	private boolean hourMismatchCheck() {
		return (JSONHandler.getUISettings().isWarnOnHoursMismatch() && parentUi.hasWorkedHoursMismatch()
				&& !parentUi.showOKCancelDialog("Hours mismatch", "Warning: The worked hours do not match the target working hours. Do you want to continue?"));
	}

	/**
	 * This is the method that is called when the tex button is clicked. First, it
	 * checks for a mismatch in worked time, then it prompts the user to save the
	 * file as LaTeX. <br/>
	 * Exposed functionality because of hotkeys.
	 */
	public void compileTexButtonClicked() {
		if (hourMismatchCheck()) {
			return;
		}
		FileExporter.printTex(this.parentUi);
	}

	/**
	 * This is the method that is called when the print button is clicked. First, it
	 * checks for a mismatch in worked time, then it prompts the user to save the
	 * file as a pdf. <br/>
	 * Exposed functionality because of hotkeys.
	 */
	public void exportPdfButtonClicked() {
		if (hourMismatchCheck()) {
			return;
		}
		FileExporter.printPDF(this.parentUi);
	}

	/**
	 * This is the method that is called when the add entry button is clicked.
	 * Simply calls upon the current {@link UserInterface} to add a new entry, after
	 * performing a check to see if there is space and showing an appropriate dialog
	 * if not. <br/>
	 * Exposed functionality because of hotkeys.
	 */
	public void addEntryButtonClicked() {
		if (this.parentUi.isSpaceForNewEntry()) {
			DialogHelper.showEntryDialog(this.parentUi, "Add Entry");
		} else {
			ErrorHandler.showError("Entry limit reached", "You have reached the maximum of %d entries".formatted(UserInterface.MAX_ENTRIES));
		}
	}

	/**
	 * Returns overflowing hours to be entered in the succ hours label.
	 * 
	 * @param workedHours The hours worked, in sum.
	 * @return Overflowing hours.
	 */
	public Time updateHours(Time workedHours) {
		UISettings uiSettings = JSONHandler.getUISettings();
		String totalHoursStr = JSONHandler.getGlobalSettings().getWorkingTime();
		Time totalHours = Time.parseTime(totalHoursStr);

		workedHours.addTime(this.parentUi.getPredTime());

		String displayedWorkedHours;
		Time successorHours;

		if (workedHours.isLongerThan(totalHours)) {
			displayedWorkedHours = totalHours + "+";
			successorHours = new Time(workedHours);
			successorHours.subtractTime(totalHours);
			hoursWorkedLabel.setFont(fontBold);

			if (uiSettings.isWarnOnHoursMismatch()) {
				hoursWorkedLabel.setForeground(TextColors.ERROR.color());
			}
		} else {
			displayedWorkedHours = workedHours.toString();
			successorHours = new Time(0, 0);
			hoursWorkedLabel.setForeground(TextColors.DEFAULT.color());

			// Same Length
			if (!totalHours.isLongerThan(workedHours))
				hoursWorkedLabel.setFont(fontBold);
			else
				hoursWorkedLabel.setFont(fontNormal);
		}

		hoursWorkedLabel.setText(HOURS_FORMAT.formatted(displayedWorkedHours, totalHours));
		return successorHours;
	}

	public void reset() {
		updateHours(new Time());
	}

}
