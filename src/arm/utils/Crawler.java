package arm.utils;

import java.io.File;

public interface Crawler {
    void onCrawlerStart();
    void startFileSearching(File file, OnCrawlerListener listener);
    void onDirectorySearch(File directory, OnCrawlerListener listener);
    void onCrawlerFinished();
}