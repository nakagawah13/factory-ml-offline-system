package com.factory.ml.service;

import com.factory.ml.model.InputRow;

import java.nio.FloatBuffer;
import java.util.List;

public class FeatureTransformer {

    public FloatBuffer createFloatInput(List<InputRow> rows) {
        // Convert InputRow data to FloatBuffer for ONNX model input
        // Implementation of date parsing and transformation logic goes here
        return FloatBuffer.allocate(0); // Placeholder for actual implementation
    }

    public String[] createStringInput(List<InputRow> rows) {
        // Convert InputRow data to String array for ONNX model input
        // Implementation of transformation logic goes here
        return new String[0]; // Placeholder for actual implementation
    }
}