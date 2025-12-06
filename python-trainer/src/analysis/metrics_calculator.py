"""Model evaluation metrics calculation module.

This module provides functionality for calculating standard classification
metrics including accuracy, precision, recall, and F1 score. Used for
evaluating trained model performance.

Main Components:
    - MetricsCalculator: Calculates classification evaluation metrics

Project Context:
    Part of the factory-ml-offline-system analysis pipeline. Used after model
    training to evaluate performance on test data. Results are included in
    training reports.

Example:
    >>> from analysis.metrics_calculator import MetricsCalculator
    >>> calculator = MetricsCalculator(y_true, y_pred)
    >>> metrics = calculator.get_metrics()
    >>> print(f"Accuracy: {metrics['accuracy']:.3f}")
"""

from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score
import numpy as np
from typing import Dict


class MetricsCalculator:
    """Calculate classification evaluation metrics.
    
    This class computes standard classification metrics (accuracy, precision,
    recall, F1 score) from true and predicted labels. Uses weighted averaging
    for multi-class classification.
    
    Attributes:
        y_true (np.ndarray): True labels.
                           (正しいラベル)
        y_pred (np.ndarray): Predicted labels.
                           (予測されたラベル)
    
    Examples:
        >>> y_true = np.array([0, 1, 1, 0, 1])
        >>> y_pred = np.array([0, 1, 0, 0, 1])
        >>> calculator = MetricsCalculator(y_true, y_pred)
        >>> print(f"Accuracy: {calculator.calculate_accuracy():.3f}")
        Accuracy: 0.800
    """
    
    def __init__(self, y_true: np.ndarray, y_pred: np.ndarray) -> None:
        """Initialize the MetricsCalculator.
        
        Args:
            y_true (np.ndarray): True labels from the dataset.
                               (データセットの正しいラベル)
            y_pred (np.ndarray): Predicted labels from the model.
                               (モデルによる予測ラベル)
        
        Raises:
            ValueError: If y_true and y_pred have different lengths.
                       (y_trueとy_predの長さが異なる場合)
        """
        if len(y_true) != len(y_pred):
            raise ValueError(
                f"y_trueとy_predの長さが一致しません: "
                f"{len(y_true)} != {len(y_pred)}"
            )
        
        self.y_true = y_true
        self.y_pred = y_pred

    def calculate_accuracy(self) -> float:
        """Calculate classification accuracy.
        
        Accuracy is the ratio of correctly predicted instances to total instances.
        
        Returns:
            float: Accuracy score between 0.0 and 1.0.
                  (0.0から1.0の精度スコア)
        
        Examples:
            >>> accuracy = calculator.calculate_accuracy()
            >>> print(f"Accuracy: {accuracy:.2%}")
            Accuracy: 85.50%
        """
        return accuracy_score(self.y_true, self.y_pred)

    def calculate_precision(self) -> float:
        """Calculate precision score with weighted averaging.
        
        Precision is the ratio of true positives to all positive predictions.
        Uses weighted averaging for multi-class problems.
        
        Returns:
            float: Weighted precision score between 0.0 and 1.0.
                  (0.0から1.0の適合率スコア)
        
        Examples:
            >>> precision = calculator.calculate_precision()
            >>> print(f"Precision: {precision:.2%}")
            Precision: 83.40%
        """
        return precision_score(self.y_true, self.y_pred, average='weighted')

    def calculate_recall(self) -> float:
        """Calculate recall score with weighted averaging.
        
        Recall is the ratio of true positives to all actual positive instances.
        Uses weighted averaging for multi-class problems.
        
        Returns:
            float: Weighted recall score between 0.0 and 1.0.
                  (0.0から1.0の再現率スコア)
        
        Examples:
            >>> recall = calculator.calculate_recall()
            >>> print(f"Recall: {recall:.2%}")
            Recall: 87.80%
        """
        return recall_score(self.y_true, self.y_pred, average='weighted')

    def calculate_f1_score(self) -> float:
        """Calculate F1 score with weighted averaging.
        
        F1 score is the harmonic mean of precision and recall.
        Uses weighted averaging for multi-class problems.
        
        Returns:
            float: Weighted F1 score between 0.0 and 1.0.
                  (0.0から1.0のF1スコア)
        
        Examples:
            >>> f1 = calculator.calculate_f1_score()
            >>> print(f"F1 Score: {f1:.2%}")
            F1 Score: 85.50%
        """
        return f1_score(self.y_true, self.y_pred, average='weighted')

    def get_metrics(self) -> Dict[str, float]:
        """Calculate all evaluation metrics at once.
        
        Convenience method that computes accuracy, precision, recall, and F1 score.
        
        Returns:
            Dict[str, float]: Dictionary containing all metrics.
                Keys: 'accuracy', 'precision', 'recall', 'f1_score'
                (すべての指標を含む辞書)
        
        Examples:
            >>> metrics = calculator.get_metrics()
            >>> for metric, value in metrics.items():
            ...     print(f"{metric}: {value:.2%}")
            accuracy: 85.50%
            precision: 83.40%
            recall: 87.80%
            f1_score: 85.50%
        """
        return {
            'accuracy': self.calculate_accuracy(),
            'precision': self.calculate_precision(),
            'recall': self.calculate_recall(),
            'f1_score': self.calculate_f1_score()
        }