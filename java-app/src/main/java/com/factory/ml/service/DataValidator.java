package com.factory.ml.service;

import com.factory.ml.model.ValidationError;
import com.factory.ml.model.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates CSV data against a schema definition.
 * 
 * This service validates incoming CSV data to ensure it meets the required
 * schema specifications including type checks, required fields, and value
 * constraints. Returns a list of validation errors for any issues found.
 * 
 * <p>Project Context:
 * Part of the factory-ml-offline-system Java application. Used to validate
 * user input before sending data to the inference service or training pipeline.
 * 
 * @see Schema
 * @see ValidationError
 */
public class DataValidator {
    
    /**
     * Validates CSV rows against the provided schema.
     * 
     * Checks each row for type correctness, required fields, and value constraints
     * defined in the schema. Returns a list of all validation errors found.
     * 
     * @param csvRows List of CSV rows where each row is an array of strings
     * @param schema Schema definition containing column specifications
     * @return List of validation errors, empty if validation passes
     */
    public List<ValidationError> validate(List<String[]> csvRows, Schema schema) {
        List<ValidationError> errors = new ArrayList<>();

        for (int i = 0; i < csvRows.size(); i++) {
            String[] row = csvRows.get(i);
            // バリデーションロジックをここに実装
            // 例: 型チェック、必須フィールドの確認など
            // エラーが見つかった場合は、ValidationErrorを作成してerrorsリストに追加
        }

        return errors;
    }
}