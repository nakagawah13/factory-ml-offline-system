# Python Trainer README

# Factory ML Offline System - Python Trainer

This directory contains the Python components of the Factory ML Offline System, which is designed to operate in an offline environment within a factory setting. The system utilizes structured data for machine learning tasks, including inference, model training, and analysis.

## Directory Structure

- **src/trainer/**: Contains the main training modules.
  - **main.py**: Entry point for the training process.
  - **data_loader.py**: Module for loading data from CSV/Excel files.
  - **preprocessor.py**: Module for preprocessing data, including validation and transformation.
  - **model_trainer.py**: Module for training machine learning models.
  - **onnx_converter.py**: Module for converting trained models to ONNX format.
  - **report_generator.py**: Module for generating analysis reports.

- **src/analysis/**: Contains modules for analyzing model performance and data drift.
  - **shap_analyzer.py**: Module for SHAP analysis to interpret model predictions.
  - **drift_detector.py**: Module for detecting data drift in incoming data.
  - **metrics_calculator.py**: Module for calculating various performance metrics.

## Requirements

### Installation with uv (Recommended)

```bash
# Install all dependencies from project root
cd ..
uv sync
```

### Installation with pip (Fallback for offline environments)

```bash
# Install from auto-generated requirements.txt
pip install -r requirements.txt
```

**Note**: The `requirements.txt` file is automatically generated from the root `pyproject.toml` using `uv export`.

## Usage

1. **Training the Model**: 
   - Run the `main.py` script to start the training process. Ensure that the training data is available in the specified input directory.

2. **Generating Reports**: 
   - After training, reports can be generated automatically. The reports will be saved in the designated output directory.

3. **Data Drift Detection**: 
   - Use the drift detection module to monitor incoming data and ensure that the model remains valid over time.

## Contributing

Contributions to the Factory ML Offline System are welcome. Please follow the standard practices for contributing to open-source projects, including forking the repository and submitting pull requests.

## License

This project is licensed under the MIT License. See the LICENSE file for more details.