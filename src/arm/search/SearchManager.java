package arm.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SearchManager {

    private volatile static SearchManager mInstance;
    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);

    public static SearchManager getInstance() {
        if (mInstance == null) {
            mInstance = new SearchManager();
        }
        return mInstance;
    }

    public void search(File mainFile, String keyword, OnSearchListener listener) {
        if (mainFile.isDirectory()) {
            searchOnDirectory(mainFile, keyword, listener);
            executor.shutdown();
        } else searchOnFile(mainFile, keyword, listener);
    }

    private void searchOnDirectory(File directory, String keyword, OnSearchListener listener) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String name = file.getName();
                    if(name.endsWith("kt") || name.endsWith("java") || name.endsWith("xml")) {
                        executor.execute(() -> searchOnFile(file, keyword, listener));
                    }
                }
                else searchOnDirectory(file, keyword, listener);
            }
        }
    }

    private void searchOnFile(File file, String keyword, OnSearchListener listener) {
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
