"""ONNX model conversion module.

This module provides functionality for converting scikit-learn models to ONNX
format for deployment. ONNX format allows models to be used across different
platforms and frameworks efficiently.

Main Components:
    - convert_model_to_onnx(): Converts sklearn model to ONNX format
    - load_model(): Loads serialized sklearn model
    - save_onnx_model(): Saves model as ONNX file

Project Context:
    Part of the factory-ml-offline-system training pipeline. Converts trained
    scikit-learn models to ONNX format for deployment in the Java inference
    service.

Example:
    >>> from trainer.onnx_converter import save_onnx_model
    >>> model = train_model(data)
    >>> onnx_path = save_onnx_model(model, 'models/current', 'defect_classifier')
    >>> print(f"Model saved to: {onnx_path}")
"""

from skl2onnx import convert_sklearn
from skl2onnx.common.data_types import FloatTensorType, StringTensorType
import joblib
import os
from typing import Any, List, Tuple


def convert_model_to_onnx(
    model: Any,
    model_path: str,
    initial_types: List[Tuple[str, Any]]
) -> None:
    """Convert scikit-learn model to ONNX format.
    
    Converts a trained scikit-learn model to ONNX format and saves it to disk.
    ONNX format enables cross-platform model deployment.
    
    Args:
        model (Any): Trained scikit-learn model to convert.
                    (変換する訓練済みscikit-learnモデル)
        model_path (str): Path where ONNX model will be saved.
                         (ONNXモデルを保存するパス)
        initial_types (List[Tuple[str, Any]]): Input type specifications for ONNX.
                                               (ONNX用の入力型仕様)
    
    Examples:
        >>> from skl2onnx.common.data_types import FloatTensorType
        >>> initial_types = [('float_input', FloatTensorType([None, 10]))]
        >>> convert_model_to_onnx(model, 'model.onnx', initial_types)
    """
    onnx_model = convert_sklearn(model, initial_types=initial_types)
    with open(model_path, "wb") as f:
        f.write(onnx_model.SerializeToString())


def load_model(model_file: str) -> Any:
    """Load serialized scikit-learn model from disk.
    
    Args:
        model_file (str): Path to the serialized model file (.joblib or .pkl).
                         (シリアライズされたモデルファイルへのパス)
    
    Returns:
        Any: Loaded scikit-learn model.
            (読み込まれたscikit-learnモデル)
    
    Raises:
        FileNotFoundError: If model file does not exist.
                          (モデルファイルが存在しない場合)
    
    Examples:
        >>> model = load_model('models/current/model.joblib')
    """
    if not os.path.exists(model_file):
        raise FileNotFoundError(f"モデルファイルが見つかりません: {model_file}")
    
    return joblib.load(model_file)


def save_onnx_model(model: Any, output_dir: str, model_name: str) -> str:
    """Save scikit-learn model as ONNX format.
    
    Converts the model to ONNX format and saves it with the specified name.
    Creates the output directory if it doesn't exist.
    
    Args:
        model (Any): Trained scikit-learn Pipeline model.
                    (訓練済みscikit-learn Pipelineモデル)
        output_dir (str): Directory where ONNX model will be saved.
                         (ONNXモデルを保存するディレクトリ)
        model_name (str): Name for the ONNX model file (without extension).
                         (ONNXモデルファイル名（拡張子なし）)
    
    Returns:
        str: Path to the saved ONNX model file.
            (保存されたONNXモデルファイルへのパス)
    
    Raises:
        AttributeError: If model lacks required attributes.
                       (モデルに必要な属性がない場合)
    
    Examples:
        >>> model = train_model(data)
        >>> path = save_onnx_model(model, 'models/current', 'defect_classifier')
        >>> print(path)
        models/current/defect_classifier.onnx
    """
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)
    
    model_path = os.path.join(output_dir, f"{model_name}.onnx")
    
    # Define input types for ONNX conversion
    # For Pipeline models, get n_features_in_ from the first step (preprocessor)
    # Pipeline モデルの場合、最初のステップ（前処理器）からn_features_in_を取得
    if hasattr(model, 'steps') and len(model.steps) > 0:
        # Get the preprocessor (first step in pipeline)
        # 前処理器（パイプラインの最初のステップ）を取得
        preprocessor = model.steps[0][1]
        if hasattr(preprocessor, 'n_features_in_'):
            n_features = preprocessor.n_features_in_
        else:
            # Fallback: use a default value
            # フォールバック: デフォルト値を使用
            n_features = 10
    else:
        n_features = getattr(model, 'n_features_in_', 10)
    
    # ONNX変換用の入力型を定義（FloatTensorTypeのみ）
    initial_types: List[Tuple[str, Any]] = [
        ('float_input', FloatTensorType([None, n_features]))
    ]
    
    convert_model_to_onnx(model, model_path, initial_types)
    return model_path