package arm.analysis;

import arm.utils.AlertManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AnalysisController implements Initializable, OnAnalysisListener {

    @FXML private Label numberOfFilesLabel;
    @FXML private Label numberOfJavaFilesLabel;
    @FXML private Label numberOfKotlinFilesLabel;
    @FXML private Label numberOfXmlFilesLabel;
    @FXML private Label numberOfJavaLinesLabel;
    @FXML private Label numberOfKotlinLinesLabel;
    @FXML private Label numberOfXmlLinesLabel;
    @FXML private Label numberOfLinesLabel;
    @FXML private Label analysisStateLabel;

    @FXML private TextField projectPathTextField;
    @FXML private Button findPathButton;
    @FXML private Button analysisButton;

    private int javaLinesCount = 0;
    private int kotlinLinesCount = 0;
    private int xmlLinesCount = 0;
    private int allLinesCount = 0;
    private int javaFilesCount = 0;
    private int kotlinFilesCount = 0;
    private int xmlFilesCount = 0;
    private int allFilesCount = 0;

    private final AnalysisManager mAnalysisManager = new AnalysisManager(this);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setViewsTooltip();
    }

    private void setViewsTooltip() {
        findPathButton.setTooltip(new Tooltip("Find button to select project path"));
        analysisButton.setTooltip(new Tooltip("Start Project Analysing"));
    }

    private void resetAllCounters() {
        javaLinesCount = 0;
        kotlinLinesCount = 0;
        xmlLinesCount = 0;
        allLinesCount = 0;
        javaFilesCount = 0;
        kotlinFilesCount = 0;
        xmlFilesCount = 0;
        allFilesCount = 0;
    }

    private void resetAllCountersUI() {
        Platform.runLater(() -> {
            numberOfJavaFilesLabel.setText(String.valueOf(javaFilesCount));
            numberOfJavaLinesLabel.setText(String.valueOf(javaLinesCount));

            numberOfKotlinFilesLabel.setText(String.valueOf(kotlinFilesCount));
            numberOfKotlinLinesLabel.setText(String.valueOf(kotlinLinesCount));

            numberOfXmlFilesLabel.setText(String.valueOf(xmlFilesCount));
            numberOfXmlLinesLabel.setText(String.valueOf(xmlLinesCount));

            numberOfFilesLabel.setText(String.valueOf(allFilesCount));
            numberOfLinesLabel.setText(String.valueOf(allLinesCount));
        });
    }

    @FXML
    private void startDirectoryAnalysis() {
        String projectPath = projectPathTextField.getText();
        File path = new File(projectPath);
        if(!path.exists()) {
            AlertManager.showErrorDialog("Search Action",
                    "Project Path",
                    "Project or File path don't exists.");
            return;
        }

        resetAllCounters();
        resetAllCountersUI();
        new Thread(() -> mAnalysisManager.search(path)).start();
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

    @Override
    public void onAnalysisStart() {
        Platform.runLater(() -> analysisStateLabel.setText("Analysing..."));
    }

    @Override
    public void onAnalysisFound(Source source) {
        String extension = source.getType();
        allLinesCount += source.getLinesNum();
        allFilesCount++;

        switch (extension) {
            case "java": {
                javaLinesCount += source.getLinesNum();
                javaFilesCount++;
                Platform.runLater(() -> {
                    numberOfJavaFilesLabel.setText(String.valueOf(javaFilesCount));
                    numberOfJavaLinesLabel.setText(String.valueOf(javaLinesCount));
                });
                break;
            }
            case "kt": {
                kotlinLinesCount += source.getLinesNum();
                kotlinFilesCount++;
                Platform.runLater(() -> {
                    numberOfKotlinFilesLabel.setText(String.valueOf(kotlinFilesCount));
                    numberOfKotlinLinesLabel.setText(String.valueOf(kotlinLinesCount));
                });
                break;
            }
            case "xml": {
                xmlLinesCount += source.getLinesNum();
                xmlFilesCount++;
                Platform.runLater(() -> {
                    numberOfXmlFilesLabel.setText(String.valueOf(xmlFilesCount));
                    numberOfXmlLinesLabel.setText(String.valueOf(xmlLinesCount));
                });
                break;
            }
        }

        Platform.runLater(() -> {
            numberOfFilesLabel.setText(String.valueOf(allFilesCount));
            numberOfLinesLabel.setText(String.valueOf(allLinesCount));
        });
    }

    @Override
    public void onAnalysisFinish() {
        Platform.runLater(() -> analysisStateLabel.setText("Done"));
    }
}
