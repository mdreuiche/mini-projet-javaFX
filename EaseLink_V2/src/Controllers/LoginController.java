package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class LoginController {

    @FXML
    private VBox mainVBox;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    public void initialize() {
        // Lier la largeur des labels à un pourcentage de la largeur du conteneur
        welcomeLabel.prefWidthProperty().bind(mainVBox.widthProperty().multiply(0.8)); // 80% de la largeur du VBox
        usernameLabel.prefWidthProperty().bind(mainVBox.widthProperty().multiply(0.8));
        passwordLabel.prefWidthProperty().bind(mainVBox.widthProperty().multiply(0.8));
    }
}
