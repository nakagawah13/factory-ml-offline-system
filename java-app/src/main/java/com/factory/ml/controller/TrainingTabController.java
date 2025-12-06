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

    public TrainingTabController() {
        this.dataValidator = new DataValidator();
        this.featureTransformer = new FeatureTransformer();
        this.inferenceService = new InferenceService();
        this.modelManagerService = new ModelManagerService();
    }

    @FXML
    private void initialize() {
        loadDataButton.setOnAction(event -> loadTrainingData());
        validateButton.setOnAction(event -> validateTrainingData());
        startTrainingButton.setOnAction(event -> startTraining());
    }

    private void loadTrainingData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            trainingDataPathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void validateTrainingData() {
        String filePath = trainingDataPathField.getText();
        List<ValidationError> errors = dataValidator.validate(filePath);
        validationErrorsListView.getItems().clear();
        validationErrorsListView.getItems().addAll(errors);
        statusLabel.setText(errors.isEmpty() ? "Validation successful!" : "Validation failed with errors.");
    }

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