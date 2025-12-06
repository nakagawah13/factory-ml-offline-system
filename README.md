# Factory ML Offline System

## 概要
このプロジェクトは、工場内のオフライン環境で稼働する構造化データを用いた機械学習システムです。エンジニアは、管理用のGUIアプリケーションを通じて推論、シミュレーション、モデルの再学習、劣化診断を行います。

## アーキテクチャ
- **完全オフライン:** インターネット接続は不要です。
- **ハイブリッド構成:**
  - **GUI / 推論 / シミュレーション:** Java (ONNX Runtimeを使用)
  - **学習 / 分析 / ドリフト検知:** Python (バッチ処理)

## 技術スタック
- **Javaアプリケーション**
  - Java 17+
  - JavaFX
  - ONNX Runtime (Java API)
- **Pythonトレーナー**
  - Python 3.10+
  - scikit-learn, pandas, onnxruntime など

## ディレクトリ構成
- `java-app`: Javaアプリケーションのソースコードとリソース
- `python-trainer`: Pythonトレーニングツール
- `models`: モデルの管理
- `config`: 設定ファイル
- `data`: 入出力データ
- `docs`: ドキュメント

## 使用方法
1. Javaアプリケーションを起動し、GUIを通じてデータを読み込みます。
2. データのバリデーションを行い、推論を実行します。
3. 必要に応じてモデルの再学習を行い、結果を分析します。

## 開発環境

### 依存関係のインストール

**推奨方法 (uv使用)**:
```bash
uv sync
```

**フォールバック方法 (オフライン環境)**:
```bash
pip install -r python-trainer/requirements.txt
```

### ビルドとテスト

- **Java:** Mavenを使用してビルド
  ```bash
  cd java-app
  mvn clean install
  ```

- **Python:** uvを使用してトレーナーを実行
  ```bash
  uv run trainer --help
  ```

## ライセンス
このプロジェクトはMITライセンスの下で提供されています。