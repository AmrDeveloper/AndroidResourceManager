package arm.search;

public interface OnSearchListener {
    void onSearchStart();
    void onSearchFound(SearchPosition position);
    void onSearchFinish();
}
