"""Data drift detection module.

This module provides functionality for detecting distribution drift between
reference (training) data and current (production) data. It uses mutual
information score as a measure of drift to identify when model retraining
may be necessary.

Main Components:
    - DriftDetector: Detects distribution drift between datasets

Project Context:
    Part of the factory-ml-offline-system analysis pipeline. Used to monitor
    production data and trigger retraining alerts when significant drift is
    detected compared to the training data distribution.

Example:
    >>> from analysis.drift_detector import DriftDetector
    >>> detector = DriftDetector(reference_data, current_data)
    >>> drift_scores = detector.detect_drift()
    >>> detector.report_drift()
    Drift score for 'temperature': 0.15
    Drift score for 'pressure': 0.23
"""

from sklearn.metrics import mutual_info_score
import pandas as pd
from typing import Dict


class DriftDetector:
    """Detect distribution drift between reference and current data.
    
    This class compares the distribution of reference (training) data with
    current (production) data to identify drift. Uses mutual information score
    to quantify the difference in distributions for each feature.
    
    Attributes:
        reference_data (pd.DataFrame): Reference data (typically training data).
                                      (参照データ（通常は訓練データ）)
        current_data (pd.DataFrame): Current data (typically production data).
                                    (現在のデータ（通常は本番データ）)
    
    Examples:
        >>> import pandas as pd
        >>> reference = pd.DataFrame({'temp': [20, 21, 22], 'pressure': [100, 101, 102]})
        >>> current = pd.DataFrame({'temp': [25, 26, 27], 'pressure': [100, 101, 102]})
        >>> detector = DriftDetector(reference, current)
        >>> drift_scores = detector.detect_drift()
        >>> print(drift_scores)
        {'temp': 0.15, 'pressure': 0.02}
    """
    
    def __init__(self, reference_data: pd.DataFrame, current_data: pd.DataFrame) -> None:
        """Initialize the DriftDetector with reference and current data.
        
        Args:
            reference_data (pd.DataFrame): Reference dataset for comparison.
                                          Typically the training data.
                                          (比較用の参照データセット。通常は訓練データ)
            current_data (pd.DataFrame): Current dataset to check for drift.
                                        Typically recent production data.
                                        (ドリフトをチェックする現在のデータセット。
                                         通常は最近の本番データ)
        """
        self.reference_data = reference_data
        self.current_data = current_data

    def detect_drift(self) -> Dict[str, float]:
        """Detect drift across all common columns.
        
        Calculates drift scores for each column that exists in both reference
        and current datasets. Higher scores indicate greater distribution drift.
        
        Returns:
            Dict[str, float]: Drift scores for each column.
                             Keys are column names, values are drift scores.
                             (各カラムのドリフトスコア。
                              キーはカラム名、値はドリフトスコア)
        
        Examples:
            >>> detector = DriftDetector(reference_data, current_data)
            >>> scores = detector.detect_drift()
            >>> for col, score in scores.items():
            ...     print(f"{col}: {score:.3f}")
        """
        drift_results: Dict[str, float] = {}
        
        # Calculate drift score for each common column
        # 各共通カラムについてドリフトスコアを計算
        for column in self.reference_data.columns:
            if column in self.current_data.columns:
                drift_score = self._calculate_drift_score(column)
                drift_results[column] = drift_score
                
        return drift_results

    def _calculate_drift_score(self, column: str) -> float:
        """Calculate drift score for a single column.
        
        Uses mutual information score as a measure of distribution similarity.
        Lower scores indicate more similar distributions (less drift).
        
        Args:
            column (str): Name of the column to calculate drift for.
                         (ドリフトを計算するカラム名)
        
        Returns:
            float: Drift score (mutual information score).
                  (ドリフトスコア（相互情報量スコア）)
        """
        reference_series = self.reference_data[column]
        current_series = self.current_data[column]
        
        # Calculate mutual information score as a measure of drift
        # ドリフトの指標として相互情報量スコアを計算
        score = mutual_info_score(reference_series, current_series)
        return score

    def report_drift(self) -> None:
        """Print drift scores for all columns.
        
        Convenience method to display drift scores in a human-readable format.
        Useful for quick inspection of drift across all features.
        
        Examples:
            >>> detector = DriftDetector(reference_data, current_data)
            >>> detector.report_drift()
            Drift score for 'temperature': 0.15
            Drift score for 'pressure': 0.23
            Drift score for 'humidity': 0.08
        """
        drift_scores = self.detect_drift()
        
        # Print drift scores for each column
        # 各カラムのドリフトスコアを表示
        for column, score in drift_scores.items():
            print(f"Drift score for '{column}': {score:.3f}")