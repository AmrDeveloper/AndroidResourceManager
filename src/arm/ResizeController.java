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
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.*;

public class ResizeController implements Initializable {

    // Preview Views
    @FXML private ImageView previewImageView;

    // Options
    @FXML private ComboBox<ImageType> imageTypeComboBox;

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

    private final ResizeManager mResizeManager = ResizeManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageListViewSetup();
        sizeListViewSetup();
        setViewsTooltip();

        imageTypeComboBox.getItems().addAll(ImageType.values());
        imageTypeComboBox.getSelectionModel().select(0);
    }

    private void setViewsTooltip() {
        // Sizes Tooltip
        addSizeButton.setTooltip(new Tooltip("Add New Size"));
        clearSizeButton.setTooltip(new Tooltip("Clear Selected Size"));
        imageSizesListView.setTooltip(new Tooltip("Image Sizes"));

        imageTypeComboBox.setTooltip(new Tooltip("Select Image Type"));
        stateProgressBar.setTooltip(new Tooltip("State ProgressBar"));

        // Images Tooltip
        pathTextField.setTooltip(new Tooltip("Output Path"));
        outputPathButton.setTooltip(new Tooltip("Set output Path"));
        clearSelectedImageButton.setTooltip(new Tooltip("Clear Selected Images"));
        clearAllImagesAction.setTooltip(new Tooltip("Clear All Images"));
        resizeButton.setTooltip(new Tooltip("Start Resizing"));
        imagesListView.setTooltip(new Tooltip("Image List"));
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
    private void resizeButtonAction() {
        String outputPath = pathTextField.getText();
        if(outputPath.isEmpty()) return;

        File outputPathFile = new File(outputPath);
        if(!outputPathFile.exists()) {
            AlertManager.showErrorDialog("Resize Action",
                    "Output Path",
                    "Output Path not exists.");
            return;
        }

        if(!outputPathFile.isDirectory()) {
            AlertManager.showErrorDialog("Resize Action",
                    "Output Path",
                    "Output Path must be Directory.");
            return;
        }

        ImageType imageType = imageTypeComboBox.getSelectionModel().getSelectedItem();
        ResizeOrder resizeOrder = new ResizeOrder(mImagesFilesList, mImagesSizeSet, imageType, outputPathFile);
        mResizeManager.resize(resizeOrder);
    }

    @FXML
    private void findOutputDirectoryPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Output Directory Path");
        File output = directoryChooser.showDialog(null);
        if(output != null) {
            pathTextField.setText(output.getPath());
        }
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

        if(height.isEmpty() || width.isEmpty()) {
            AlertManager.showErrorDialog("Add Size Action",
                    "Image Size",
                    "Height and Width can't be empty.");
            return;
        }

        if(!ValidationUtils.isInteger(height)
                || !ValidationUtils.isInteger(width)) {
            AlertManager.showErrorDialog("Add Size Action",
                    "Image Size",
                    "Height and Width must be integers.");
            return;
        }

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
        this.imagesListView.setPlaceholder(new Label("Drop you images here :)"));
    }

    private void sizeListViewSetup() {
        this.imageSizesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Add default 7 sizes for Android Development
        this.mImagesSizeSet.add(new ImageSize(36, 36, "ldpi"));
        this.mImagesSizeSet.add(new ImageSize(48, 48, "mdpi"));
        this.mImagesSizeSet.add(new ImageSize(64, 64, "tvdpi"));
        this.mImagesSizeSet.add(new ImageSize(72, 72, "hdpi"));
        this.mImagesSizeSet.add(new ImageSize(96, 96, "xhdpi"));
        this.mImagesSizeSet.add(new ImageSize(144, 144, "xxhdpi"));
        this.mImagesSizeSet.add(new ImageSize(192, 192, "xxxhdpi"));
        this.imageSizesListView.getItems().addAll(mImagesSizeSet);
    }

    private void onImageSelected(MouseEvent event) {
        ObservableList<File> selectedItems = imagesListView.getSelectionModel().getSelectedItems();
        selectedItems.forEach(file -> {
            Image image = new Image(file.toURI().toString());
            previewImageView.setImage(image);
        });
    }
}
