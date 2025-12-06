package com.factory.ml.model;

import java.util.List;

public class Schema {
    private String version;
    private List<Column> columns;

    public Schema(String version, List<Column> columns) {
        this.version = version;
        this.columns = columns;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public static class Column {
        private String name;
        private String type;
        private boolean required;
        private String format;
        private List<String> allowedValues;
        private Double min;
        private Double max;

        public Column(String name, String type, boolean required) {
            this.name = name;
            this.type = type;
            this.required = required;
        }

        public Column(String name, String type, boolean required, String format) {
            this(name, type, required);
            this.format = format;
        }

        public Column(String name, String type, boolean required, List<String> allowedValues) {
            this(name, type, required);
            this.allowedValues = allowedValues;
        }

        public Column(String name, String type, boolean required, Double min, Double max) {
            this(name, type, required);
            this.min = min;
            this.max = max;
        }

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public List<String> getAllowedValues() {
            return allowedValues;
        }

        public void setAllowedValues(List<String> allowedValues) {
            this.allowedValues = allowedValues;
        }

        public Double getMin() {
            return min;
        }

        public void setMin(Double min) {
            this.min = min;
        }

        public Double getMax() {
            return max;
        }

        public void setMax(Double max) {
            this.max = max;
        }
    }
}