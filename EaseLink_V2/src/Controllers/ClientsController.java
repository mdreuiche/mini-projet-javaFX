package Controllers;

import application.Clients;
import application.Connect;
import application.Main;
import application.Providers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

public class ClientsController extends Main {

    @FXML
    private TableView<Clients> clientTable;
    @FXML
    private TableColumn<Clients, Integer> idColumn;
    @FXML
    private TableColumn<Clients, String> nameColumn;
    @FXML
    private TableColumn<Clients, String> emailColumn;
    @FXML
    private TableColumn<Clients, String> phoneColumn;
    @FXML
    private TableColumn<Clients, String> serviceRequestedColumn; 
    @FXML
    private TableColumn<Clients, String> descriptionColumn; 
    @FXML
    private TableColumn<Clients, String> addressColumn; 
    @FXML
    private TableColumn<Clients, String> addedDateColumn; 

    private ObservableList<Clients> clientData = FXCollections.observableArrayList();

    @FXML
    private Button addButton, deleteButton, updateButton;
    
    @FXML
    private TextField searchClientField;

    public void initialize() {
        // Configure TableView columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        serviceRequestedColumn.setCellValueFactory(new PropertyValueFactory<>("serviceRequested")); // Updated
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description")); // Updated
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        addedDateColumn.setCellValueFactory(new PropertyValueFactory<>("addedDate")); // Updated

        clientTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
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

        // Load data from database
        loadDataFromDatabase();
        
        //Search
        searchClientField.textProperty().addListener((observable, oldValue, newValue) -> searchClients(newValue));

        // Add Client
        Main main = new Main();
        addButton.setOnAction(event -> {
            try {
                main.switchScene("/Fxmls/addClientPage.fxml", "Add Client");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Delete Client
        deleteButton.setOnAction(event -> deleteSelectedClients());

        // Update Client
        updateButton.setOnAction(event -> {
            try {
                updateSelectedClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadDataFromDatabase() {
        clientData.clear();
        try (Connection connection = Connect.getConnection()) {
            String query = "SELECT id, name, email, phone, service_requested, description, address, added_date FROM clients"; // Updated
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                clientData.add(new Clients(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("service_requested"), // Updated
                        resultSet.getString("description"), // Updated
                        resultSet.getString("address"),
                        resultSet.getString("added_date") // Updated
                ));
            }

            clientTable.setItems(clientData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteSelectedClients() {
        ObservableList<Clients> selectedClients = clientTable.getSelectionModel().getSelectedItems();

        if (selectedClients.isEmpty()) {
            System.out.println("No client selected.");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("No client selected!");
            alert.setContentText("Ctrl + Click to select multiple clients.");
            alert.showAndWait();
            return;
        }

        List<Integer> idsToDelete = new ArrayList<>();
        for (Clients client : selectedClients) {
            idsToDelete.add(client.getId());
        }

        if (deleteClient(idsToDelete)) {
            System.out.println("Clients deleted successfully !");
            clientTable.getItems().removeAll(selectedClients); // Update the display
        } else {
            System.out.println("Failed to delete clients.");
        }
    }

    public boolean deleteClient(List<Integer> ids) {
        try (Connection connection = Connect.getConnection()) {
            Statement statement = connection.createStatement();

            for (int id : ids) {
                String query = "DELETE FROM clients WHERE id = " + id; // Updated
                statement.executeUpdate(query);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void updateSelectedClient() throws IOException {
        Clients selectedClient = clientTable.getSelectionModel().getSelectedItem();

        if (selectedClient == null) {
            System.out.println("No client selected.");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("No client selected!");
            alert.setContentText("Select a client to update.");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxmls/UpdateClientPage.fxml"));
        Parent root = loader.load();

        // Pass the client data to the update window
        UpdateClientController controller = loader.getController();
        controller.setClient(selectedClient);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Update Client");
        stage.setOnCloseRequest(event -> controller.handleWindowCloseRequest(event));

        stage.showAndWait();

        // Reload data after the update
        loadDataFromDatabase();
    }
    
    private void searchClients(String keyword) {
        ObservableList<Clients> filteredClients = FXCollections.observableArrayList();
        for (Clients client : clientData) {
        	boolean matchesId = false;
        	
        	// Vérifier si le mot-clé peut être interprété comme un ID
            try {
                int id = Integer.parseInt(keyword);
                matchesId = client.getId() == id;
            } catch (NumberFormatException e) {
                // Si le mot-clé n'est pas un nombre, ignorer cette exception
            }
            if (matchesId || client.getServiceRequested().toLowerCase().contains(keyword.toLowerCase())
            	||client.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredClients.add(client);
            }
        }
        clientTable.setItems(filteredClients);
    }
}