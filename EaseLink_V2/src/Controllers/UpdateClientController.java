package Controllers;

import application.Clients;
import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class UpdateClientController extends Main {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField serviceRequestedField;
    @FXML
    private TextField descriptionField;

    @FXML
    private Button saveButton;
    @FXML
    private Label errorLabel;

    private Clients client;

    private boolean isSaved = false; // To verify if the changes have been saved

    @FXML
    void initialize() {
        // Handle "Enter" key events for navigation
        nameField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                emailField.requestFocus();
            }
        });

        emailField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                phoneField.requestFocus();
            }
        });

        phoneField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                serviceRequestedField.requestFocus();
            }
        });

        serviceRequestedField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                descriptionField.requestFocus();
            }
        });

        descriptionField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                addressField.requestFocus();
            }
        });

        addressField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                saveButton.requestFocus();
            }
        });

        // Activate the save button with the "Enter" key
        saveButton.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                saveButton.fire(); // Simulate a button click
            }
        });

        // Save button action
        saveButton.setOnAction(event -> {
            saveChanges();
        });
    }

    public void setClient(Clients client) {
        this.client = client;
        nameField.setText(client.getName());
        emailField.setText(client.getEmail());
        phoneField.setText(client.getPhone());
        addressField.setText(client.getAddress());
        serviceRequestedField.setText(client.getServiceRequested());
        descriptionField.setText(client.getDescription());
    }

    @FXML
    private void saveChanges() {
        // Check for empty fields
        if (nameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty() || addressField.getText().isEmpty() || serviceRequestedField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
            errorLabel.setText("Tous les champs sont obligatoires.");
            errorLabel.setVisible(true);
            return; // Stop the method if any field is empty
        }

        // Update the client object with new values
        client.setName(nameField.getText());
        client.setEmail(emailField.getText());
        client.setPhone(phoneField.getText());
        client.setAddress(addressField.getText());
        client.setServiceRequested(serviceRequestedField.getText());
        client.setDescription(descriptionField.getText());

        if (updateClient(client)) {
            System.out.println("Client mis à jour avec succès !");
            isSaved = true;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Client Updated!");
            alert.showAndWait();
            Stage stage = (Stage) saveButton.getScene().getWindow(); // Get the stage from the save button
            stage.close();
        } else {
            System.out.println("Échec de la mise à jour.");
        }
    }

    public void handleWindowCloseRequest(WindowEvent event) {
        if (!isSaved) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Les modifications ne sont pas sauvegardées. Voulez-vous quitter sans enregistrer ?",
                    ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.NO) {
                    event.consume(); // Cancel the window close if the user clicks "No"
                }
            });
        }
    }

    public boolean updateClient(Clients client) {
        try {
            String query = "UPDATE clients SET name = ?, email = ?, phone = ?, address = ?, service_requested = ?, description = ? WHERE id = ?";
            try (var connection = application.Connect.getConnection();
                 var preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, client.getName());
                preparedStatement.setString(2, client.getEmail());
                preparedStatement.setString(3, client.getPhone());
                preparedStatement.setString(4, client.getAddress());
                preparedStatement.setString(5, client.getServiceRequested());
                preparedStatement.setString(6, client.getDescription());
                preparedStatement.setInt(7, client.getId());

                int rowsUpdated = preparedStatement.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
