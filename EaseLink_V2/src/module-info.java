module EaseLink {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.graphics;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
	opens Controllers to javafx.graphics, javafx.fxml, javafx.base;
}
