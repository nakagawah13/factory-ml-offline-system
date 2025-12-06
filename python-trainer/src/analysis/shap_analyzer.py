from sklearn.inspection import permutation_importance
import shap
import numpy as np
import pandas as pd

class SHAPAnalyzer:
    def __init__(self, model, feature_names):
        self.model = model
        self.feature_names = feature_names

    def calculate_shap_values(self, X):
        explainer = shap.Explainer(self.model)
        shap_values = explainer(X)
        return shap_values

    def plot_shap_summary(self, shap_values):
        shap.summary_plot(shap_values, feature_names=self.feature_names)

    def calculate_feature_importance(self, X, y):
        result = permutation_importance(self.model, X, y, n_repeats=10, random_state=42)
        importance_df = pd.DataFrame({
            'Feature': self.feature_names,
            'Importance': result.importances_mean
        })
        return importance_df.sort_values(by='Importance', ascending=False)

    def plot_feature_importance(self, importance_df):
        importance_df.plot(kind='barh', x='Feature', y='Importance', legend=False)
        plt.title('Feature Importance')
        plt.xlabel('Importance')
        plt.ylabel('Feature')
        plt.show()