package com.factory.ml.model;

/**
 * Represents the result of a model inference operation.
 * 
 * Contains the predicted label and probability scores for each class.
 * Used to communicate inference results from the InferenceService to
 * controllers and the UI.
 * 
 * @see InferenceService
 */
public class InferenceResult {
    private String label;
    private double[] probabilities;

    /**
     * Constructs an InferenceResult with label and probabilities.
     * 
     * @param label Predicted class label
     * @param probabilities Probability scores for each class
     */
    public InferenceResult(String label, double[] probabilities) {
        this.label = label;
        this.probabilities = probabilities;
    }

    /**
     * Gets the predicted class label.
     * 
     * @return Predicted label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the probability scores for each class.
     * 
     * @return Array of probability scores
     */
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