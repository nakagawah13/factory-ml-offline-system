package com.factory.ml.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a single row of input data for model inference.
 * 
 * Stores feature values as key-value pairs where keys are feature names
 * and values are the corresponding feature values. Supports various data
 * types for feature values.
 * 
 * @see FeatureTransformer
 * @see InferenceService
 */
public class InputRow {
    private Map<String, Object> features;

    /**
     * Constructs an empty InputRow.
     * 
     * Initializes the features map to store feature name-value pairs.
     */
    public InputRow() {
        features = new HashMap<>();
    }

    /**
     * Sets a feature value.
     * 
     * @param key Feature name
     * @param value Feature value
     */
    public void setFeature(String key, Object value) {
        features.put(key, value);
    }

    /**
     * Gets a feature value by name.
     * 
     * @param key Feature name
     * @return Feature value, or null if not found
     */
    public Object getFeature(String key) {
        return features.get(key);
    }

    /**
     * Gets all features as a map.
     * 
     * @return Map of feature names to values
     */
    public Map<String, Object> getFeatures() {
        return features;
    }
}