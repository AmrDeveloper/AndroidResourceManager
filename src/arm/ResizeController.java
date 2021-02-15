package arm;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ResizeController implements Initializable {

    public ImageView previewImageView;
    public TextField pathTextField;

    // Options
    public CheckBox idpiCheckBox;
    public CheckBox mdpiCheckBox;
    public CheckBox tvdpiCheckBox;
    public CheckBox hdpiCheckBox;
    public CheckBox xhdpiCheckBox;
    public CheckBox xxhdpiCheckBox;
    public CheckBox xxxhdpiCheckBox;
    public ComboBox<String> imageTypeComboBox;

    // State
    public ProgressBar stateProgressBar;

    // Actions
    public Button resizeButton;
    public Button outputPathButton;

    public ListView<String> imagesListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
