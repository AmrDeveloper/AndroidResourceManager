package arm.utils;

public interface OnSearchListener {
    void onSearchStart();
    void onSearchFound(SearchPosition position);
    void onSearchFinish();
}
