package arm.analysis;

import arm.utils.DeviceInfo;
import arm.utils.FileCrawler;
import arm.utils.FileUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class AnalysisManager extends FileCrawler {

    private final OnAnalysisListener mAnalysisListener;

    private static final Set<String> mSupportedExtensions = new HashSet<>();
    static {
        mSupportedExtensions.add("java");
        mSupportedExtensions.add("kt");
        mSupportedExtensions.add("xml");
    }

    public AnalysisManager(OnAnalysisListener listener) {
        mAnalysisListener = listener;
    }

    public void search(File projectDirectory) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(DeviceInfo.CORE_NUM);
        startFileSearching(projectDirectory, file -> {
            String extensionName = FileUtils.extensionName(file.getName());
            if(mSupportedExtensions.contains(extensionName)) {
                if(extensionName.equals("xml")) {
                    if(file.getParent().endsWith("res\\layout"))
                    executor.execute(() -> SourceAnalysis.analysisSourceFile(file, extensionName, mAnalysisListener));
                }
                else {
                    executor.execute(() -> SourceAnalysis.analysisSourceFile(file, extensionName, mAnalysisListener));
                }
            }
        });
        executor.shutdown();
    }

    @Override
    public void onCrawlerStart() {
        mAnalysisListener.onAnalysisStart();
    }

    @Override
    public void onCrawlerFinished() {
        mAnalysisListener.onAnalysisFinish();
    }
}
