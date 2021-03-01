package arm.dimens;

import arm.utils.OnSearchListener;
import arm.utils.SearchPosition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class DimensionSearch {

    private static final Pattern dimensionPattern = Pattern.compile("[0-9]+(dp|sp)$");

    public static void searchOnFile(File file, OnSearchListener listener) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            int lineNumber = 0;
            while ((line = bufferedReader.readLine()) != null) {
                int firstQuoteIndex = line.indexOf('\"');
                if(firstQuoteIndex != -1) {
                    int lastQuoteIndex = line.lastIndexOf('\"');
                    if((lastQuoteIndex != -1) && (lastQuoteIndex != firstQuoteIndex)) {
                        String value = line.substring(firstQuoteIndex + 1, lastQuoteIndex);
                        if(dimensionPattern.matcher(value).matches()) {
                            SearchPosition position =
                                    new DimensionPosition(file, value, lineNumber, firstQuoteIndex, lastQuoteIndex);
                            listener.onSearchFound(position);
                        }
                    }
                }
                lineNumber++;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
