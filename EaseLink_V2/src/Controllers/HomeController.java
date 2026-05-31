	package Controllers;
	
	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import javafx.fxml.FXML;
	import javafx.fxml.FXMLLoader;
	import javafx.scene.control.Button;
	import javafx.scene.control.Label;
	import javafx.scene.control.TableColumn;
	import javafx.scene.control.TableView;
	import javafx.scene.control.cell.PropertyValueFactory;
	import javafx.scene.image.ImageView;
	import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.Statement;
	import java.sql.Timestamp;
	import java.util.Stack;

import application.Connect;

	
	public class HomeController {
	
	    
	    @FXML
	    private Label logo_text111; // "Service Providers"
	    @FXML
	    private Label logo_text1111 ; // "Requests"
	    @FXML
	    private Label logo_text111111 ; // "Clients"
	    @FXML
	    private Label logo_text112; // "DashBoard"
	    @FXML
	    private ImageView logo_image11; // "Service Providers" 
	    @FXML
	    private ImageView logo_image1111; // "Requests" 
	    @FXML
	    private ImageView logo_image111; // "Clients" 
	    @FXML
	    private ImageView logo_image112; // "DashBoard"
	    
	    @FXML
	    private AnchorPane mainAnchorPane; // La zone principale où les fenêtres changeront
	

	    @FXML
	    public void initialize() {
	        
	    	loadView("/Fxmls/DashBoard.fxml");
	        logo_text111.setOnMouseClicked(event -> loadView("/Fxmls/ServiceProviders.fxml"));
	        logo_text1111.setOnMouseClicked(event -> loadView("/Fxmls/Requests.fxml"));
	        logo_text111111.setOnMouseClicked(event -> loadView("/Fxmls/Clients.fxml"));
	        logo_text112.setOnMouseClicked(event -> loadView("/Fxmls/DashBoard.fxml"));
	        logo_image11.setOnMouseClicked(event -> loadView("/Fxmls/ServiceProviders.fxml"));
	        logo_image111.setOnMouseClicked(event -> loadView("/Fxmls/Clients.fxml"));
	        logo_image1111.setOnMouseClicked(event -> loadView("/Fxmls/Requests.fxml"));
	        logo_image112.setOnMouseClicked(event -> loadView("/Fxmls/DashBoard.fxml"));
	        
	        
	    }
	    
    
	    public void loadView(String fxmlFile) {
	    	try {
	    		// Enregistrer la vue actuelle
	    	
	        
	        	// Charger la nouvelle vue
	        	AnchorPane newView = FXMLLoader.load(getClass().getResource(fxmlFile));
	        	mainAnchorPane.getChildren().setAll(newView);
	        	
	        	AnchorPane.setTopAnchor(newView, 0.0);
	            AnchorPane.setBottomAnchor(newView, 0.0);
	            AnchorPane.setLeftAnchor(newView, 0.0);
	            AnchorPane.setRightAnchor(newView, 0.0);
	        
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    }
	
        /*-------------------Dreuiche's Task ends here------------*/ 
	
	}	
