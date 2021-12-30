package com.technokid.FileManagerDesktopApp;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * JavaFX App
 */
public class App extends Application {

	private double xOffset, yOffset;

	@SuppressWarnings("exports")
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane) loadFXML("DashboardUi");
			Scene scene = new Scene(root, 1200, 850);
			scene.getStylesheets().add(App.class.getResource("application.css").toExternalForm());
			scene.setFill(Color.TRANSPARENT);
			primaryStage.setResizable(true);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(true);

			root.setOnMousePressed(event -> {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			});
			root.setOnMouseDragged(event -> {
				primaryStage.setX(event.getScreenX() - xOffset);
				primaryStage.setY(event.getScreenY() - yOffset);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

	public static void main(String[] args) {
		launch();
	}

}