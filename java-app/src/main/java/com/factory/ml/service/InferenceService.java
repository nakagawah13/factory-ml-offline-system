package com.factory.ml.service;

import ai.onnxruntime.*;
import com.factory.ml.model.InferenceResult;
import com.factory.ml.model.InputRow;
import com.factory.ml.util.ProcessExecutor;

import java.nio.FloatBuffer;
import java.util.Arrays;

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

        OrtTensor floatTensor = OrtTensor.createTensor(env, floats, new long[]{1, floats.remaining()});
        OrtTensor stringTensor = OrtTensor.createTensor(env, strings, new long[]{1, strings.length});

        OrtInputs inputs = new OrtInputs();
        inputs.add("float_input", floatTensor);
        inputs.add("string_input", stringTensor);

        OrtOutputs outputs = session.run(inputs);
        float[] probabilities = (float[]) outputs.get("probabilities").getValue();
        String label = (String) outputs.get("label").getValue();

        return new InferenceResult(label, probabilities);
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
     */
    public void close() {
        if (currentSession != null) {
            currentSession.close();
        }
        if (candidateSession != null) {
            candidateSession.close();
        }
        env.close();
    }
}