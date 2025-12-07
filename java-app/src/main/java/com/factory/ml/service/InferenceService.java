package com.factory.ml.service;

import ai.onnxruntime.*;
import com.factory.ml.model.InferenceResult;
import com.factory.ml.model.InputRow;
import com.factory.ml.util.ProcessExecutor;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ONNX model inference service.
 * 
 * This service manages ONNX Runtime sessions for model inference. Supports
 * shadow testing by maintaining both current and candidate model sessions.
 * Provides predictions using float and string input tensors.
 * 
 * <p>Project Context:
 * Core component of the factory-ml-offline-system Java application. Loads
 * ONNX models and performs real-time inference on factory production data.
 * Supports A/B testing through shadow deployment of candidate models.
 * 
 * <p>Usage Example:
 * <pre>
 * InferenceService service = new InferenceService("models/current/model.onnx");
 * InferenceResult result = service.predict(floatBuffer, stringArray, false);
 * System.out.println("Prediction: " + result.getLabel());
 * service.close();
 * </pre>
 * 
 * @see InferenceResult
 * @see ModelManagerService
 */
public class InferenceService {
    private OrtEnvironment env;
    private OrtSession currentSession;
    private OrtSession candidateSession; // Shadow Test用

    /**
     * Constructs an InferenceService with the specified model.
     * 
     * Initializes ONNX Runtime environment and creates a session with the
     * provided model file. The model must be in ONNX format.
     * 
     * @param modelPath Path to the ONNX model file
     * @throws OrtException if model loading fails
     */
    public InferenceService(String modelPath) throws OrtException {
        this.env = OrtEnvironment.getEnvironment();
        this.currentSession = env.createSession(modelPath, new OrtSession.SessionOptions());
    }

    /**
     * Performs inference using the specified model.
     * 
     * Accepts float and string inputs as tensors, runs inference through
     * ONNX Runtime, and returns prediction results with probabilities.
     * 
     * @param floats Float buffer containing numeric features
     * @param strings String array containing categorical features
     * @param useCandidate If true, uses candidate model; otherwise uses current model
     * @return Inference result containing predicted label and probabilities
     * @throws OrtException if inference execution fails
     */
    public InferenceResult predict(FloatBuffer floats, String[] strings, boolean useCandidate) throws OrtException {
        OrtSession session = useCandidate ? candidateSession : currentSession;

        // Convert FloatBuffer to float array for ONNX tensor creation
        // Note: This involves FloatBuffer → float[] → FloatBuffer conversion.
        // The intermediate array is required because OnnxTensor.createTensor()
        // needs explicit shape specification (new long[]{1, length}).
        // Direct FloatBuffer pass-through is not supported by ONNX Runtime API.
        float[] floatArray = new float[floats.remaining()];
        floats.get(floatArray);
        floats.rewind();

        // Create tensors using ONNX Runtime 1.15.0 API
        // Use try-with-resources to ensure tensors and result are properly closed
        // even if an exception occurs during inference
        try (OnnxTensor floatTensor = OnnxTensor.createTensor(env, FloatBuffer.wrap(floatArray), new long[]{1, floatArray.length});
             OnnxTensor stringTensor = OnnxTensor.createTensor(env, strings)) {

            // Create input map
            Map<String, OnnxTensor> inputs = new HashMap<>();
            inputs.put("float_input", floatTensor);
            inputs.put("string_input", stringTensor);

            // Run inference
            try (OrtSession.Result result = session.run(inputs)) {
                // Extract results with type conversion
                OnnxValue probValue = result.get("probabilities").get();
                double[] probabilities = convertToDoubleArray(probValue);

                OnnxValue labelValue = result.get("label").get();
                String label = (String) labelValue.getValue();

                return new InferenceResult(label, probabilities);
            }
        }
    }

    /**
     * Converts ONNX value to double array.
     * 
     * Handles type conversion from double[] or float[] to double[].
     * ONNX Runtime may return different numeric types depending on the model.
     * 
     * CLARIFICATION: Despite the method name, this method handles BOTH float[] and double[].
     * - If ONNX model outputs float[] (common for float32 models), converts to double[]
     * - If ONNX model outputs double[] (less common), returns as-is
     * - Always returns double[] for consistent downstream processing
     * 
     * @param value ONNX value to convert (must be double[] or float[])
     * @return Double array representation of the ONNX value
     * @throws OrtException if value extraction fails
     * @throws IllegalStateException if value is neither double[] nor float[]
     */
    private double[] convertToDoubleArray(OnnxValue value) throws OrtException {
        Object obj = value.getValue();
        if (obj instanceof double[]) {
            return (double[]) obj;
        } else if (obj instanceof float[]) {
            float[] floats = (float[]) obj;
            double[] doubles = new double[floats.length];
            for (int i = 0; i < floats.length; i++) {
                doubles[i] = floats[i];
            }
            return doubles;
        }
        throw new IllegalStateException("Unexpected probability type: " + obj.getClass());
    }

    /**
     * Sets a candidate model for shadow testing.
     * 
     * Loads a new candidate model that can be used for A/B testing alongside
     * the current production model. Closes any existing candidate session first.
     * 
     * @param modelPath Path to the candidate ONNX model file
     * @throws OrtException if model loading fails
     */
    public void setCandidateModel(String modelPath) throws OrtException {
        if (candidateSession != null) {
            candidateSession.close();
        }
        candidateSession = env.createSession(modelPath, new OrtSession.SessionOptions());
    }

    /**
     * Closes all ONNX Runtime sessions and environment.
     * 
     * Releases resources held by current and candidate model sessions.
     * Should be called when the service is no longer needed.
     * 
     * @throws OrtException if resource cleanup fails
     */
    public void close() throws OrtException {
        if (currentSession != null) {
            currentSession.close();
        }
        if (candidateSession != null) {
            candidateSession.close();
        }
        env.close();
    }
}