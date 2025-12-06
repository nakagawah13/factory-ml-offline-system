package com.factory.ml.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Utility for executing external system commands.
 * 
 * Provides static methods for running shell commands and capturing their
 * output. Used primarily for invoking the Python training pipeline from
 * the Java GUI.
 * 
 * <p>Project Context:
 * Part of the factory-ml-offline-system utility layer. Enables communication
 * between the Java GUI and Python training scripts.
 * 
 * <p>Security Note:
 * Commands are executed directly without validation. Ensure commands are
 * from trusted sources to prevent security vulnerabilities.
 */
public class ProcessExecutor {

    /**
     * Executes a system command and returns its output.
     * 
     * Runs the command, captures stdout/stderr, and waits for completion.
     * Throws an exception if the command exits with a non-zero status.
     * 
     * @param command Command string to execute (space-separated)
     * @return Command output as a string
     * @throws IOException if command execution fails
     * @throws InterruptedException if waiting for process is interrupted
     */
    public static String executeCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("Command execution failed with exit code: " + exitCode);
        }

        return output.toString().trim();
    }
}