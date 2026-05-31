package Controllers;

import application.Clients;
import application.Connect;
import application.Requests;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RequestsController {

    @FXML
    private TableView<Requests> requestsTable;
    @FXML
    private TableColumn<Requests, Integer> requestIdColumn;
    @FXML
    private TableColumn<Requests, Integer> clientIdColumn;
    @FXML
    private TableColumn<Requests, String> serviceRequestedColumn;
    @FXML
    private TableColumn<Requests, String> requestedDateColumn;
    @FXML
    private TableColumn<Requests, String> completionDateColumn;
    @FXML
    private TableColumn<Requests, String> locationColumn;
    @FXML
    private TableColumn<Requests, Double> estimatedPriceColumn;

    private ObservableList<Requests> requestsData = FXCollections.observableArrayList();

    @FXML
    private Button addButton, deleteButton;
    
    @FXML
    private TextField searchRequestField;

    public void initialize() {
        // Configure TableView columns
        requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        serviceRequestedColumn.setCellValueFactory(new PropertyValueFactory<>("serviceRequested"));
        requestedDateColumn.setCellValueFactory(new PropertyValueFactory<>("requestedDate"));
        completionDateColumn.setCellValueFactory(new PropertyValueFactory<>("completionDate"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        estimatedPriceColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedPrice"));

        requestsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Load data from database
        loadDataFromDatabase();
        
        Platform.runLater(() -> {
            // Trouver le HBox parent des boutons
            HBox buttonHBox = (HBox) addButton.getParent();
            if (buttonHBox != null) {
                buttonHBox.setStyle("-fx-background-color: transparent;");
                
                // Écouteur pour maintenir le style au hover
                buttonHBox.hoverProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal) {
                        buttonHBox.setStyle("-fx-background-color: transparent;");
                    }
                });
            }
        });
        
        //Search Request
        searchRequestField.textProperty().addListener((observable, oldValue, newValue) -> searchRequests(newValue));

        // Add Request
        addButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxmls/AddRequest.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Add Request");
                stage.showAndWait();
                loadDataFromDatabase(); // Reload data after adding
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Delete Request
        deleteButton.setOnAction(event -> deleteSelectedRequests());

        
    }

    private void loadDataFromDatabase() {
        requestsData.clear();
        try (Connection connection = Connect.getConnection()) {
            String query = "SELECT request_id, client_id, service_requested, requested_date, completion_date, address, estimated_price FROM requests";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                requestsData.add(new Requests(
                        resultSet.getInt("request_id"),
                        resultSet.getInt("client_id"),
                        resultSet.getString("service_requested"),
                        resultSet.getString("requested_date"),
                        resultSet.getString("completion_date"),
                        resultSet.getString("address"),
                        resultSet.getDouble("estimated_price")
                ));
            }

            requestsTable.setItems(requestsData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteSelectedRequests() {
        ObservableList<Requests> selectedRequests = requestsTable.getSelectionModel().getSelectedItems();

        if (selectedRequests.isEmpty()) {
            System.out.println("No request selected.");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("No request selected!");
            alert.setContentText("Ctrl + Click to select multiple requests.");
            alert.showAndWait();
            return;
        }

        List<Integer> idsToDelete = new ArrayList<>();
        for (Requests request : selectedRequests) {
            idsToDelete.add(request.getRequestId());
        }

        if (deleteRequests(idsToDelete)) {
            System.out.println("Requests deleted successfully!");
            requestsTable.getItems().removeAll(selectedRequests); // Update the display
        } else {
            System.out.println("Failed to delete requests.");
        }
    }

    public boolean deleteRequests(List<Integer> ids) {
        try (Connection connection = Connect.getConnection()) {
            Statement statement = connection.createStatement();

            for (int id : ids) {
                String query = "DELETE FROM requests WHERE request_id = " + id;
                statement.executeUpdate(query);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void searchRequests(String keyword) {
        ObservableList<Requests> filteredClients = FXCollections.observableArrayList();
        for (Requests request : requestsData) {
        	boolean matchesId = false;
        	
        	// Vérifier si le mot-clé peut être interprété comme un ID
            try {
                int id = Integer.parseInt(keyword);
                matchesId = request.getRequestId() == id;
            } catch (NumberFormatException e) {
                // Si le mot-clé n'est pas un nombre, ignorer cette exception
            }
            if (matchesId || request.getServiceRequested().toLowerCase().contains(keyword.toLowerCase())) {
                filteredClients.add(request);
            }
        }
        requestsTable.setItems(filteredClients);
    }
    
}