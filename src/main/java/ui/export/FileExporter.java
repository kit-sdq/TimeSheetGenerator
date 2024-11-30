/* Licensed under MIT 2024. */
package ui.export;

import ui.ErrorHandler;
import ui.UserInterface;
import ui.fileexplorer.FileChooser;
import ui.json.JSONHandler;

import java.io.File;
import java.util.Optional;

public final class FileExporter {

	private FileExporter() {
		throw new IllegalAccessError();
	}

	public static void printTex(UserInterface parentUi) {
		try (TempFiles tempFiles = TempFiles.generateNewTemp(parentUi)) {
			if (tempFiles == null) {
				ErrorHandler.showError("Failed to create temporary file", "Could not create month.json file. If you have unsaved changes, try saving.");
				return;
			}

			Optional<String> error = TexCompiler.validateContents(tempFiles);
			if (error.isPresent()) {
				error("Validation error", error.get());
				return;
			}

			File texFile = FileChooser.chooseCreateTexFile(parentUi, "Compile to Tex");
			if (texFile == null)
				return; // Cancelled

			TexCompiler.compileToTex(tempFiles.getMonthFile(), texFile);

			if (!texFile.exists()) {
				error("Latex compiler error", "Tex file creation failed!");
			}
		}
	}

	public static void printPDF(UserInterface parentUi) {
		try (TempFiles tempFiles = TempFiles.generateNewTemp(parentUi)) {
			if (tempFiles == null)
				return;

			Optional<String> error = TexCompiler.validateContents(tempFiles);
			if (error.isPresent()) {
				error("Validation error", error.get());
				return;
			}

			File pdfFile = FileChooser.chooseCreatePDFFile(parentUi, "Print to PDF");
			if (pdfFile == null) {
				return; // Cancelled
			}

			error = PDFCompiler.compileToPDF(JSONHandler.getGlobalSettings(), parentUi.getCurrentMonth(), pdfFile);

			if (error.isPresent()) {
				error("PDF compiler error", error.get());
				return;
			}

			if (!pdfFile.exists()) {
				error("Failed to create PDF", "PDF file creation failed! Perhaps try to compile to tex?");
			}
		}
	}

	private static void error(String title, String error) {
		ErrorHandler.showError(title, "%s%s%s".formatted("Error: Invalid Timesheet:", System.lineSeparator(), error));
	}
}
