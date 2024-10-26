import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DialogueHelper {

    public static void showEntryDialog_old(String title, String existingText, int index) {
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setSize(600, 400);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null); // Center the dialog

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding of 10

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10); // Spacing between components

        int row = 0;

        // 3. Action field: multiline text area
        JLabel actionLabel = new JLabel("Activity:");
        actionLabel.setHorizontalAlignment(JLabel.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(actionLabel, gbc);

        JTextArea actionTextArea = new JTextArea(2, 20);
        actionTextArea.setText(existingText);
        addPlaceholderText_old(actionTextArea, "Describe the activity"); // 9. Placeholder text

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(actionTextArea), gbc);

        row++;

        // 4. Time fields
        String[] labels = {"Start Time:", "End Time:", "Break Time:"};
        JTextField[] timeFields = new JTextField[3];
        String[] placeholders = {"HH:MM", "HH:MM", "HH:MM"};

        for (int i = 0; i < labels.length; i++) {
            JLabel timeLabel = new JLabel(labels[i]);
            timeLabel.setHorizontalAlignment(JLabel.RIGHT);
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(timeLabel, gbc);

            JTextField timeField = new JTextField(20);
            addPlaceholderText_old(timeField, placeholders[i]); // 9. Placeholder text
            timeFields[i] = timeField;

            gbc.gridx = 1;
            gbc.gridy = row;
            gbc.weightx = 1;
            panel.add(timeField, gbc);

            row++;
        }

        // 5. Vacation checkbox
        JLabel vacationLabel = new JLabel("Vacation:");
        vacationLabel.setHorizontalAlignment(JLabel.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(vacationLabel, gbc);

        JCheckBox vacationCheckBox = new JCheckBox();
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        panel.add(vacationCheckBox, gbc);

        row++;

        // 8. Buttons at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton makeEntryButton = new JButton("Make Entry");
        JButton discardButton = new JButton("Discard");
        buttonPanel.add(makeEntryButton);
        buttonPanel.add(discardButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(buttonPanel, gbc);

        // Action listeners for buttons
        makeEntryButton.addActionListener(e -> {
            // Handle make entry logic here
            dialog.dispose();
        });

        discardButton.addActionListener(e -> {
            dialog.dispose();
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // Helper method to add placeholder text
    public static void addPlaceholderText_old(JTextComponent component, String placeholder) {
        component.setForeground(Color.GRAY);
        component.setText(placeholder);

        component.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (component.getText().equals(placeholder)) {
                    component.setText("");
                    component.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (component.getText().isEmpty()) {
                    component.setForeground(Color.GRAY);
                    component.setText(placeholder);
                }
            }
        });
    }

    private static final String TIME_PLACEHOLDER = "HH:MM";
    private static final String TIME_BREAK_PLACEHOLDER = "(HH:)MM";
    private static final Pattern TIME_PATTERN = Pattern.compile("^(\\d{1,2}):(\\d{2})$");
    private static final Pattern TIME_PATTERN_SMALL = Pattern.compile("^(\\d{1,2})$");
    private static final Pattern TIME_PATTERN_SEMI_SMALL = Pattern.compile("^(\\d{1,2}):(\\d)$");

    public static void showEntryDialog(String title, String existingText) {
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setSize(600, 400);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null); // Center the dialog

        // Labels for later
        JLabel taskSummaryValue = new JLabel("");
        JLabel durationWarningLabel = new JLabel(" ");
        JLabel durationSummaryValue = new JLabel("");

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding of 10

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10); // Spacing between components

        int row = 0;

        // Activity field: multiline text area
        JLabel actionLabel = new JLabel("Activity:");
        actionLabel.setHorizontalAlignment(JLabel.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(actionLabel, gbc);

        JTextArea actionTextArea = new JTextArea(2, 20);
        addPlaceholderText(actionTextArea, "Describe the activity", existingText.trim().isEmpty());
        if (!existingText.trim().isEmpty()) {
            actionTextArea.setText(existingText);
            taskSummaryValue.setText(existingText);
        }

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(actionTextArea), gbc);

        row++;

        // 4. Time fields
        String[] labels = {"Start Time:", "End Time:", "Break Time:"};
        JTextField[] timeFields = new JTextField[3];
        JLabel[] errorLabels = new JLabel[3]; // For validation error messages
        String[] placeholders = {TIME_PLACEHOLDER, TIME_PLACEHOLDER, TIME_BREAK_PLACEHOLDER}; // 2. Changed placeholder

        for (int i = 0; i < labels.length; i++) {
            JLabel timeLabel = new JLabel(labels[i]);
            timeLabel.setHorizontalAlignment(JLabel.RIGHT);
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(timeLabel, gbc);

            JTextField timeField = new JTextField(5); // 4. Reduced width
            addPlaceholderText(timeField, placeholders[i], true);
            timeFields[i] = timeField;

            gbc.gridx = 1;
            gbc.gridy = row;
            gbc.weightx = 1;
            panel.add(timeField, gbc);

            // 5. Error label for invalid time
            JLabel errorLabel = new JLabel(" ");
            errorLabel.setForeground(Color.RED);
            errorLabels[i] = errorLabel;
            gbc.gridx = 2;
            gbc.gridy = row;
            gbc.weightx = 0;
            panel.add(errorLabel, gbc);

            final int index = i;

            timeField.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    validateTimeField(timeFields[index], errorLabels[index], index == labels.length - 1);
                    checkStartEndTime(timeFields[0], timeFields[1]);
                    updateDurationSummary(durationSummaryValue, timeFields[0], timeFields[1], timeFields[2], durationWarningLabel);
                    //checkBreakDuration(timeFields[0], timeFields[1], timeFields[2], durationWarningLabel);
                }
            });

            row++;
        }

        // 5. Vacation checkbox
        JLabel vacationLabel = new JLabel("Vacation:");
        vacationLabel.setHorizontalAlignment(JLabel.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(vacationLabel, gbc);

        JCheckBox vacationCheckBox = new JCheckBox();
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        panel.add(vacationCheckBox, gbc);

        row++;

        // 3. Summary labels
        JLabel taskSummaryLabel = new JLabel("Task:");
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(taskSummaryLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        panel.add(taskSummaryValue, gbc);

        row++;

        JLabel durationSummaryLabel = new JLabel("Duration worked:");
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(durationSummaryLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        panel.add(durationSummaryValue, gbc);

        row++;

        // 7. Warning label for break time
        durationWarningLabel.setForeground(Color.RED);
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        panel.add(durationWarningLabel, gbc);

        row++;

        // 8. Buttons at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton makeEntryButton = new JButton("Make Entry");
        JButton discardButton = new JButton("Discard");
        buttonPanel.add(makeEntryButton);
        buttonPanel.add(discardButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(buttonPanel, gbc);

        // Action listeners for buttons
        makeEntryButton.addActionListener(e -> {
            // Handle make entry logic here
            dialog.dispose();
        });

        discardButton.addActionListener(e -> {
            dialog.dispose();
        });

        // Update task summary when activity text changes
        actionTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (actionTextArea.getForeground() != Color.BLACK) return;  // Not if placeholder text
                taskSummaryValue.setText(actionTextArea.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                if (actionTextArea.getForeground() != Color.BLACK) return;  // Not if placeholder text
                taskSummaryValue.setText(actionTextArea.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                if (actionTextArea.getForeground() != Color.BLACK) return;  // Not if placeholder text
                taskSummaryValue.setText(actionTextArea.getText());
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // Helper method to add placeholder text
    public static void addPlaceholderText(JTextComponent component, String placeholder, boolean setPlaceholerText) {
        if (setPlaceholerText) {
            component.setForeground(Color.GRAY);
            component.setText(placeholder);
        } else {
            component.setForeground(Color.BLACK);
        }

        component.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (component.getText().equals(placeholder)) {
                    component.setText("");
                    component.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (component.getText().isEmpty()) {
                    component.setForeground(Color.GRAY);
                    component.setText(placeholder);
                }
            }
        });
    }

    private static void validateTimeField(JTextField timeField, JLabel errorLabel, boolean isBreak) {
        String text = timeField.getText().trim();
        if (text.equals(TIME_PLACEHOLDER) || text.equals(TIME_BREAK_PLACEHOLDER)) {
            errorLabel.setText(" ");
            return;
        }

        if (TIME_PATTERN_SMALL.matcher(text).matches()) {
            // If is on break and double-digit, convert to minutes instead of the default (hours)
            // It is unlikely that we use 30-hour breaks, but more likely that it's a 30-minute break
            if (isBreak && text.length() > 1) text = "00:" + text;
            else text += ":00";
            timeField.setText(text);
        } else if (TIME_PATTERN_SEMI_SMALL.matcher(text).matches()) {
            text += "0";
            timeField.setText(text);
        }

        Matcher matcher = TIME_PATTERN.matcher(text);
        if (matcher.matches()) {
            int hours = Integer.parseInt(matcher.group(1));
            int minutes = Integer.parseInt(matcher.group(2));
            if (hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59) {
                errorLabel.setText(" ");
            } else {
                errorLabel.setText("Invalid time");
            }
        } else {
            errorLabel.setText("Invalid time");
        }
    }

    // 6. Check if start time is after end time
    private static void checkStartEndTime(JTextField startField, JTextField endField) {
        String startText = startField.getText();
        String endText = endField.getText();
        if (startText.equals("HH:MM") || endText.equals("HH:MM")) {
            return;
        }
        LocalTime startTime = parseTime(startText);
        LocalTime endTime = parseTime(endText);
        if (startTime != null && endTime != null) {
            if (startTime.isAfter(endTime)) {
                startField.setForeground(Color.RED);
                endField.setForeground(Color.RED);
            } else {
                startField.setForeground(Color.BLACK);
                endField.setForeground(Color.BLACK);
            }
        }
    }

    // 3 & 7. Update duration summary and check break time validity
    private static void updateDurationSummary(JLabel durationSummaryLabel, JTextField startField, JTextField endField, JTextField breakField, JLabel durationWarningLabel) {
        String startText = startField.getText();
        String endText = endField.getText();
        String breakText = breakField.getText();
        if (startText.equals("HH:MM") || endText.equals("HH:MM") || breakText.equals("HH:MM")) {
            durationSummaryLabel.setText("");
            durationWarningLabel.setText(" ");
            return;
        }
        LocalTime startTime = parseTime(startText);
        LocalTime endTime = parseTime(endText);
        LocalTime breakTime = parseTime(breakText);
        if (startTime != null && endTime != null && breakTime != null) {
            Duration workDuration = Duration.between(startTime, endTime).minus(Duration.ofHours(breakTime.getHour()).plusMinutes(breakTime.getMinute()));
            long hours = workDuration.toHours();
            long minutes = workDuration.toMinutes() % 60;
            durationSummaryLabel.setText(String.format("%d hours %d minutes", hours, minutes));

            // 7. Check break time requirements
            long totalMinutes = workDuration.toMinutes();
            long breakMinutes = breakTime.getHour() * 60 + breakTime.getMinute();
            if (totalMinutes >= 480 && breakMinutes < 60) {
                durationWarningLabel.setText("Break must be at least 1 hour for work of 8 hours or more");
            } else if (totalMinutes > 240 && breakMinutes < 30) {
                durationWarningLabel.setText("Break must be at least 30 minutes for work over 4 hours");
            } else {
                durationWarningLabel.setText(" ");
            }
        } else {
            durationSummaryLabel.setText("");
            durationWarningLabel.setText(" ");
        }
    }

    // Helper method to parse time strings
    private static LocalTime parseTime(String timeStr) {
        try {
            return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("H:mm"));
        } catch (DateTimeParseException e) {
            return null;
        }
    }


    private static void checkBreakDuration(JTextField timeField, JTextField timeField1, JTextField timeField2, JLabel durationWarningLabel) {

    }

}
