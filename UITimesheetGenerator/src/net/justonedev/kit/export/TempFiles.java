package net.justonedev.kit.export;

import net.justonedev.kit.Main;
import net.justonedev.kit.json.JSONHandler;

import java.io.File;

class TempFiles {

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

    public void close() {
        if (isTempMonthFile) {
            monthFile.deleteOnExit();
        }
    }

    public static TempFiles generateNewTemp() {
        File monthFile;
        boolean tempMonth = false;
        if (Main.hasUnsavedChanges() || Main.getCurrentOpenFile() == null) {
            monthFile = Main.generateTempMonthFile();
            tempMonth = true;
        } else {
            monthFile = Main.getCurrentOpenFile();
        }
        if (monthFile == null) return null;
        return new TempFiles(JSONHandler.getConfigFile(), monthFile, tempMonth);
    }

}
