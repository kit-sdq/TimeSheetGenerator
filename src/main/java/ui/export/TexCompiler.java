/* Licensed under MIT 2024-2025. */
package ui.export;

import checker.ExportType;
import ui.json.JSONHandler;

import java.io.File;
import java.util.Optional;

public final class TexCompiler {

	private TexCompiler() {
		throw new IllegalAccessError();
	}

	public static void compileToTex(File monthFile, File texFile) {
		File globalFile = JSONHandler.getConfigFile();
		main.Main.main(new String[] { "-f", globalFile.getAbsolutePath(), monthFile.getAbsolutePath(), texFile.getAbsolutePath() });
	}

	/**
	 * Wrapper method for the timesheet generator module timesheet compiler.
	 * 
	 * @param tempFiles  The temporary files.
	 * @param exportType The type of file that is being exported to, as they have
	 *                   slight differences.
	 * @return An optional of the error message, empty if success.
	 */
	static Optional<String> validateContents(TempFiles tempFiles, ExportType exportType) {
		return main.Main.validateTimesheet(tempFiles.getGlobalFile(), tempFiles.getMonthFile(), exportType);
	}

}
