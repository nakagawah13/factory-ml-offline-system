from pathlib import Path
import argparse
import pandas as pd
from trainer.data_loader import load_data
from trainer.preprocessor import preprocess_data
from trainer.model_trainer import train_model
from trainer.onnx_converter import convert_to_onnx
from trainer.report_generator import generate_report

def main():
    parser = argparse.ArgumentParser(description='Train a machine learning model.')
    parser.add_argument('--data', type=str, required=True, help='Path to the input data file (CSV/Excel)')
    parser.add_argument('--output', type=str, required=True, help='Directory to save the trained model and reports')
    parser.add_argument('--config', type=str, required=True, help='Path to the configuration file')
    parser.add_argument('--report', action='store_true', help='Generate analysis report')

    args = parser.parse_args()

    # Load data
    data = load_data(args.data)

    # Preprocess data
    processed_data = preprocess_data(data)

    # Train model
    model = train_model(processed_data, args.config)

    # Convert model to ONNX format
    onnx_model_path = Path(args.output) / 'model.onnx'
    convert_to_onnx(model, onnx_model_path)

    # Generate report if requested
    if args.report:
        generate_report(processed_data, model, args.output)

if __name__ == '__main__':
    main()