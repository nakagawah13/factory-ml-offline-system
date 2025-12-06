"""SHAP-based model interpretability analysis module.

This module provides functionality for analyzing machine learning models using
SHAP (SHapley Additive exPlanations) values and permutation importance.
Helps understand feature importance and model decision-making process.

Main Components:
    - SHAPAnalyzer: Calculates and visualizes SHAP values and feature importance

Project Context:
    Part of the factory-ml-offline-system analysis pipeline. Used after model
    training to understand which features contribute most to predictions.
    Results are included in analysis reports.

Example:
    >>> from analysis.shap_analyzer import SHAPAnalyzer
    >>> analyzer = SHAPAnalyzer(model, ['temperature', 'pressure', 'humidity'])
    >>> shap_values = analyzer.calculate_shap_values(X_test)
    >>> importance = analyzer.calculate_feature_importance(X_test, y_test)
"""

from sklearn.inspection import permutation_importance
import shap
import pandas as pd
from typing import Any, List


class SHAPAnalyzer:
    """Analyze model interpretability using SHAP values.
    
    This class provides methods to calculate SHAP values for understanding
    feature contributions to model predictions, and to compute feature
    importance using permutation importance.
    
    Attributes:
        model (Any): Trained machine learning model.
                    (訓練済み機械学習モデル)
        feature_names (List[str]): Names of input features.
                                  (入力特徴量の名前)
    
    Examples:
        >>> model = train_model(data)
        >>> feature_names = ['temperature', 'pressure', 'humidity']
        >>> analyzer = SHAPAnalyzer(model, feature_names)
        >>> shap_values = analyzer.calculate_shap_values(X_test)
    """
    
    def __init__(self, model: Any, feature_names: List[str]) -> None:
        """Initialize the SHAPAnalyzer.
        
        Args:
            model (Any): Trained machine learning model.
                        (訓練済み機械学習モデル)
            feature_names (List[str]): Names of features used in the model.
                                      (モデルで使用される特徴量名)
        """
        self.model = model
        self.feature_names = feature_names

    def calculate_shap_values(self, X: pd.DataFrame) -> Any:
        """Calculate SHAP values for the given data.
        
        Computes SHAP values to explain how each feature contributes to
        individual predictions.
        
        Args:
            X (pd.DataFrame): Input data for which to calculate SHAP values.
                             (SHAP値を計算する入力データ)
        
        Returns:
            Any: SHAP values object containing explanations.
                (SHAP値オブジェクト)
        
        Examples:
            >>> shap_values = analyzer.calculate_shap_values(X_test)
            >>> print(shap_values.shape)
            (100, 10)  # 100 samples, 10 features
        """
        explainer = shap.Explainer(self.model)
        shap_values = explainer(X)
        return shap_values

    def plot_shap_summary(self, shap_values: Any) -> None:
        """Plot SHAP summary visualization.
        
        Creates a summary plot showing the impact of each feature on model output.
        Higher SHAP values indicate greater positive impact on predictions.
        
        Args:
            shap_values (Any): SHAP values object from calculate_shap_values().
                              (calculate_shap_values()からのSHAP値オブジェクト)
        
        Examples:
            >>> shap_values = analyzer.calculate_shap_values(X_test)
            >>> analyzer.plot_shap_summary(shap_values)
        """
        shap.summary_plot(shap_values, feature_names=self.feature_names)

    def calculate_feature_importance(
        self,
        X: pd.DataFrame,
        y: pd.Series
    ) -> pd.DataFrame:
        """Calculate feature importance using permutation importance.
        
        Computes feature importance by measuring how model performance changes
        when each feature is randomly shuffled. Higher values indicate more
        important features.
        
        Args:
            X (pd.DataFrame): Input features.
                             (入力特徴量)
            y (pd.Series): Target values.
                          (ターゲット値)
        
        Returns:
            pd.DataFrame: DataFrame with features and their importance scores,
                         sorted by importance in descending order.
                         (特徴量とその重要度スコアを含むDataFrame、
                          重要度の高い順にソート済み)
        
        Examples:
            >>> importance_df = analyzer.calculate_feature_importance(X_test, y_test)
            >>> print(importance_df.head())
                  Feature  Importance
            0  temperature       0.45
            1     pressure       0.32
            2     humidity       0.23
        """
        # Calculate permutation importance with 10 repeats
        # 10回の繰り返しで順列重要度を計算
        result = permutation_importance(
            self.model, X, y,
            n_repeats=10,
            random_state=42
        )
        
        importance_df = pd.DataFrame({
            'Feature': self.feature_names,
            'Importance': result.importances_mean
        })
        
        return importance_df.sort_values(by='Importance', ascending=False)

    def plot_feature_importance(self, importance_df: pd.DataFrame) -> None:
        """Plot feature importance as horizontal bar chart.
        
        Creates a visualization of feature importance scores.
        
        Args:
            importance_df (pd.DataFrame): DataFrame from calculate_feature_importance().
                                         (calculate_feature_importance()からのDataFrame)
        
        Examples:
            >>> importance_df = analyzer.calculate_feature_importance(X_test, y_test)
            >>> analyzer.plot_feature_importance(importance_df)
        """
        import matplotlib.pyplot as plt
        
        importance_df.plot(kind='barh', x='Feature', y='Importance', legend=False)
        plt.title('Feature Importance')
        plt.xlabel('Importance')
        plt.ylabel('Feature')
        plt.tight_layout()
        plt.show()