package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;



public class Main extends Application {
	private static Stage primaryStage;
	private static Stage stage ;
	@Override
	public void start(Stage Stage) {
		primaryStage = Stage;
		
		try {
			Pane root = FXMLLoader.load(getClass().getResource("/Fxmls/Home.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.jpg")));
			
			primaryStage.setTitle("ServiceLink");
			primaryStage.setMaximized(false);
			primaryStage.centerOnScreen();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	//Add providers
	public boolean addProvider(String name, Double price , String services, String description, String email, String phone) {
        String query = "INSERT INTO providers (name, email, phone, services, description, price) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = Connect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, services);
            preparedStatement.setString(5, description);
            preparedStatement.setDouble(6, price);

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  
        }
    }
 
	//Delete Providers
	public boolean deleteProvider(List<Integer> ids) {
		if (ids == null || ids.isEmpty()) {
	        System.out.println("Aucun ID à supprimer.");
	        return false;
	    }

	    StringBuilder queryBuilder = new StringBuilder("DELETE FROM providers WHERE id IN (");
	    for (int i = 0; i < ids.size(); i++) {
	        queryBuilder.append("?");
	        if (i < ids.size() - 1) {
	            queryBuilder.append(", ");
	        }
	    }
	    queryBuilder.append(")");

	    String query = queryBuilder.toString();

        try (Connection connection = Connect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        	for (int i = 0; i < ids.size(); i++) {
                preparedStatement.setInt(i + 1, ids.get(i));
            }

            int rowsDeleted = preparedStatement.executeUpdate();
            System.out.println(rowsDeleted + " prestataire(s) supprimé(s).");
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  
        }
    }
	
	//Update providers
	public boolean updateProvider(Providers provider) {
		String query = "UPDATE providers SET name = ?, price = ?, services = ?, description = ?, phone = ?, email = ? WHERE id = ?";

	    try (Connection connection = Connect.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        // Définir les paramètres de la requête
	        preparedStatement.setString(1, provider.getName());
	        preparedStatement.setDouble(2, provider.getPrice());
	        preparedStatement.setString(3, provider.getServices());
	        preparedStatement.setString(4, provider.getDescription());
	        preparedStatement.setString(5, provider.getPhone());
	        preparedStatement.setString(6, provider.getEmail());
	        preparedStatement.setInt(7, provider.getId());

	        // Exécuter la mise à jour
	        int rowsUpdated = preparedStatement.executeUpdate();
	        System.out.println(rowsUpdated + " prestataire(s) mis à jour.");
	        return rowsUpdated > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	//Delete a link between Provider and Client
	public boolean deleteLinkProviderClient(List<Integer> ids) {
		if (ids == null || ids.isEmpty()) {
	        System.out.println("Aucun ID à supprimer.");
	        return false;
	    }

	    StringBuilder queryBuilder = new StringBuilder("DELETE FROM provider_client WHERE id IN (");
	    for (int i = 0; i < ids.size(); i++) {
	        queryBuilder.append("?");
	        if (i < ids.size() - 1) {
	            queryBuilder.append(", ");
	        }
	    }
	    queryBuilder.append(")");

	    String query = queryBuilder.toString();

        try (Connection connection = Connect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        	for (int i = 0; i < ids.size(); i++) {
                preparedStatement.setInt(i + 1, ids.get(i));
            }

            int rowsDeleted = preparedStatement.executeUpdate();
            System.out.println(rowsDeleted + " ligne(s) supprimée(s).");
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  
        }
    }
	
	
	//Update a link between Provider and Client
	
	// switching scenes
	public static void switchScene(String fxmlFile, String title) throws Exception {
        // Ensure primaryStage is properly initialized
        if (primaryStage != null) {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));
            Scene scene = new Scene(loader.load());
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.centerOnScreen();
            stage.show();
            stage.setResizable(false);
         
        } else {
            throw new IllegalStateException("Primary Stage is not initialized.");
        }
    }
	
    /*-------------------Dreuiche's Task ends here------------*/
	
	
	public static void main(String[] args) {
		launch(args);
	}
}