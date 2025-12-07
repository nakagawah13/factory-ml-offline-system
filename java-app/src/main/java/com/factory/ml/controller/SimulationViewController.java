package com.factory.ml.controller;

import ai.onnxruntime.OrtException;
import com.factory.ml.model.InferenceResult;
import com.factory.ml.service.InferenceService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(SimulationViewController.class);

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
        try {
            this.inferenceService = new InferenceService("models/current/model.onnx");
        } catch (OrtException e) {
            throw new RuntimeException("Failed to initialize InferenceService", e);
        }
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
        // TODO: Implement proper simulation logic with SimulationService
        // For now, create a simple demo prediction
        try {
            // Create dummy input for demonstration
            java.nio.FloatBuffer floatInput = java.nio.FloatBuffer.allocate(1);
            floatInput.put((float) modifiedValue);
            floatInput.flip();
            
            String[] stringInput = new String[0];
            
            InferenceResult result = inferenceService.predict(floatInput, stringInput, false);
            updateChart(result);
        } catch (OrtException e) {
            logger.error("Simulation failed: {}", e.getMessage(), e);
        }
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