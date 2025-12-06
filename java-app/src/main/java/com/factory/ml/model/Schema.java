package com.factory.ml.model;

import java.util.List;

/**
 * Represents the schema definition for data validation.
 * 
 * This class defines the structure and constraints for CSV data columns,
 * including data types, required fields, allowed values, and numeric ranges.
 * Used by the DataValidator service to validate incoming data.
 * 
 * <p>Project Context:
 * Core data model in the factory-ml-offline-system Java application. Loaded
 * from config/schema.json to define the expected structure of production data.
 * 
 * <p>Usage Example:
 * <pre>
 * Schema schema = new Schema("1.0", Arrays.asList(
 *     new Schema.Column("temperature", "float", true, -50.0, 150.0),
 *     new Schema.Column("pressure", "float", true, 0.0, 1000.0)
 * ));
 * </pre>
 * 
 * @see DataValidator
 */
public class Schema {
    private String version;
    private List<Column> columns;

    /**
     * Constructs a Schema with version and column definitions.
     * 
     * @param version Schema version identifier (e.g., "1.0")
     * @param columns List of column definitions
     */
    public Schema(String version, List<Column> columns) {
        this.version = version;
        this.columns = columns;
    }

    /**
     * Gets the schema version.
     * 
     * @return Schema version identifier
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the schema version.
     * 
     * @param version Schema version identifier
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the list of column definitions.
     * 
     * @return List of column definitions
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * Sets the list of column definitions.
     * 
     * @param columns List of column definitions
     */
    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    /**
     * Represents a single column definition in the schema.
     * 
     * Defines the name, type, and validation constraints for a data column.
     * Supports various validation types including required fields, format
     * patterns, allowed values lists, and numeric ranges.
     */
    public static class Column {
        private String name;
        private String type;
        private boolean required;
        private String format;
        private List<String> allowedValues;
        private Double min;
        private Double max;

        /**
         * Constructs a basic column definition.
         * 
         * @param name Column name
         * @param type Column data type (e.g., "string", "int", "float")
         * @param required Whether the column is required
         */
        public Column(String name, String type, boolean required) {
            this.name = name;
            this.type = type;
            this.required = required;
        }

        /**
         * Constructs a column with format validation.
         * 
         * @param name Column name
         * @param type Column data type
         * @param required Whether the column is required
         * @param format Format pattern (e.g., date format, regex)
         */
        public Column(String name, String type, boolean required, String format) {
            this(name, type, required);
            this.format = format;
        }

        /**
         * Constructs a column with allowed values constraint.
         * 
         * @param name Column name
         * @param type Column data type
         * @param required Whether the column is required
         * @param allowedValues List of allowed values
         */
        public Column(String name, String type, boolean required, List<String> allowedValues) {
            this(name, type, required);
            this.allowedValues = allowedValues;
        }

        /**
         * Constructs a column with numeric range constraints.
         * 
         * @param name Column name
         * @param type Column data type
         * @param required Whether the column is required
         * @param min Minimum allowed value
         * @param max Maximum allowed value
         */
        public Column(String name, String type, boolean required, Double min, Double max) {
            this(name, type, required);
            this.min = min;
            this.max = max;
        }

        // Getters and Setters
        
        /**
         * Gets the column name.
         * 
         * @return Column name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the column name.
         * 
         * @param name Column name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Gets the column data type.
         * 
         * @return Column data type (e.g., "string", "int", "float")
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the column data type.
         * 
         * @param type Column data type
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * Checks if the column is required.
         * 
         * @return true if column is required, false otherwise
         */
        public boolean isRequired() {
            return required;
        }

        /**
         * Sets whether the column is required.
         * 
         * @param required true if column should be required
         */
        public void setRequired(boolean required) {
            this.required = required;
        }

        /**
         * Gets the format pattern for validation.
         * 
         * @return Format pattern (e.g., date format, regex)
         */
        public String getFormat() {
            return format;
        }

        /**
         * Sets the format pattern for validation.
         * 
         * @param format Format pattern
         */
        public void setFormat(String format) {
            this.format = format;
        }

        /**
         * Gets the list of allowed values.
         * 
         * @return List of allowed values, or null if not constrained
         */
        public List<String> getAllowedValues() {
            return allowedValues;
        }

        /**
         * Sets the list of allowed values.
         * 
         * @param allowedValues List of allowed values
         */
        public void setAllowedValues(List<String> allowedValues) {
            this.allowedValues = allowedValues;
        }

        /**
         * Gets the minimum allowed value for numeric columns.
         * 
         * @return Minimum value, or null if not constrained
         */
        public Double getMin() {
            return min;
        }

        /**
         * Sets the minimum allowed value for numeric columns.
         * 
         * @param min Minimum value
         */
        public void setMin(Double min) {
            this.min = min;
        }

        /**
         * Gets the maximum allowed value for numeric columns.
         * 
         * @return Maximum value, or null if not constrained
         */
        public Double getMax() {
            return max;
        }

        /**
         * Sets the maximum allowed value for numeric columns.
         * 
         * @param max Maximum value
         */
        public void setMax(Double max) {
            this.max = max;
        }
    }
}