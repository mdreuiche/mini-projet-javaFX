package Controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import application.Clients;
import application.Connect;
import application.Main;
import application.ProviderClient;
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

public class DashBoardController extends Main {
	@FXML
    private TableView<ProviderClient> pcTable;
	@FXML
	private TableColumn<ProviderClient, Integer>idColumn;
	@FXML
	private TableColumn<ProviderClient, Integer>provider_idColumn;
	@FXML
	private TableColumn<ProviderClient, Integer>client_idColumn;
    @FXML
    private TableColumn<ProviderClient, Double> priceColumn;
    @FXML
    private TableColumn<ProviderClient, String> serviceColumn;
    @FXML
    private TableColumn<ProviderClient, String> descriptionColumn;
    @FXML
    private TableColumn<ProviderClient, String> start_dateColumn;
    @FXML
    private TableColumn<ProviderClient, String> end_dateColumn;
    
    @FXML
    private TextField searchLinkField;
    
    private ObservableList<ProviderClient> provider_client = FXCollections.observableArrayList();
    
    @FXML
    Button linkButton, deleteButton, updateButton;
    
    
    public void initialize() {
        // Configure TableView columns
    	//scheduleCleanup();
    	cleanupExpiredRows();
    	idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    	provider_idColumn.setCellValueFactory(new PropertyValueFactory<>("Provider_Id"));
    	client_idColumn.setCellValueFactory(new PropertyValueFactory<>("Client_Id"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("service"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        start_dateColumn.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        end_dateColumn.setCellValueFactory(new PropertyValueFactory<>("end_date"));
        
        
        pcTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Load data from database
        loadDataFromDatabase();
        
        //Search
        searchLinkField.textProperty().addListener((observable, oldValue, newValue) -> searchLinks(newValue));
        
        //link Client With Provider
        Main main = new Main();
        linkButton.setOnAction(event -> {
			try {
				main.switchScene("/Fxmls/LinkPage.fxml", "Link");
				loadDataFromDatabase();
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
				updateSelectedLink();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
        });
        
        //Aucun element n'aura le focus!
        Platform.runLater(() -> pcTable.getScene().getRoot().requestFocus());
        Platform.runLater(() -> {
            // Trouver le HBox parent des boutons
            HBox buttonHBox = (HBox) linkButton.getParent();
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
        
    }
    
    private void loadDataFromDatabase() {
    	provider_client.clear();
        try (Connection connection = Connect.getConnection()) {
            String query = "SELECT id, provider_id, client_id, service, description,price, start_date , end_date FROM provider_client";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
            	provider_client.add(new ProviderClient(
                		resultSet.getInt("id"),
                		resultSet.getInt("provider_id"),
                		resultSet.getInt("client_id"),
                        resultSet.getString("service"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price"),
                        resultSet.getString("start_date"),
                        resultSet.getString("end_date")
                ) );
            }

            pcTable.setItems(provider_client);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Delete link
    @FXML
    private void deleteSelectedProviders() {
        ObservableList<ProviderClient> selectedProviders = pcTable.getSelectionModel().getSelectedItems();

        if (selectedProviders.isEmpty()) {
            System.out.println("Aucune ligne sélectionné.");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("Aucune ligne sélectionné!");
            alert.setContentText("Ctrl + Clique droit pour selectionner");
            alert.showAndWait();
            return;
        }

        List<Integer> idsToDelete = new ArrayList<>();
        for (ProviderClient provider : selectedProviders) {
            idsToDelete.add(provider.getId());
        }

        if (deleteLinkProviderClient(idsToDelete)) {
            System.out.println("lignes supprimées avec succès !");
            pcTable.getItems().removeAll(selectedProviders); // Mettre à jour l'affichage
        } else {
            System.out.println("Échec de la suppression.");
        }
    }
    
    //Update link   
    @FXML
    private void updateSelectedLink() throws IOException {
    	ProviderClient selectedLink = pcTable.getSelectionModel().getSelectedItem();

        if (selectedLink == null) {
            System.out.println("Aucun Ligne sélectionnée.");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("Aucun ligne sélectionnée!");
            alert.setContentText("Ctrl + Clique droit pour selectionner");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxmls/UpdateLinkPage.fxml"));
        Parent root = loader.load();

        // Passer les données du prestataire à la fenêtre de mise à jour
        UpdateLinkController controller = loader.getController();
        controller.setProviderClientId(selectedLink.getId());
        controller.setInitialDates(LocalDate.parse(selectedLink.getStart_date()), LocalDate.parse(selectedLink.getEnd_date()));

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Modify Link");
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> controller.handleWindowCloseRequest(event));
        
        stage.showAndWait();

        // Recharger les données après la mise à jour
        loadDataFromDatabase();;
    }
    
    //Delete expired links
    public void scheduleCleanup() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Planifie la tâche toutes les heures
        scheduler.scheduleAtFixedRate(() -> {
            try {
                cleanupExpiredRows();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.HOURS); // Exécute toutes les heures
    }
    private void cleanupExpiredRows() {
        String query = "DELETE FROM Provider_Client WHERE end_date <= ?";

        try (Connection connection = Connect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Définir l'heure actuelle pour la comparaison
            preparedStatement.setTimestamp(1, java.sql.Timestamp.valueOf(LocalDateTime.now()));

            int rowsDeleted = preparedStatement.executeUpdate();
            System.out.println(rowsDeleted + " lignes supprimées.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    private void searchLinks(String keyword) {
        ObservableList<ProviderClient> filteredClients = FXCollections.observableArrayList();
        for (ProviderClient pc : provider_client) {
        	boolean matchesId = false;
        	boolean matchesPrice = false;
        	
        	// Vérifier si le mot-clé peut être interprété comme un ID
            try {
                int id = Integer.parseInt(keyword);
                matchesId = pc.getId() == id;
            } catch (NumberFormatException e) {
                // Si le mot-clé n'est pas un nombre, ignorer cette exception
            }
            try {
                double price = Double.parseDouble(keyword);
                matchesPrice = pc.getPrice() == price;
            } catch (NumberFormatException e) {
                // Si le mot-clé n'est pas un nombre, ignorer cette exception
            }
            if (matchesId || matchesPrice || pc.getService().toLowerCase().contains(keyword.toLowerCase())) {
                filteredClients.add(pc);
            }
        }
        pcTable.setItems(filteredClients);
        
        /*-------------------Dreuiche's Task ends here------------*/
    }

}
