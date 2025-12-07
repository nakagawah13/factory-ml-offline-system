package com.factory.ml.service;

import ai.onnxruntime.OrtException;
import com.factory.ml.model.InputRow;
import com.factory.ml.model.InferenceResult;

import java.util.Map;

/**
 * Simulates model predictions with modified input parameters.
 * 
 * This service allows users to modify input features and observe how
 * the model's prediction changes. Useful for what-if analysis and
 * understanding model behavior.
 * 
 * <p>Project Context:
 * Part of the factory-ml-offline-system simulation feature. Used by
 * SimulationViewController to provide interactive parameter tuning.
 * 
 * @see InferenceService
 * @see InputRow
 */
public class SimulationService {
    
    /**
     * Simulates inference with modified input parameters.
     * 
     * Creates a copy of the original input, applies modifications,
     * and performs inference to show how changes affect predictions.
     * 
     * @param original Original input row
     * @param modifications Map of feature names to modified values
     * @return Inference result with the modified input
     * @throws RuntimeException if inference fails
     */
    public InferenceResult simulate(InputRow original, Map<String, Object> modifications) {
        // Create a copy of the original input row
        InputRow modifiedRow = new InputRow(original);
        
        // Apply modifications to the copied row
        for (Map.Entry<String, Object> entry : modifications.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            modifiedRow.setValue(key, value);
        }
        
        // Perform inference using the modified row
        // TODO: Model path should be configurable or injected via constructor
        try {
            InferenceService inferenceService = new InferenceService("models/current/model.onnx");
            InferenceResult result = inferenceService.predict(
                modifiedRow.getFloatInput(), 
                modifiedRow.getStringInput(), 
                false
            );
            inferenceService.close();
            return result;
        } catch (OrtException e) {
            throw new RuntimeException("Simulation inference failed: " + e.getMessage(), e);
        }
    }
}