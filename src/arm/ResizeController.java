package arm;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    public Button outputPathButton;
    public Button clearSelectedImageButton;
    public Button clearAllImagesAction;
    public Button resizeButton;

    public ListView<File> imagesListView;

    private final List<File> imagesFiles = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageListViewSetup();
    }

    @FXML
    private void onImageDragging(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
        event.consume();
    }

    @FXML
    private void onImageDragDropped(DragEvent event) {
        List<File> currentDropped = event.getDragboard().getFiles();
        for(File imageFile : currentDropped) {
            this.imagesFiles.add(imageFile);
            this.imagesListView.getItems().add(imageFile);
        }
        event.setDropCompleted(true);
        event.consume();
    }

    @FXML
    private void onImagesListClearSelectedItems() {
        List<Integer> selectedImagesIndex = imagesListView.getSelectionModel().getSelectedIndices();
        for (int i = selectedImagesIndex.size() - 1; i > -1; i--) {
            int currentIndex = selectedImagesIndex.get(i);
            this.imagesListView.getItems().remove(currentIndex);
            this.imagesFiles.remove(currentIndex);
        }
    }

    @FXML
    private void onImagesListClearAllItems() {
        this.imagesFiles.clear();
        this.imagesListView.getItems().clear();
    }

    private void imageListViewSetup() {
        this.imagesListView.setOnMouseClicked(this::onImageSelected);
        this.imagesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void onImageSelected(MouseEvent event) {
        ObservableList<File> selectedItems = imagesListView.getSelectionModel().getSelectedItems();
        selectedItems.forEach(file -> {
            Image image = new Image(file.toURI().toString());
            previewImageView.setImage(image);
        });
    }

    private boolean[] getImageSizesOptions() {
        boolean[] sizeOption = new boolean[7];
        sizeOption[0] = idpiCheckBox.isSelected();
        sizeOption[1] = mdpiCheckBox.isSelected();
        sizeOption[2] = tvdpiCheckBox.isSelected();
        sizeOption[3] = hdpiCheckBox.isSelected();
        sizeOption[4] = xhdpiCheckBox.isSelected();
        sizeOption[5] = xxhdpiCheckBox.isSelected();
        sizeOption[5] = xxxhdpiCheckBox.isSelected();
        return sizeOption;
    }
}
