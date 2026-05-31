package Controllers;


import application.Main;
import application.Providers;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class UpdateProviderController extends Main {
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField servicesField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    
    @FXML
    private Button saveButton;
    @FXML
    Label errorLabel;

    private Providers provider;
    
    private boolean isSaved = false; // Pour vérifier si les changements ont été sauvegardés

    @FXML 
    void initialize() {
    	nameField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                servicesField.requestFocus();
            }
        });
    	
    	emailField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                phoneField.requestFocus();
            }
        });
    	
    	phoneField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                servicesField.requestFocus();
            }
        });
    	
        priceField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                saveButton.requestFocus();
            }
        });

        servicesField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                descriptionField.requestFocus();
            }
        });

        descriptionField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                priceField.requestFocus(); // Déplacer le focus vers le bouton Ajouter
            }
        });

        // Bouton Ajouter activé avec Enter
        saveButton.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
            	saveButton.fire(); // Simuler un clic sur le bouton Ajouter
            }
        });
        
    	saveButton.setOnAction(event ->{
    		saveChanges();
    	});
    }
    public void setProvider(Providers provider) {
        this.provider = provider;
        nameField.setText(provider.getName());
        emailField.setText(provider.getEmail());
        phoneField.setText(provider.getPhone());        
        priceField.setText(String.valueOf(provider.getPrice()));
        servicesField.setText(provider.getServices());
        descriptionField.setText(provider.getDescription());
    }

    @FXML
    private void saveChanges() {
    	if (nameField.getText().isEmpty() || priceField.getText().isEmpty() || servicesField.getText().isEmpty() || descriptionField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            errorLabel.setText("Tous les champs sont obligatoires.");
            errorLabel.setVisible(true);
            return; // Arrête la méthode si un champ est vide
        }
    	
    	Double provider_price = null;
    	try {
    		provider_price = Double.parseDouble(priceField.getText()); // Essaye de convertir en Double
            errorLabel.setText(""); // Efface le message d'erreur
            errorLabel.setVisible(false); // Cache le label d'erreur
        } catch (NumberFormatException e) {
            errorLabel.setText("Veuillez entrer un nombre valide pour le prix.");
            errorLabel.setVisible(true);
            return; // Arrête la méthode si l'entrée est invalide
        }
    	
        provider.setName(nameField.getText());
        provider.setEmail(emailField.getText());
        provider.setPhone(phoneField.getText());
        provider.setPrice(Double.parseDouble(priceField.getText()));
        provider.setServices(servicesField.getText());
        provider.setDescription(descriptionField.getText());

        if (updateProvider(provider)) {
            System.out.println("Prestataire mis à jour avec succès !");
            isSaved = true;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Provider Updated!");
            alert.showAndWait();
            Stage stage = (Stage) saveButton.getScene().getWindow(); // Récupère le stage à partir du bouton "save"
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
                    event.consume(); // Annule la fermeture si l'utilisateur clique sur "No"
                }
            });
        }
    }
    
    /*-------------------Dreuiche's Task ends here------------*/
}

