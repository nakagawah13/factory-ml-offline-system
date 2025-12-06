package com.factory.ml.controller;

import com.factory.ml.service.DataValidator;
import com.factory.ml.service.ModelManagerService;
import com.factory.ml.service.FeatureTransformer;
import com.factory.ml.service.InferenceService;
import com.factory.ml.model.ValidationError;
import com.factory.ml.model.InputRow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
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
        this.inferenceService = new InferenceService();
        this.modelManagerService = new ModelManagerService();
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
        List<ValidationError> errors = dataValidator.validate(filePath);
        validationErrorsListView.getItems().clear();
        validationErrorsListView.getItems().addAll(errors);
        statusLabel.setText(errors.isEmpty() ? "Validation successful!" : "Validation failed with errors.");
    }

    /**
     * Starts the model training process.
     * 
     * Validates the data first, then initiates training if validation passes.
     * Updates the status label with training results.
     */
    private void startTraining() {
        String filePath = trainingDataPathField.getText();
        if (dataValidator.validate(filePath).isEmpty()) {
            // Proceed with training logic
            modelManagerService.trainModel(filePath);
            statusLabel.setText("Training completed successfully.");
        } else {
            statusLabel.setText("Please fix validation errors before training.");
        }
    }
}