package net.justonedev.kit;

import net.justonedev.kit.export.TempFiles;
import net.justonedev.kit.json.JSONHandler;

import java.io.File;

public class TexCompiler {

    public static void compileToTex(File monthFile, File texFile) {

        File globalFile = JSONHandler.getConfigFile();

        main.Main.main(new String[]{"-f", globalFile.getAbsolutePath(), monthFile.getAbsolutePath(), texFile.getAbsolutePath()});
    }

    public static void validateContents(TempFiles tempFiles) {

    }

}
