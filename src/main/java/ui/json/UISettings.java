/* Licensed under MIT 2024. */
package ui.json;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Getter;
import lombok.Setter;
import ui.fileexplorer.FileChooserType;

import java.io.File;

@Getter
@Setter
public class UISettings {
	private boolean addSignature = true;
	private boolean addVacationEntry = false;
	private boolean useYYYY = false;
	private boolean useGermanMonths = false;
	private boolean warnOnHoursMismatch = true;
	private String monthPath;
	private String texPath;
	private String pdfPath;

	public UISettings() {
		// Default Constructor is required
	}

	public UISettings(UISettings uiSettings) {
		this.addSignature = uiSettings.addSignature;
		this.addVacationEntry = uiSettings.addVacationEntry;
		this.useYYYY = uiSettings.useYYYY;
		this.useGermanMonths = uiSettings.useGermanMonths;
		this.warnOnHoursMismatch = uiSettings.warnOnHoursMismatch;
		this.monthPath = uiSettings.monthPath;
		this.texPath = uiSettings.texPath;
		this.pdfPath = uiSettings.pdfPath;
	}

	// Constructors, Getters, and Setters

	public void setMonthPath(String openMonthPath) {
		this.monthPath = openMonthPath;
		save();
	}

	public void setTexPath(String texPath) {
		this.texPath = texPath;
		save();
	}

	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
		save();
	}

	public void setPath(FileChooserType type, File selectedFile) {
		String folderPath;
		if (selectedFile == null)
			folderPath = "";
		else
			folderPath = selectedFile.getParent();
		switch (type) {
		case MONTH_PATH -> setMonthPath(folderPath);
		case TEX_PATH -> setTexPath(folderPath);
		case PDF_PATH -> setPdfPath(folderPath);
		}
	}

	public File getPath(FileChooserType type) {
		String pathStr = switch (type) {
		case MONTH_PATH -> getMonthPath();
		case TEX_PATH -> getTexPath();
		case PDF_PATH -> getPdfPath();
		};
		// If Pdf path is empty but tex isn't, use tex path for pdf default and vice
		// versa
		if (pathStr == null || pathStr.isEmpty()) {
			switch (type) {
			case PDF_PATH:
				pathStr = getTexPath();
				if (pathStr == null || pathStr.isEmpty())
					return null;
				break;
			case TEX_PATH:
				pathStr = getPdfPath();
				if (pathStr == null || pathStr.isEmpty())
					return null;
				break;
			default:
				return null;
			}
		}
		File pathFile = new File(pathStr);
		if (!pathFile.exists())
			return null;
		return pathFile;
	}

	public void save() {
		JSONHandler.saveUISettings(this);
	}
}
