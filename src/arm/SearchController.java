package arm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

    @FXML private Label matchesCountLabel;
    @FXML private TextField projectPathTextField;
    @FXML private TextField searchKeywordTextField;

    @FXML private Button findPathButton;
    @FXML private Button searchButton;

    @FXML private ListView<String> searchResultListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
