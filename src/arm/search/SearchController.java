package arm.search;

import arm.utils.AlertManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable, OnSearchListener {

    @FXML private Label matchesCountLabel;
    @FXML private TextField projectPathTextField;
    @FXML private TextField searchKeywordTextField;

    @FXML private Button findPathButton;
    @FXML private Button searchButton;

    @FXML private ListView<SearchPosition> searchResultListView;

    private int mSearchResultCounter = 0;
    private static final SearchManager mSearchManager = SearchManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setViewsTooltip();
        resultListViewSetup();
    }

    private void setViewsTooltip() {
        matchesCountLabel.setTooltip(new Tooltip("Number of matches search"));
        projectPathTextField.setTooltip(new Tooltip("Project Path to search in it"));
        searchKeywordTextField.setTooltip(new Tooltip("Search keyword to search with it"));

        findPathButton.setTooltip(new Tooltip("Find button to select project path"));
        searchButton.setTooltip(new Tooltip("Search button to start searching with keyword"));

        searchResultListView.setTooltip(new Tooltip("List of search Result"));
    }

    @FXML
    private void findProjectPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Output Directory Path");
        File output = directoryChooser.showDialog(null);
        if(output != null) {
            projectPathTextField.setText(output.getPath());
        }
    }

    @FXML
    private void keywordSearching() {
        mSearchResultCounter = 0;
        String projectPath = projectPathTextField.getText();
        File path = new File(projectPath);
        if(!path.exists()) {
            AlertManager.showErrorDialog("Search Action",
                    "Project Path",
                    "Project or File path don't exists.");
            return;
        }

        String keyword = searchKeywordTextField.getText();
        if(keyword.isEmpty()) {
            AlertManager.showErrorDialog("Search Action",
                    "Search Keyword",
                    "Search keyword mustn't be empty.");
            return;
        }

        new Thread(() -> mSearchManager.search(path, keyword, SearchController.this)).start();
    }

    private void resultListViewSetup() {
        searchResultListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        searchResultListView.setPlaceholder(new Label("List of your search result :)"));
    }

    @Override
    public void onSearchFound(SearchPosition position) {
        Platform.runLater(() -> {
            searchResultListView.getItems().add(position);
            mSearchResultCounter++;
            matchesCountLabel.setText(String.valueOf(mSearchResultCounter));
        });
    }
}
