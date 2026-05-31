package Controllers;

import application.Connect;
import application.Requests;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddRequestController {

    @FXML
    private TextField clientIdField;
    @FXML
    private TextField serviceRequestedField;
    @FXML
    private DatePicker requestedDateField;
    @FXML
    private DatePicker completionDateField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField estimatedPriceField;
    @FXML
    private Button addButton;

    @FXML
    void initialize() {
        addButton.setOnAction(event -> addRequest());
    }

    private void addRequest() {
        String clientId = clientIdField.getText();
        String serviceRequested = serviceRequestedField.getText();
        String requestedDate = requestedDateField.getValue() != null ? requestedDateField.getValue().toString() : null;
        String completionDate = completionDateField.getValue() != null ? completionDateField.getValue().toString() : null;
        String location = locationField.getText();
        String estimatedPrice = estimatedPriceField.getText();

        // Validate input fields
        if (clientId.isEmpty() || serviceRequested.isEmpty() || requestedDate == null || location.isEmpty() || estimatedPrice.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields must be filled!");
            alert.showAndWait();
            return;
        }

        // Logic to add the request to the database
        try {
            String query = "INSERT INTO requests (client_id, service_requested, requested_date, completion_date, address, estimated_price) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection connection = Connect.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, Integer.parseInt(clientId));
                preparedStatement.setString(2, serviceRequested);
                preparedStatement.setString(3, requestedDate);
                preparedStatement.setString(4, completionDate);
                preparedStatement.setString(5, location);
                preparedStatement.setDouble(6, Double.parseDouble(estimatedPrice));
                preparedStatement.executeUpdate();
            }

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Request added successfully!");
            alert.showAndWait();

            // Close the window
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to add request!");
            alert.showAndWait();
        }
    }
}