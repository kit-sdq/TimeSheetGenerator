/* Licensed under MIT 2024-2025. */
package ui.json;

import lombok.Getter;
import lombok.Setter;
import ui.fileexplorer.FileChooserType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
	/**
	 * Specifies the naming format for exported PDF files.
	 * <p>
	 * The format can include placeholders for dynamic values from other current
	 * settings, such as the name, month and year. The default value is defined by
	 * the default filename for Prog in the Default Values object retrieved by
	 * {@link JSONHandler#getFieldDefaults()}.
	 * </p>
	 */
	private String exportPdfNameFormat = JSONHandler.getFieldDefaults().getDefaultFilenameProg();
	private String mailRecipient = JSONHandler.getFieldDefaults().getDefaultMailRecipientProg();
	private List<String> mailRecipientsCC = new ArrayList<>();
	private String mailSubjectFormat = JSONHandler.getFieldDefaults().getDefaultMailSubjectProg();

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
		this.exportPdfNameFormat = uiSettings.exportPdfNameFormat;
		this.mailRecipient = uiSettings.mailRecipient;
		this.mailRecipientsCC = new ArrayList<>(uiSettings.mailRecipientsCC);
		this.mailSubjectFormat = uiSettings.mailSubjectFormat;
		// Recipient may not be null
		if (mailRecipient == null || mailRecipient.isBlank()) {
			mailRecipient = JSONHandler.getFieldDefaults().getDefaultMailRecipientProg();
		}
	}

	// Constructors, Getters, and Setters

	private void setMonthPath(String openMonthPath) {
		this.monthPath = openMonthPath;
		save();
	}

	private void setTexPath(String texPath) {
		this.texPath = texPath;
		save();
	}

	private void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
		save();
	}

	public void setMailRecipient(String mailRecipient) {
		this.mailRecipient = mailRecipient;
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
