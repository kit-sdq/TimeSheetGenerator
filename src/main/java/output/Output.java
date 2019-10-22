package output;

import data.FullDocumentation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Output implements IOutput {
    @Override


    public String generateLaTeX(FullDocumentation documentation) {
        try {
            return readTexTxt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readTexTxt() throws IOException {
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try (BufferedReader reader = new BufferedReader(new FileReader("checker/src/main/resources/MiLoG_Arbeitszeitdokumentation.txt"))) {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        }
    }
}
