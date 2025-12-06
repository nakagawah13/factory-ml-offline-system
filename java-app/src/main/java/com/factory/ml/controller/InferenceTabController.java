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

    public InferenceTabController() {
        this.dataValidator = new DataValidator();
        this.featureTransformer = new FeatureTransformer();
        this.inferenceService = new InferenceService();
    }

    @FXML
    private void initialize() {
        loadDataButton.setOnAction(event -> loadData());
        runInferenceButton.setOnAction(event -> runInference());
    }

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

    private void runInference() {
        List<InputRow> inputData = dataTable.getItems();
        InferenceResult result = inferenceService.predict(inputData);
        // Handle the inference result (e.g., display in the UI)
        statusLabel.setText("Inference completed.");
    }
}