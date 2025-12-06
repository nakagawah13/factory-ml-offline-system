import pandas as pd

class DataLoader:
    def __init__(self, schema):
        self.schema = schema

    def load_data(self, file_path):
        # Load data from CSV or Excel file
        if file_path.endswith('.csv'):
            data = pd.read_csv(file_path)
        elif file_path.endswith(('.xls', '.xlsx')):
            data = pd.read_excel(file_path)
        else:
            raise ValueError("Unsupported file format. Please provide a CSV or Excel file.")

        # Validate data against schema
        self.validate_data(data)

        return data

    def validate_data(self, data):
        # Implement validation logic based on the schema
        for column in self.schema['columns']:
            if column['name'] not in data.columns:
                raise ValueError(f"Missing column: {column['name']}")
            if column.get('required') and data[column['name']].isnull().any():
                raise ValueError(f"Column {column['name']} contains null values.")
            # Additional validation checks can be added here
