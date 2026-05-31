package Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import application.Connect;
import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddClientController extends Main {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button add;

    @FXML
    private TextField name;

    @FXML
    private TextField email;

    @FXML
    private TextField phone;

    @FXML
    private TextField address;

    @FXML
    private TextField serviceRequested;

    @FXML
    private TextField description;

    @FXML
    private Label errorLabel;

    @FXML
    void initialize() {
        name.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                email.requestFocus();
            }
        });

        email.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                phone.requestFocus();
            }
        });

        phone.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                serviceRequested.requestFocus();
            }
        });

        address.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                add.requestFocus();
            }
        });

        serviceRequested.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                description.requestFocus();
            }
        });

        description.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                add.requestFocus();
            }
        });

        add.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                add.fire();
            }
        });
    }

    @FXML
    void onAdd(ActionEvent event) throws Exception {
        // Validate input fields
        if (name.getText().isEmpty() || email.getText().isEmpty() || phone.getText().isEmpty() ||
            address.getText().isEmpty() || serviceRequested.getText().isEmpty() || description.getText().isEmpty()) {
            errorLabel.setText("Tous les champs sont obligatoires.");
            errorLabel.setVisible(true);
            return;
        }

        String clientName = name.getText();
        String clientEmail = email.getText();
        String clientPhone = phone.getText();
        String clientAddress = address.getText();
        String clientServiceRequested = serviceRequested.getText();
        String clientDescription = description.getText();

        // Validate phone number (should be numeric)
        try {
            Double.parseDouble(clientPhone);
            errorLabel.setText("");
            errorLabel.setVisible(false);
        } catch (NumberFormatException e) {
            errorLabel.setText("Veuillez entrer un numéro de téléphone valide.");
            errorLabel.setVisible(true);
            return;
        }

        // Add client logic
        if (addClient(clientName, clientEmail, clientPhone, clientAddress, clientServiceRequested, clientDescription)) {
            System.out.println("Client ajouté avec succès !");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Client ajouté avec succès !");
            alert.showAndWait();

            // Close the add client window
            Stage stage = (Stage) add.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Échec de l'ajout du client.");
            alert.showAndWait();
            System.out.println("Échec de l'ajout du client.");
        }
    }

    public boolean addClient(String name, String email, String phone, String address, String serviceRequested, String description) {
        try (Connection connection = Connect.getConnection()) {
            String query = "INSERT INTO clients (name, email, phone, address, service_requested, description) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, serviceRequested);
            preparedStatement.setString(6, description);
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
