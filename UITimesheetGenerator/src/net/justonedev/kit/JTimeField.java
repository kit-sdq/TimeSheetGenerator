package net.justonedev.kit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import static net.justonedev.kit.DialogHelper.TIME_PATTERN_SEMI_SMALL;
import static net.justonedev.kit.DialogHelper.TIME_PATTERN_SMALL;

public class JTimeField extends JTextField {

    private static final String PLACEHOLDER = "00:00";

    public JTimeField() {
        this(null);
    }
    public JTimeField(String text) {
        super(4);
        this.setHorizontalAlignment(CENTER);

        DialogHelper.addPlaceholderText(this, PLACEHOLDER, text);

        this.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) return;
                validateField();
                Main.updateTotalTimeWorkedUI();
            }
        });
    }

    private void validateField() {
        String text = this.getText();

        if (TIME_PATTERN_SMALL.matcher(text).matches()) {
            text += ":00";
            super.setText(text);
        } else if (TIME_PATTERN_SEMI_SMALL.matcher(text).matches()) {
            text += "0";
            super.setText(text);
        }

        if (!text.isBlank() && !DialogHelper.TIME_PATTERN.matcher(text).matches()) {
            setForeground(Color.RED);
        } else {
            setForeground(getText().equals(PLACEHOLDER) ? Color.GRAY : Color.BLACK);
        }
    }

    public boolean isValid() {
        return getForeground() != Color.RED;
    }

    public void setText(String text) {
        super.setText(text);
        validateField();
    }

    /*
    private JTextField textField = new JTextField();
    private JLabel errorLabel = new JLabel();

    public JTimeField() {
        this(null);
    }
    public JTimeField(String text) {
        setLayout(new BorderLayout());  // Explicitly set layout to FlowLayout
        textField = new JTextField(5);
        errorLabel = new JLabel();

        errorLabel.setText("");
        errorLabel.setForeground(Color.RED);

        DialogHelper.addPlaceholderText(textField, "00:00", text);

        this.add(textField);
        this.add(errorLabel);

        textField.setVisible(true);
        errorLabel.setVisible(true);

        textField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) return;
                validateField();
            }
        });
    }

    private void validateField() {
        String text = textField.getText();
        if (!text.isBlank() && !DialogHelper.TIME_PATTERN.matcher(text).matches()) {
            errorLabel.setText("You must enter a valid time");
        } else {
            errorLabel.setText("");
        }
    }

    public boolean isValid() {
        return errorLabel == null || errorLabel.getText().isEmpty();
    }

    public String getText() {
        // Will return placeholder 00:00 if "empty"
        return textField == null ? "" : textField.getText();
    }

    public void setText(String text) {
        if (textField == null) return;
        textField.setText(text);
        validateField();
    }
     */

}
