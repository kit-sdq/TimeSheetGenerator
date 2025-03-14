/* Licensed under MIT 2024. */
package ui.export;

import ui.UserInterface;
import ui.json.JSONHandler;

import java.io.File;

class TempFiles implements AutoCloseable {

	private final File globalFile;
	private final File monthFile;

	private final boolean isTempMonthFile;

	private TempFiles(File globalFile, File monthFile, boolean isTempMonthFile) {
		this.globalFile = globalFile;
		this.monthFile = monthFile;
		this.isTempMonthFile = isTempMonthFile;
	}

	public File getGlobalFile() {
		return globalFile;
	}

	public File getMonthFile() {
		return monthFile;
	}

	@Override
	public void close() {
		if (isTempMonthFile && !monthFile.delete()) {
			monthFile.deleteOnExit();
		}
	}

	public static TempFiles generateNewTemp(UserInterface parentUi) {
		File monthFile;
		boolean tempMonth = false;
		if (parentUi.hasUnsavedChanges() || parentUi.getCurrentOpenFile() == null) {
			monthFile = parentUi.generateTempMonthFile();
			tempMonth = true;
		} else {
			monthFile = parentUi.getCurrentOpenFile();
		}
		if (monthFile == null)
			return null;
		return new TempFiles(JSONHandler.getConfigFile(), monthFile, tempMonth);
	}

}
