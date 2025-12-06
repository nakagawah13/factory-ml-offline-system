from datetime import datetime
import json
import os

class ReportGenerator:
    def __init__(self, output_dir):
        self.output_dir = output_dir
        os.makedirs(self.output_dir, exist_ok=True)

    def generate_metrics_report(self, metrics):
        report_path = os.path.join(self.output_dir, 'metrics.json')
        with open(report_path, 'w') as f:
            json.dump(metrics, f, indent=4)

    def generate_shap_report(self, shap_values, feature_names):
        report_path = os.path.join(self.output_dir, 'shap_summary.html')
        with open(report_path, 'w') as f:
            f.write("<html><head><title>SHAP Summary</title></head><body>")
            f.write("<h1>SHAP Summary Report</h1>")
            f.write("<table><tr><th>Feature</th><th>SHAP Value</th></tr>")
            for feature, value in zip(feature_names, shap_values):
                f.write(f"<tr><td>{feature}</td><td>{value}</td></tr>")
            f.write("</table></body></html>")

    def generate_drift_report(self, drift_info):
        report_path = os.path.join(self.output_dir, 'drift_report.html')
        with open(report_path, 'w') as f:
            f.write("<html><head><title>Drift Report</title></head><body>")
            f.write("<h1>Data Drift Report</h1>")
            f.write("<p>Drift detected: {}</p>".format(drift_info['detected']))
            f.write("<p>Drift score: {}</p>".format(drift_info['score']))
            f.write("</body></html>")

    def save_report(self, metrics, shap_values, feature_names, drift_info):
        self.generate_metrics_report(metrics)
        self.generate_shap_report(shap_values, feature_names)
        self.generate_drift_report(drift_info)

# Example usage:
# report_generator = ReportGenerator(output_dir='reports')
# report_generator.save_report(metrics, shap_values, feature_names, drift_info)