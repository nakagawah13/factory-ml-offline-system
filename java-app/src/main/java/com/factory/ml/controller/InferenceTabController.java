package com.factory.ml.controller;

import ai.onnxruntime.OrtException;
import com.factory.ml.model.InputRow;
import com.factory.ml.model.InferenceResult;
import com.factory.ml.model.Schema;
import com.factory.ml.model.ValidationError;
import com.factory.ml.service.DataValidator;
import com.factory.ml.service.FeatureTransformer;
import com.factory.ml.service.InferenceService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Inference tab in the Factory ML GUI.
 * 
 * This controller manages the inference tab UI, allowing users to load data,
 * validate it, and run model inference. Displays results in a table view.
 * 
 * <p>Project Context:
 * Part of the factory-ml-offline-system JavaFX GUI. Provides the interface
 * for batch inference operations on loaded data.
 * 
 * @see InferenceService
 * @see DataValidator
 * @see FeatureTransformer
 */
public class InferenceTabController {

    @FXML
    private Button loadDataButton;

    @FXML
    private Button runInferenceButton;

    @FXML
    private TableView<InputRow> dataTable;

    @FXML
    private Label statusLabel;

    private final DataValidator dataValidator;
    private final FeatureTransformer featureTransformer;
    private final InferenceService inferenceService;

    /**
     * Constructs an InferenceTabController.
     * 
     * Initializes service instances for data validation, feature transformation,
     * and model inference.
     */
    public InferenceTabController() {
        this.dataValidator = new DataValidator();
        this.featureTransformer = new FeatureTransformer();
        try {
            this.inferenceService = new InferenceService("models/current/model.onnx");
        } catch (OrtException e) {
            throw new RuntimeException("Failed to initialize InferenceService", e);
        }
    }

    /**
     * Initializes the controller after FXML loading.
     * 
     * Sets up event handlers for UI buttons.
     */
    @FXML
    private void initialize() {
        loadDataButton.setOnAction(event -> loadData());
        runInferenceButton.setOnAction(event -> runInference());
    }

    /**
     * Loads data from a user-selected file.
     * 
     * Opens a file chooser dialog for CSV files, loads the data,
     * validates it, and displays it in the table view.
     */
    private void loadData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                List<InputRow> inputData = loadCsvAsInputRows(selectedFile.getAbsolutePath());
                List<String[]> csvRows = loadCsvRows(selectedFile.getAbsolutePath());
                // TODO: Implement proper schema loading from config/schema.json
                Schema schema = new Schema("1.0", new ArrayList<>());
                List<ValidationError> errors = dataValidator.validate(csvRows, schema);
                
                if (errors.isEmpty()) {
                    dataTable.getItems().setAll(inputData);
                    statusLabel.setText("Data loaded successfully.");
                } else {
                    statusLabel.setText("Data validation failed: " + errors.size() + " errors found.");
                }
            } catch (IOException e) {
                statusLabel.setText("Error loading file: " + e.getMessage());
            }
        }
    }

    /**
     * Runs inference on the loaded data.
     * 
     * Retrieves input data from the table view, performs inference using
     * the inference service, and displays the results.
     */
    private void runInference() {
        List<InputRow> inputData = dataTable.getItems();
        if (inputData.isEmpty()) {
            statusLabel.setText("No data loaded. Please load data first.");
            return;
        }
        
        try {
            // Run inference on the first row as an example
            // TODO: Implement batch inference for all rows
            InputRow firstRow = inputData.get(0);
            InferenceResult result = inferenceService.predict(
                firstRow.getFloatInput(), 
                firstRow.getStringInput(), 
                false
            );
            statusLabel.setText("Inference completed. Label: " + result.getLabel());
        } catch (OrtException e) {
            statusLabel.setText("Inference failed: " + e.getMessage());
        }
    }

    /**
     * Loads CSV rows from a file.
     * 
     * @param filePath Path to the CSV file
     * @return List of String arrays, each representing a CSV row
     * @throws IOException if file reading fails
     */
    private List<String[]> loadCsvRows(String filePath) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                rows.add(line.split(","));
            }
        }
        return rows;
    }

    /**
     * Loads CSV file as InputRow objects.
     * 
     * @param filePath Path to the CSV file
     * @return List of InputRow objects
     * @throws IOException if file reading fails
     */
    private List<InputRow> loadCsvAsInputRows(String filePath) throws IOException {
        List<InputRow> inputRows = new ArrayList<>();
        List<String[]> csvRows = loadCsvRows(filePath);
        
        if (csvRows.isEmpty()) {
            return inputRows;
        }
        
        // First row is header
        String[] headers = csvRows.get(0);
        
        // Convert remaining rows to InputRow objects
        for (int i = 1; i < csvRows.size(); i++) {
            String[] row = csvRows.get(i);
            InputRow inputRow = new InputRow();
            for (int j = 0; j < headers.length && j < row.length; j++) {
                inputRow.setFeature(headers[j], row[j]);
            }
            inputRows.add(inputRow);
        }
        
        return inputRows;
    }
}