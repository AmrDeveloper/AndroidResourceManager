package arm.dimens;

import arm.color.ColorPosition;
import arm.utils.AlertManager;
import arm.utils.OnSearchListener;
import arm.utils.SearchPosition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class DimensionController implements Initializable, OnSearchListener {

    @FXML private Label searchStateLabel;
    @FXML private Label matchesCountLabel;
    @FXML private Label colorValueLabel;
    @FXML private TextField layoutsPathTextField;

    @FXML private Button layoutPathButton;
    @FXML private Button dimensionSearchButton;

    @FXML private ListView<DimensionPosition> dimensionListView;

    private int mSearchResultCounter = 0;
    private final DimensionSearchManager mDimensionSearchManager = new DimensionSearchManager(this);

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
        dimensionSearchButton.setTooltip(new Tooltip("Search button to start search for colors"));

        dimensionListView.setTooltip(new Tooltip("List of dimensions result"));
    }

    private void resultListViewSetup() {
        dimensionListView.setOnMouseClicked(this::onSearchResultSelected);
        dimensionListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        dimensionListView.setPlaceholder(new Label("List of your dimensions search result :)"));
    }

    private void onSearchResultSelected(MouseEvent event) {
        DimensionPosition position = dimensionListView.getSelectionModel().getSelectedItem();
        if(position != null) {
            colorValueLabel.setText(position.getValue());
        }
    }

    @FXML
    private void findProjectPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Output Directory Path");
        File output = directoryChooser.showDialog(null);
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

        dimensionListView.getItems().clear();
        new Thread(() -> mDimensionSearchManager.search(layoutsDirectory)).start();
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
            dimensionListView.getItems().add((DimensionPosition) position);
            mSearchResultCounter++;
            matchesCountLabel.setText(String.valueOf(mSearchResultCounter));
        });
    }

    @Override
    public void onSearchFinish() {
        Platform.runLater(() -> searchStateLabel.setText("Done"));
    }
}
