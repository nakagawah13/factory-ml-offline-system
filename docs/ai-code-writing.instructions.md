---
applyTo: "**"
---

# AIコード執筆ガイドライン

## 概要

このファイルは、AIがコードを執筆する際の指示を定義します。一貫性のある高品質なコードを維持するための必須要件を規定します。

## 言語使用ガイドライン

### Docstringとコメントの言語選択原則

本プロジェクトでは、ツールチェーン(Ruff等のリンター)との互換性とAIツールとの協調性を重視し、以下の方針を採用します。

#### 基本方針: ハイブリッドアプローチ

1. **コード要素(必須)**: 英語
   - 関数名、クラス名、変数名、モジュール名
   - 理由: 国際的な標準、AIツールとの親和性、将来的な国際化対応

2. **Docstring**: 英語 + 重要部分に日本語併記
   - 1行目の概要: 英語(ピリオド `.` で終わる)
   - 詳細説明: 英語で記述し、必要に応じて日本語を括弧書きで補足
   - 理由: Ruff等のリンターとの完全互換性、AIツールが最も理解しやすい

3. **型ヒント**: 英語(Python標準)
   - `List[int]`, `Optional[str]`, `Dict[str, Any]` など

4. **インラインコメント**: 日本語可
   - 複雑なビジネスロジックや計算式の説明
   - ドメイン知識が必要な箇所

5. **定数の説明コメント**: 日本語可
   - 設定値の意味、単位、影響範囲の説明

6. **エラーメッセージ**: 日本語推奨
   - エンドユーザーや運用チームが読む可能性があるため

#### Ruffとの互換性に関する重要な注意

Ruffのデフォルト設定では、docstringの1行目が英語のピリオド `.` で終わることを期待しています(D400ルール)。
日本語の句点 `。` で終わるとエラーになるため、以下のいずれかの対応が必要です:

**推奨: 英語docstring + 日本語併記**
```python
def process_data(data: pd.DataFrame) -> pd.DataFrame:
    """Process input data for analysis.
    
    入力データを分析用に前処理します。欠損値の処理、正規化、
    特徴量エンジニアリングを実行します。
    
    Args:
        data (pd.DataFrame): Input data to process.
                            (処理対象の入力データ)
    
    Returns:
        pd.DataFrame: Processed data ready for analysis.
                     (分析準備が完了した処理済みデータ)
    """
```

**代替案: Ruff設定の調整**
```toml
# pyproject.toml
[tool.ruff.lint]
ignore = ["D400"]  # docstringの1行目がピリオドで終わる要件を無効化
```

ただし、この方法は業界標準から逸脱するため、特別な理由がない限り推奨しません。

### 言語選択の判断基準

| 要素 | 言語 | 理由 |
|------|------|------|
| 関数名・クラス名 | 英語 | 国際標準、AI理解、将来の国際化 |
| 変数名 | 英語 | コードの可読性、一貫性 |
| Docstring 1行目 | 英語 | リンター互換、AI最適化 |
| Docstring 詳細 | 英語+日本語併記 | 正確性と標準準拠の両立 |
| インラインコメント | 日本語可 | ドメイン知識の正確な伝達 |
| エラーメッセージ | 日本語 | ユーザビリティ |
| ログメッセージ | 日本語 | 運用チームの利便性 |

## 必須要件

### Google Style Docstring の必須実装

すべてのクラス、関数、メソッドには、Google Style のdocstringを必ず実装してください。

#### Google Style Docstring の必要要件

1. **概要(Summary)**
   - 1行で関数/クラスの目的を簡潔に説明
   - ピリオドで終わる

2. **詳細説明(Extended Description)** (必要に応じて)
   - 空行を挟んで概要の後に記述
   - 機能の詳細、アルゴリズム、使用上の注意点など

3. **Args (引数)**
   - すべての引数を記述
   - 形式: `引数名 (型): 説明`
   - オプション引数の場合はデフォルト値を記載

4. **Returns (戻り値)**
   - 戻り値の型と意味を説明
   - 形式: `型: 説明`
   - 戻り値がない場合は省略可

5. **Raises (例外)**
   - 発生する可能性のある例外を記述
   - 形式: `例外名: 発生条件`

6. **Yields (ジェネレータの場合)**
   - ジェネレータが生成する値の型と説明

7. **Examples (使用例)** (推奨)
   - 実際の使用方法を示すコード例

#### Google Style Docstring の記述例

**関数の例:**

```python
def calculate_discount(price, discount_rate, tax_rate=0.1):
    """Calculate the final price after applying discount and tax.
    
    This function applies a discount rate to the original price and then
    adds the tax rate to compute the final price. The result is rounded
    to two decimal places.
    
    Args:
        price (float): The original price of the product. Must be positive.
        discount_rate (float): The discount rate to apply. Must be between 0.0 and 1.0.
        tax_rate (float, optional): The tax rate to apply. Defaults to 0.1 (10%).
    
    Returns:
        float: The final price after discount and tax, rounded to 2 decimal places.
    
    Raises:
        ValueError: If price is negative or discount_rate is not in range [0.0, 1.0].
    
    Examples:
        >>> calculate_discount(1000, 0.2)
        880.0
        >>> calculate_discount(1000, 0.2, 0.08)
        864.0
    """
    if price < 0:
        raise ValueError("Price must be positive")
    if not 0.0 <= discount_rate <= 1.0:
        raise ValueError("Discount rate must be between 0.0 and 1.0")
    
    discounted_price = price * (1 - discount_rate)
    final_price = discounted_price * (1 + tax_rate)
    return round(final_price, 2)
```

**クラスの例:**

```python
class DataProcessor:
    """Process and transform data for machine learning.
    
    This class loads data from CSV files and performs preprocessing operations
    including missing value handling, normalization, and feature engineering.
    
    Attributes:
        file_path (str): Path to the CSV file to be processed.
        data (pd.DataFrame): The loaded DataFrame.
        scaler (StandardScaler): Scaler for data normalization.
    
    Examples:
        >>> processor = DataProcessor("data/input.csv")
        >>> processor.load_data()
        >>> processor.handle_missing_values()
        >>> normalized_data = processor.normalize()
    """
    
    def __init__(self, file_path):
        """Initialize the DataProcessor.
        
        Args:
            file_path (str): Path to the CSV file to be processed.
        
        Raises:
            FileNotFoundError: If the specified file does not exist.
        """
        self.file_path = file_path
        self.data = None
        self.scaler = StandardScaler()
    
    def load_data(self):
        """Load data from the CSV file.
        
        Returns:
            pd.DataFrame: The loaded DataFrame.
        
        Raises:
            FileNotFoundError: If the file is not found.
            pd.errors.ParserError: If CSV parsing fails.
        """
        try:
            self.data = pd.read_csv(self.file_path)
            return self.data
        except FileNotFoundError:
            raise FileNotFoundError(f"File not found: {self.file_path}")
```

**ジェネレータの例:**

```python
def batch_generator(data, batch_size=32):
    """Generate data in batches for memory-efficient processing.
    
    Split large datasets into smaller batches to enable memory-efficient
    processing of data that might not fit in memory all at once.
    
    Args:
        data (list): The list of data to be processed.
        batch_size (int, optional): The size of each batch. Defaults to 32.
    
    Yields:
        list: A list containing batch_size elements. The last batch may
            contain fewer elements if the data length is not evenly divisible.
    
    Examples:
        >>> data = list(range(100))
        >>> for batch in batch_generator(data, batch_size=10):
        ...     process_batch(batch)
    """
    for i in range(0, len(data), batch_size):
        yield data[i:i + batch_size]
```

#### 重要な注意点

- docstringは必ず三重引用符(`"""`)で囲む
- インデントはコードブロックと統一する
- 型ヒント(type hints)がある場合でも、docstringで型を明記する
- **1行目の概要は英語で記述し、ピリオド `.` で終わる**(Ruff互換のため)
- 詳細説明では、必要に応じて日本語を併記する
- 1行の概要は命令形または説明形で記述する

#### 英語+日本語併記の実践例

本プロジェクトで推奨される、Ruff互換かつ日本語も含む記述スタイル:

**実践例1: 工場データ処理関数**

```python
def calculate_defect_rate(total_count: int, defect_count: int) -> float:
    """Calculate the defect rate from production counts.
    
    全体の生産数と不良品数から不良品率(%)を計算します。
    結果は小数点以下2桁に丸められます。
    
    Args:
        total_count (int): Total number of products. Must be positive.
                          (全生産数。正の整数である必要があります)
        defect_count (int): Number of defective products. 
                           Must be between 0 and total_count.
                           (不良品数。0以上でtotal_count以下)
    
    Returns:
        float: Defect rate as percentage (0.0 to 100.0).
              (不良品率(パーセント)。0.0から100.0の範囲)
    
    Raises:
        ValueError: If total_count is not positive or defect_count is invalid.
                   (total_countが正でない、またはdefect_countが不正な場合)
    
    Examples:
        >>> calculate_defect_rate(1000, 25)
        2.5
        >>> calculate_defect_rate(500, 0)
        0.0
    """
    if total_count <= 0:
        raise ValueError("total_countは正の整数である必要があります")
    if defect_count < 0 or defect_count > total_count:
        raise ValueError("defect_countは0以上、total_count以下である必要があります")
    
    rate = (defect_count / total_count) * 100
    return round(rate, 2)
```

**実践例2: クラスのdocstring**

```python
class ProductionLineMonitor:
    """Monitor production line status and detect anomalies.
    
    生産ラインの状態を監視し、異常を検出するクラスです。
    リアルタイムデータを受け取り、統計的手法で異常値を判定します。
    
    Attributes:
        line_id (str): Production line identifier.
                      (生産ライン識別子)
        threshold (float): Anomaly detection threshold (z-score).
                          (異常検知閾値(zスコア))
        history (List[float]): Historical data for baseline calculation.
                              (ベースライン計算用の履歴データ)
    
    Examples:
        >>> monitor = ProductionLineMonitor("LINE-A", threshold=3.0)
        >>> monitor.add_measurement(25.5)
        >>> is_anomaly = monitor.check_anomaly(45.2)
        >>> print(is_anomaly)
        True
    """
    
    def __init__(self, line_id: str, threshold: float = 3.0) -> None:
        """Initialize the production line monitor.
        
        Args:
            line_id (str): Production line identifier.
                          (生産ライン識別子)
            threshold (float, optional): Anomaly detection threshold.
                                        Defaults to 3.0.
                                        (異常検知閾値。デフォルトは3.0)
        """
        self.line_id = line_id
        self.threshold = threshold
        self.history: List[float] = []
```

**実践例3: 簡潔な関数(日本語補足なし)**

簡単な関数では、英語のみで十分明確な場合は日本語を省略可:

```python
def normalize_temperature(celsius: float) -> float:
    """Normalize temperature value to 0-1 range.
    
    Args:
        celsius (float): Temperature in Celsius (-50 to 150).
    
    Returns:
        float: Normalized value between 0.0 and 1.0.
    
    Raises:
        ValueError: If temperature is out of valid range.
    """
    if not -50 <= celsius <= 150:
        raise ValueError("温度は-50℃から150℃の範囲である必要があります")
    return (celsius + 50) / 200
```

### ファイル冒頭コメントの必須実装

すべてのコードファイルの冒頭には、以下の情報を含むコメントを必ず実装してください。

- ファイルの役割と機能
- 主要なクラスや関数の概要
- プロジェクト内での位置づけ
- 使用方法の簡単な例

#### ファイル冒頭コメントの記述例

**データ処理モジュールの例:**

```python
"""Data preprocessing and feature engineering module.

This module provides classes and functions for loading, validating, and
transforming raw factory data into features suitable for machine learning.
It handles missing values, applies normalization, and generates time-series
features for production line monitoring.

Main Components:
    - DataLoader: Loads data from CSV files with schema validation
    - FeatureTransformer: Transforms raw data into ML-ready features
    - ValidationError: Custom exception for data validation errors

Project Context:
    Part of the factory-ml-offline-system training pipeline. This module
    is used by the model trainer to prepare data before model training.

Example:
    >>> from preprocessor import DataLoader, FeatureTransformer
    >>> loader = DataLoader("config/schema.json")
    >>> data = loader.load_csv("data/input/raw_data.csv")
    >>> transformer = FeatureTransformer()
    >>> features = transformer.transform(data)
"""

import pandas as pd
import numpy as np
from typing import Dict, List, Optional
# ... rest of the code
```

**機械学習モデルトレーナーの例:**

```python
"""Machine learning model training module.

This module implements the complete training pipeline for factory production
prediction models. It handles data preprocessing, model training with
hyperparameter optimization, evaluation, and ONNX export for deployment.

Main Components:
    - ModelTrainer: Main training orchestrator with cross-validation
    - train_model(): Trains a single model with given hyperparameters
    - evaluate_model(): Evaluates model performance on test data
    - export_to_onnx(): Converts trained model to ONNX format

Project Context:
    Core component of the training pipeline. Called by main.py when
    training mode is selected. Outputs trained models to models/current/
    and generates training reports in data/output/reports/.

Example:
    >>> from model_trainer import ModelTrainer
    >>> trainer = ModelTrainer(config_path="config/app_settings.json")
    >>> metrics = trainer.train(data_path="data/input/training_data.csv")
    >>> print(f"Model accuracy: {metrics['accuracy']:.2f}")
"""

import logging
from pathlib import Path
import lightgbm as lgb
# ... rest of the code
```

**ユーティリティモジュールの例:**

```python
"""Utility functions for configuration and date parsing.

This module provides helper functions used across the application for
common tasks like loading configuration files and parsing date strings
in various formats.

Main Components:
    - load_config(): Loads and validates JSON configuration files
    - parse_date(): Parses date strings with multiple format support
    - ConfigError: Custom exception for configuration errors

Project Context:
    Shared utility module used by both training and inference components.
    Provides consistent configuration handling across the system.

Example:
    >>> from util import load_config, parse_date
    >>> config = load_config("config/app_settings.json")
    >>> date = parse_date("2025-12-06", format="yyyy-MM-dd")
"""

import json
from datetime import datetime
from typing import Any, Dict
# ... rest of the code
```

#### ファイル冒頭コメントの重要な注意点

- モジュールdocstringは必ずファイルの最初に配置する（shebangやエンコーディング宣言の後）
- 三重引用符(`"""`)で囲む
- 1行目は簡潔な概要で、ピリオドで終わる
- 空行を挟んで詳細説明を記述
- 主要なコンポーネントは箇条書きで明記
- プロジェクト内での役割を明確に説明
- 実際の使用例を含める

### データの型の明記

すべての関数およびメソッドの引数と戻り値には、型ヒント (Type Hints) を必ず実装してください。
型ヒントがある場合でも、Google Style Docstring の Args セクションで型を明記してください。

#### 基本的な型ヒントの例

**プリミティブ型とコレクション型:**

```python
from typing import List, Dict, Set, Tuple, Optional

def process_numbers(values: List[int], threshold: float = 0.5) -> Dict[str, float]:
    """Process a list of numbers and return statistics.
    
    Args:
        values (List[int]): List of integer values to process.
        threshold (float, optional): Threshold for filtering. Defaults to 0.5.
    
    Returns:
        Dict[str, float]: Dictionary containing mean, median, and std values.
    """
    return {
        "mean": sum(values) / len(values),
        "median": sorted(values)[len(values) // 2],
        "std": calculate_std(values)
    }

def find_unique_items(items: List[str]) -> Set[str]:
    """Extract unique items from a list.
    
    Args:
        items (List[str]): List of string items.
    
    Returns:
        Set[str]: Set of unique items.
    """
    return set(items)

def get_coordinate() -> Tuple[float, float]:
    """Get current coordinate.
    
    Returns:
        Tuple[float, float]: Tuple of (latitude, longitude).
    """
    return (35.6762, 139.6503)
```

**Optional型とNone:**

```python
from typing import Optional

def find_user(user_id: int) -> Optional[Dict[str, str]]:
    """Find a user by ID.
    
    Args:
        user_id (int): The ID of the user to find.
    
    Returns:
        Optional[Dict[str, str]]: User data dictionary if found, None otherwise.
    """
    user = database.query(user_id)
    return user if user else None

def create_report(data: List[float], title: Optional[str] = None) -> str:
    """Create a report from data.
    
    Args:
        data (List[float]): Data to include in the report.
        title (Optional[str], optional): Report title. Defaults to None.
    
    Returns:
        str: Generated report as a string.
    """
    report_title = title if title else "Untitled Report"
    return f"{report_title}\n{format_data(data)}"
```

#### 複雑な型の定義例

**Union型とカスタム型:**

```python
from typing import Union, List, Dict, Any
from pathlib import Path

def load_data(source: Union[str, Path]) -> pd.DataFrame:
    """Load data from a file path or string.
    
    Args:
        source (Union[str, Path]): File path as string or Path object.
    
    Returns:
        pd.DataFrame: Loaded data as DataFrame.
    """
    if isinstance(source, str):
        source = Path(source)
    return pd.read_csv(source)

def process_value(value: Union[int, float, str]) -> float:
    """Convert various types to float.
    
    Args:
        value (Union[int, float, str]): Value to convert.
    
    Returns:
        float: Converted float value.
    
    Raises:
        ValueError: If value cannot be converted to float.
    """
    return float(value)
```

**ジェネレータとイテレータ:**

```python
from typing import Iterator, Generator, List

def read_large_file(file_path: str) -> Iterator[str]:
    """Read large file line by line.
    
    Args:
        file_path (str): Path to the file to read.
    
    Yields:
        str: Each line from the file.
    """
    with open(file_path, 'r') as f:
        for line in f:
            yield line.strip()

def batch_processor(data: List[int], batch_size: int) -> Generator[List[int], None, None]:
    """Process data in batches.
    
    Args:
        data (List[int]): Data to process in batches.
        batch_size (int): Size of each batch.
    
    Yields:
        List[int]: A batch of data.
    """
    for i in range(0, len(data), batch_size):
        yield data[i:i + batch_size]
```

**Callable型とコールバック:**

```python
from typing import Callable, List, Any

def apply_transform(
    data: List[float],
    transform_func: Callable[[float], float]
) -> List[float]:
    """Apply a transformation function to each element.
    
    Args:
        data (List[float]): List of values to transform.
        transform_func (Callable[[float], float]): Function that takes a float
            and returns a transformed float.
    
    Returns:
        List[float]: List of transformed values.
    """
    return [transform_func(x) for x in data]

def create_processor(
    validator: Callable[[Any], bool],
    error_handler: Optional[Callable[[Exception], None]] = None
) -> Callable[[Any], Any]:
    """Create a data processor with validation.
    
    Args:
        validator (Callable[[Any], bool]): Function to validate input.
        error_handler (Optional[Callable[[Exception], None]], optional): 
            Function to handle errors. Defaults to None.
    
    Returns:
        Callable[[Any], Any]: A processor function.
    """
    def processor(data: Any) -> Any:
        if not validator(data):
            raise ValueError("Validation failed")
        return process(data)
    return processor
```

**TypedDictとカスタム型定義:**

```python
from typing import TypedDict, List, Optional

class ModelConfig(TypedDict):
    """Configuration for model training."""
    model_type: str
    learning_rate: float
    epochs: int
    batch_size: int

class DataPoint(TypedDict, total=False):
    """Data point with optional fields."""
    timestamp: str
    value: float
    label: Optional[str]

def train_model(config: ModelConfig, data: List[DataPoint]) -> Dict[str, Any]:
    """Train a model with the given configuration.
    
    Args:
        config (ModelConfig): Model configuration dictionary.
        data (List[DataPoint]): Training data points.
    
    Returns:
        Dict[str, Any]: Training results and metrics.
    """
    model = create_model(config["model_type"])
    model.train(data, lr=config["learning_rate"], epochs=config["epochs"])
    return {"accuracy": model.evaluate(), "loss": model.get_loss()}
```

**Generic型とクラス:**

```python
from typing import Generic, TypeVar, List, Optional

T = TypeVar('T')

class DataContainer(Generic[T]):
    """Generic container for storing and processing data.
    
    Attributes:
        items (List[T]): List of stored items.
    """
    
    def __init__(self) -> None:
        """Initialize an empty container."""
        self.items: List[T] = []
    
    def add(self, item: T) -> None:
        """Add an item to the container.
        
        Args:
            item (T): Item to add.
        """
        self.items.append(item)
    
    def get(self, index: int) -> Optional[T]:
        """Get an item by index.
        
        Args:
            index (int): Index of the item to retrieve.
        
        Returns:
            Optional[T]: Item at the index, or None if out of bounds.
        """
        if 0 <= index < len(self.items):
            return self.items[index]
        return None
    
    def filter(self, predicate: Callable[[T], bool]) -> List[T]:
        """Filter items using a predicate function.
        
        Args:
            predicate (Callable[[T], bool]): Function to test each item.
        
        Returns:
            List[T]: Filtered list of items.
        """
        return [item for item in self.items if predicate(item)]

# 使用例
int_container: DataContainer[int] = DataContainer[int]()
int_container.add(42)

str_container: DataContainer[str] = DataContainer[str]()
str_container.add("hello")
```

#### データの型の明記の重要な注意点

- Pythonの組み込み型、コレクション型、カスタムクラスなど、適切な型を使用する
- Optional 型は `Optional[Type]` として明記し、デフォルト値が None の場合は docstring にも記載する
- 複数の型が考えられる場合は `Union[Type1, Type2]` を使用する（Python 3.10以降は `Type1 | Type2` も可）
- ジェネレータの場合は `Iterator[Type]` または `Generator[YieldType, SendType, ReturnType]` を使用する
- 型ヒントはPEP 484に準拠する
- 型ヒントを使用する場合、`from typing import ...` を忘れずにインポートする
- コレクション型（`List`, `Dict`, `Set`）は内部の型も明示する（例: `List[int]`, `Dict[str, float]`）
- 複雑な型は `TypedDict` や `TypeVar` を使って明確に定義する
- 関数の戻り値がない場合は `-> None` を明示する

## 品質保証チェックリスト

コードを執筆した後、以下のチェックリストを使用して品質を確認してください。

### ドキュメンテーション

- [ ] すべてのモジュールにファイル冒頭コメントが実装されている
- [ ] ファイル冒頭コメントに以下が含まれている：
  - [ ] 簡潔な1行の概要（ピリオドで終わる）
  - [ ] 詳細な機能説明
  - [ ] 主要なコンポーネントの箇条書き
  - [ ] プロジェクト内での位置づけ
  - [ ] 実際の使用例
- [ ] すべてのクラスにGoogle Style Docstringが実装されている
- [ ] すべての関数とメソッドにGoogle Style Docstringが実装されている
- [ ] Docstringに以下が含まれている：
  - [ ] 1行の概要（ピリオドで終わる）
  - [ ] Args セクション（すべての引数を記載）
  - [ ] Returns セクション（戻り値がある場合）
  - [ ] Raises セクション（例外が発生する場合）
  - [ ] Examples セクション（推奨）

### 型ヒント

- [ ] すべての関数とメソッドの引数に型ヒントが実装されている
- [ ] すべての関数とメソッドの戻り値に型ヒントが実装されている
- [ ] Optional型を適切に使用している（None になる可能性がある場合）
- [ ] Union型を適切に使用している（複数の型が考えられる場合）
- [ ] コレクション型の内部の型も明示している（例: `List[int]`）
- [ ] 必要な typing モジュールのインポートが含まれている
- [ ] 型ヒントとdocstringの型定義が一致している

### コード品質

- [ ] 関数は単一責任の原則に従っている
- [ ] クラスは適切にカプセル化されている
- [ ] 変数名、関数名、クラス名が明確で説明的である
- [ ] マジックナンバーを避け、定数を使用している
- [ ] エラーハンドリングが適切に実装されている
- [ ] 例外メッセージが明確で具体的である
- [ ] 不要なコメントや古いコードが残っていない
- [ ] インデントとフォーマットが一貫している

### テスト

- [ ] ユニットテストが実装されている（該当する場合）
- [ ] エッジケースのテストが含まれている
- [ ] 例外処理のテストが含まれている
- [ ] テスト名が明確で意図が理解できる

### パフォーマンスとセキュリティ

- [ ] 不要なループや計算が含まれていない
- [ ] ファイルやリソースが適切にクローズされている
- [ ] 機密情報（パスワード、APIキーなど）がハードコードされていない
- [ ] SQL インジェクションなどのセキュリティリスクがない
- [ ] 入力値の検証が適切に行われている

### プロジェクト固有

- [ ] 設定ファイル（`config/app_settings.json`）を適切に使用している
- [ ] ログ出力が適切に実装されている
- [ ] ファイルパスは絶対パスまたは設定から取得している
- [ ] プロジェクトの命名規則に従っている
- [ ] 既存のユーティリティ関数を再利用している

## 例外とベストプラクティス

必須要件の例外や、追加のベストプラクティスについて説明します。

### 1. プライベート関数にも簡潔なdocstringをつけること

プライベート関数（アンダースコアで始まる関数やメソッド）にも、簡潔なdocstringを実装してください。
公開APIほど詳細である必要はありませんが、関数の目的と基本的な引数・戻り値の説明は必須です。

**プライベート関数のdocstring例:**

```python
def _validate_input_data(data: pd.DataFrame) -> bool:
    """Validate that input data meets required schema.
    
    Args:
        data (pd.DataFrame): Input data to validate.
    
    Returns:
        bool: True if data is valid, False otherwise.
    """
    required_columns = ["timestamp", "value", "label"]
    return all(col in data.columns for col in required_columns)

def _calculate_rolling_average(values: List[float], window_size: int) -> List[float]:
    """Calculate rolling average with specified window size.
    
    Args:
        values (List[float]): List of values to process.
        window_size (int): Size of the rolling window.
    
    Returns:
        List[float]: List of rolling averages.
    """
    result = []
    for i in range(len(values) - window_size + 1):
        window = values[i:i + window_size]
        result.append(sum(window) / window_size)
    return result

class ModelTrainer:
    """Train machine learning models."""
    
    def _preprocess_features(self, features: pd.DataFrame) -> np.ndarray:
        """Preprocess features before training.
        
        Args:
            features (pd.DataFrame): Raw features.
        
        Returns:
            np.ndarray: Preprocessed feature array.
        """
        # Normalization and scaling
        return self.scaler.fit_transform(features.values)
```

**重要なポイント:**

- プライベート関数でも、1行の概要と基本的なArgs/Returnsセクションは必須
- Examples セクションは省略可能（公開関数ほど重要ではない）
- 詳細な説明は必要に応じて追加（複雑なロジックの場合）
- 型ヒントも忘れずに実装する

### 2. 定数とグローバル変数には説明コメントをつけること

重要な定数、グローバル変数、設定値には、必ず説明コメントを追加してください。
コメントは定数の目的、単位、使用される場面を明確に説明する必要があります。

**定数の説明コメント例:**

```python
# Maximum number of retries for API calls before giving up
MAX_RETRIES: int = 3

# Timeout in seconds for external API requests
API_TIMEOUT_SECONDS: int = 30

# Default batch size for data processing
# Used when batch_size is not specified in configuration
DEFAULT_BATCH_SIZE: int = 32

# Threshold for anomaly detection (z-score)
# Values with z-score above this threshold are flagged as anomalies
ANOMALY_THRESHOLD: float = 3.0

# Supported model types for training
# Add new model types here when implementing new algorithms
SUPPORTED_MODELS: List[str] = [
    "lightgbm",
    "xgboost",
    "random_forest",
]

# Column names for input data schema
# These columns must exist in all input CSV files
REQUIRED_COLUMNS: List[str] = [
    "timestamp",      # Data collection timestamp (ISO 8601 format)
    "temperature",    # Temperature in Celsius
    "pressure",       # Pressure in kPa
    "humidity",       # Relative humidity (0-100%)
    "defect_flag",    # Binary flag: 1=defect, 0=normal
]

# Model performance thresholds
# Models below these thresholds will trigger retraining alerts
MIN_ACCURACY: float = 0.85  # Minimum acceptable accuracy (85%)
MIN_PRECISION: float = 0.80  # Minimum acceptable precision (80%)
MIN_RECALL: float = 0.75     # Minimum acceptable recall (75%)
```

**設定クラスでの定数定義例:**

```python
class Config:
    """Application configuration constants.
    
    This class centralizes all configuration constants used throughout
    the application. Values should be modified here rather than hardcoded
    in individual modules.
    """
    
    # Data paths
    INPUT_DATA_DIR: str = "data/input"          # Directory for raw input data
    OUTPUT_DATA_DIR: str = "data/output"        # Directory for processed output
    MODEL_DIR: str = "models/current"           # Directory for trained models
    ARCHIVE_DIR: str = "models/archive"         # Directory for archived models
    
    # Model training parameters
    TRAIN_TEST_SPLIT: float = 0.8               # 80% training, 20% testing
    CROSS_VALIDATION_FOLDS: int = 5             # Number of CV folds
    RANDOM_SEED: int = 42                       # For reproducibility
    
    # Feature engineering
    TIME_WINDOW_SIZE: int = 10                  # Window size for time-series features
    LAG_FEATURES: List[int] = [1, 2, 3, 5]     # Lag periods for lag features
    
    # Data validation
    MAX_MISSING_RATE: float = 0.1               # Maximum 10% missing values allowed
    OUTLIER_STD_THRESHOLD: float = 3.0          # Z-score threshold for outliers


# Global state (use sparingly)
# Logger instance shared across modules
# Initialized in main.py at application startup
_logger: Optional[logging.Logger] = None

# Cache for loaded configuration
# Avoids repeated file I/O for configuration access
_config_cache: Optional[Dict[str, Any]] = None
```

**列挙型（Enum）での定数定義例:**

```python
from enum import Enum

class ModelType(Enum):
    """Supported machine learning model types.
    
    Each enum value represents a model type that can be trained
    and deployed in the system.
    """
    LIGHTGBM = "lightgbm"        # LightGBM gradient boosting
    XGBOOST = "xgboost"          # XGBoost gradient boosting
    RANDOM_FOREST = "random_forest"  # Scikit-learn Random Forest
    NEURAL_NET = "neural_net"    # Simple feedforward neural network


class DataQuality(Enum):
    """Data quality status codes.
    
    Used to categorize data quality check results.
    """
    GOOD = 0      # Data passes all quality checks
    WARNING = 1   # Data has minor issues but is usable
    ERROR = 2     # Data has critical issues and should not be used
    UNKNOWN = 3   # Data quality cannot be determined
```

**重要なポイント:**

- すべての定数に説明コメントを追加する
- 単位や値の範囲を明記する（温度なら摂氏、圧力ならkPaなど）
- なぜその値を選んだのかを説明する（可能であれば）
- 設定を変更する際の影響範囲を示す
- グローバル変数は極力避け、使用する場合は用途を明確にする
- 関連する定数は Config クラスや Enum でグループ化する

### 3. AIツール(GitHub Copilot)との協調

GitHub Copilotが最も効果的に機能するための推奨事項:

#### 命名規則

**関数名は動詞で始める:**
```python
# Good: AIが目的を即座に理解できる
def calculate_average(values: List[float]) -> float:
def validate_input_data(data: pd.DataFrame) -> bool:
def process_raw_data(file_path: str) -> pd.DataFrame:
def transform_features(data: pd.DataFrame) -> np.ndarray:

# Avoid: 目的が不明確
def average(values: List[float]) -> float:
def data(data: pd.DataFrame) -> bool:
def handler(file_path: str) -> pd.DataFrame:
```

**変数名は説明的に:**
```python
# Good: AIがコンテキストを理解できる
defect_count: int = 25
temperature_celsius: float = 45.5
total_production_time_minutes: int = 120
anomalous_data_points: List[float] = []

# Avoid: 意味が不明確
x: int = 25
temp: float = 45.5
time: int = 120
data: List[float] = []
```

**クラス名は名詞で、役割を明確に:**
```python
# Good
class DataValidator:
class ModelTrainer:
class FeatureTransformer:
class ProductionLineMonitor:

# Avoid
class Data:
class Model:
class Helper:
class Manager:
```

#### 型ヒントを必ず記述

AIが引数と戻り値の型を理解することで、より正確なコード補完が可能になります:

```python
# Good: 型が明確でAIが次のコードを正確に予測できる
def merge_dataframes(
    left: pd.DataFrame,
    right: pd.DataFrame,
    on: str,
    how: str = "inner"
) -> pd.DataFrame:
    return pd.merge(left, right, on=on, how=how)

# Avoid: 型が不明でAIの補完精度が低下
def merge_dataframes(left, right, on, how="inner"):
    return pd.merge(left, right, on=on, how=how)
```

#### Docstringの1行目を明確に

AIは最初の1行でコンテキストを把握します:

```python
# Good: 明確な動詞+目的で始まる
def preprocess_sensor_data(raw_data: pd.DataFrame) -> pd.DataFrame:
    """Preprocess raw sensor data for machine learning.
    
    センサーの生データを機械学習用に前処理します。
    """

# Avoid: 曖昧な説明
def preprocess_sensor_data(raw_data: pd.DataFrame) -> pd.DataFrame:
    """Data processing.
    
    データを処理します。
    """
```

#### コンテキストを提供するコメント

複雑なロジックには、AIが理解できるようにコンテキストを提供:

```python
def detect_anomalies(data: pd.DataFrame, threshold: float = 3.0) -> pd.DataFrame:
    """Detect anomalies using z-score method.
    
    Args:
        data (pd.DataFrame): Time series data with 'value' column.
        threshold (float): Z-score threshold for anomaly detection.
    
    Returns:
        pd.DataFrame: Data with 'is_anomaly' column added.
    """
    # Calculate z-score for each data point
    # Z-score = (value - mean) / std
    mean = data['value'].mean()
    std = data['value'].std()
    data['z_score'] = (data['value'] - mean) / std
    
    # Flag values exceeding threshold as anomalies
    # 閾値を超える値を異常値としてフラグ付け
    data['is_anomaly'] = data['z_score'].abs() > threshold
    
    return data
```

### 4. エラーメッセージとログの言語ガイドライン

エラーメッセージとログの言語選択は、対象読者によって使い分けます。

#### エラーメッセージ: 日本語推奨

エンドユーザーや運用チームが読む可能性があるため、日本語を使用:

```python
def load_csv_file(file_path: str) -> pd.DataFrame:
    """Load data from CSV file.
    
    Args:
        file_path (str): Path to the CSV file.
    
    Returns:
        pd.DataFrame: Loaded data.
    
    Raises:
        FileNotFoundError: If the file does not exist.
        ValueError: If the file format is invalid.
    """
    if not os.path.exists(file_path):
        raise FileNotFoundError(
            f"ファイルが見つかりません: {file_path}"
        )
    
    try:
        data = pd.read_csv(file_path)
    except pd.errors.ParserError as e:
        raise ValueError(
            f"CSVファイルの形式が不正です: {file_path}\n"
            f"詳細: {str(e)}"
        )
    
    if data.empty:
        raise ValueError(
            f"ファイルにデータが含まれていません: {file_path}"
        )
    
    return data
```

#### バリデーションエラー: 日本語で具体的に

```python
def validate_production_data(data: pd.DataFrame) -> None:
    """Validate production data schema and values.
    
    Args:
        data (pd.DataFrame): Production data to validate.
    
    Raises:
        ValueError: If validation fails.
    """
    required_columns = ['timestamp', 'temperature', 'pressure', 'defect_flag']
    missing_columns = [col for col in required_columns if col not in data.columns]
    
    if missing_columns:
        raise ValueError(
            f"必須カラムが不足しています: {', '.join(missing_columns)}\n"
            f"必要なカラム: {', '.join(required_columns)}"
        )
    
    if data['temperature'].min() < -50 or data['temperature'].max() > 150:
        raise ValueError(
            "温度の値が有効範囲(-50℃〜150℃)を超えています\n"
            f"最小値: {data['temperature'].min()}℃\n"
            f"最大値: {data['temperature'].max()}℃"
        )
    
    if not data['defect_flag'].isin([0, 1]).all():
        raise ValueError(
            "defect_flagは0または1である必要があります"
        )
```

#### ログメッセージ: 日本語推奨

運用チームが読むため、日本語で記録:

```python
import logging

logger = logging.getLogger(__name__)

def train_model(data_path: str, output_path: str) -> Dict[str, float]:
    """Train machine learning model.
    
    Args:
        data_path (str): Path to training data.
        output_path (str): Path to save trained model.
    
    Returns:
        Dict[str, float]: Training metrics.
    """
    logger.info(f"モデル訓練を開始します: {data_path}")
    
    try:
        data = load_data(data_path)
        logger.info(f"データ読み込み完了: {len(data)} 件")
        
        model = train_lightgbm(data)
        logger.info("モデル訓練完了")
        
        metrics = evaluate_model(model, data)
        logger.info(
            f"評価結果 - Accuracy: {metrics['accuracy']:.3f}, "
            f"Precision: {metrics['precision']:.3f}, "
            f"Recall: {metrics['recall']:.3f}"
        )
        
        save_model(model, output_path)
        logger.info(f"モデルを保存しました: {output_path}")
        
        return metrics
        
    except Exception as e:
        logger.error(f"モデル訓練中にエラーが発生しました: {str(e)}")
        raise
```

#### アサーションメッセージ: 開発者向けは英語または日本語

開発者向けのデバッグ情報は、チームの状況に応じて選択:

```python
def calculate_metrics(predictions: np.ndarray, labels: np.ndarray) -> Dict[str, float]:
    """Calculate classification metrics.
    
    Args:
        predictions (np.ndarray): Predicted labels.
        labels (np.ndarray): True labels.
    
    Returns:
        Dict[str, float]: Calculated metrics.
    """
    assert len(predictions) == len(labels), \
        f"予測値とラベルの長さが一致しません: {len(predictions)} vs {len(labels)}"
    
    assert predictions.ndim == 1, \
        f"予測値は1次元配列である必要があります: shape={predictions.shape}"
    
    assert set(predictions).issubset({0, 1}), \
        "予測値は0または1である必要があります"
    
    # Calculate metrics
    accuracy = (predictions == labels).mean()
    # ...
```

#### 言語選択のまとめ

| メッセージ種別 | 対象読者 | 推奨言語 | 理由 |
|--------------|---------|---------|------|
| 例外メッセージ | エンドユーザー/運用 | 日本語 | 理解しやすさ |
| ログメッセージ | 運用チーム | 日本語 | 運用効率 |
| アサーション | 開発者 | 英語or日本語 | チーム方針次第 |
| デバッグ出力 | 開発者 | 英語or日本語 | チーム方針次第 |