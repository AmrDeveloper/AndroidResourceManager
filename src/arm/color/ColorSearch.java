package arm.color;

import arm.utils.OnSearchListener;
import arm.utils.SearchPosition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ColorSearch {

    public static void searchOnFile(File file, OnSearchListener listener) {

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            int lineNumber = 0;
            while ((line = bufferedReader.readLine()) != null) {
                int startIndex = line.indexOf("#");
                if (startIndex != -1) {
                    int index = startIndex + 1;
                    int lineLen = line.length() - 1;
                    while ((index < lineLen) && isColorChar(line.charAt(index))) {
                        index++;
                    }
                    if((index - startIndex) < 4) continue;
                    String value = line.substring(startIndex, index);
                    SearchPosition position = new ColorPosition(file, value, lineNumber, startIndex, index);
                    listener.onSearchFound(position);
                }
                lineNumber++;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    private static boolean isColorChar(char c) {
        if(c >= '0' && c <= '9') return true;
        if(c >= 'a' && c <= 'f') return true;
        return (c >= 'A' && c <= 'F');
    }
}
