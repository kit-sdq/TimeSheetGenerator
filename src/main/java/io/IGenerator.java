/* Licensed under MIT 2023-2024. */
package io;

import data.Entry;
import data.TimeSheet;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * A generator is able to generate a document filled with values coming from a
 * {@link TimeSheet} and the associated {@link Entry Entries}.
 */
public interface IGenerator {

	/**
	 * Generates a document with information from a {@link TimeSheet} and the
	 * associated {@link Entry entries}.
	 * 
	 * @return The generated document.
	 */
	String generate();

	/**
	 * Returns the {@link FileNameExtensionFilter} associated with the generated
	 * file. This can be used if the {@link String} given by {@link #generate()}
	 * should be saved.
	 * 
	 * @return The {@link FileNameExtensionFilter} associated to the generated file.
	 */
	FileNameExtensionFilter getFileNameExtensionFilter();
}
