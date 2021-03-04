package arm.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private ImageView resizeImageView;
    @FXML private ImageView searchImageView;
    @FXML private ImageView colorImageView;
    @FXML private ImageView dimensionImageView;
    @FXML private ImageView analysisImageView;
    @FXML private BorderPane viewBorderPane;

    private static ServiceName mServiceName = ServiceName.RESIZING;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadViewIntoBorderPane("/arm/resize/resize_view", viewBorderPane);
    }

    @FXML
    private void openResizeView() {
        if(mServiceName != ServiceName.RESIZING) {
            loadViewIntoBorderPane("/arm/resize/resize_view", viewBorderPane);
            mServiceName = ServiceName.RESIZING;
        }
    }

    @FXML
    private void openSearchView() {
        if(mServiceName != ServiceName.SEARCHING) {
            loadViewIntoBorderPane("/arm/search/search_view", viewBorderPane);
            mServiceName = ServiceName.SEARCHING;
        }
    }

    @FXML
    private void openColorSearchView() {
        if(mServiceName != ServiceName.COLOR_ANALYSIS) {
            loadViewIntoBorderPane("/arm/color/color_view", viewBorderPane);
            mServiceName = ServiceName.COLOR_ANALYSIS;
        }
    }

    @FXML
    private void openDimensionSearchView() {
        if(mServiceName != ServiceName.DIMENS_ANALYSIS) {
            loadViewIntoBorderPane("/arm/dimens/dimen_view", viewBorderPane);
            mServiceName = ServiceName.DIMENS_ANALYSIS;
        }
    }

    @FXML
    private void openAnalysisSearchView() {
        if(mServiceName != ServiceName.DIMENS_ANALYSIS) {
            loadViewIntoBorderPane("/arm/analysis/analysis_view", viewBorderPane);
            mServiceName = ServiceName.DIMENS_ANALYSIS;
        }
    }

    private void loadViewIntoBorderPane(String viewName, BorderPane pane)  {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(viewName + ".fxml"));
        } catch (IOException e) {
            System.err.println("Invalid View Loader : " + e.getMessage());
        }
        pane.setCenter(root);
    }
}