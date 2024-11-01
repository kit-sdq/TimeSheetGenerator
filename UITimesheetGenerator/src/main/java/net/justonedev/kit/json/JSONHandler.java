/*
 * Licensed under the MIT License (MIT)
 * Copyright (c) 2024 Benjamin Claus
 */
package net.justonedev.kit.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.justonedev.kit.Main;
import net.justonedev.kit.MonthlySettingsBar;
import net.justonedev.kit.TimesheetEntry;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class JSONHandler {

    public static Global globalSettings;

    private static String configDir;
    private static final String configFile = "global.json";


    public static void initialize() {
        String OS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        if (OS.contains("win")) {
            configDir = System.getenv("APPDATA");
        } else if (OS.contains("mac")) {
            configDir = System.getProperty("user.home") + "/Library/Application Support";
        } else if (OS.contains("nux") || OS.contains("nix")) {
            configDir = System.getProperty("user.home") + "/.config";
        } else {
            // Default to user home directory
            configDir = System.getProperty("user.home");
        }

        // Create a subdirectory for your application
        configDir += "/TimeSheetGenerator";

        createDefaultGlobalSettings();
        loadGlobal();
    }

    public static void loadGlobal() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            /*
            // Read month.json
            Month month = objectMapper.readValue(new File("month.json"), Month.class);
            System.out.println("Year: " + month.getYear() + ", Month: " + month.getMonth());

            // Access entries
            for (Month.Entry entry : month.getEntries()) {
                System.out.println("Action: " + entry.getAction() + ", Day: " + entry.getDay());
            }
            */

            // Read global.json
            globalSettings = objectMapper.readValue(getConfigFile(), Global.class);
            System.out.println("Loaded Global Settings.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveGlobal(Global globalSettings) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            objectMapper.writeValue(getConfigFile(), globalSettings);
            System.out.println("Saved global settings.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMonth(File monthFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            Month month = objectMapper.readValue(monthFile, Month.class);

            Main.importMonthBarSettings(month);

            for (Month.Entry entry : month.getEntries()) {
                Main.addEntry(new TimesheetEntry(entry));
            }

            System.out.println("Year: " + month.getYear() + ", Month: " + month.getMonth());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Month getMonth(MonthlySettingsBar settingsBar, DefaultListModel<TimesheetEntry> entries) {
        Month month = new Month();
        List<Month.Entry> monthEntries = new ArrayList<>();
        settingsBar.fillMonth(month);
        entries.elements().asIterator().forEachRemaining(entry -> {
            monthEntries.add(entry.toMonthEntry());
        });
        month.setEntries(monthEntries);
        return month;
    }

    public static void saveMonth(File saveFile, MonthlySettingsBar settingsBar, DefaultListModel<TimesheetEntry> entries) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            Month month = getMonth(settingsBar, entries);
            objectMapper.writeValue(saveFile, month);
            System.out.println("Saved month.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File generateTemporaryJSONFile(MonthlySettingsBar settingsBar, DefaultListModel<TimesheetEntry> entries) {
        File f;
        do {
            f = new File(configDir, "/temp-%s.json".formatted(UUID.randomUUID()));
        } while (f.exists());
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        saveMonth(f, settingsBar, entries);
        return f;
    }

    public static File getTemporaryTexFile() {
        File f;
        do {
            f = new File(configDir, "/temp-%s.tex".formatted(UUID.randomUUID()));
        } while (f.exists());
        return f;
    }

    public static File getConfigFile() {
        return new File(configDir, configFile);
    }

    private static boolean globalConfigExists() {
        return getConfigFile().exists();
    }

    public static void createDefaultGlobalSettings() {
        if (globalConfigExists()) return;
        try {
            File f = getConfigFile();
            new File(configDir).mkdirs();
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Global global = new Global();
        global.setName("Max Mustermann");
        global.setStaffId(1234567);
        global.setDepartment("Fakultät für Informatik");
        global.setWorkingTime("40:00");
        global.setWage(13.25);
        global.setWorkingArea("ub");
        saveGlobal(global);
        System.out.println("Created Default Global Settings.");
    }

}
