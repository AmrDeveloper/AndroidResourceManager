package arm.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class KeywordSearch {

    public static void searchOnFile(File file, String keyword, OnSearchListener listener) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            int lineNumber = 0;
            while ((line = bufferedReader.readLine()) != null) {
                int startIndex = line.indexOf(keyword);
                if (startIndex != -1) {
                    int end = startIndex + keyword.length();
                    SearchPosition position = new SearchPosition(file, lineNumber, startIndex, end);
                    listener.onSearchFound(position);
                }
                lineNumber++;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
