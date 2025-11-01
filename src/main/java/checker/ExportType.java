/* Licensed under MIT 2025. */
package checker;

import lombok.Getter;

/**
 * This enum is for telling the MiLoGChecker what its exporting to, as some
 * details can be different between the PDF and Latex templates.<br/>
 * For example, currently, the PDF supports 22 rows while the Latex template
 * only supports 20 rows.
 */
@Getter
public enum ExportType {
	EXPORT_LATEX(20), EXPORT_PDF(22);

	private final int maxEntries;

	ExportType(int maxEntries) {
		this.maxEntries = maxEntries;
	}
}
