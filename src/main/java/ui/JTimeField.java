/* Licensed under MIT 2024-2025. */
package ui;

import javax.swing.*;
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

	private void validateField() {
		String text = this.getText().trim().replace('.', ':');

		if (DialogHelper.TIME_PATTERN_SMALL.matcher(text).matches()) {
			text += ":00";
		} else if (DialogHelper.TIME_PATTERN_SEMI_SMALL.matcher(text).matches()) {
			text += "0";
		}
		if (TIME_PATTERN_SEMI_SMALL_2.matcher(text).matches()) {
			text = "0" + text;
		}
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
