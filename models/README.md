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

Contains test models and reports intended for development and local testing.

**Status**: This directory may be present locally for development and testing, but it is not guaranteed to be tracked in Git because `models/` is ignored by default. Do not assume the `test/` directory exists in all clones of the repository.

**If present (local workflows)**:
- Test models for unit tests and integration tests (example: `model.joblib`)
- Training reports and evaluation metrics (example: `reports/training_summary.json`)
- Experimental models not ready for production

**Notes**:
- Because `models/` is excluded by `.gitignore`, test fixtures under `models/test/` are typically managed locally or via CI artifacts rather than committed to the repo.
- If you need reproducible test fixtures in the repository, consider placing small, non-sensitive fixtures under `tests/` or `data/`, or use an external artifact store (e.g. MLflow, DVC, S3/GCS) and document retrieval steps in `docs/`.
- To track a file inside `models/` (strongly discouraged for large binaries), use `git add -f <file>` and document why the force-add is necessary.

## Git Management

**Note**: The `models/` directory is excluded in `.gitignore` to prevent large model files from being tracked in Git.

Summary of Git behavior for `models/`:

1. **Default**: All files under `models/` are ignored by Git because of the `.gitignore` rule.

2. **Exception — `.gitkeep` files**: `.gitkeep` files are explicitly un-ignored in `.gitignore` and can be added without `-f`. Use these to keep empty directories in the repository.

3. **Exception — other files**: Actual model binaries and large artifacts (e.g., `.onnx`, `.joblib`) remain ignored by default. If you truly need to track a specific file in Git (not recommended for large artifacts), you must use `git add -f <file>` to force-add it locally.

Recommendations:

- Do not commit model binaries to Git for production workloads. Instead, manage them with a model registry or external storage (MLflow, DVC, S3/GCS, etc.).
- Use `.gitkeep` only for directory presence; avoid trying to track other files under `models/` unless absolutely necessary.
- Consider revising the `.gitignore` policy if your workflow requires versioning small artifacts in `models/`.

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
