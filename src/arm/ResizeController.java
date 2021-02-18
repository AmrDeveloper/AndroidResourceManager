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
import java.util.*;

public class ResizeController implements Initializable {

    // Preview Views
    @FXML private ImageView previewImageView;

    // Options
    @FXML private ComboBox<String> imageTypeComboBox;

    // State
    @FXML private ProgressBar stateProgressBar;

    // Image Sizes Views
    @FXML private TextField heightTextField;
    @FXML private TextField widthTextField;
    @FXML private Button addSizeButton;
    @FXML private Button clearSizeButton;
    @FXML private ListView<ImageSize> imageSizesListView;

    // Image List Views
    @FXML private TextField pathTextField;
    @FXML private Button outputPathButton;
    @FXML private Button clearSelectedImageButton;
    @FXML private Button clearAllImagesAction;
    @FXML private Button resizeButton;
    @FXML private ListView<File> imagesListView;

    private final List<File> mImagesFilesList = new ArrayList<>();
    private final Set<ImageSize> mImagesSizeSet = new HashSet<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageListViewSetup();
        this.imageSizesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
            this.mImagesFilesList.add(imageFile);
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
            mImagesFilesList.remove(currentIndex);
        }
    }

    @FXML
    private void onImagesListClearAllItems() {
        mImagesFilesList.clear();
        this.imagesListView.getItems().clear();
    }

    @FXML
    private void addImageSize() {
        String height = heightTextField.getText();
        String width = widthTextField.getText();

        if(height.isEmpty() || width.isEmpty()) return;

        if(!ValidationUtils.isInteger(height)
                || !ValidationUtils.isInteger(width))
            return;

        int iHeight = Integer.parseInt(height);
        int iWidth = Integer.parseInt(width);
        ImageSize imageSize = new ImageSize(iHeight, iWidth);

        boolean isUnique = mImagesSizeSet.add(imageSize);
        if(isUnique) {
            imageSizesListView.getItems().add(imageSize);
            heightTextField.clear();
            widthTextField.clear();
        }
    }

    @FXML
    private void clearSelectedImageSize() {
        ObservableList<ImageSize> selectedImageSizes = imageSizesListView.getSelectionModel().getSelectedItems();
        mImagesSizeSet.removeAll(selectedImageSizes);
        imageSizesListView.getItems().removeAll(selectedImageSizes);
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
}
