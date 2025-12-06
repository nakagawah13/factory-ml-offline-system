package com.factory.ml.model;

public class InferenceResult {
    private String label;
    private double[] probabilities;

    public InferenceResult(String label, double[] probabilities) {
        this.label = label;
        this.probabilities = probabilities;
    }

    public String getLabel() {
        return label;
    }

    public double[] getProbabilities() {
        return probabilities;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("InferenceResult{");
        sb.append("label='").append(label).append('\'');
        sb.append(", probabilities=").append(java.util.Arrays.toString(probabilities));
        sb.append('}');
        return sb.toString();
    }
}