package com.factory.ml.service;

import ai.onnxruntime.*;
import com.factory.ml.model.InferenceResult;
import com.factory.ml.model.InputRow;
import com.factory.ml.util.ProcessExecutor;

import java.nio.FloatBuffer;
import java.util.Arrays;

public class InferenceService {
    private OrtEnvironment env;
    private OrtSession currentSession;
    private OrtSession candidateSession; // Shadow Test用

    public InferenceService(String modelPath) throws OrtException {
        this.env = OrtEnvironment.getEnvironment();
        this.currentSession = env.createSession(modelPath, new OrtSession.SessionOptions());
    }

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

    public void setCandidateModel(String modelPath) throws OrtException {
        if (candidateSession != null) {
            candidateSession.close();
        }
        candidateSession = env.createSession(modelPath, new OrtSession.SessionOptions());
    }

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