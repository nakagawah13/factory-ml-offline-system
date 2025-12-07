package com.factory.ml.service;

import ai.onnxruntime.OrtException;
import com.factory.ml.model.InputRow;
import com.factory.ml.model.InferenceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Simulates model predictions with modified input parameters.
 * 
 * This service allows users to modify input features and observe how
 * the model's prediction changes. Useful for what-if analysis and
 * understanding model behavior.
 * 
 * <p>Supports dependency injection of InferenceService to enable
 * service reuse and avoid repeated model loading overhead.
 * 
 * <p>Project Context:
 * Part of the factory-ml-offline-system simulation feature. Used by
 * SimulationViewController to provide interactive parameter tuning.
 * 
 * @see InferenceService
 * @see InputRow
 */
public class SimulationService {
    private static final Logger logger = LoggerFactory.getLogger(SimulationService.class);
    private final InferenceService inferenceService;
    
    /**
     * Constructs a SimulationService with dependency injection.
     * 
     * Accepts an InferenceService instance for reuse across multiple
     * simulation calls, avoiding repeated model loading.
     * 
     * @param inferenceService Pre-initialized inference service
     */
    public SimulationService(InferenceService inferenceService) {
        this.inferenceService = inferenceService;
    }
    
    /**
     * Simulates inference with modified input parameters.
     * 
     * Creates a copy of the original input, applies modifications,
     * and performs inference to show how changes affect predictions.
     * Reuses the injected InferenceService for efficiency.
     * 
     * @param original Original input row
     * @param modifications Map of feature names to modified values
     * @return Inference result with the modified input
     * @throws RuntimeException if inference fails
     */
    public InferenceResult simulate(InputRow original, Map<String, Object> modifications) {
        logger.debug("Starting simulation with {} modifications", modifications.size());
        
        // Create a copy of the original input row
        InputRow modifiedRow = new InputRow(original);
        
        // Apply modifications to the copied row
        for (Map.Entry<String, Object> entry : modifications.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            modifiedRow.setValue(key, value);
            logger.trace("Modified feature {}: {}", key, value);
        }
        
        // Perform inference using the injected service
        try {
            InferenceResult result = inferenceService.predict(
                modifiedRow.getFloatInput(), 
                modifiedRow.getStringInput(), 
                false
            );
            logger.debug("Simulation completed successfully. Label: {}", result.getLabel());
            return result;
        } catch (OrtException e) {
            logger.error("Simulation inference failed", e);
            throw new RuntimeException("Simulation inference failed: " + e.getMessage(), e);
        }
    }
}