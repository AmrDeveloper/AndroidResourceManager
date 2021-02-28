package arm.search;

import java.io.File;

public abstract class FileCrawler implements Crawler {

    @Override
    public void startFileSearching(File file, OnCrawlerListener listener) {
        onCrawlerStart();
        if (file.isFile()) listener.onFileFound(file);
        else onDirectorySearch(file, listener);
        onCrawlerFinished();
    }

    @Override
    public void onDirectorySearch(File directory, OnCrawlerListener listener) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) listener.onFileFound(file);
                else onDirectorySearch(file, listener);
            }
        }
    }
}
