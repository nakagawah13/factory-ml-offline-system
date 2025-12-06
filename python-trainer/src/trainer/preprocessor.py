"""Data preprocessing and feature transformation module.

This module provides functionality for preprocessing raw data by applying
StandardScaler to numerical features and OneHotEncoder to categorical features.
It uses schema definitions to determine feature types.

Main Components:
    - Preprocessor: Transforms raw data into ML-ready features

Project Context:
    Part of the factory-ml-offline-system training pipeline. Used by the
    model trainer and inference service to ensure consistent feature
    transformation between training and inference.

Example:
    >>> from trainer.preprocessor import Preprocessor
    >>> schema = {
    ...     'columns': [
    ...         {'name': 'temperature', 'type': 'NUMERIC'},
    ...         {'name': 'product_type', 'type': 'CATEGORY'}
    ...     ]
    ... }
    >>> preprocessor = Preprocessor(schema)
    >>> transformed_data = preprocessor.fit_transform(raw_data)
"""

from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import StandardScaler, OneHotEncoder
import pandas as pd
import numpy as np
from typing import Dict, Any, List


class Preprocessor:
    """Preprocess data using schema-defined feature transformations.
    
    This class creates a preprocessing pipeline based on a schema definition,
    applying StandardScaler to numerical features and OneHotEncoder to
    categorical features. Ensures consistent transformation between training
    and inference.
    
    Attributes:
        schema (Dict[str, Any]): Schema definition with column types.
                                (カラムタイプを含むスキーマ定義)
        column_transformer (ColumnTransformer): Scikit-learn transformer.
                                               (scikit-learn変換器)
    
    Examples:
        >>> schema = {
        ...     'columns': [
        ...         {'name': 'temperature', 'type': 'NUMERIC'},
        ...         {'name': 'pressure', 'type': 'NUMERIC'},
        ...         {'name': 'product_type', 'type': 'CATEGORY'}
        ...     ]
        ... }
        >>> preprocessor = Preprocessor(schema)
        >>> preprocessor.fit(training_data)
        >>> transformed = preprocessor.transform(new_data)
    """
    
    def __init__(self, schema: Dict[str, Any]) -> None:
        """Initialize the Preprocessor with a schema.
        
        Args:
            schema (Dict[str, Any]): Schema definition containing column
                specifications with 'name' and 'type' fields.
                (カラム仕様（'name'と'type'フィールド）を含むスキーマ定義)
        """
        self.schema = schema
        self.column_transformer = self._create_column_transformer()

    def _create_column_transformer(self) -> ColumnTransformer:
        """Create a ColumnTransformer based on schema.
        
        Extracts numerical and categorical features from the schema and
        creates appropriate transformers for each type.
        
        Returns:
            ColumnTransformer: Configured transformer for all features.
                              (すべての特徴量用の設定済み変換器)
        """
        # Extract feature names by type from schema
        # スキーマからタイプ別に特徴量名を抽出
        numerical_features: List[str] = [
            col['name'] for col in self.schema['columns'] 
            if col['type'] == 'NUMERIC'
        ]
        categorical_features: List[str] = [
            col['name'] for col in self.schema['columns'] 
            if col['type'] == 'CATEGORY'
        ]

        return ColumnTransformer(
            transformers=[
                ('num', StandardScaler(), numerical_features),
                ('cat', OneHotEncoder(), categorical_features)
            ]
        )

    def fit(self, data: pd.DataFrame) -> None:
        """Fit the transformer to the training data.
        
        Learns the transformation parameters (mean/std for numerical features,
        categories for categorical features) from the training data.
        
        Args:
            data (pd.DataFrame): Training data to fit the transformer.
                               (変換器を適合させる訓練データ)
        """
        self.column_transformer.fit(data)

    def transform(self, data: pd.DataFrame) -> np.ndarray:
        """Transform data using fitted transformer.
        
        Applies the learned transformations to new data.
        
        Args:
            data (pd.DataFrame): Data to transform.
                               (変換するデータ)
        
        Returns:
            np.ndarray: Transformed feature matrix.
                       (変換された特徴量行列)
        
        Raises:
            sklearn.exceptions.NotFittedError: If transformer not fitted yet.
                                               (変換器がまだ適合されていない場合)
        """
        return self.column_transformer.transform(data)

    def fit_transform(self, data: pd.DataFrame) -> np.ndarray:
        """Fit transformer and transform data in one step.
        
        Convenience method that combines fit() and transform() operations.
        Typically used on training data.
        
        Args:
            data (pd.DataFrame): Data to fit and transform.
                               (適合して変換するデータ)
        
        Returns:
            np.ndarray: Transformed feature matrix.
                       (変換された特徴量行列)
        """
        return self.column_transformer.fit_transform(data)