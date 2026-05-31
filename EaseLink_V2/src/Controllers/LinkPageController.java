package Controllers;

import application.Connect;
import application.Clients;
import application.Providers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LinkPageController {

    @FXML
    private TableView<Clients> clientTable;
    @FXML
    private TableColumn<Clients, String> clientNameColumn;
    @FXML
    private TableColumn<Clients, Integer> clientIdColumn;
    @FXML
    private TextField searchClientField;

    @FXML
    private TableView<Providers> providerTable;
    @FXML
    private TableColumn<Providers, String> providerNameColumn;
    @FXML
    private TableColumn<Providers, String> providerServiceColumn;
    @FXML
    private TableColumn<Providers, Double> providerPriceColumn;
    @FXML
    private TableColumn<Providers, Integer> providerIdColumn;
    @FXML
    private TextField searchProviderField;

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button linkButton;

    private ObservableList<Clients> clients = FXCollections.observableArrayList();
    private ObservableList<Providers> providers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configure Table Columns
    	clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    	clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    	providerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    	providerServiceColumn.setCellValueFactory(new PropertyValueFactory<>("services"));
    	providerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));;
    	providerPriceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));;

        // Load Data
        loadClients();
        loadProviders();

        // Add Search Functionality
        searchClientField.textProperty().addListener((observable, oldValue, newValue) -> searchClients(newValue));
        searchProviderField.textProperty().addListener((observable, oldValue, newValue) -> searchProviders(newValue));

        // Link Button Action
        linkButton.setOnAction(event -> linkClientWithProvider());
    }

    private void loadClients() {
        clients.clear();
        try (Connection connection = Connect.getConnection()) {
            String query = "SELECT id, name FROM Clients ";
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            while (resultSet.next()) {
                clients.add(new Clients(resultSet.getInt("id"), resultSet.getString("name")));
            }
            clientTable.setItems(clients);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadProviders() {
        providers.clear();
        try (Connection connection = Connect.getConnection()) {
            String query = "SELECT id, name, services, price FROM Providers where id not in (select provider_id from provider_client)";
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            while (resultSet.next()) {
                providers.add(new Providers(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("services"), resultSet.getDouble("price")));
            }
            providerTable.setItems(providers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchClients(String keyword) {
        ObservableList<Clients> filteredClients = FXCollections.observableArrayList();
        for (Clients client : clients) {
        	boolean matchesId = false;
        	
        	// Vérifier si le mot-clé peut être interprété comme un ID
            try {
                int id = Integer.parseInt(keyword);
                matchesId = client.getId() == id;
            } catch (NumberFormatException e) {
                // Si le mot-clé n'est pas un nombre, ignorer cette exception
            }
            if (matchesId || client.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredClients.add(client);
            }
        }
        clientTable.setItems(filteredClients);
    }

    private void searchProviders(String keyword) {
        ObservableList<Providers> filteredProviders = FXCollections.observableArrayList();
        for (Providers provider : providers) {
            boolean matchesId = false;

            // Vérifier si le mot-clé peut être interprété comme un ID
            try {
                int id = Integer.parseInt(keyword);
                matchesId = provider.getId() == id;
            } catch (NumberFormatException e) {
                // Si le mot-clé n'est pas un nombre, ignorer cette exception
            }

            // Vérifier si le mot-clé correspond au nom, au service ou à l'ID
            if (matchesId ||
                provider.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                provider.getServices().toLowerCase().contains(keyword.toLowerCase())) {
                filteredProviders.add(provider);
            }
        }
        providerTable.setItems(filteredProviders);
    }

    private void linkClientWithProvider() {
        Clients selectedClient = clientTable.getSelectionModel().getSelectedItem();
        Providers selectedProvider = providerTable.getSelectionModel().getSelectedItem();

        if (selectedClient == null || selectedProvider == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select both a client and a provider.");
            return;
        }

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
        	showAlert(Alert.AlertType.ERROR, "Input Error", "Please select both start and end dates.");
        	return;
        }
        
        //Verification des dates
        if (startDate.isAfter(endDate)) {
            showAlert(Alert.AlertType.ERROR, "Date Error", "The start date cannot be after the end date.");
            return;
        }
        String formattedStartDate = startDate.toString(); // Format par défaut : "YYYY-MM-DD"
        String formattedEndDate = endDate.toString();
        
        try (Connection connection = Connect.getConnection()) {
        	double providerPrice = getProviderPrice(connection, selectedProvider.getId());
        	String providerDescription = getProviderDescription(connection, selectedProvider.getId());
            String query = "INSERT INTO Provider_Client (provider_id, client_id, service, description, price, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, selectedProvider.getId());
            statement.setInt(2, selectedClient.getId());
            statement.setString(3, selectedProvider.getServices());
            statement.setString(4, providerDescription);
            statement.setDouble(5, providerPrice); // Placeholder price
            statement.setString(6, formattedStartDate);
            statement.setString(7, formattedEndDate);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Client and Provider linked successfully!");
                ((Stage) linkButton.getScene().getWindow()).close(); // Close the window
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to link client and provider.");
        }
    }
    
    //get provider price
    private double getProviderPrice(Connection connection, int providerId) throws SQLException {
        String query = "SELECT price FROM Providers WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, providerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("price");
            } else {
                throw new SQLException("Provider ID not found.");
            }
        }
    }
    private String getProviderDescription(Connection connection, int providerId) throws SQLException {
        String query = "SELECT description FROM Providers WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, providerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("description");
            } else {
                throw new SQLException("Provider ID not found.");
            }
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /*-------------------Dreuiche's Task ends here------------*/
}
