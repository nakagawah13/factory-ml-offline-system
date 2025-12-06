# コード品質向上実装計画

## 概要

このドキュメントは、既存コードベースに型ヒント、Docstring、ファイル冒頭コメントを追加し、コード品質を向上させるための実装計画です。

**ブランチ**: `refactor/add-type-hints-and-docstrings`
**作成日**: 2025-12-07

---

## 目的

以下のガイドラインに準拠したコード品質の向上:
- [ai-code-writing.instructions.md](.github/instructions/ai-code-writing.instructions.md) - コード執筆ガイドライン
- [ai-code-examples-reference.instructions.md](.github/instructions/ai-code-examples-reference.instructions.md) - コード例集
- [ai-advanced-patterns.instructions.md](.github/instructions/ai-advanced-patterns.instructions.md) - 高度なパターン

### 主要な改善項目

1. **型ヒント (Type Hints)**: すべての関数・メソッドの引数と戻り値に型アノテーションを追加
2. **Google Style Docstring**: すべての公開クラス・関数にdocstringを追加
3. **ファイル冒頭コメント**: すべてのPythonファイルにモジュールdocstringを追加
4. **Javadoc**: すべてのJavaクラス・メソッドにJavadocを追加

---

## 対象ファイル分析

### Pythonファイル (13ファイル、322行)

| ファイル | 行数 | 優先度 | 現状分析 |
|---------|------|--------|---------|
| `main.py` | 6 | High | 型ヒント・docstring・モジュールdocstring すべて欠落 |
| `python-trainer/setup.py` | 23 | Low | セットアップスクリプト、最小限の対応 |
| `python-trainer/src/trainer/__init__.py` | 0 | Low | 空ファイル |
| `python-trainer/src/analysis/__init__.py` | 0 | Low | 空ファイル |
| **`python-trainer/src/trainer/data_loader.py`** | 28 | **High** | 型ヒント・docstring・モジュールdocstring すべて欠落 |
| **`python-trainer/src/trainer/model_trainer.py`** | 57 | **High** | 型ヒント・docstring・モジュールdocstring すべて欠落 |
| **`python-trainer/src/trainer/preprocessor.py`** | 27 | **High** | 型ヒント・docstring・モジュールdocstring すべて欠落 |
| `python-trainer/src/trainer/onnx_converter.py` | 22 | Medium | 型ヒント・docstring・モジュールdocstring すべて欠落 |
| `python-trainer/src/trainer/report_generator.py` | 40 | Medium | 型ヒント・docstring・モジュールdocstring すべて欠落 |
| `python-trainer/src/trainer/main.py` | 36 | Medium | 型ヒント・docstring・モジュールdocstring すべて欠落 |
| **`python-trainer/src/analysis/drift_detector.py`** | 27 | **High** | 型ヒント・docstring・モジュールdocstring すべて欠落 |
| `python-trainer/src/analysis/shap_analyzer.py` | 31 | Medium | 型ヒント・docstring・モジュールdocstring すべて欠落 |
| `python-trainer/src/analysis/metrics_calculator.py` | 25 | Medium | 型ヒント・docstring・モジュールdocstring すべて欠落 |

### Javaファイル (20ファイル、846行)

| ファイル | 行数 | 優先度 | 現状分析 |
|---------|------|--------|---------|
| `java-app/src/main/java/com/factory/ml/FactoryMLApp.java` | 21 | High | Javadoc・クラスコメント欠落 |
| **`java-app/src/main/java/com/factory/ml/service/DataValidator.java`** | 21 | **High** | Javadoc・クラスコメント欠落 |
| **`java-app/src/main/java/com/factory/ml/service/InferenceService.java`** | 53 | **High** | Javadoc・クラスコメント欠落 |
| **`java-app/src/main/java/com/factory/ml/service/ModelManagerService.java`** | 44 | **High** | Javadoc・クラスコメント欠落 |
| `java-app/src/main/java/com/factory/ml/service/FeatureTransformer.java` | 20 | Medium | Javadoc・クラスコメント欠落 |
| `java-app/src/main/java/com/factory/ml/service/SimulationService.java` | 24 | Medium | Javadoc・クラスコメント欠落 |
| `java-app/src/main/java/com/factory/ml/model/Schema.java` | 117 | High | Javadoc・クラスコメント欠落 |
| `java-app/src/main/java/com/factory/ml/model/InferenceResult.java` | 28 | Medium | Javadoc・クラスコメント欠落 |
| `java-app/src/main/java/com/factory/ml/model/InputRow.java` | 23 | Medium | Javadoc・クラスコメント欠落 |
| `java-app/src/main/java/com/factory/ml/model/ValidationError.java` | 31 | Medium | Javadoc・クラスコメント欠落 |
| **`java-app/src/main/java/com/factory/ml/util/ConfigLoader.java`** | 34 | **High** | Javadoc・クラスコメント欠落 |
| `java-app/src/main/java/com/factory/ml/util/DateParser.java` | 20 | Medium | Javadoc・クラスコメント欠落 |
| `java-app/src/main/java/com/factory/ml/util/ProcessExecutor.java` | 28 | Medium | Javadoc・クラスコメント欠落 |
| `java-app/src/main/java/com/factory/ml/controller/*` | 240 | Low | UIコントローラ、後回し可 |
| `java-app/src/test/java/com/factory/ml/*Test.java` | 142 | Low | テストコード、後回し可 |

---

## 実装フェーズ

### Phase 1: Python Core Modules (最優先)

コアビジネスロジックを含む重要なPythonモジュール

| Task ID | ファイル | 作業内容 | 見積時間 |
|---------|---------|---------|----------|
| T-001 | `main.py` | モジュールdocstring、型ヒント、関数docstring追加 | 15分 |
| T-002 | `python-trainer/src/trainer/data_loader.py` | モジュールdocstring、クラス・メソッドdocstring、型ヒント追加 | 45分 |
| T-003 | `python-trainer/src/trainer/model_trainer.py` | モジュールdocstring、クラス・メソッドdocstring、型ヒント追加 | 60分 |
| T-004 | `python-trainer/src/trainer/preprocessor.py` | モジュールdocstring、クラス・メソッドdocstring、型ヒント追加 | 45分 |
| T-005 | `python-trainer/src/analysis/drift_detector.py` | モジュールdocstring、クラス・メソッドdocstring、型ヒント追加 | 45分 |

**Phase 1 合計**: 約3.5時間

### Phase 2: Python Supporting Modules

補助的なPythonモジュール

| Task ID | ファイル | 作業内容 | 見積時間 |
|---------|---------|---------|----------|
| T-006 | `python-trainer/src/trainer/onnx_converter.py` | モジュールdocstring、関数docstring、型ヒント追加 | 30分 |
| T-007 | `python-trainer/src/trainer/report_generator.py` | モジュールdocstring、クラス・メソッドdocstring、型ヒント追加 | 45分 |
| T-008 | `python-trainer/src/trainer/main.py` | モジュールdocstring、関数docstring、型ヒント追加 | 45分 |
| T-009 | `python-trainer/src/analysis/shap_analyzer.py` | モジュールdocstring、クラス・メソッドdocstring、型ヒント追加 | 45分 |
| T-010 | `python-trainer/src/analysis/metrics_calculator.py` | モジュールdocstring、関数docstring、型ヒント追加 | 30分 |

**Phase 2 合計**: 約3時間

### Phase 3: Java Core Services (高優先度)

コアビジネスロジックを含む重要なJavaクラス

| Task ID | ファイル | 作業内容 | 見積時間 |
|---------|---------|---------|----------|
| T-011 | `java-app/.../service/DataValidator.java` | クラスJavadoc、メソッドJavadoc追加 | 30分 |
| T-012 | `java-app/.../service/InferenceService.java` | クラスJavadoc、メソッドJavadoc追加 | 60分 |
| T-013 | `java-app/.../service/ModelManagerService.java` | クラスJavadoc、メソッドJavadoc追加 | 45分 |
| T-014 | `java-app/.../util/ConfigLoader.java` | クラスJavadoc、メソッドJavadoc追加 | 30分 |
| T-015 | `java-app/.../model/Schema.java` | クラスJavadoc、フィールド・メソッドJavadoc追加 | 60分 |

**Phase 3 合計**: 約3.5時間

### Phase 4: Java Supporting Classes

補助的なJavaクラス

| Task ID | ファイル | 作業内容 | 見積時間 |
|---------|---------|---------|----------|
| T-016 | `java-app/.../FactoryMLApp.java` | クラスJavadoc、メソッドJavadoc追加 | 30分 |
| T-017 | `java-app/.../service/FeatureTransformer.java` | クラスJavadoc、メソッドJavadoc追加 | 30分 |
| T-018 | `java-app/.../service/SimulationService.java` | クラスJavadoc、メソッドJavadoc追加 | 30分 |
| T-019 | `java-app/.../model/InferenceResult.java` | クラスJavadoc、フィールド・メソッドJavadoc追加 | 30分 |
| T-020 | `java-app/.../model/InputRow.java` | クラスJavadoc、フィールド・メソッドJavadoc追加 | 30分 |
| T-021 | `java-app/.../model/ValidationError.java` | クラスJavadoc、フィールド・メソッドJavadoc追加 | 30分 |
| T-022 | `java-app/.../util/DateParser.java` | クラスJavadoc、メソッドJavadoc追加 | 30分 |
| T-023 | `java-app/.../util/ProcessExecutor.java` | クラスJavadoc、メソッドJavadoc追加 | 30分 |

**Phase 4 合計**: 約4時間

### Phase 5: Controllers and Tests (低優先度)

UIコントローラとテストコード（余裕があれば）

| Task ID | ファイル | 作業内容 | 見積時間 |
|---------|---------|---------|----------|
| T-024 | `java-app/.../controller/InferenceTabController.java` | クラスJavadoc、メソッドJavadoc追加 | 45分 |
| T-025 | `java-app/.../controller/TrainingTabController.java` | クラスJavadoc、メソッドJavadoc追加 | 60分 |
| T-026 | `java-app/.../controller/SimulationViewController.java` | クラスJavadoc、メソッドJavadoc追加 | 45分 |
| T-027 | `java-app/.../controller/AnalysisReportController.java` | クラスJavadoc、メソッドJavadoc追加 | 30分 |
| T-028 | テストコード全般 | Javadoc追加 | 120分 |

**Phase 5 合計**: 約5時間

---

## 総作業時間見積

| フェーズ | 時間 | 優先度 |
|---------|------|--------|
| Phase 1: Python Core Modules | 3.5時間 | 最優先 |
| Phase 2: Python Supporting Modules | 3時間 | 高 |
| Phase 3: Java Core Services | 3.5時間 | 高 |
| Phase 4: Java Supporting Classes | 4時間 | 中 |
| Phase 5: Controllers and Tests | 5時間 | 低 |
| **合計** | **19時間** | - |

---

## 実装ガイドライン遵守事項

### Python

#### 必須事項 (MUST)

1. **ファイル冒頭のモジュールdocstring**:
   - 1行目は英語でピリオド終わり (Ruff互換)
   - Main Components セクションで主要クラス・関数を列挙
   - Project Context セクションでプロジェクト内での役割を説明
   - Example セクションで使用例を記載

2. **Google Style Docstring**:
   - すべてのクラス・関数・メソッドに実装
   - 1行目は英語でピリオド終わり
   - Args, Returns, Raises セクションを完備
   - 必要に応じて Examples セクションを追加

3. **型ヒント**:
   - すべての引数と戻り値に型アノテーションを追加
   - `from typing import List, Dict, Optional, Any` などを適切に使用
   - コレクション型の内部型も明示 (`List[int]`, `Dict[str, Any]`)

4. **言語使用規則**:
   - コード要素（関数名、クラス名、変数名）: 英語
   - Docstring: 英語 + 必要に応じて日本語併記
   - ログメッセージ: 日本語
   - エラーメッセージ: 日本語

#### 推奨事項 (SHOULD)

- プライベート関数にも簡潔なdocstring
- 定数には説明コメント
- 複雑なロジックにはインラインコメント（日本語可）

### Java

#### 必須事項 (MUST)

1. **クラスレベルのJavadoc**:
   - クラスの目的と責任範囲を説明
   - 主要な公開メソッドの概要
   - プロジェクト内での役割
   - 使用例（必要に応じて）

2. **メソッドレベルのJavadoc**:
   - メソッドの目的を簡潔に説明
   - `@param` で各パラメータを説明
   - `@return` で戻り値を説明
   - `@throws` で発生する例外を説明

3. **フィールドのJavadoc**:
   - 公開フィールドとprotectedフィールドには必ず記載
   - privateフィールドも重要な場合は記載

---

## 品質チェック基準

各ファイル修正後、以下を確認:

### Python

- [ ] ファイル冒頭にモジュールdocstringがある
- [ ] Docstring 1行目が英語でピリオド終わり
- [ ] すべての公開クラス・関数にGoogle Style Docstring
- [ ] すべての引数・戻り値に型ヒント
- [ ] `ruff check` でエラーなし
- [ ] `mypy` で型エラーなし

### Java

- [ ] すべてのクラスにJavadocがある
- [ ] すべての公開メソッドにJavadocがある
- [ ] `@param`, `@return`, `@throws` が適切に記載
- [ ] `mvn compile` でコンパイルエラーなし

---

## 作業進捗トラッキング

| Task ID | 状態 | 担当 | 備考 |
|---------|------|------|------|
| T-001 | ✅ Done | AI | モジュールdocstring、型ヒント、関数docstring追加完了 |
| T-002 | ✅ Done | AI | モジュールdocstring、クラス・メソッドdocstring、型ヒント追加完了 |
| T-003 | ✅ Done | AI | モジュールdocstring、クラス・メソッドdocstring、型ヒント追加完了 |
| T-004 | ✅ Done | AI | モジュールdocstring、クラス・メソッドdocstring、型ヒント追加完了 |
| T-005 | ✅ Done | AI | モジュールdocstring、クラス・メソッドdocstring、型ヒント追加完了 |
| T-006 | ⚪ Not Started | - | - |
| T-007 | ⚪ Not Started | - | - |
| T-008 | ⚪ Not Started | - | - |
| T-009 | ⚪ Not Started | - | - |
| T-010 | ⚪ Not Started | - | - |
| T-011 | ⚪ Not Started | - | - |
| T-012 | ⚪ Not Started | - | - |
| T-013 | ⚪ Not Started | - | - |
| T-014 | ⚪ Not Started | - | - |
| T-015 | ⚪ Not Started | - | - |
| T-016 | ⚪ Not Started | - | - |
| T-017 | ⚪ Not Started | - | - |
| T-018 | ⚪ Not Started | - | - |
| T-019 | ⚪ Not Started | - | - |
| T-020 | ⚪ Not Started | - | - |
| T-021 | ⚪ Not Started | - | - |
| T-022 | ⚪ Not Started | - | - |
| T-023 | ⚪ Not Started | - | - |
| T-024 | ⚪ Not Started | - | - |
| T-025 | ⚪ Not Started | - | - |
| T-026 | ⚪ Not Started | - | - |
| T-027 | ⚪ Not Started | - | - |
| T-028 | ⚪ Not Started | - | - |

**状態凡例**:
- ⚪ Not Started（未着手）
- 🔵 In Progress（進行中）
- ✅ Done（完了）
- ⏸️ Blocked（ブロック中）
- ❌ Cancelled（キャンセル）

---

## PR戦略

### PR分割方針

コードレビューの効率化のため、以下のようにPRを分割:

1. **PR #1**: Phase 1 (Python Core Modules)
   - `main.py`, `data_loader.py`, `model_trainer.py`, `preprocessor.py`, `drift_detector.py`
   - レビュー規模: 約250行の変更

2. **PR #2**: Phase 2 (Python Supporting Modules)
   - `onnx_converter.py`, `report_generator.py`, `main.py`, `shap_analyzer.py`, `metrics_calculator.py`
   - レビュー規模: 約200行の変更

3. **PR #3**: Phase 3 (Java Core Services)
   - `DataValidator.java`, `InferenceService.java`, `ModelManagerService.java`, `ConfigLoader.java`, `Schema.java`
   - レビュー規模: 約300行の変更

4. **PR #4**: Phase 4 (Java Supporting Classes)
   - 残りのJavaクラス
   - レビュー規模: 約250行の変更

5. **PR #5**: Phase 5 (Controllers and Tests) - オプション
   - コントローラとテストコード
   - レビュー規模: 約400行の変更

### PR作成時の必須セクション

- 変更概要（What）
- 変更理由（Why）
- タスク対応（このドキュメントへの参照）
- 影響範囲（変更ファイルリスト）
- テスト / 検証（Lint・型チェック結果）
- リスク / 互換性（破壊的変更の有無）

---

## リスクと対策

### 想定されるリスク

1. **型ヒント追加による互換性問題**
   - 対策: 既存の動作を変更しない、型チェックツールで検証

2. **Docstring追加による可読性低下**
   - 対策: 簡潔で明確な説明を心がける、冗長な説明を避ける

3. **作業時間の超過**
   - 対策: Phase 1-3を優先、Phase 4-5は余裕があれば実施

4. **Lintエラーの大量発生**
   - 対策: 段階的に修正、`ruff check --fix` で自動修正可能なものは活用

### 対策の優先順位

1. Phase 1を完遂（最重要）
2. Phase 2を完遂（重要）
3. Phase 3を完遂（重要）
4. Phase 4は可能な範囲で
5. Phase 5は時間があれば

---

## 次のステップ

1. ✅ 作業ブランチ `refactor/add-type-hints-and-docstrings` を作成
2. ✅ 実装計画ドキュメント作成
3. ✅ Phase 1実装完了（T-001 ~ T-005）
4. ✅ Phase 1コミット（2コミット: コード修正、ドキュメント修正）
5. ⚪ Phase 2実装開始（T-006から順次）
6. ⚪ 各Phaseごとにコミット
7. ⚪ PR作成・レビュー依頼

---

## 参考資料

- [ai-code-writing.instructions.md](../.github/instructions/ai-code-writing.instructions.md)
- [ai-code-examples-reference.instructions.md](../.github/instructions/ai-code-examples-reference.instructions.md)
- [ai-advanced-patterns.instructions.md](../.github/instructions/ai-advanced-patterns.instructions.md)
- [Google Python Style Guide - Docstrings](https://google.github.io/styleguide/pyguide.html#38-comments-and-docstrings)
- [PEP 484 - Type Hints](https://www.python.org/dev/peps/pep-0484/)
