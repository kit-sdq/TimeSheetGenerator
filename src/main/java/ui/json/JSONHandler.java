/* Licensed under MIT 2024-2025. */
package ui.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Getter;
import ui.ErrorHandler;
import ui.UserInterface;
import ui.MonthlySettingsBar;
import ui.TimesheetEntry;
import ui.json.api.DefaultsFetcher;
import ui.json.api.FieldDefaults;
import ui.json.api.PresetCollection;
import ui.json.api.PresetFetcher;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public final class JSONHandler {

	private JSONHandler() {
		// Don't allow instances of this class
	}

	private static Global globalSettings;
	private static UISettings uiSettings;
	// Should have default value for constructor in UISettings, although the loading
	// order prevents any exception
	private static FieldDefaults fieldDefaults = FieldDefaults.DEFAULT_VALUES;
	/**
	 * -- GETTER -- Gets a copy of the loaded preset collection.
	 */
	@Getter
	private static PresetCollection presets;

	private static String configDir;

	private static final String CONFIG_FILE_NAME = "global.json";
	private static final String UI_SETTINGS_FILE_NAME = "settings.json";
	private static final String DEFAULT_VALUES_FILE_NAME = "defaults.json";
	private static final String KNOWN_PRESETS_FILE_NAME = "presets.json";

	private static final String ERROR = "An unexpected error occurred:%s%s".formatted(System.lineSeparator(), "%s");

	public static void initialize() {
		final String homePropertyName = "user.home";
		String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

		if (os.contains("win")) {
			configDir = System.getenv("APPDATA");
		} else if (os.contains("mac")) {
			configDir = System.getProperty(homePropertyName) + "/Library/Application Support";
		} else if (os.contains("nux") || os.contains("nix")) {
			configDir = System.getProperty(homePropertyName) + "/.config";
		} else {
			// Default to user home directory
			configDir = System.getProperty(homePropertyName);
		}

		// Create a subdirectory for your application
		configDir += "/TimeSheetGenerator";

		loadDefaultValues();
		loadPresets();
		createDefaultGlobalSettings();
		createDefaultOtherGlobalSettings();
		loadGlobal();
		loadUiSettings();

		cleanUp();
	}

    /**
     * Gets the folder path for the application data and config.
     * @return the config path.
     */
    public static String getApplicationDataPath() {
        return configDir;
    }

	/**
	 * Gets a copy of the current global settings.
	 * 
	 * @return Copy of global settings
	 */
	public static Global getGlobalSettings() {
		return new Global(globalSettings);
	}

	/**
	 * Gets a copy of the current additional ui settings.
	 *
	 * @return Copy of ui settings
	 */
	public static UISettings getUISettings() {
		return new UISettings(uiSettings);
	}

	/**
	 * Gets a copy of the current default values for given fields.
	 *
	 * @return Copy of field defaults.
	 */
	public static FieldDefaults getFieldDefaults() {
		return new FieldDefaults(fieldDefaults);
	}

	private static void setGlobalSettings(Global globalSettings) {
		JSONHandler.globalSettings = globalSettings;
	}

	private static void setUISettings(UISettings otherSettings) {
		JSONHandler.uiSettings = otherSettings;
	}

	// region Global Settings JSON Object methods

	public static void loadGlobal() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			globalSettings = objectMapper.readValue(getConfigFile(), Global.class);
		} catch (IOException e) {
			ErrorHandler.showError("Error loading global settings file", ERROR.formatted(e.getMessage()));
		}
	}

	public static void saveGlobal(Global globalSettings) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			objectMapper.writeValue(getConfigFile(), globalSettings);
			setGlobalSettings(globalSettings);
		} catch (IOException e) {
			ErrorHandler.showError("Error saving global settings file", ERROR.formatted(e.getMessage()));
		}
	}

	private static void loadUiSettings() {
		ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			uiSettings = objectMapper.readValue(getUiSettingsFile(), UISettings.class);
		} catch (IOException e) {
			ErrorHandler.showError("Error loading UI settings file", ERROR.formatted(e.getMessage()));
		}
	}

	public static void saveUISettings(UISettings uiSettings) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			objectMapper.writeValue(getUiSettingsFile(), uiSettings);
			setUISettings(uiSettings);
		} catch (IOException e) {
			ErrorHandler.showError("Error saving UI settings file", ERROR.formatted(e.getMessage()));
		}
	}

	// endregion

	// region Month JSON Object methods

	public static void loadMonth(UserInterface parentUi, File monthFile) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			Month month = objectMapper.readValue(monthFile, Month.class);

			parentUi.importMonthBarSettings(month);

			for (Month.Entry entry : month.getEntries()) {
				parentUi.addEntry(new TimesheetEntry(entry));
			}
		} catch (IOException e) {
			ErrorHandler.showError("Error loading month file", ERROR.formatted(e.getMessage()));
		}
	}

	public static boolean isFileValidMonth(File monthFile) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			objectMapper.readValue(monthFile, Month.class);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static Month getMonth(MonthlySettingsBar settingsBar, DefaultListModel<TimesheetEntry> entries) {
		Month month = new Month();
		List<Month.Entry> monthEntries = new ArrayList<>();
		settingsBar.fillMonth(month);
		entries.elements().asIterator().forEachRemaining(entry -> monthEntries.add(entry.toMonthEntry()));
		month.setEntries(monthEntries);
		return month;
	}

	public static void saveMonth(File saveFile, MonthlySettingsBar settingsBar, DefaultListModel<TimesheetEntry> entries) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			Month month = getMonth(settingsBar, entries);
			objectMapper.writeValue(saveFile, month);
		} catch (IOException e) {
			ErrorHandler.showError("Error saving month file", ERROR.formatted(e.getMessage()));
		}
	}

	// endregion

	// region Default JSON File Creation + Temp File Helper Methods

	public static File generateTemporaryJSONFile(MonthlySettingsBar settingsBar, DefaultListModel<TimesheetEntry> entries) {
		File f;
		do {
			f = new File(configDir, "/temp-%s.json".formatted(UUID.randomUUID()));
		} while (f.exists());
		try {
			if (!f.createNewFile()) {
				// Show error in main and return using catch block
				throw new IOException("Failed to create temporary json file %s.".formatted(f.getAbsolutePath()));
			}
		} catch (IOException e) {
			ErrorHandler.showError("Error generating temp json file", ERROR.formatted(e.getMessage()));
			return null;
		}
		saveMonth(f, settingsBar, entries);
		return f;
	}

	public static File getConfigFile() {
		return new File(configDir, CONFIG_FILE_NAME);
	}

	private static boolean globalConfigExists() {
		return getConfigFile().exists();
	}

	private static void createDefaultGlobalSettings() {
		if (globalConfigExists())
			return;
		try {
			File folder = new File(configDir);
			if (!folder.exists() && !folder.mkdirs()) {
				// Show error in main and return using catch block
				throw new IOException("Failed to create folder %s for the global configuration file.".formatted(configDir));
			}
			File f = getConfigFile();
			if (!f.createNewFile()) {
				// Show error in main and return using catch block
				throw new IOException("Failed to create global configuration file %s.".formatted(f.getAbsolutePath()));
			}
		} catch (IOException e) {
			ErrorHandler.showError("Error creating global settings file", ERROR.formatted(e.getMessage()));
			return;
		}

		Global global = new Global();
		global.setName("Max Mustermann");
		global.setStaffId(1234567);
		global.setDepartment("KASTEL");
		global.setWorkingTime("39:00");
		global.setWage(13.98);
		global.setWorkingArea("ub");
		saveGlobal(global);
	}

	// endregion

	// region UI Settings JSON Object methods

	public static File getUiSettingsFile() {
		return new File(configDir, UI_SETTINGS_FILE_NAME);
	}

	private static boolean uiSettingsFileExists() {
		return getUiSettingsFile().exists();
	}

	public static void createDefaultOtherGlobalSettings() {
		if (uiSettingsFileExists())
			return;
		try {
			File f = getUiSettingsFile();
			File folder = new File(configDir);
			if (!folder.exists() && !folder.mkdirs()) {
				// Show error in main and return using catch block
				throw new IOException("Failed to access folder %s for the global settings file.".formatted(configDir));
			}
			if (!f.createNewFile()) {
				// Show error in main and return using catch block
				throw new IOException("Failed to create global settings file %s.".formatted(f.getAbsolutePath()));
			}
		} catch (IOException e) {
			ErrorHandler.showError("Error creating UI settings file", ERROR.formatted(e.getMessage()));
			return;
		}

		UISettings settings = new UISettings();
		settings.setAddSignature(false);
		settings.setAddVacationEntry(false);
		settings.setUseYYYY(false);
		settings.setUseGermanMonths(false);
		settings.setWarnOnHoursMismatch(true);
		settings.setExportPdfNameFormat(fieldDefaults.getDefaultFilenameProg());
		saveUISettings(settings);
	}

	// endregion

	// region Default Value JSON Object methods

	private static File getValueDefaultsFile() {
		return new File(configDir, DEFAULT_VALUES_FILE_NAME);
	}

	private static File getKnownPresetsFile() {
		return new File(configDir, KNOWN_PRESETS_FILE_NAME);
	}

	public static void loadDefaultValues() {
		FieldDefaults fieldDefaults;
		try {
			fieldDefaults = attemptLoadDefaultValues();
		} catch (IOException | IllegalStateException e) {
			// No need to write default values. If the endpoint is not reachable,
			// we always want up-to-date values. In any case, the hardcoded values
			// will be more up-to-date than (different) previously hardcoded values,
			// so there is no need to save the hardcoded values to a file.
			fieldDefaults = FieldDefaults.DEFAULT_VALUES;
		}
		JSONHandler.fieldDefaults = fieldDefaults;
	}

	private static FieldDefaults attemptLoadDefaultValues() throws IOException, IllegalStateException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		Optional<String> loadedJson = DefaultsFetcher.fetchJSONFromEndpoint();
		File defaultsFile = getValueDefaultsFile();

		if (loadedJson.isPresent()) {
			String json = loadedJson.get();
			try {
				Files.writeString(defaultsFile.toPath(), json);
			} catch (IOException ignored) {
				// ignore, we just save if we can
			}
			return objectMapper.readValue(json, FieldDefaults.class);
		} else {
			// Attempt to load from file or return default
			if (defaultsFile.exists()) {
				return objectMapper.readValue(defaultsFile, FieldDefaults.class);
			} else {
				throw new IllegalStateException();
			}
		}
	}

	// endregion

	// region Load Presets from File and API

	private static void loadPresets() {
		JSONHandler.presets = loadPresetCollection();
	}

	private static PresetCollection loadPresetCollection() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		File presetsFile = getKnownPresetsFile();
		Optional<String> presetsJSON = PresetFetcher.fetchJSONFromEndpoint();

		Optional<PresetCollection> fromFile = parsePresetCollection(objectMapper, presetsFile);
		Optional<PresetCollection> fromAPI = parsePresetCollection(objectMapper, presetsJSON);

		PresetCollection mergedCollection;
		if (fromFile.isPresent() || fromAPI.isPresent()) {
			mergedCollection = PresetCollection.merge(fromFile, fromAPI);
		} else {
			return new PresetCollection();
		}

		// write merged collection to file
		try {
			objectMapper.writeValue(presetsFile, mergedCollection);
		} catch (IOException e) {
			Logger.getGlobal().warning("Failed to write presets to file: %s%n".formatted(presetsFile.getAbsolutePath()));
		}
		return mergedCollection;
	}

	/**
	 * Parses a {@link PresetCollection} from a given file using the given
	 * {@link ObjectMapper} and returns it as an Optional. Returns an empty optional
	 * if either the file doesn't exist or does not contain a parseable
	 * {@link PresetCollection}.<br/>
	 * Used to provide more readable code by moving try-catch blocks away from the
	 * main {@link JSONHandler#loadPresetCollection()} method.
	 * <p>
	 * Similar to {@link JSONHandler#parsePresetCollection(ObjectMapper, Optional)}.
	 * </p>
	 * 
	 * @param objectMapper the ObjectMapper used to parse JSON.
	 * @param file         The file to parse from.
	 * @return An optional of the parsed preset collection or empty.
	 */
	private static Optional<PresetCollection> parsePresetCollection(ObjectMapper objectMapper, File file) {
		if (file.exists()) {
			try {
				return Optional.of(objectMapper.readValue(file, PresetCollection.class));
			} catch (IOException ignored) {
				// return empty
			}
		}
		return Optional.empty();
	}

	/**
	 * Parses a {@link PresetCollection} from a given string using the given
	 * {@link ObjectMapper} and returns it as an Optional. Returns an empty optional
	 * if either the string is null/empty or does not contain a parseable
	 * {@link PresetCollection}.<br/>
	 * Used to provide more readable code by moving try-catch blocks away from the
	 * main {@link JSONHandler#loadPresetCollection()} method.
	 * <p>
	 * Similar to {@link JSONHandler#parsePresetCollection(ObjectMapper, File)}.
	 * </p>
	 * 
	 * @param objectMapper the ObjectMapper used to parse JSON.
	 * @param json         The json of the PresetCollection.
	 * @return An optional of the parsed preset collection or empty.
	 */
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Optional.empty() default value otherwise needed in multiple places
	private static Optional<PresetCollection> parsePresetCollection(ObjectMapper objectMapper, Optional<String> json) {
		if (json.isPresent() && !json.get().isBlank()) {
			try {
				return Optional.of(objectMapper.readValue(json.get(), PresetCollection.class));
			} catch (IOException ignored) {
				// return empty
			}
		}
		return Optional.empty();
	}

	// endregion

	private static void cleanUp() {
		File[] files = new File(configDir).listFiles();
		if (files == null)
			return;
		for (File file : files) {
			if (file.getName().startsWith("temp") && !file.delete()) {
				file.deleteOnExit();
			}
		}
	}

}
