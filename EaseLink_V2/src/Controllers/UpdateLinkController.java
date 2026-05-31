package Controllers;

import application.Connect;
import application.ProviderClient;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class UpdateLinkController {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button saveButton;

    private int providerClientId; // ID de la ligne à modifier
    
    private boolean isSaved = false; // Pour vérifier si les changements ont été sauvegardés
    
    @FXML
    void initialize()
    {    
    	saveButton.setOnAction(event ->{
    		System.out.println("Button clicked");
    		saveDates();
    	});
    }
    public void setProviderClientId(int id) {
        this.providerClientId = id;
    }
    public void setInitialDates(LocalDate startDate, LocalDate endDate) {
        startDatePicker.setValue(startDate);
        endDatePicker.setValue(endDate);
    }

    @FXML
    private void saveDates() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Both start and end dates are required.");
            return;
        }

        if (startDate.isAfter(endDate)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Start date cannot be after end date.");
            return;
        }

        // Update in database
        try (Connection connection = Connect.getConnection()) {
            String query = "UPDATE Provider_Client SET start_date = ?, end_date = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, startDate.toString());
            statement.setString(2, endDate.toString());
            statement.setInt(3, providerClientId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Dates updated successfully.");
                closeWindow();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update dates.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
    public void handleWindowCloseRequest(WindowEvent event) {
        if (!isSaved) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, 
                "Les modifications ne sont pas sauvegardées. Voulez-vous quitter sans enregistrer ?", 
                ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.NO) {
                    event.consume(); // Annule la fermeture si l'utilisateur clique sur "No"
                }
            });
        }
    }
    
    /*-------------------Dreuiche's Task ends here------------*/
}
