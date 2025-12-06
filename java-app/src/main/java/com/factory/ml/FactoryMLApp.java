package com.factory.ml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main JavaFX application entry point for Factory ML Offline System.
 * 
 * This class initializes and launches the JavaFX GUI application, loading
 * the main view FXML file and setting up the primary stage.
 * 
 * <p>Project Context:
 * Entry point for the factory-ml-offline-system Java GUI application.
 * Provides the main window for model training, inference, and simulation.
 * 
 * <p>Usage:
 * Run with: java -jar factory-ml-offline-system.jar
 * Or through Maven: mvn javafx:run
 */
public class FactoryMLApp extends Application {

    /**
     * Starts the JavaFX application.
     * 
     * Loads the main view FXML, creates the scene, and displays the
     * primary stage window.
     * 
     * @param primaryStage The primary stage for this application
     * @throws Exception if FXML loading fails
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_view.fxml"));
        primaryStage.setTitle("Factory ML Offline System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Application entry point.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}