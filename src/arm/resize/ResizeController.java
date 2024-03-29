package arm.resize;

import arm.utils.FileUtils;
import arm.utils.ValidationUtils;
import arm.utils.AlertManager;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.*;

public class ResizeController implements Initializable, OnProgressListener {

    // Preview Views
    @FXML private ImageView previewImageView;
    @FXML private Label previewImageSize;
    @FXML private Label previewImageDimensions;

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
    @FXML private BorderPane resizeViewLayout;

    private final Set<File> mImagesFilesSet = new HashSet<>();
    private final Set<ImageSize> mImagesSizeSet = new HashSet<>();

    private final ResizeManager mResizeManager = ResizeManager.getInstance();
    private final static Image mDefaultPreviewImage = new Image("resources/icon/preview_128.png");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageListViewSetup();
        sizeListViewSetup();
        setViewsTooltip();
        setPreviewImageDefaultValues();
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
            List<File> files = event.getDragboard().getFiles();
            boolean isValidFileType = true;
            for(File file : files) {
                if(file.isFile() && !FileUtils.isImageExtension(file.getName())) isValidFileType = false;
            }
            if(isValidFileType) event.acceptTransferModes(TransferMode.ANY);
        }
        event.consume();
    }

    @FXML
    private void onImageDragDropped(DragEvent event) {
        List<File> currentDropped = event.getDragboard().getFiles();
        for(File imageFile : currentDropped) {
            if(imageFile.isFile()) {
                if(mImagesFilesSet.add(imageFile)) {
                    imagesListView.getItems().add(imageFile);
                }
            } else {
                crawlImagesFromDirectory(imageFile);
            }
        }
        event.setDropCompleted(true);
        event.consume();
    }

    private void crawlImagesFromDirectory(File directory) {
        for(File file : Objects.requireNonNull(directory.listFiles())) {
            if(file.isDirectory()) crawlImagesFromDirectory(file);
            else if(file.isFile() && FileUtils.isImageExtension(file.getName())) {
                this.mImagesFilesSet.add(file);
                this.imagesListView.getItems().add(file);
            }
        }
    }

    @FXML
    private void resizeButtonAction() {
        String outputPath = pathTextField.getText();
        if(outputPath.isEmpty()) {
            AlertManager.showErrorDialog("Resize Action",
                    "Output Path",
                    "Output Path Can't be empty.");
            return;
        }

        File outputPathFile = new File(outputPath);
        if(!outputPathFile.exists() && !outputPathFile.canWrite()) {
            AlertManager.showErrorDialog("Resize Action",
                    "Output Path",
                    "Output Path not exists or can't write.");
            return;
        }

        if(!outputPathFile.isDirectory()) {
            AlertManager.showErrorDialog("Resize Action",
                    "Output Path",
                    "Output Path must be Directory.");
            return;
        }

        if (mImagesSizeSet.isEmpty()) {
            AlertManager.showErrorDialog("Resize Action",
                    "Number of sizes",
                    "You must have at last one size.");
            return;
        }

        ImageType imageType = imageTypeComboBox.getSelectionModel().getSelectedItem();
        ResizeOrder resizeOrder = new ResizeOrder(mImagesFilesSet, mImagesSizeSet, imageType, outputPathFile);
        new Thread(() -> mResizeManager.resize(resizeOrder, ResizeController.this)).start();
    }

    @FXML
    private void findOutputDirectoryPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Output Directory Path");
        Stage currentStage = (Stage) resizeViewLayout.getScene().getWindow();
        File output = directoryChooser.showDialog(currentStage);
        if(output != null) {
            pathTextField.setText(output.getPath());
        }
    }

    @FXML
    private void onImagesListClearSelectedItems() {
        List<Integer> selectedImagesIndex = imagesListView.getSelectionModel().getSelectedIndices();
        for (int i = selectedImagesIndex.size() - 1; i > -1; i--) {
            int currentIndex = selectedImagesIndex.get(i);
            File removedImage = imagesListView.getItems().remove(currentIndex);
            mImagesFilesSet.remove(removedImage);
        }

        if(imagesListView.getItems().size() == 0) {
            setPreviewImageDefaultValues();
            return;
        }

        updatePreviewWithSelectedImage();
    }

    @FXML
    private void onImagesListClearAllItems() {
        mImagesFilesSet.clear();
        this.imagesListView.getItems().clear();
        setPreviewImageDefaultValues();
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
        updatePreviewWithSelectedImage();
    }

    private void setPreviewImageDefaultValues() {
        previewImageView.setImage(mDefaultPreviewImage);
        previewImageSize.setText("0 bytes");
        previewImageDimensions.setText("0x0");
    }

    private void updatePreviewWithSelectedImage() {
        File selectedItem = imagesListView.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            Image image = new Image(selectedItem.toURI().toString());
            previewImageSize.setText(FileUtils.getFormattedFileSize(selectedItem));
            previewImageDimensions.setText(image.getHeight() + "x" + image.getWidth());
            previewImageView.setImage(image);
        } else {
            setPreviewImageDefaultValues();
        }
    }

    @Override
    public void onProcessChange(float progress) {
        Platform.runLater(() -> stateProgressBar.setProgress(progress));
    }
}
