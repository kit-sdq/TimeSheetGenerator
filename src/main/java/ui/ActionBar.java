/* Licensed under MIT 2024. */
package ui;

import ui.export.FileExporter;
import ui.json.JSONHandler;

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
			if (this.parentUi.isSpaceForNewEntry()) {
				DialogHelper.showEntryDialog(this.parentUi, "Add Entry");
			} else {
				ErrorHandler.showError("Entry limit reached", "You have reached the maximum of %d entries".formatted(UserInterface.MAX_ENTRIES));
			}
		});
		duplicateButton.addActionListener(l -> this.parentUi.duplicateSelectedListEntry());
		removeButton.addActionListener(l -> this.parentUi.removeSelectedListEntry());
		editButton.addActionListener(l -> this.parentUi.editSelectedListEntry());

		compileButton.addActionListener(l -> FileExporter.printTex(this.parentUi));
		printButton.addActionListener(l -> FileExporter.printPDF(this.parentUi));

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

		workedHours.addTime(this.parentUi.getPredTime());

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
