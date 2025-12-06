from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import StandardScaler, OneHotEncoder
import pandas as pd

class Preprocessor:
    def __init__(self, schema):
        self.schema = schema
        self.column_transformer = self._create_column_transformer()

    def _create_column_transformer(self):
        numerical_features = [col['name'] for col in self.schema['columns'] if col['type'] == 'NUMERIC']
        categorical_features = [col['name'] for col in self.schema['columns'] if col['type'] == 'CATEGORY']

        return ColumnTransformer(
            transformers=[
                ('num', StandardScaler(), numerical_features),
                ('cat', OneHotEncoder(), categorical_features)
            ]
        )

    def fit(self, data):
        self.column_transformer.fit(data)

    def transform(self, data):
        return self.column_transformer.transform(data)

    def fit_transform(self, data):
        return self.column_transformer.fit_transform(data)