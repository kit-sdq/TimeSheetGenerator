/* Licensed under MIT 2024. */
package ui.fileexplorer;

import ui.UserInterface;
import ui.json.JSONHandler;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public final class FileChooser {

	private FileChooser() {
		// Don't allow instances of this class
	}

	// Timesheet Format: Nachname_Vorname_Monat Jahr.pdf
	private static final String FORMAT = "%s_%s 20%s";

	public static String getDefaultFileName(UserInterface parentUI) {
		return FORMAT.formatted(JSONHandler.getGlobalSettings().getNameUnderscoreFormat(), getGermanMonth(parentUI.getCurrentMonthNumber()),
				parentUI.getYear());
	}

	public static File chooseFile(String title, FileChooserType chooserType) {
		// Later: Implement Windows Version ?
		// -> Later, I tried doing it simple and failed, and I don't have the time
		// (currently) to figure it out
		// Once I do, I'll make another pull request. It might not be as pretty, but
		// because it remembers the
		// folders, you don't need to go through the navigation hassle each time, so I
		// think it's a fair
		// compromise for now.
		// - JustOneDeveloper
		return chooseFileSwing(title, chooserType);

	}

	private static File chooseFileSwing(String title, FileChooserType chooserType) {
		JFileChooser fileChooser = getFileChooser(chooserType);

		fileChooser.setDialogTitle(title);

		int userSelection = fileChooser.showOpenDialog(null);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			JSONHandler.getUISettings().setPath(chooserType, file);
			return file;
		} else {
			return null;
		}
	}

	public static File chooseCreateJSONFile(UserInterface parentUi, String title) {
		String month = parentUi.getCurrentMonthName();
		if (month.isBlank())
			month = "month";
		return chooseCreateFile(title, FileChooserType.MONTH_PATH, "%s%s".formatted(month, parentUi.getYear()), "json", "JSON Files (*.json)");
	}

	public static File chooseCreateTexFile(UserInterface parentUi, String title) {
		return chooseCreateFile(title, FileChooserType.TEX_PATH, getDefaultFileName(parentUi), "tex", "LaTeX Files (*.tex)");
	}

	public static File chooseCreatePDFFile(UserInterface parentUi, String title) {
		return chooseCreateFile(title, FileChooserType.PDF_PATH, getDefaultFileName(parentUi), "pdf", "PDF Files (*.pdf)");
	}

	public static File chooseCreateFile(String title, FileChooserType chooserType, String defaultFileName, String extension, String extensionDescription) {
		JFileChooser fileChooser = getFileChooser(chooserType);
		fileChooser.setDialogTitle(title);

		// Suggest a default file name
		fileChooser.setSelectedFile(new File("%s.%s".formatted(defaultFileName, extension == null ? "" : extension)));

		// Set up file filter for .json files
		FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter(extensionDescription, extension);
		fileChooser.setFileFilter(jsonFilter);

		int userSelection = fileChooser.showSaveDialog(null);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			if (fileToSave.exists()) {
				int result = JOptionPane.showConfirmDialog(null, "The file already exists. Do you want to override it?", "Existing file",
						JOptionPane.YES_NO_OPTION);

				if (result != JOptionPane.YES_OPTION) {
					return null;
				}
			}

			JSONHandler.getUISettings().setPath(chooserType, fileToSave);
			return fileToSave;
		} else {
			return null;
		}
	}

	/**
	 * Gets the german name for a month from 1-12.
	 * 
	 * @param monthName The month number, from 1-12.
	 * @return The name of the month.
	 */
	private static String getGermanMonth(int monthName) {
		return switch (monthName) {
		case 1 -> "Januar";
		case 2 -> "Februar";
		case 3 -> "März";
		case 4 -> "April";
		case 5 -> "Mai";
		case 6 -> "Juni";
		case 7 -> "Juli";
		case 8 -> "August";
		case 9 -> "September";
		case 10 -> "Oktober";
		case 11 -> "November";
		case 12 -> "Dezember";
		default -> "unbekannt";
		};
	}

	private static JFileChooser getFileChooser(FileChooserType chooserType) {
		JFileChooser fileChooser;
		File parent = JSONHandler.getUISettings().getPath(chooserType);
		if (parent == null)
			fileChooser = new JFileChooser();
		else
			fileChooser = new JFileChooser(parent);
		return fileChooser;
	}

}
