module DACS4 {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires java.desktop;
	requires javafx.web;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
}
