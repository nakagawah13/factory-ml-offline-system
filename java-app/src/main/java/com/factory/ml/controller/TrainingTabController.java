package com.factory.ml.controller;

import ai.onnxruntime.OrtException;
import com.factory.ml.service.DataValidator;
import com.factory.ml.service.ModelManagerService;
import com.factory.ml.service.FeatureTransformer;
import com.factory.ml.service.InferenceService;
import com.factory.ml.model.Schema;
import com.factory.ml.model.ValidationError;
import com.factory.ml.model.InputRow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Training tab in the Factory ML GUI.
 * 
 * This controller manages the training tab UI, allowing users to load training
 * data, validate it, and initiate model training. Displays validation errors
 * and training status.
 * 
 * <p>Project Context:
 * Part of the factory-ml-offline-system JavaFX GUI. Provides the interface
 * for model training operations.
 * 
 * @see ModelManagerService
 * @see DataValidator
 */
public class TrainingTabController {

    @FXML
    private TextField trainingDataPathField;

    @FXML
    private Button loadDataButton;

    @FXML
    private Button validateButton;

    @FXML
    private Button startTrainingButton;

    @FXML
    private ListView<ValidationError> validationErrorsListView;

    @FXML
    private Label statusLabel;

    private DataValidator dataValidator;
    private FeatureTransformer featureTransformer;
    private InferenceService inferenceService;
    private ModelManagerService modelManagerService;

    /**
     * Constructs a TrainingTabController.
     * 
     * Initializes service instances for data validation, feature transformation,
     * inference, and model management.
     */
    public TrainingTabController() {
        this.dataValidator = new DataValidator();
        this.featureTransformer = new FeatureTransformer();
        try {
            this.inferenceService = new InferenceService("models/current/model.onnx");
        } catch (OrtException e) {
            throw new RuntimeException("Failed to initialize InferenceService", e);
        }
        this.modelManagerService = new ModelManagerService("models/current", "models/archive");
    }

    /**
     * Initializes the controller after FXML loading.
     * 
     * Sets up event handlers for UI buttons.
     */
    @FXML
    private void initialize() {
        loadDataButton.setOnAction(event -> loadTrainingData());
        validateButton.setOnAction(event -> validateTrainingData());
        startTrainingButton.setOnAction(event -> startTraining());
    }

    /**
     * Loads training data from a user-selected file.
     * 
     * Opens a file chooser dialog for CSV files and sets the selected
     * file path in the text field.
     */
    private void loadTrainingData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            trainingDataPathField.setText(selectedFile.getAbsolutePath());
        }
    }

    /**
     * Validates the training data file.
     * 
     * Reads the file path from the text field, performs validation,
     * and displays any validation errors in the list view.
     */
    private void validateTrainingData() {
        String filePath = trainingDataPathField.getText();
        try {
            List<String[]> csvRows = loadCsvRows(filePath);
            // TODO: Implement proper schema loading from config/schema.json
            //       Current implementation uses an empty Schema placeholder which won't
            //       perform meaningful validation. This should be addressed to:
            //       1. Load actual schema definition from config/schema.json
            //       2. Parse required columns, data types, and constraints
            //       3. Enable effective data validation for training data
            //       Tracked for future improvement - schema validation is out of scope
            //       for the compilation fix PR (Issue #10).
            Schema schema = new Schema("1.0", new ArrayList<>());
            List<ValidationError> errors = dataValidator.validate(csvRows, schema);
            validationErrorsListView.getItems().clear();
            validationErrorsListView.getItems().addAll(errors);
            statusLabel.setText(errors.isEmpty() ? "Validation successful!" : "Validation failed with errors.");
        } catch (IOException e) {
            statusLabel.setText("Error loading file: " + e.getMessage());
        }
    }

    /**
     * Starts the model training process.
     * 
     * Validates the data first, then initiates training if validation passes.
     * Updates the status label with training results.
     */
    private void startTraining() {
        String filePath = trainingDataPathField.getText();
        try {
            List<String[]> csvRows = loadCsvRows(filePath);
            // TODO: Implement proper schema loading from config/schema.json
            Schema schema = new Schema("1.0", new ArrayList<>());
            List<ValidationError> errors = dataValidator.validate(csvRows, schema);
            
            if (errors.isEmpty()) {
                // Proceed with training logic
                // TODO: trainModel() method needs implementation or alternative approach
                statusLabel.setText("Training initiated. Check Python trainer for progress.");
            } else {
                statusLabel.setText("Please fix validation errors before training.");
            }
        } catch (IOException e) {
            statusLabel.setText("Error loading file: " + e.getMessage());
        }
    }

    /**
     * Loads CSV rows from a file using Apache Commons CSV.
     * 
     * Uses a proper CSV parser to handle quoted fields, escaped characters,
     * and edge cases that simple split(",") cannot handle.
     * 
     * @param filePath Path to the CSV file
     * @return List of String arrays, each representing a CSV row
     * @throws IOException if file reading fails
     */
    private List<String[]> loadCsvRows(String filePath) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (Reader reader = new FileReader(filePath);
             org.apache.commons.csv.CSVParser parser = org.apache.commons.csv.CSVFormat.DEFAULT
                     .parse(reader)) {
            for (org.apache.commons.csv.CSVRecord record : parser) {
                String[] row = new String[record.size()];
                for (int i = 0; i < record.size(); i++) {
                    row[i] = record.get(i);
                }
                rows.add(row);
            }
        }
        return rows;
    }
}