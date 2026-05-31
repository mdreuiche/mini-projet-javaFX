package Controllers;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddProviderController extends Main{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button add;

    @FXML
    private TextField desc;
    
    @FXML
    private TextField name;
    
    @FXML
    private TextField email;

    @FXML
    private TextField phone;
    
    @FXML
    private TextField prix;

    @FXML
    private TextField services;
    
    @FXML
    Label errorLabel;
    
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
                services.requestFocus();
            }
        });

        prix.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                add.requestFocus();
            }
        });

        services.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                desc.requestFocus();
            }
        });

        desc.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                prix.requestFocus(); // Déplacer le focus vers le bouton Ajouter
            }
        });

        // Bouton Ajouter activé avec Enter
        add.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                add.fire(); // Simuler un clic sur le bouton Ajouter
            }
        });
    }

    @FXML
    void onadd(ActionEvent event) throws Exception {
    	
    	if (name.getText().isEmpty() || prix.getText().isEmpty() || services.getText().isEmpty() || desc.getText().isEmpty() || email.getText().isEmpty() || phone.getText().isEmpty()) {
            errorLabel.setText("Tous les champs sont obligatoires.");
            errorLabel.setVisible(true);
            return; // Arrête la méthode si un champ est vide
        }
    	
    	String provider_name = name.getText(); 
    	String provider_email = email.getText();
    	String provider_phonex = phone.getText();
    	Double provider_phone = null; 
    	String provider_services = services.getText();     
    	String provider_desc = desc.getText();
    	Double provider_price = null;
    	
    	try {
    		provider_price = Double.parseDouble(prix.getText()); // Essaye de convertir en Double
            errorLabel.setText(""); // Efface le message d'erreur
            errorLabel.setVisible(false); // Cache le label d'erreur
        } catch (NumberFormatException e) {
            errorLabel.setText("Veuillez entrer un nombre valide pour le prix.");
            errorLabel.setVisible(true);
            return; // Arrête la méthode si l'entrée est invalide
        }
    	try {
    		provider_phone = Double.parseDouble(phone.getText()); // Essaye de convertir en Double
            errorLabel.setText(""); // Efface le message d'erreur
            errorLabel.setVisible(false); // Cache le label d'erreur
        } catch (NumberFormatException e) {
            errorLabel.setText("Veuillez entrer un nombre valide pour le phone.");
            errorLabel.setVisible(true);
            return; // Arrête la méthode si l'entrée est invalide
        }
    	
//    	name.clear();
//    	email.clear();
//    	phone.clear();
//    	services.clear();
//    	desc.clear();
//    	prix.clear();
    	

         if (addProvider(provider_name, provider_price, provider_services,provider_desc,provider_email, provider_phonex )) {
             System.out.println("Provider added successfully!");
             Alert alert = new Alert(AlertType.INFORMATION);
             alert.setTitle("Success");
             alert.setHeaderText(null);
             alert.setContentText("Provider Added!");
             alert.showAndWait();
             Stage stage = (Stage) add.getScene().getWindow(); // Récupère le stage à partir du bouton "add"
             stage.close();
         } else {
             Alert alert = new Alert(AlertType.ERROR);
             alert.setTitle("Error");
             alert.setHeaderText(null);
             alert.setContentText("Provider Not Added!");
             alert.showAndWait();
             System.out.println("Failed to add Provider.");
             
         }
         
    }
    /*-------------------Dreuiche's Task ends here------------*/
}