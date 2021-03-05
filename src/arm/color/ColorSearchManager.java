package arm.color;

import arm.utils.DeviceInfo;
import arm.utils.FileCrawler;
import arm.utils.FileUtils;
import arm.utils.OnSearchListener;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ColorSearchManager extends FileCrawler {

    private final OnSearchListener mSearchListener;

    public ColorSearchManager(OnSearchListener listener) {
        mSearchListener = listener;
    }

    public void search(File mainFile) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(DeviceInfo.CORE_NUM);
        startFileSearching(mainFile, file -> {
            String extensionName = FileUtils.extensionName(file.getName());
            if(extensionName.equals("xml") && file.getParent().endsWith("res\\layout")) {
                executor.execute(() -> ColorSearch.searchOnFile(file, mSearchListener));
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
