package com.factory.ml.model;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a single row of input data for model inference.
 * 
 * Stores feature values as key-value pairs where keys are feature names
 * and values are the corresponding feature values. Supports various data
 * types for feature values.
 * 
 * Uses LinkedHashMap to maintain insertion order, ensuring consistent
 * feature ordering for ONNX model input.
 * 
 * @see FeatureTransformer
 * @see InferenceService
 */
public class InputRow {
    private Map<String, Object> features;

    /**
     * Constructs an empty InputRow.
     * 
     * Initializes the features map with LinkedHashMap to preserve
     * insertion order for consistent feature extraction.
     */
    public InputRow() {
        features = new LinkedHashMap<>();
    }

    /**
     * Constructs an InputRow as a copy of another InputRow.
     * 
     * Creates a deep copy of the features map from the provided InputRow.
     * Used for simulation scenarios where modifications are needed without
     * affecting the original data. Preserves insertion order.
     * 
     * @param other The InputRow to copy from
     */
    public InputRow(InputRow other) {
        this.features = new LinkedHashMap<>(other.features);
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
     * Sets a value for a specific column.
     * 
     * Alias for setFeature() to provide alternative naming convention.
     * 
     * @param column Column name
     * @param value Value to set
     */
    public void setValue(String column, Object value) {
        features.put(column, value);
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

    /**
     * Extracts float input values from features.
     * 
     * Iterates through all features and extracts numeric values, converting
     * them to float type. Returns a FloatBuffer suitable for ONNX model input.
     * 
     * @return FloatBuffer containing all numeric feature values
     */
    public FloatBuffer getFloatInput() {
        List<Float> floatValues = new ArrayList<>();
        for (Object value : features.values()) {
            if (value instanceof Number) {
                floatValues.add(((Number) value).floatValue());
            }
        }
        FloatBuffer buffer = FloatBuffer.allocate(floatValues.size());
        for (Float f : floatValues) {
            buffer.put(f);
        }
        buffer.flip();
        return buffer;
    }

    /**
     * Extracts string input values from features.
     * 
     * Iterates through all features and extracts string values, returning
     * them as a String array suitable for ONNX model input.
     * 
     * @return String array containing all string feature values
     */
    public String[] getStringInput() {
        List<String> stringValues = new ArrayList<>();
        for (Object value : features.values()) {
            if (value instanceof String) {
                stringValues.add((String) value);
            }
        }
        return stringValues.toArray(new String[0]);
    }
}