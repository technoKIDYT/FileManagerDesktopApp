module com.technokid.FileManagerDesktopApp {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires java.desktop;

	opens com.technokid.FileManagerDesktopApp to javafx.fxml, javafx.base;
	opens com.technokid.FileManagerDesktopApp.model to javafx.base;

	exports com.technokid.FileManagerDesktopApp;
	exports com.technokid.FileManagerDesktopApp.model;
	exports com.technokid.FileManagerDesktopApp.Utility;
}
