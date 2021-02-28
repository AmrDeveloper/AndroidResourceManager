package arm.search;

import arm.utils.DeviceInfo;
import arm.utils.FileUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TextSearchManager extends FileCrawler {

    private final OnSearchListener mSearchListener;
    private static final Set<String> mSupportedExtensions = new HashSet<>(5);

    static {
        mSupportedExtensions.add("java");
        mSupportedExtensions.add("kt");
        mSupportedExtensions.add("cpp");
        mSupportedExtensions.add("xml");
        mSupportedExtensions.add("txt");
    }

    public TextSearchManager(OnSearchListener listener) {
        mSearchListener = listener;
    }

    public void search(File mainFile, String keyword) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(DeviceInfo.CORE_NUM);
        startFileSearching(mainFile, file -> {
            String extensionName = FileUtils.extensionName(file.getName());
            if(mSupportedExtensions.contains(extensionName)) {
                executor.execute(() -> KeywordSearch.searchOnFile(file, keyword, mSearchListener));
            }
        });
        executor.shutdown();
    }

    @Override
    public void onCrawlerStart() {
        mSearchListener.onSearchStart();
    }

    @Override
    public void onCrawlerFinished() {
        mSearchListener.onSearchFinish();
    }
}
