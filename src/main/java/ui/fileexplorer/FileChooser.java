/* Licensed under MIT 2024-2025. */
package ui.fileexplorer;

import ui.UserInterface;
import ui.json.Global;
import ui.json.JSONHandler;
import ui.json.UISettings;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public final class FileChooser {

	private FileChooser() {
		// Don't allow instances of this class
	}

	/**
	 * Returns the default file name for the PDF export based on the users settings.
	 *
	 * @param parentUI The UserInterface instance to get the current month and year
	 *                 from.
	 * @return The default file name for the PDF export.
	 */
	public static String getDefaultFileName(UserInterface parentUI) {
		UISettings uiSettings = JSONHandler.getUISettings();
		Global global = JSONHandler.getGlobalSettings();
		return uiSettings.getExportPdfNameFormat().replace("%FIRST_U%", global.getFirstnameUnderscoreFormat()).replace("%FIRST%", global.getFirstname())
				.replace("%LAST%", global.getLastname()).replace("%MM%", "%02d".formatted(parentUI.getCurrentMonthNumber()))
				.replace("%MM_GER%", parentUI.getCurrentMonth().getGermanName()).replace("%MM_ENG%", parentUI.getCurrentMonthName())
				.replace("%YY%", parentUI.getYear()).replace("%YYYY%", parentUI.getFullYear());
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
