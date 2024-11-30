/* Licensed under MIT 2024. */
package ui.json;

import ui.fileexplorer.FileChooserType;

import java.io.File;

public class UISettings {
	private boolean addSignature;
	private String monthPath;
	private String texPath;
	private String pdfPath;

	public UISettings() {
		// Default Constructor is required
	}

	public UISettings(UISettings uiSettings) {
		this.addSignature = uiSettings.addSignature;
		this.monthPath = uiSettings.monthPath;
		this.texPath = uiSettings.texPath;
		this.pdfPath = uiSettings.pdfPath;
	}

	// Constructors, Getters, and Setters

	public boolean getAddSignature() {
		return addSignature;
	}

	public void setAddSignature(boolean addSignature) {
		this.addSignature = addSignature;
	}

	public String getMonthPath() {
		return monthPath;
	}

	public void setMonthPath(String openMonthPath) {
		this.monthPath = openMonthPath;
		save();
	}

	public String getTexPath() {
		return texPath;
	}

	public void setTexPath(String texPath) {
		this.texPath = texPath;
		save();
	}

	public String getPdfPath() {
		return pdfPath;
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
