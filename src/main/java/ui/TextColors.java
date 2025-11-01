/* Licensed under MIT 2025. */
package ui;

import javax.swing.*;
import java.awt.*;

public enum TextColors {
	DEFAULT(UIManager.getColor("Label.foreground")), HOVER(new Color(160, 160, 160)), PLACEHOLDER(Color.GRAY), ERROR(Color.RED);

	private final Color color;

	TextColors(Color color) {
		this.color = color;
	}

	public Color color() {
		return color;
	}
}
