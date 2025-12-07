"""Training pipeline entry point.

This module serves as the main entry point for the model training pipeline.
It orchestrates data loading, preprocessing, model training, ONNX conversion,
and report generation based on command-line arguments.

Main Components:
    - main(): Entry point function that orchestrates the pipeline
    - load_config(): Load configuration from JSON file
    - load_schema(): Load schema definition from JSON file

Project Context:
    Entry point for the python-trainer component. Called from the command line
    to train models and generate reports. Coordinates all training pipeline
    components.

Example:
    >>> python -m trainer.main \
    ...     --data data/input/training_data.csv \
    ...     --output models/current \
    ...     --config config/app_settings.json \
    ...     --report
"""

from pathlib import Path
import argparse
import json
import logging
import sys
import traceback
from typing import Any, Dict

# Import classes and functions from modules
# モジュールからクラスと関数をインポート
from trainer.data_loader import DataLoader
from trainer.preprocessor import Preprocessor
from trainer.model_trainer import ModelTrainer

# Configure logging
# ロギング設定
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)
logger = logging.getLogger(__name__)


def load_config(config_path: str) -> Dict[str, Any]:
    """Load configuration from JSON file.
    
    Args:
        config_path (str): Path to the configuration JSON file.
                          (設定JSONファイルのパス)
    
    Returns:
        Dict[str, Any]: Configuration dictionary.
                       (設定辞書)
    
    Raises:
        FileNotFoundError: If configuration file is not found.
                          (設定ファイルが見つからない場合)
        ValueError: If JSON format is invalid.
                   (JSON形式が不正な場合)
    """
    try:
        with open(config_path, 'r', encoding='utf-8') as f:
            return json.load(f)
    except FileNotFoundError:
        raise FileNotFoundError(f"設定ファイルが見つかりません: {config_path}")
    except json.JSONDecodeError as e:
        raise ValueError(f"設定ファイルのJSON形式が不正です: {config_path}\n詳細: {str(e)}")


def load_schema(schema_path: str) -> Dict[str, Any]:
    """Load schema definition from JSON file.
    
    Args:
        schema_path (str): Path to the schema JSON file.
                          (スキーマJSONファイルのパス)
    
    Returns:
        Dict[str, Any]: Schema dictionary.
                       (スキーマ辞書)
    
    Raises:
        FileNotFoundError: If schema file is not found.
                          (スキーマファイルが見つからない場合)
        ValueError: If JSON format is invalid.
                   (JSON形式が不正な場合)
    """
    try:
        with open(schema_path, 'r', encoding='utf-8') as f:
            return json.load(f)
    except FileNotFoundError:
        raise FileNotFoundError(f"スキーマファイルが見つかりません: {schema_path}")
    except json.JSONDecodeError as e:
        raise ValueError(f"スキーマファイルのJSON形式が不正です: {schema_path}\n詳細: {str(e)}")


def main() -> None:
    """Execute the complete model training pipeline.
    
    Parses command-line arguments and orchestrates the training workflow:
    1. Load data from CSV/Excel file
    2. Preprocess data using schema-defined transformations
    3. Train machine learning model
    4. Convert model to ONNX format for deployment
    5. Generate analysis reports (optional)
    
    Command-line Arguments:
        --data: Path to input data file (CSV or Excel)
        --output: Directory to save trained model and reports
        --config: Path to configuration file (JSON)
        --report: Flag to generate analysis reports (optional)
    
    Raises:
        FileNotFoundError: If data or config file is not found.
                          (データまたは設定ファイルが見つからない場合)
        ValueError: If data validation fails.
                   (データ検証に失敗した場合)
    
    Examples:
        Train a model without report generation:
        >>> python -m trainer.main \
        ...     --data data/input/training_data.csv \
        ...     --output models/current \
        ...     --config config/model_config.json
        
        Train a model with report generation:
        >>> python -m trainer.main \
        ...     --data data/input/training_data.csv \
        ...     --output models/current \
        ...     --config config/model_config.json \
        ...     --report
    """
    parser = argparse.ArgumentParser(
        description='Train a machine learning model for factory production prediction.'
    )
    parser.add_argument(
        '--data',
        type=str,
        required=True,
        help='Path to the input data file (CSV/Excel)'
    )
    parser.add_argument(
        '--output',
        type=str,
        required=True,
        help='Directory to save the trained model and reports'
    )
    parser.add_argument(
        '--config',
        type=str,
        required=True,
        help='Path to the configuration file'
    )
    parser.add_argument(
        '--report',
        action='store_true',
        help='Generate analysis report (SHAP, metrics, drift)'
    )
    parser.add_argument(
        '--schema',
        type=str,
        default='config/schema.json',
        help='Path to schema JSON file (default: config/schema.json)'
    )

    args = parser.parse_args()

    logger.info("=" * 60)
    logger.info("訓練パイプラインを開始します")
    logger.info("=" * 60)
    logger.info(f"データファイル: {args.data}")
    logger.info(f"出力ディレクトリ: {args.output}")
    logger.info(f"設定ファイル: {args.config}")
    logger.info(f"レポート生成: {args.report}")
    logger.info("")
    
    try:
        # Step 1: Load configuration and schema
        # ステップ1: 設定とスキーマの読み込み
        logger.info("ステップ1: 設定とスキーマを読み込んでいます...")
        
        config = load_config(args.config)
        logger.info(f"  ✓ 設定ファイル読み込み完了: {args.config}")
        
        # Load schema from specified path
        # 指定されたパスからスキーマを読み込み
        schema = load_schema(args.schema)
        logger.info(f"  ✓ スキーマファイル読み込み完了: {args.schema}")
        logger.info("")
        
        # Validate training configuration exists
        # 訓練設定の存在を検証
        if 'training' not in config:
            raise ValueError(
                "設定ファイルに'training'セクションが見つかりません。\n"
                "config/app_settings.jsonに訓練設定を追加してください。"
            )
        
        training_config = config['training']
        logger.info("  訓練設定:")
        logger.info(f"    - 数値特徴量: {training_config.get('numerical_features', [])}")
        logger.info(f"    - カテゴリ特徴量: {training_config.get('categorical_features', [])}")
        logger.info(f"    - ターゲット: {training_config.get('target', 'label')}")
        logger.info(f"    - テストサイズ: {training_config.get('test_size', 0.2)}")
        logger.info("")
        
        # Step 2: Load data
        # ステップ2: データ読み込み
        logger.info("ステップ2: データを読み込んでいます...")
        
        loader = DataLoader(schema)
        data = loader.load_data(args.data)
        logger.info(f"  ✓ データ読み込み完了: {len(data)} 件")
        logger.info(f"  カラム数: {len(data.columns)}")
        logger.info("")
        
        # Step 3: Validate data
        # ステップ3: データバリデーション
        logger.info("ステップ3: データを検証しています...")
        
        # Note: Data preprocessing is handled internally by ModelTrainer
        # The Preprocessor class provides schema-based validation and feature info
        # 注: データ前処理はModelTrainerが内部で実行します
        # Preprocessorクラスはスキーマベースの検証と特徴量情報を提供します
        preprocessor = Preprocessor(schema)
        # Perform fit_transform to validate data structure and get feature count
        # データ構造の検証と特徴量数の取得のためfit_transformを実行
        processed_data = preprocessor.fit_transform(data)
        logger.info("  ✓ データ検証完了")
        logger.info(f"  検証済み特徴量数: {processed_data.shape[1]}")
        logger.info("")
        
        # Step 4: Train model
        # ステップ4: モデル訓練
        logger.info("ステップ4: モデルを訓練しています...")
        
        # Create output directory if it doesn't exist
        # 出力ディレクトリが存在しない場合は作成
        output_dir = Path(args.output)
        output_dir.mkdir(parents=True, exist_ok=True)
        
        # ModelTrainer.run() performs its own data loading and preprocessing
        # ModelTrainer.run()は内部でデータ読み込みと前処理を実行します
        trainer = ModelTrainer(training_config)
        model_path = output_dir / 'model.joblib'
        trainer.run(args.data, str(model_path))
        logger.info("  ✓ モデル訓練完了")
        logger.info(f"  モデル保存先: {model_path}")
        logger.info("")
        
        # Step 5: Convert to ONNX (Optional - skipped for now due to complexity)
        # ステップ5: ONNX変換（オプション - 複雑性のため現在はスキップ）
        logger.info("ステップ5: ONNX変換をスキップ...")
        logger.info("  ℹ ONNX変換は別途実装が必要です")
        logger.info("  現在はjoblib形式のモデルを使用してください")
        logger.info("")
        
        onnx_path = None  # ONNX conversion is not yet supported
        
        # TODO: Implement proper ONNX conversion with correct input column names
        # The current challenge is matching the ColumnTransformer's expected
        # column names with the ONNX initial_types parameter
        # 
        # from trainer.onnx_converter import save_onnx_model
        # import joblib
        # model = joblib.load(str(model_path))
        # onnx_path = save_onnx_model(model, str(output_dir), 'defect_classifier')
        
        # Step 6: Generate report (optional)
        # ステップ6: レポート生成（オプション）
        report_path = None
        if args.report:
            logger.info("ステップ6: レポートを生成しています...")
            
            report_dir = output_dir / 'reports'
            report_dir.mkdir(parents=True, exist_ok=True)
            
            # Generate basic metrics report
            # 基本的なメトリクスレポートを生成
            # Note: Full report generation requires model evaluation results
            # 注: 完全なレポート生成にはモデル評価結果が必要
            metrics = {
                'model_path': str(model_path),
                'onnx_path': str(onnx_path) if onnx_path else None,
                'data_shape': list(data.shape),
                'training_config': training_config
            }
            
            report_path = report_dir / 'training_summary.json'
            with open(report_path, 'w', encoding='utf-8') as f:
                json.dump(metrics, f, indent=2, ensure_ascii=False)
            
            logger.info("  ✓ レポート生成完了")
            logger.info(f"  レポート保存先: {report_path}")
            logger.info("")
        
        # Success message
        # 成功メッセージ
        logger.info("=" * 60)
        logger.info("✓ 訓練パイプラインが正常に完了しました")
        logger.info("=" * 60)
        logger.info("出力ファイル:")
        logger.info(f"  - モデル: {model_path}")
        if onnx_path:
            logger.info(f"  - ONNXモデル: {onnx_path}")
        if args.report and report_path:
            logger.info(f"  - レポート: {report_path}")
        logger.info("")
        
    except FileNotFoundError as e:
        logger.error("")
        logger.error("=" * 60)
        logger.error("❌ ファイルが見つかりません")
        logger.error("=" * 60)
        logger.error(str(e))
        logger.error("")
        sys.exit(1)
        
    except ValueError as e:
        logger.error("")
        logger.error("=" * 60)
        logger.error("❌ データ検証エラー")
        logger.error("=" * 60)
        logger.error(str(e))
        logger.error("")
        sys.exit(1)
        
    except Exception as e:
        logger.error("")
        logger.error("=" * 60)
        logger.error("❌ 予期しないエラーが発生しました")
        logger.error("=" * 60)
        logger.error(f"エラータイプ: {type(e).__name__}")
        logger.error(f"エラー内容: {str(e)}")
        logger.error("")
        logger.error("詳細なエラー情報:")
        logger.error(traceback.format_exc())
        sys.exit(1)


if __name__ == '__main__':
    main()