package com.factory.ml.model;

/**
 * Represents a data validation error.
 * 
 * Contains information about validation failures including the column name,
 * error message, and row index where the error occurred. Used by DataValidator
 * to report validation issues.
 * 
 * @see DataValidator
 * @see Schema
 */
public class ValidationError {
    private String columnName;
    private String errorMessage;
    private int rowIndex;

    /**
     * Constructs a ValidationError with error details.
     * 
     * @param columnName Name of the column with validation error
     * @param errorMessage Description of the validation error
     * @param rowIndex Zero-based index of the row with error
     */
    public ValidationError(String columnName, String errorMessage, int rowIndex) {
        this.columnName = columnName;
        this.errorMessage = errorMessage;
        this.rowIndex = rowIndex;
    }

    /**
     * Gets the name of the column with validation error.
     * 
     * @return Column name
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Gets the validation error message.
     * 
     * @return Error message describing the validation failure
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Gets the row index where the error occurred.
     * 
     * @return Zero-based row index
     */
    public int getRowIndex() {
        return rowIndex;
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "columnName='" + columnName + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", rowIndex=" + rowIndex +
                '}';
    }
}