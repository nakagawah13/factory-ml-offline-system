package com.factory.ml.service;

import com.factory.ml.model.InputRow;
import com.factory.ml.model.InferenceResult;

import java.util.Map;

public class SimulationService {
    
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
        InferenceService inferenceService = new InferenceService();
        return inferenceService.predict(modifiedRow.getFloatInput(), modifiedRow.getStringInput(), false);
    }
}