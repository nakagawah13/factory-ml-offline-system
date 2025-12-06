from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import StandardScaler, OneHotEncoder
from sklearn.compose import ColumnTransformer
import pandas as pd
import joblib
import os

class ModelTrainer:
    def __init__(self, config):
        self.config = config
        self.model = None
        self.pipeline = None

    def load_data(self, data_path):
        data = pd.read_csv(data_path)
        return data

    def preprocess_data(self, data):
        numerical_features = self.config['numerical_features']
        categorical_features = self.config['categorical_features']

        preprocessor = ColumnTransformer(
            transformers=[
                ('num', StandardScaler(), numerical_features),
                ('cat', OneHotEncoder(), categorical_features)
            ]
        )

        self.pipeline = Pipeline(steps=[('preprocessor', preprocessor),
                                         ('classifier', RandomForestClassifier())])
        
        X = data[numerical_features + categorical_features]
        y = data[self.config['target']]
        return X, y

    def train_model(self, X, y):
        self.model = self.pipeline.fit(X, y)

    def save_model(self, model_path):
        joblib.dump(self.model, model_path)

    def run(self, data_path, model_path):
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