/* Licensed under MIT 2024. */
package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.regex.Pattern;

public class JTimeField extends JTextField {

	private static final String PLACEHOLDER = "00:00";
	static final Pattern TIME_PATTERN_SEMI_SMALL_2 = Pattern.compile("^(\\d):(\\d{2})$");

	public JTimeField(UserInterface parentUi) {
		this(parentUi, null);
	}

	public JTimeField(UserInterface parentUi, String text) {
		super(4);
		this.setHorizontalAlignment(CENTER);

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

	private void validateField() {
		String text = this.getText();

		if (DialogHelper.TIME_PATTERN_SMALL.matcher(text).matches()) {
			text += ":00";
			super.setText(text);
		} else if (DialogHelper.TIME_PATTERN_SEMI_SMALL.matcher(text).matches()) {
			text += "0";
			super.setText(text);
		}
		if (TIME_PATTERN_SEMI_SMALL_2.matcher(text).matches()) {
			text = "0" + text;
			super.setText(text);
		}

		if (!text.isBlank() && !DialogHelper.TIME_PATTERN.matcher(text).matches()) {
			setForeground(Color.RED);
		} else {
			setForeground(getText().equals(PLACEHOLDER) ? Color.GRAY : Color.BLACK);
		}
	}

	@Override
	public boolean isValid() {
		return getForeground() != Color.RED;
	}

	@Override
	public void setText(String text) {
		super.setText(text);
		validateField();
	}

}
