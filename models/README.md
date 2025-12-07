# Models Directory

This directory contains machine learning models for the factory-ml-offline-system.

## Directory Structure

```
models/
├── current/          # Currently active models
├── archive/          # Archived models
└── test/            # Test models for development
```

## Current Model Directory (`current/`)

Contains the currently active machine learning model used in production.

**Expected files**:
- `model.onnx`: Current ONNX model for inference
- `model.joblib`: Scikit-learn pipeline (for Python inference)
- `metadata.json`: Model version and training information

**Usage**:
- Java application (`InferenceService`) loads ONNX model from this location
- Python scripts may use joblib model for validation

## Archive Directory (`archive/`)

Contains archived machine learning models for version history and rollback capability.

**Naming convention**: `model_YYYYMMDD_HHMMSS.onnx`

**Purpose**:
- Maintain model version history
- Enable quick rollback to previous versions if needed
- Compare performance across different model versions

**Automatic archiving**: Models are automatically moved here when a new model is deployed to `current/`.

## Test Directory (`test/`)

Contains test models and reports for development and testing purposes.

**Contents**:
- Test models for unit tests and integration tests
- Training reports and evaluation metrics
- Experimental models not ready for production

## Git Management

**Note**: The `models/` directory is excluded in `.gitignore` to prevent large model files from being tracked in Git.

- Only `.gitkeep` files are tracked to maintain directory structure
- `.gitkeep` files are explicitly un-ignored in `.gitignore` and can be added normally (no `-f` needed because of the un-ignore rule)
- Actual model files (`.onnx`, `.joblib`) should be managed separately (e.g., MLflow, DVC, or cloud storage)
- Other files (such as `.onnx`, `.joblib` models) require `git add -f <file>` to force-add if needed locally

## Model Deployment Workflow

1. **Training**: Python trainer generates model in `models/current/`
2. **Validation**: Test model performance before deployment
3. **Archiving**: Previous model is moved to `models/archive/` with timestamp
4. **Deployment**: New model is placed in `models/current/`
5. **Verification**: Java application reloads model and verifies inference

## Related Files

- `python-trainer/src/trainer/onnx_converter.py`: ONNX model export
- `java-app/src/main/java/com/factory/ml/service/InferenceService.java`: Model loading
- `config/app_settings.json`: Model configuration settings
