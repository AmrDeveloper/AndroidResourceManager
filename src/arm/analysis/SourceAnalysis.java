package arm.analysis;

import java.io.*;

public class SourceAnalysis {

    public static void analysisSourceFile(File sourceFile, String extension, OnAnalysisListener listener) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
            int lines = 0;
            while (reader.readLine() != null) lines++;
            Source source = new Source(extension, lines);
            listener.onAnalysisFound(source);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
