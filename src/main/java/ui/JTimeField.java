/* Licensed under MIT 2024-2025. */
package ui;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.regex.Pattern;

public class JTimeField extends JTextField {

    private static final String PLACEHOLDER = "00:00";
    static final Pattern TIME_PATTERN_SEMI_SMALL_2 = Pattern.compile("^(\\d):(\\d{2})$");
    static final Pattern TIME_PATTERN_SEMI_SMALLER = Pattern.compile("^(\\d){1,2}:$");

    public JTimeField(UserInterface parentUi) {
        this(parentUi, null);
    }

    public JTimeField(UserInterface parentUi, String text) {
        super(4);
        this.setHorizontalAlignment(CENTER);
        this.setForeground(TextColors.DEFAULT.color());

        DialogHelper.addPlaceholderText(this, PLACEHOLDER, text);

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty())
                    return;
                validateField();
                parentUi.updateTotalTimeWorkedUI();
            }
        });
    }

    /**
     * Formats an improper time string to the standard format HH:MM.<br/>
     * Performs the following transformations:
     * <ul>
     *     <li>19.00 -> 19:00</li>
     *     <li>19 -> 19:00</li>
     *     <li>19: -> 19:00</li>
     *     <li>19:0 -> 19:00</li>
     *     <li>9 -> 19:00</li>
     * </ul>
     * @param time The (improper) time as string.
     * @return The time as string, properly formatted as HH:MM.
     */
    public static String formatTimeString(String time) {
        time = time.trim().replace('.', ':');
        if (DialogHelper.TIME_PATTERN_SMALL.matcher(time).matches()) {
            time += ":00";
        } else if (DialogHelper.TIME_PATTERN_SEMI_SMALL.matcher(time).matches()) {
            time += "0";
        } else if (TIME_PATTERN_SEMI_SMALLER.matcher(time).matches()) {
            time += "00";
        }
        if (TIME_PATTERN_SEMI_SMALL_2.matcher(time).matches()) {
            time = "0" + time;
        }
        return time;
    }

    private void validateField() {
        String text = formatTimeString(this.getText());
        super.setText(text);

        if (!text.isBlank() && !DialogHelper.TIME_PATTERN.matcher(text).matches()) {
            setForeground(TextColors.ERROR.color());
        } else {
            setForeground(getText().equals(PLACEHOLDER) ? TextColors.PLACEHOLDER.color() : TextColors.DEFAULT.color());
        }
    }

    public void clear() {
        // Prevent auto-focus on clear
        super.setFocusable(false);
        super.setText(PLACEHOLDER);
        setForeground(TextColors.PLACEHOLDER.color());
        super.setFocusable(true);
    }

    @Override
    public boolean isValid() {
        return getForeground() != TextColors.ERROR.color();
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        validateField();
    }

}
