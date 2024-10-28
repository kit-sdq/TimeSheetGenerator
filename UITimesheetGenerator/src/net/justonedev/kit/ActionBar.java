package net.justonedev.kit;

import net.justonedev.kit.json.JSONHandler;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

public class ActionBar extends JPanel {

    private final JButton addButton;
    private final JButton duplicateButton;
    private final JButton editButton;
    private final JButton removeButton;
    private final JButton printButton;

    private final JLabel hoursWorkedLabel;

    private static final String HOURS_FORMAT = "Total Time: %s/%s          ";

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

        printButton = new JButton("Print");
        printButton.setPreferredSize(new Dimension(70, 50));
        buttonPanel.add(printButton);

        this.add(buttonPanel, BorderLayout.WEST);

        addButton.addActionListener(e -> DialogHelper.showEntryDialog("Add Entry"));
        duplicateButton.addActionListener((l) -> Main.duplicateSelectedListEntry());
        removeButton.addActionListener((l) -> Main.removeSelectedListEntry());
        editButton.addActionListener((l) -> Main.editSelectedListEntry());

        hoursWorkedLabel = new JLabel();
        hoursWorkedLabel.setFont(hoursWorkedLabel.getFont().deriveFont(18f));
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
        } else {
            displayedWorkedHours = workedHours;
            succHours = new Time(0, 0);
        }

        hoursWorkedLabel.setText(HOURS_FORMAT.formatted(displayedWorkedHours, totalHours));
        return succHours;
    }

}
