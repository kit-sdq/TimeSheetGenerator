/* Licensed under MIT 2024. */
package ui.export;

import ui.UserInterface;
import ui.fileexplorer.FileChooser;
import ui.json.JSONHandler;

import java.io.File;
import java.util.Optional;

public final class FileExporter {

	private FileExporter() {
		// Don't allow instances of this class
	}

	public static void printTex(UserInterface parentUI) {
		try (TempFiles tempFiles = TempFiles.generateNewTemp(parentUI)) {
			if (tempFiles == null) {
				parentUI.showError("Failed to create temporary file", "Could not create month.json file. If you have unsaved changes, try saving.");
				return;
			}

			Optional<String> error = TexCompiler.validateContents(tempFiles);
			if (error.isPresent()) {
				error(parentUI, "Validation error", error.get());
				return;
			}

			File texFile = FileChooser.chooseCreateTexFile(parentUI, "Compile to Tex");
			if (texFile == null)
				return; // Cancelled

			TexCompiler.compileToTex(tempFiles.getMonthFile(), texFile);

			if (!texFile.exists()) {
				error(parentUI, "Latex compiler error", "Tex file creation failed!");
			}
		}
	}

	public static void printPDF(UserInterface parentUI) {
		try (TempFiles tempFiles = TempFiles.generateNewTemp(parentUI)) {
			if (tempFiles == null)
				return;

			Optional<String> error = TexCompiler.validateContents(tempFiles);
			if (error.isPresent()) {
				error(parentUI, "Validation error", error.get());
				return;
			}

			File pdfFile = FileChooser.chooseCreatePDFFile(parentUI, "Print to PDF");
			if (pdfFile == null)
				return; // Cancelled

			error = PDFCompiler.compileToPDF(JSONHandler.getGlobalSettings(), parentUI.getCurrentMonth(), pdfFile);

			if (error.isPresent()) {
				error(parentUI, "PDF compiler error", error.get());
				return;
			}

			if (!pdfFile.exists()) {
				error(parentUI, "Failed to create PDF", "PDF file creation failed! Perhaps try to compile to tex?");
			}
		}
	}

	private static void error(UserInterface parentUI, String title, String error) {
		parentUI.showError(title, "%s%s%s".formatted("Error: Invalid Timesheet:", System.lineSeparator(), error));
	}
}
