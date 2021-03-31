package arm.color;

import arm.utils.AlertManager;
import arm.utils.OnSearchListener;
import arm.utils.SearchPosition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ColorController implements Initializable, OnSearchListener {

    @FXML private Label searchStateLabel;
    @FXML private Label matchesCountLabel;
    @FXML private Label colorValueLabel;
    @FXML private TextField layoutsPathTextField;

    @FXML private Button layoutPathButton;
    @FXML private Button colorSearchButton;

    @FXML private ListView<ColorPosition> colorListView;
    @FXML private BorderPane colorViewLayout;

    private int mSearchResultCounter = 0;
    private final ColorSearchManager mColorSearchManager = new ColorSearchManager(this);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setViewsTooltip();
        resultListViewSetup();
    }

    private void setViewsTooltip() {
        searchStateLabel.setTooltip(new Tooltip("State of search"));
        matchesCountLabel.setTooltip(new Tooltip("Number of matches search"));
        colorValueLabel.setTooltip(new Tooltip("The value of current color"));

        layoutPathButton.setTooltip(new Tooltip("Find button to select project path"));
        colorSearchButton.setTooltip(new Tooltip("Search button to start search for colors"));

        colorListView.setTooltip(new Tooltip("List of color result"));
    }

    private void resultListViewSetup() {
        colorListView.setOnMouseClicked(this::onSearchResultSelected);
        colorListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        colorListView.setPlaceholder(new Label("List of your Colors search result :)"));
    }

    private void onSearchResultSelected(MouseEvent event) {
        ColorPosition position = colorListView.getSelectionModel().getSelectedItem();
        if(position != null) {
            colorValueLabel.setText(position.getValue());
        }
    }

    @FXML
    private void findProjectPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Output Directory Path");
        Stage currentStage = (Stage) colorViewLayout.getScene().getWindow();
        File output = directoryChooser.showDialog(currentStage);
        if(output != null) {
            layoutsPathTextField.setText(output.getPath());
        }
    }

    @FXML
    private void startColorAnalysis() {
        String layoutsPath = layoutsPathTextField.getText();
        if(layoutsPath.isEmpty()) {
            AlertManager.showErrorDialog("Search Action",
                    "Project Path",
                    "Project or File path is empty.");
            return;
        }

        File layoutsDirectory = new File(layoutsPath);
        if(!layoutsDirectory.exists()) {
            AlertManager.showErrorDialog("Search Action",
                    "Project Path",
                    "Project or File path don't exists.");
            return;
        }

        colorListView.getItems().clear();
        new Thread(() -> mColorSearchManager.search(layoutsDirectory)).start();
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
            colorListView.getItems().add((ColorPosition) position);
            mSearchResultCounter++;
            matchesCountLabel.setText(String.valueOf(mSearchResultCounter));
        });
    }

    @Override
    public void onSearchFinish() {
        Platform.runLater(() -> searchStateLabel.setText("Done"));
    }
}
