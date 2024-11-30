/* Licensed under MIT 2024. */
package ui;

import ui.export.FileExporter;
import ui.json.JSONHandler;

import javax.swing.*;
import java.awt.*;

public class ActionBar extends JPanel {

	private static final String HOURS_FORMAT = "Total Time: %s/%s          ";

	private transient final UserInterface parentUI;

	private final JLabel hoursWorkedLabel;
	private final Font fontNormal;
	private final Font fontBold;

	public ActionBar(UserInterface parentUI) {
		this.parentUI = parentUI;
		this.setPreferredSize(new Dimension(this.parentUI.getWidth(), 70));
		this.setLayout(new BorderLayout());

		JPanel buttonPanel = new JPanel();

		JButton addButton = new JButton("+");
		addButton.setPreferredSize(new Dimension(50, 50));
		buttonPanel.add(addButton);

		JButton duplicateButton = new JButton("Duplicate");
		duplicateButton.setPreferredSize(new Dimension(90, 50));
		buttonPanel.add(duplicateButton);

		JButton editButton = new JButton("Edit");
		editButton.setPreferredSize(new Dimension(60, 50));
		buttonPanel.add(editButton);

		JButton removeButton = new JButton("Remove");
		removeButton.setPreferredSize(new Dimension(90, 50));
		buttonPanel.add(removeButton);

		JButton compileButton = new JButton("Compile to Tex");
		compileButton.setPreferredSize(new Dimension(110, 50));
		buttonPanel.add(compileButton);

		JButton printButton = new JButton("Print to PDF");
		printButton.setPreferredSize(new Dimension(95, 50));
		buttonPanel.add(printButton);

		this.add(buttonPanel, BorderLayout.WEST);

		addButton.addActionListener(e -> {
			if (this.parentUI.isSpaceForNewEntry()) {
				DialogHelper.showEntryDialog(this.parentUI, "Add Entry");
			} else {
				this.parentUI.showError("Entry limit reached", "You have reached the maximum of %d entries".formatted(UserInterface.MAX_ENTRIES));
			}
		});
		duplicateButton.addActionListener((l) -> this.parentUI.duplicateSelectedListEntry());
		removeButton.addActionListener((l) -> this.parentUI.removeSelectedListEntry());
		editButton.addActionListener((l) -> this.parentUI.editSelectedListEntry());

		compileButton.addActionListener((l) -> FileExporter.printTex(this.parentUI));
		printButton.addActionListener((l) -> FileExporter.printPDF(this.parentUI));

		hoursWorkedLabel = new JLabel();
		fontNormal = hoursWorkedLabel.getFont().deriveFont(18f);
		fontBold = fontNormal.deriveFont(Font.BOLD);
		hoursWorkedLabel.setFont(fontNormal);
		this.add(hoursWorkedLabel, BorderLayout.EAST);
		updateHours(new Time());
	}

	/**
	 * Returns overflowing hours to be entered in the succ hours label.
	 * 
	 * @param workedHours The hours worked, in sum.
	 * @return Overflowing hours.
	 */
	public Time updateHours(Time workedHours) {
		String totalHoursStr = JSONHandler.getGlobalSettings().getWorkingTime();
		Time totalHours = Time.parseTime(totalHoursStr);

		workedHours.addTime(this.parentUI.getPredTime());

		Time displayedWorkedHours;
		Time successorHours;

		if (workedHours.isLongerThan(totalHours)) {
			displayedWorkedHours = totalHours;
			successorHours = new Time(workedHours);
			successorHours.subtractTime(totalHours);
			hoursWorkedLabel.setFont(fontBold);
		} else {
			displayedWorkedHours = workedHours;
			successorHours = new Time(0, 0);

			// Same Length
			if (!totalHours.isLongerThan(workedHours))
				hoursWorkedLabel.setFont(fontBold);
			else
				hoursWorkedLabel.setFont(fontNormal);
		}

		hoursWorkedLabel.setText(HOURS_FORMAT.formatted(displayedWorkedHours, totalHours));
		return successorHours;
	}

}
