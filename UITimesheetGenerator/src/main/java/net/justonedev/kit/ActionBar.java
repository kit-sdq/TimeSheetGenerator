/*
 * Licensed under the MIT License (MIT)
 * Copyright (c) 2024 Benjamin Claus
 */
package net.justonedev.kit;

import net.justonedev.kit.export.FileExporter;
import net.justonedev.kit.json.JSONHandler;

import javax.swing.*;
import java.awt.*;

public class ActionBar extends JPanel {

    private final JButton addButton;
    private final JButton duplicateButton;
    private final JButton editButton;
    private final JButton removeButton;
    private final JButton compileButton;
    private final JButton printButton;

    private final JLabel hoursWorkedLabel;

    private static final String HOURS_FORMAT = "Total Time: %s/%s          ";

    private final Font fontNormal, fontBold;

    public ActionBar() {
        this.setPreferredSize(new Dimension(Main.getWidth(), 70));
        this.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();

        addButton = new JButton("+");
        addButton.setPreferredSize(new Dimension(50, 50));
        buttonPanel.add(addButton);

        duplicateButton = new JButton("Duplicate");
        duplicateButton.setPreferredSize(new Dimension(90, 50));
        buttonPanel.add(duplicateButton);

        editButton = new JButton("Edit");
        editButton.setPreferredSize(new Dimension(50, 50));
        buttonPanel.add(editButton);

        removeButton = new JButton("-");
        removeButton.setPreferredSize(new Dimension(50, 50));
        buttonPanel.add(removeButton);

        compileButton = new JButton("Compile to Tex");
        compileButton.setPreferredSize(new Dimension(110, 50));
        buttonPanel.add(compileButton);

        printButton = new JButton("Print to PDF");
        printButton.setPreferredSize(new Dimension(95, 50));
        buttonPanel.add(printButton);

        this.add(buttonPanel, BorderLayout.WEST);

        addButton.addActionListener(e -> {
            if (Main.isSpaceForNewEntry()) {
                DialogHelper.showEntryDialog("Add Entry");
            } else {
                Main.showError("You have reached the maximum of %d entries".formatted(Main.MAX_ENTRIES));
            }
        });
        duplicateButton.addActionListener((l) -> Main.duplicateSelectedListEntry());
        removeButton.addActionListener((l) -> Main.removeSelectedListEntry());
        editButton.addActionListener((l) -> Main.editSelectedListEntry());

        compileButton.addActionListener((l) -> FileExporter.printTex());
        printButton.addActionListener((l) -> FileExporter.printPDF());

        hoursWorkedLabel = new JLabel();
        fontNormal = hoursWorkedLabel.getFont().deriveFont(18f);
        fontBold = fontNormal.deriveFont(Font.BOLD);
        hoursWorkedLabel.setFont(fontNormal);
        this.add(hoursWorkedLabel, BorderLayout.EAST);
        updateHours(new Time());   // Todo
    }

    /**
     * Returns overflowing hours to be entered in the succ hours label.
     * @param workedHours The hours worked, in sum.
     * @return Overflowing hours.
     */
    public Time updateHours(Time workedHours) {
        String totalHoursStr = JSONHandler.globalSettings.getWorkingTime();
        Time totalHours = Time.parseTime(totalHoursStr);

        workedHours.addTime(Main.getPredTime());

        Time displayedWorkedHours, succHours;
        if (workedHours.isLongerThan(totalHours)) {
            displayedWorkedHours = totalHours;
            succHours = new Time(workedHours);
            succHours.subtractTime(totalHours);
            hoursWorkedLabel.setFont(fontBold);
        } else {
            displayedWorkedHours = workedHours;
            succHours = new Time(0, 0);
            
            // Same Length
            if (!totalHours.isLongerThan(workedHours)) hoursWorkedLabel.setFont(fontBold);
            else hoursWorkedLabel.setFont(fontNormal);
        }

        hoursWorkedLabel.setText(HOURS_FORMAT.formatted(displayedWorkedHours, totalHours));
        return succHours;
    }

}
