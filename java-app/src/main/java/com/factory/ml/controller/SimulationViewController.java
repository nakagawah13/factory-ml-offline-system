package com.factory.ml.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

/**
 * Controller for the Simulation view in the Factory ML GUI.
 * 
 * This controller manages the what-if simulation interface, allowing users
 * to modify input parameters and visualize how changes affect model predictions.
 * 
 * <p>Project Context:
 * Part of the factory-ml-offline-system JavaFX GUI. Provides interactive
 * simulation capabilities for exploring model behavior.
 * 
 * @see SimulationService
 * @see InferenceService
 */
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

    /**
     * Constructs a SimulationViewController.
     * 
     * Initializes the inference service for running simulations.
     */
    public SimulationViewController() {
        this.inferenceService = new InferenceService();
    }

    /**
     * Initializes the controller after FXML loading.
     * 
     * Sets up slider listener to update text field and button event handler
     * for running simulations.
     */
    @FXML
    public void initialize() {
        valueSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            modifiedValueField.setText(String.valueOf(newValue.intValue()));
        });

        runSimulationButton.setOnAction(event -> runSimulation());
    }

    /**
     * Runs a simulation with the modified input value.
     * 
     * Retrieves the slider value, performs inference with the modified
     * parameter, and updates the chart with results.
     */
    private void runSimulation() {
        double modifiedValue = valueSlider.getValue();
        // Call the inference service with the modified value
        InferenceResult result = inferenceService.simulate(modifiedValue);
        updateChart(result);
    }

    /**
     * Updates the simulation chart with inference results.
     * 
     * Populates the line chart with probability data from the inference result.
     * 
     * @param result The inference result containing prediction probabilities
     */
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