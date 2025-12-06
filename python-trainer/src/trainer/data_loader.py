"""Data loading and validation module.

This module provides functionality for loading data from CSV or Excel files
and validating it against a schema definition. It ensures data integrity
before processing in the ML pipeline.

Main Components:
    - DataLoader: Loads and validates data from files

Project Context:
    Part of the factory-ml-offline-system training pipeline. Used by the
    model trainer to load and validate training data before preprocessing.

Example:
    >>> from trainer.data_loader import DataLoader
    >>> schema = {'columns': [{'name': 'value', 'required': True}]}
    >>> loader = DataLoader(schema)
    >>> data = loader.load_data('data/input/training_data.csv')
"""

import pandas as pd
from typing import Dict, Any


class DataLoader:
    """Load and validate data from CSV or Excel files.
    
    This class handles loading data from various file formats and validates
    the loaded data against a predefined schema to ensure data quality.
    
    Attributes:
        schema (Dict[str, Any]): Schema definition for data validation.
                                (データ検証用のスキーマ定義)
    
    Examples:
        >>> schema = {'columns': [{'name': 'temperature', 'required': True}]}
        >>> loader = DataLoader(schema)
        >>> data = loader.load_data('data/input/sensor_data.csv')
    """
    
    def __init__(self, schema: Dict[str, Any]) -> None:
        """Initialize the DataLoader with a schema.
        
        Args:
            schema (Dict[str, Any]): Schema definition containing column
                specifications for validation.
                (検証用のカラム仕様を含むスキーマ定義)
        """
        self.schema = schema

    def load_data(self, file_path: str) -> pd.DataFrame:
        """Load data from CSV or Excel file.
        
        Loads data from the specified file path and validates it against
        the schema. Supports CSV and Excel formats.
        
        Args:
            file_path (str): Path to the data file (CSV or Excel).
                           (データファイルへのパス（CSVまたはExcel）)
        
        Returns:
            pd.DataFrame: Loaded and validated data.
                         (読み込まれて検証されたデータ)
        
        Raises:
            ValueError: If file format is unsupported or validation fails.
                       (ファイル形式が非対応、または検証に失敗した場合)
        
        Examples:
            >>> loader = DataLoader(schema)
            >>> data = loader.load_data('data/input/training_data.csv')
        """
        # Load data from CSV or Excel file
        if file_path.endswith('.csv'):
            data = pd.read_csv(file_path)
        elif file_path.endswith(('.xls', '.xlsx')):
            data = pd.read_excel(file_path)
        else:
            raise ValueError(
                f"サポートされていないファイル形式です: {file_path}\n"
                "CSVまたはExcelファイルを指定してください。"
            )

        # Validate data against schema
        self.validate_data(data)

        return data

    def validate_data(self, data: pd.DataFrame) -> None:
        """Validate data against the schema.
        
        Checks that all required columns are present and contain no null values
        if specified as required in the schema.
        
        Args:
            data (pd.DataFrame): Data to validate.
                               (検証対象のデータ)
        
        Raises:
            ValueError: If required columns are missing or contain null values.
                       (必須カラムが不足、またはnull値が含まれる場合)
        """
        # Implement validation logic based on the schema
        for column in self.schema['columns']:
            if column['name'] not in data.columns:
                raise ValueError(
                    f"必須カラムが不足しています: {column['name']}\n"
                    f"存在するカラム: {', '.join(data.columns)}"
                )
            if column.get('required') and data[column['name']].isnull().any():
                null_count = data[column['name']].isnull().sum()
                raise ValueError(
                    f"カラム '{column['name']}' にnull値が含まれています。\n"
                    f"null値の数: {null_count} / {len(data)}"
                )
            # Additional validation checks can be added here
