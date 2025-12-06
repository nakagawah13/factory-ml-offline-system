package com.factory.ml.model;

import java.util.HashMap;
import java.util.Map;

public class InputRow {
    private Map<String, Object> features;

    public InputRow() {
        features = new HashMap<>();
    }

    public void setFeature(String key, Object value) {
        features.put(key, value);
    }

    public Object getFeature(String key) {
        return features.get(key);
    }

    public Map<String, Object> getFeatures() {
        return features;
    }
}