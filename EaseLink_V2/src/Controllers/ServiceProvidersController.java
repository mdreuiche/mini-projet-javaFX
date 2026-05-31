package Controllers;

import application.Main;
import application.ProviderClient;
import application.Providers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.Connect;
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

public class ServiceProvidersController extends Main{

	@FXML
    private TableView<Providers> providerTable;
	@FXML
	private TableColumn<Providers, Integer>idColumn;
    @FXML
    private TableColumn<Providers, String> nameColumn;
    @FXML
    private TableColumn<Providers, String> emailColumn;
    @FXML
    private TableColumn<Providers, String> phoneColumn;
    @FXML
    private TableColumn<Providers, Double> priceColumn;
    @FXML
    private TableColumn<Providers, String> servicesColumn;
    @FXML
    private TableColumn<Providers, String> descriptionColumn;
    @FXML
    private TableColumn<Providers, String> added_dateColumn;
    
    @FXML
    private TextField searchProviderField;
    
    private ObservableList<Providers> providerData = FXCollections.observableArrayList();
    
    @FXML
    Button addButton, deleteButton, updateButton;
    
    
    public void initialize() {
        // Configure TableView columns
    	idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        servicesColumn.setCellValueFactory(new PropertyValueFactory<>("services"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        added_dateColumn.setCellValueFactory(new PropertyValueFactory<>("added_date"));
        
        
        providerTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        

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
        
        //Search
        searchProviderField.textProperty().addListener((observable, oldValue, newValue) -> searchProviders(newValue));
        
        //Add Provider
        Main main = new Main();
        addButton.setOnAction(event -> {
			try {
				main.switchScene("/Fxmls/addProviderPage.fxml", "Add");
			} catch (Exception e) {
				e.printStackTrace();
			}			
        });
        //Delete Provider
        deleteButton.setOnAction(event ->{
        	deleteSelectedProviders();
        });
        //Update Provider
        updateButton.setOnAction(event ->{
        	try {
				updateSelectedProvider();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
        });
      //Aucun element n'aura le focus!
        Platform.runLater(() -> providerTable.getScene().getRoot().requestFocus());
    }
    
    private void loadDataFromDatabase() {
    	providerData.clear();
        try (Connection connection = Connect.getConnection()) {
            String query = "SELECT id, name, email, phone, services, description,price, added_date  FROM providers";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
            	providerData.add(new Providers(
                		resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("services"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price"),
                        resultSet.getString("added_date")
                ) );
            }

            providerTable.setItems(providerData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void deleteSelectedProviders() {
        ObservableList<Providers> selectedProviders = providerTable.getSelectionModel().getSelectedItems();

        if (selectedProviders.isEmpty()) {
            System.out.println("Aucun prestataire sélectionné.");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("Aucun prestataire sélectionné!");
            alert.setContentText("Ctrl + Clique droit pour selectionner");
            alert.showAndWait();
            return;
        }

        List<Integer> idsToDelete = new ArrayList<>();
        for (Providers provider : selectedProviders) {
            idsToDelete.add(provider.getId());
        }

        if (deleteProvider(idsToDelete)) {
            System.out.println("Prestataires supprimés avec succès !");
            providerTable.getItems().removeAll(selectedProviders); // Mettre à jour l'affichage
        } else {
            System.out.println("Échec de la suppression.");
        }
    }
    
    @FXML
    private void updateSelectedProvider() throws IOException {
    	Providers selectedProvider = providerTable.getSelectionModel().getSelectedItem();

        if (selectedProvider == null) {
            System.out.println("Aucun prestataire sélectionné.");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("Aucun prestataire sélectionné!");
            alert.setContentText("Ctrl + Clique droit pour selectionner");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxmls/UpdateProviderPage.fxml"));
        Parent root = loader.load();

        // Passer les données du prestataire à la fenêtre de mise à jour
        UpdateProviderController controller = loader.getController();
        controller.setProvider(selectedProvider);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Modify Provider");
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> controller.handleWindowCloseRequest(event));
        
        stage.showAndWait();

        // Recharger les données après la mise à jour
        loadDataFromDatabase();;
    }
    
    private void searchProviders(String keyword) {
        ObservableList<Providers> filteredClients = FXCollections.observableArrayList();
        for (Providers provider : providerData) {
        	boolean matchesId = false;
        	boolean matchesPrice = false;
        	
        	// Vérifier si le mot-clé peut être interprété comme un ID
            try {
                int id = Integer.parseInt(keyword);
                matchesId = provider.getId() == id;
            } catch (NumberFormatException e) {
                // Si le mot-clé n'est pas un nombre, ignorer cette exception
            }
            try {
                double price = Double.parseDouble(keyword);
                matchesPrice = provider.getPrice() == price;
            } catch (NumberFormatException e) {
                // Si le mot-clé n'est pas un nombre, ignorer cette exception
            }
            if (matchesId || provider.getServices().toLowerCase().contains(keyword.toLowerCase())
            	||provider.getName().toLowerCase().contains(keyword.toLowerCase()) || matchesPrice) {
                filteredClients.add(provider);
            }
        }
        providerTable.setItems(filteredClients);
    }

    /*-------------------Dreuiche's Task ends here------------*/
    
}
