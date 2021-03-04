package arm.analysis;

public interface OnAnalysisListener {
    void onAnalysisStart();
    void onAnalysisFound(Source source);
    void onAnalysisFinish();
}
