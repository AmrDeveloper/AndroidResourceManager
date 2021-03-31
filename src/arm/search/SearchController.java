package arm.search;

import arm.utils.AlertManager;
import arm.utils.OnSearchListener;
import arm.utils.SearchPosition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable, OnSearchListener {

    @FXML private Label matchesCountLabel;
    @FXML private Label searchStateLabel;
    @FXML private TextField projectPathTextField;
    @FXML private TextField searchKeywordTextField;

    @FXML private Button findPathButton;
    @FXML private Button searchButton;

    @FXML private ListView<KeywordPosition> searchResultListView;

    @FXML private BorderPane searchViewLayout;

    private int mSearchResultCounter = 0;
    private final TextSearchManager mTextSearchManager = new TextSearchManager(this);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setViewsTooltip();
        resultListViewSetup();
    }

    private void setViewsTooltip() {
        matchesCountLabel.setTooltip(new Tooltip("Number of matches search"));
        searchStateLabel.setTooltip(new Tooltip("Search State Label"));

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
        Stage currentStage = (Stage) searchViewLayout.getScene().getWindow();
        File output = directoryChooser.showDialog(currentStage);
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

        searchResultListView.getItems().clear();

        new Thread(() -> mTextSearchManager.search(path, keyword)).start();
    }

    private void resultListViewSetup() {
        searchResultListView.setOnMouseClicked(this::onSearchResultSelected);
        searchResultListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        searchResultListView.setPlaceholder(new Label("List of your search result :)"));
    }

    private void onSearchResultSelected(MouseEvent event) {
        SearchPosition position = searchResultListView.getSelectionModel().getSelectedItem();
        if (position != null) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(position.getFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSearchStart() {
        Platform.runLater(() -> {
            searchStateLabel.setText("Searching...");
            matchesCountLabel.setText("0");
        });
    }

    @Override
    public void onSearchFound(SearchPosition position) {
        Platform.runLater(() -> {
            searchResultListView.getItems().add((KeywordPosition) position);
            mSearchResultCounter++;
            matchesCountLabel.setText(String.valueOf(mSearchResultCounter));
        });
    }

    @Override
    public void onSearchFinish() {
        Platform.runLater(() -> searchStateLabel.setText("Done"));
    }
}
