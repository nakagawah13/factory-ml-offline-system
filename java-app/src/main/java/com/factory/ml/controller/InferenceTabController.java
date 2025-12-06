package com.factory.ml.controller;

import com.factory.ml.model.InputRow;
import com.factory.ml.model.InferenceResult;
import com.factory.ml.service.DataValidator;
import com.factory.ml.service.FeatureTransformer;
import com.factory.ml.service.InferenceService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;
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
        this.inferenceService = new InferenceService();
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
     * Opens a file chooser dialog for CSV or Excel files, loads the data,
     * validates it, and displays it in the table view.
     */
    private void loadData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            List<InputRow> inputData = featureTransformer.loadData(selectedFile);
            if (dataValidator.validate(inputData)) {
                dataTable.getItems().setAll(inputData);
                statusLabel.setText("Data loaded successfully.");
            } else {
                statusLabel.setText("Data validation failed.");
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
        InferenceResult result = inferenceService.predict(inputData);
        // Handle the inference result (e.g., display in the UI)
        statusLabel.setText("Inference completed.");
    }
}