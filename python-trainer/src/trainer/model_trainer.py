"""Machine learning model training module.

This module implements the complete training pipeline for factory production
prediction models. It handles data loading, preprocessing with feature
transformation, model training using RandomForest, and model serialization.

Main Components:
    - ModelTrainer: Orchestrates the entire training pipeline

Project Context:
    Core component of the training pipeline. Receives training data,
    applies preprocessing, trains RandomForest classifier, and outputs
    trained models to models/current/ directory.

Example:
    >>> from trainer.model_trainer import ModelTrainer
    >>> config = {
    ...     'numerical_features': ['temperature', 'pressure'],
    ...     'categorical_features': ['product_type'],
    ...     'target': 'defect_flag'
    ... }
    >>> trainer = ModelTrainer(config)
    >>> trainer.run('data/input/training_data.csv', 'models/current/model.joblib')
"""

from sklearn.ensemble import RandomForestClassifier
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import StandardScaler, OneHotEncoder
from sklearn.compose import ColumnTransformer
import pandas as pd
import joblib
import os
from typing import Dict, Any, Tuple, Optional, List


class ModelTrainer:
    """Train machine learning models for factory production prediction.
    
    This class orchestrates the complete training pipeline including data loading,
    preprocessing, model training, and serialization. It uses scikit-learn's
    Pipeline to ensure consistent preprocessing between training and inference.
    
    Attributes:
        config (Dict[str, Any]): Configuration containing feature names and target.
                                (特徴量名とターゲットを含む設定)
        model (Optional[Pipeline]): Trained model pipeline.
                                   (訓練済みモデルパイプライン)
        pipeline (Optional[Pipeline]): Preprocessing and model pipeline.
                                      (前処理とモデルのパイプライン)
    
    Examples:
        >>> config = {
        ...     'numerical_features': ['sensor_val_1', 'sensor_val_2'],
        ...     'categorical_features': ['product_type'],
        ...     'target': 'label'
        ... }
        >>> trainer = ModelTrainer(config)
        >>> trainer.run('data/input/training_data.csv', 'models/current/model.joblib')
    """
    
    def __init__(self, config: Dict[str, Any]) -> None:
        """Initialize the ModelTrainer with configuration.
        
        Args:
            config (Dict[str, Any]): Configuration dictionary containing:
                - numerical_features (List[str]): Names of numerical columns
                - categorical_features (List[str]): Names of categorical columns
                - target (str): Name of the target column
                (設定辞書。numerical_features、categorical_features、targetを含む)
        """
        self.config = config
        self.model: Optional[Pipeline] = None
        self.pipeline: Optional[Pipeline] = None

    def load_data(self, data_path: str) -> pd.DataFrame:
        """Load training data from CSV file.
        
        Args:
            data_path (str): Path to the CSV file containing training data.
                           (訓練データを含むCSVファイルへのパス)
        
        Returns:
            pd.DataFrame: Loaded training data.
                         (読み込まれた訓練データ)
        
        Raises:
            FileNotFoundError: If the specified file does not exist.
                              (指定されたファイルが存在しない場合)
        """
        if not os.path.exists(data_path):
            raise FileNotFoundError(f"訓練データが見つかりません: {data_path}")
        
        data = pd.read_csv(data_path)
        return data

    def preprocess_data(self, data: pd.DataFrame) -> Tuple[pd.DataFrame, pd.Series]:
        """Preprocess data and create feature/target split.
        
        Creates a preprocessing pipeline with StandardScaler for numerical features
        and OneHotEncoder for categorical features, then combines it with a
        RandomForestClassifier.
        
        Args:
            data (pd.DataFrame): Raw training data.
                               (生の訓練データ)
        
        Returns:
            Tuple[pd.DataFrame, pd.Series]: Feature matrix (X) and target vector (y).
                                           (特徴量行列Xとターゲットベクトルy)
        
        Raises:
            KeyError: If required columns are missing from the data.
                     (必要なカラムがデータに存在しない場合)
        """
        numerical_features: List[str] = self.config['numerical_features']
        categorical_features: List[str] = self.config['categorical_features']

        # Create preprocessing pipeline
        # 数値特徴量の標準化とカテゴリ特徴量のOne-Hotエンコーディング
        preprocessor = ColumnTransformer(
            transformers=[
                ('num', StandardScaler(), numerical_features),
                ('cat', OneHotEncoder(), categorical_features)
            ]
        )

        # Combine preprocessing and model into a single pipeline
        # 前処理とモデルを単一のパイプラインに結合
        self.pipeline = Pipeline(steps=[
            ('preprocessor', preprocessor),
            ('classifier', RandomForestClassifier())
        ])
        
        X = data[numerical_features + categorical_features]
        y = data[self.config['target']]
        return X, y

    def train_model(self, X: pd.DataFrame, y: pd.Series) -> None:
        """Train the model using provided features and target.
        
        Args:
            X (pd.DataFrame): Feature matrix for training.
                            (訓練用の特徴量行列)
            y (pd.Series): Target vector for training.
                         (訓練用のターゲットベクトル)
        
        Raises:
            ValueError: If pipeline has not been initialized.
                       (パイプラインが初期化されていない場合)
        """
        if self.pipeline is None:
            raise ValueError(
                "パイプラインが初期化されていません。"
                "preprocess_data()を先に実行してください。"
            )
        
        self.model = self.pipeline.fit(X, y)

    def save_model(self, model_path: str) -> None:
        """Save the trained model to disk.
        
        Args:
            model_path (str): Path where the model will be saved.
                            (モデルを保存するパス)
        
        Raises:
            ValueError: If model has not been trained yet.
                       (モデルがまだ訓練されていない場合)
        """
        if self.model is None:
            raise ValueError(
                "保存するモデルがありません。train_model()を先に実行してください。"
            )
        
        # Create directory if it doesn't exist
        # ディレクトリが存在しない場合は作成
        os.makedirs(os.path.dirname(model_path), exist_ok=True)
        
        joblib.dump(self.model, model_path)

    def run(self, data_path: str, model_path: str) -> None:
        """Execute the complete training pipeline.
        
        Loads data, preprocesses it, trains the model, and saves it to disk.
        This is the main entry point for the training process.
        
        Args:
            data_path (str): Path to the training data CSV file.
                           (訓練データCSVファイルへのパス)
            model_path (str): Path where the trained model will be saved.
                            (訓練済みモデルを保存するパス)
        
        Examples:
            >>> trainer = ModelTrainer(config)
            >>> trainer.run(
            ...     'data/input/training_data.csv',
            ...     'models/current/model.joblib'
            ... )
        """
        data = self.load_data(data_path)
        X, y = self.preprocess_data(data)
        self.train_model(X, y)
        self.save_model(model_path)

if __name__ == "__main__":
    config = {
        'numerical_features': ['sensor_val_1', 'sensor_val_2'],  # Example numerical features
        'categorical_features': ['product_type'],  # Example categorical features
        'target': 'label'  # Example target variable
    }
    
    trainer = ModelTrainer(config)
    trainer.run('data/input/training_data.csv', 'models/current/model.joblib')