package net.justonedev.kit;

import net.justonedev.kit.json.Month;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDateTime;

public class MonthlySettingsBar extends JPanel {

    private static final String[] MONTHS = new String[] {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    private final JComboBox<String> monthSelector;
    private final JComboBox<String> semesterSelector;
    private final JTextField semesterTextField;
    private final JLabel semesterTextFieldLabel;
    private final JButton settingsButton;

    public MonthlySettingsBar() {
        super(new BorderLayout());
        // Top Panel with Selectors and Button
        JPanel selectorsPanel = new JPanel();

        // Month Selector
        monthSelector = new JComboBox<>(MONTHS);
        selectorsPanel.add(monthSelector);

        // SS/WS Selector
        semesterSelector = new JComboBox<>(new String[] {"SS", "WS"});
        selectorsPanel.add(semesterSelector);

        semesterTextField = new JTextField(2);
        selectorsPanel.add(semesterTextField);

        semesterTextFieldLabel = new JLabel();
        semesterTextFieldLabel.setText("/25");
        selectorsPanel.add(semesterTextFieldLabel);

        this.add(selectorsPanel, BorderLayout.WEST);

        // Right Side Button
        settingsButton = new JButton("Global Settings");
        this.add(settingsButton, BorderLayout.EAST);

        reset();

        // Add Events

        semesterSelector.addActionListener((l) -> {
            updateSemesterView();
        });

        semesterTextField.addActionListener((l) -> {
            updateSemesterView();
        });

        semesterTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateSemesterView();
            }
        });

        monthSelector.addActionListener((l) -> {
            int month = monthSelector.getSelectedIndex();
            if (month >= 3 && month <= 8) {
                semesterSelector.setSelectedIndex(0);
            } else {
                semesterSelector.setSelectedIndex(1);
            }
            updateSemesterView();
        });
    }

    public void reset() {
        setTimeFromCurrentDate();
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
        // Todo pred_transfer
        // Todo succ_transfer
        updateSemesterView();
    }

    public void fillMonth(Month month) {
        month.setYear(2000 + Integer.parseInt(semesterTextField.getText()));
        month.setMonth(monthSelector.getSelectedIndex() + 1);
    }

    private void setTimeFromCurrentDate() {
        LocalDateTime time = LocalDateTime.now();
        int month = time.getMonthValue();
        monthSelector.setSelectedIndex(month - 1);
        if (month >= 4 && month <= 9) {
            semesterSelector.setSelectedIndex(0);
        } else {
            semesterSelector.setSelectedIndex(1);
        }
        String year = String.valueOf(time.getYear());
        semesterTextField.setText(year.substring(year.length() - 2));
        updateSemesterView();
    }

    private void updateSemesterView() {

        // Catch invalid year on both occasions
        int year = 0;
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
        Main.setHasUnsavedChanges(true);
    }

}
