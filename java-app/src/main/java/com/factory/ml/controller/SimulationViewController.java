package com.factory.ml.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class SimulationViewController {

    @FXML
    private Slider valueSlider;

    @FXML
    private TextField modifiedValueField;

    @FXML
    private Button runSimulationButton;

    @FXML
    private LineChart<Number, Number> simulationChart;

    private InferenceService inferenceService;

    public SimulationViewController() {
        this.inferenceService = new InferenceService();
    }

    @FXML
    public void initialize() {
        valueSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            modifiedValueField.setText(String.valueOf(newValue.intValue()));
        });

        runSimulationButton.setOnAction(event -> runSimulation());
    }

    private void runSimulation() {
        double modifiedValue = valueSlider.getValue();
        // Call the inference service with the modified value
        InferenceResult result = inferenceService.simulate(modifiedValue);
        updateChart(result);
    }

    private void updateChart(InferenceResult result) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        // Populate the series with data from the result
        for (int i = 0; i < result.getProbabilities().length; i++) {
            series.getData().add(new XYChart.Data<>(i, result.getProbabilities()[i]));
        }
        simulationChart.getData().clear();
        simulationChart.getData().add(series);
    }
}