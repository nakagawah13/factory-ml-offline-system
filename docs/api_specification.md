# API Specification for Factory ML Offline System

## Overview

This document outlines the API specifications for the Factory ML Offline System, which operates in an offline environment to perform machine learning tasks using structured data. The system consists of a Java-based GUI application for inference and simulation, and a Python-based training tool for model management and analysis.

## API Endpoints

### 1. Data Validation

**Endpoint:** `/api/validate`

**Method:** POST

**Description:** Validates the uploaded CSV/Excel file against the defined schema.

**Request Body:**
```json
{
  "file_path": "string"
}
```

**Response:**
- **200 OK:** Validation successful.
```json
{
  "status": "success",
  "errors": []
}
```
- **400 Bad Request:** Validation failed.
```json
{
  "status": "error",
  "errors": [
    {
      "row": 1,
      "message": "Invalid data type for column 'sensor_val_1'"
    }
  ]
}
```

### 2. Inference

**Endpoint:** `/api/inference`

**Method:** POST

**Description:** Runs inference on the validated data.

**Request Body:**
```json
{
  "float_input": [[0.5, 0.3, ...]],
  "string_input": [["A", "B", ...]]
}
```

**Response:**
- **200 OK:** Inference successful.
```json
{
  "label": "A",
  "probabilities": [0.7, 0.2, 0.1]
}
```
- **500 Internal Server Error:** Inference failed.

### 3. Model Training

**Endpoint:** `/api/train`

**Method:** POST

**Description:** Initiates the training process with the selected training data.

**Request Body:**
```json
{
  "training_data_path": "string"
}
```

**Response:**
- **202 Accepted:** Training started.
```json
{
  "status": "training_started",
  "message": "Training process has been initiated."
}
```
- **400 Bad Request:** Training data validation failed.

### 4. Simulation

**Endpoint:** `/api/simulate`

**Method:** POST

**Description:** Performs a What-If simulation based on modified input values.

**Request Body:**
```json
{
  "original_input": {
    "float_input": [0.5, 0.3, ...],
    "string_input": ["A", "B", ...]
  },
  "modifications": {
    "float_input": [0.6, 0.2, ...]
  }
}
```

**Response:**
- **200 OK:** Simulation successful.
```json
{
  "new_label": "B",
  "new_probabilities": [0.4, 0.5, 0.1]
}
```

## Error Handling

All API responses should include appropriate HTTP status codes and error messages. Common error responses include:

- **400 Bad Request:** Invalid input data.
- **404 Not Found:** Endpoint not found.
- **500 Internal Server Error:** Unexpected server error.

## Conclusion

This API specification serves as a guide for developers to interact with the Factory ML Offline System. It provides a clear understanding of the available endpoints, request/response formats, and error handling mechanisms.