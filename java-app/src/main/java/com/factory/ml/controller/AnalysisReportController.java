package com.factory.ml.controller;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.scene.control.Label;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AnalysisReportController {

    @FXML
    private WebView reportWebView;

    @FXML
    private Label statusLabel;

    public void loadReport(String reportPath) {
        try {
            File reportFile = new File(reportPath);
            if (reportFile.exists()) {
                String content = new String(Files.readAllBytes(Paths.get(reportPath)));
                reportWebView.getEngine().loadContent(content);
                statusLabel.setText("Report loaded successfully.");
            } else {
                statusLabel.setText("Report file not found.");
            }
        } catch (Exception e) {
            statusLabel.setText("Error loading report: " + e.getMessage());
        }
    }
}