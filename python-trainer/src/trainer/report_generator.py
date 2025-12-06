"""Analysis report generation module.

This module provides functionality for generating HTML and JSON reports
for model training analysis, including metrics, SHAP values, and drift detection.
Reports are saved to a specified output directory.

Main Components:
    - ReportGenerator: Generates various analysis reports

Project Context:
    Part of the factory-ml-offline-system training pipeline. Generates
    comprehensive reports after model training for evaluation and monitoring.
    Reports are saved to data/output/reports/ directory.

Example:
    >>> from trainer.report_generator import ReportGenerator
    >>> generator = ReportGenerator('data/output/reports')
    >>> generator.save_report(metrics, shap_values, feature_names, drift_info)
"""

import json
import os
from typing import Dict, List, Any


class ReportGenerator:
    """Generate analysis reports for model training results.
    
    This class creates HTML and JSON reports for various analysis outputs
    including evaluation metrics, SHAP values, and drift detection results.
    
    Attributes:
        output_dir (str): Directory where reports will be saved.
                         (レポートを保存するディレクトリ)
    
    Examples:
        >>> generator = ReportGenerator('data/output/reports')
        >>> metrics = {'accuracy': 0.85, 'precision': 0.83}
        >>> generator.generate_metrics_report(metrics)
    """
    
    def __init__(self, output_dir: str) -> None:
        """Initialize the ReportGenerator.
        
        Creates the output directory if it doesn't exist.
        
        Args:
            output_dir (str): Directory where reports will be saved.
                            (レポートを保存するディレクトリ)
        """
        self.output_dir = output_dir
        os.makedirs(self.output_dir, exist_ok=True)

    def generate_metrics_report(self, metrics: Dict[str, Any]) -> None:
        """Generate JSON report for evaluation metrics.
        
        Saves evaluation metrics (accuracy, precision, recall, F1) to a JSON file.
        
        Args:
            metrics (Dict[str, Any]): Dictionary containing evaluation metrics.
                                     (評価指標を含む辞書)
        
        Examples:
            >>> metrics = {'accuracy': 0.85, 'precision': 0.83, 'recall': 0.87}
            >>> generator.generate_metrics_report(metrics)
        """
        report_path = os.path.join(self.output_dir, 'metrics.json')
        with open(report_path, 'w', encoding='utf-8') as f:
            json.dump(metrics, f, indent=4, ensure_ascii=False)

    def generate_shap_report(self, shap_values: List[float], feature_names: List[str]) -> None:
        """Generate HTML report for SHAP values.
        
        Creates an HTML report displaying SHAP values for each feature,
        helping to understand feature importance and model interpretability.
        
        Args:
            shap_values (List[float]): SHAP values for each feature.
                                      (各特徴量のSHAP値)
            feature_names (List[str]): Names of features corresponding to SHAP values.
                                      (SHAP値に対応する特徴量名)
        
        Examples:
            >>> shap_values = [0.5, 0.3, 0.2]
            >>> feature_names = ['temperature', 'pressure', 'humidity']
            >>> generator.generate_shap_report(shap_values, feature_names)
        """
        report_path = os.path.join(self.output_dir, 'shap_summary.html')
        with open(report_path, 'w', encoding='utf-8') as f:
            f.write("<html><head><title>SHAP Summary</title></head><body>")
            f.write("<h1>SHAP Summary Report</h1>")
            f.write("<table border='1'><tr><th>Feature</th><th>SHAP Value</th></tr>")
            for feature, value in zip(feature_names, shap_values):
                f.write(f"<tr><td>{feature}</td><td>{value:.4f}</td></tr>")
            f.write("</table></body></html>")

    def generate_drift_report(self, drift_info: Dict[str, Any]) -> None:
        """Generate HTML report for data drift detection.
        
        Creates an HTML report showing drift detection results, including
        whether drift was detected and the drift score.
        
        Args:
            drift_info (Dict[str, Any]): Dictionary containing drift detection results.
                Keys: 'detected' (bool), 'score' (float)
                (ドリフト検出結果を含む辞書)
        
        Examples:
            >>> drift_info = {'detected': True, 'score': 0.23}
            >>> generator.generate_drift_report(drift_info)
        """
        report_path = os.path.join(self.output_dir, 'drift_report.html')
        with open(report_path, 'w', encoding='utf-8') as f:
            f.write("<html><head><title>Drift Report</title></head><body>")
            f.write("<h1>Data Drift Report</h1>")
            f.write(f"<p>ドリフト検出: {'はい' if drift_info['detected'] else 'いいえ'}</p>")
            f.write(f"<p>ドリフトスコア: {drift_info['score']:.4f}</p>")
            f.write("</body></html>")

    def save_report(
        self,
        metrics: Dict[str, Any],
        shap_values: List[float],
        feature_names: List[str],
        drift_info: Dict[str, Any]
    ) -> None:
        """Generate all analysis reports.
        
        Convenience method that generates metrics, SHAP, and drift reports
        all at once.
        
        Args:
            metrics (Dict[str, Any]): Evaluation metrics.
                                     (評価指標)
            shap_values (List[float]): SHAP values for features.
                                      (特徴量のSHAP値)
            feature_names (List[str]): Feature names.
                                      (特徴量名)
            drift_info (Dict[str, Any]): Drift detection results.
                                        (ドリフト検出結果)
        
        Examples:
            >>> generator = ReportGenerator('data/output/reports')
            >>> generator.save_report(
            ...     metrics={'accuracy': 0.85},
            ...     shap_values=[0.5, 0.3],
            ...     feature_names=['temp', 'pressure'],
            ...     drift_info={'detected': False, 'score': 0.05}
            ... )
        """
        self.generate_metrics_report(metrics)
        self.generate_shap_report(shap_values, feature_names)
        self.generate_drift_report(drift_info)

# Example usage:
# report_generator = ReportGenerator(output_dir='reports')
# report_generator.save_report(metrics, shap_values, feature_names, drift_info)