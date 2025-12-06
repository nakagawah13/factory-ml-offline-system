from sklearn.metrics import mutual_info_score
import pandas as pd

class DriftDetector:
    def __init__(self, reference_data, current_data):
        self.reference_data = reference_data
        self.current_data = current_data

    def detect_drift(self):
        drift_results = {}
        for column in self.reference_data.columns:
            if column in self.current_data.columns:
                drift_score = self._calculate_drift_score(column)
                drift_results[column] = drift_score
        return drift_results

    def _calculate_drift_score(self, column):
        reference_series = self.reference_data[column]
        current_series = self.current_data[column]
        
        # Calculate mutual information score as a measure of drift
        score = mutual_info_score(reference_series, current_series)
        return score

    def report_drift(self):
        drift_scores = self.detect_drift()
        for column, score in drift_scores.items():
            print(f"Drift score for '{column}': {score}")