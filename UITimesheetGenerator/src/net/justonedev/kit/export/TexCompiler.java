package net.justonedev.kit.export;

import net.justonedev.kit.json.JSONHandler;

import java.io.File;
import java.util.Optional;

public class TexCompiler {

    public static void compileToTex(File monthFile, File texFile) {

        File globalFile = JSONHandler.getConfigFile();

        main.Main.main(new String[]{"-f", globalFile.getAbsolutePath(), monthFile.getAbsolutePath(), texFile.getAbsolutePath()});
    }

    /**
     * Wrapper method for the timesheet generator module timesheet compiler.
     * @param tempFiles The temporary files.
     * @return An optional of the error message, empty if success.
     */
    static Optional<String> validateContents(TempFiles tempFiles) {
        return main.Main.validateTimesheet(tempFiles.getGlobalFile(), tempFiles.getMonthFile());
    }

}
