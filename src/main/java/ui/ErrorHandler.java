/* Licensed under MIT 2024. */
package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public final class ErrorHandler {
	private static Component parentComponent;

	private ErrorHandler() {
		throw new IllegalAccessError();
	}

	public static synchronized void setParentComponent(Component parentComponent) {
		ErrorHandler.parentComponent = Objects.requireNonNull(parentComponent);
	}

	public static void showError(String title, String error) {
		if (parentComponent == null) {
			System.err.printf("Error (%s): %s%n", title, error);
			return;
		}
		JOptionPane.showMessageDialog(parentComponent, error, title, JOptionPane.ERROR_MESSAGE);
	}

}
