---
applyTo: "**"
---

# Git Workflow Instructions

このガイドラインは、**Gitワークフローと技術的操作**に特化したAI向けインストラクションです。

## このファイルの役割と対象範囲

### 対象範囲

このインストラクションは以下の内容を含みます:

- **Git操作**: ブランチ作成、切り替え、マージ
- **ブランチ戦略**: ブランチタイプと命名規則
- **コミットメッセージ規約**: Conventional Commits準拠
- **ファイル操作**: Git履歴を保持する移動・削除方法
- **プルリクエスト**: 作成から承認までのプロセス
- **マージポリシー**: マージ戦略とコンフリクト解決

### 他ガイドラインとの関係

- **プロジェクト構造**: [ai-project-structure-core.instructions.md](./ai-project-structure-core.instructions.md)および関連ファイルで定義
- **コード品質**: [ai-code-writing.instructions.md](./ai-code-writing.instructions.md)で定義
- **ML/AI実験管理**: [git-workflow-ml-experiments.instructions.md](./git-workflow-ml-experiments.instructions.md)で定義
- **PR作成**: [ai-pull-request.instructions.md](./ai-pull-request.instructions.md)で定義
- **Git操作とプロジェクト構造は独立**: Gitワークフローはディレクトリ構造のルールとは別に管理

---

## ブランチ戦略

### ブランチタイプと命名規則

プロジェクトでは以下のブランチタイプを使用します。

#### 主要ブランチ

| ブランチ名 | 説明 | 保護設定 |
|-----------|------|----------|
| `main` | 本番環境のコード | 直接コミット禁止、PRのみ |
| `develop` | 開発統合ブランチ（使用する場合） | PRのみ |

#### 作業ブランチ

作業ブランチは以下の命名規則に従います:

```
<type>/<description>
```

**`<type>`の種類**:

| タイプ | 用途 | 例 |
|--------|------|-----|
| `feature` | 新機能の開発 | `feature/add-user-authentication` |
| `bugfix` | バグ修正 | `bugfix/correct-data-validation` |
| `experiment` | 実験的な実装・検証 | `experiment/test-new-algorithm` |
| `model` | モデル開発・改善 | `model/implement-lightgbm` |
| `train` | 訓練パイプライン | `train/add-cross-validation` |
| `eval` | 評価・検証 | `eval/add-metrics-calculation` |
| `data` | データ処理・前処理 | `data/add-feature-engineering` |
| `refactor` | リファクタリング | `refactor/optimize-query-performance` |
| `docs` | ドキュメント更新 | `docs/update-api-specification` |
| `test` | テスト追加・修正 | `test/add-unit-tests-for-validator` |
| `chore` | ビルド・ツール設定 | `chore/update-dependencies` |
| `config` | 設定ファイル変更 | `config/update-app-settings` |
| `perf` | パフォーマンス改善 | `perf/optimize-model-inference` |
| `style` | コードスタイル修正 | `style/format-python-files` |
| `infra` | インフラ整備 | `infra/setup-docker-environment` |

**`<description>`のルール**:

- 小文字のケバブケース（kebab-case）を使用
- 英語で簡潔に記述（3-5単語が目安）
- 変更の目的が明確にわかること
- 例: `add-defect-detection`, `correct-csv-parser`, `update-model-config`

#### 設定ファイル変更ブランチの詳細

`config/` タイプのブランチは、以下のような設定ファイル変更に使用します:

**開発環境設定**:
- `.vscode/settings.json`, `pyrightconfig.json`, `.editorconfig`
- 例: `config/update-vscode-settings`

**GitHub関連設定**:
- `.github/workflows/`, `.github/instructions/`
- 例: `config/add-ci-workflow`

**プロジェクト設定**:
- `pyproject.toml`, `pom.xml`, `requirements.txt`
- 例: `config/add-python-dependencies`

**アプリケーション設定**:
- `config/app_settings.json`, `config/schema.json`
- 例: `config/update-inference-threshold`

**ML/AI特有の設定**:
- モデル設定、ハイパーパラメータ設定
- 例: `config/tune-model-hyperparameters`

**品質管理・テスト設定**:
- `.pylintrc`, `pytest.ini`, `ruff.toml`
- 例: `config/update-linter-rules`

### ブランチ作成のワークフロー

#### 1. 最新の状態を取得

```bash
# mainブランチの最新状態を取得
git switch main
git pull origin main
```

#### 2. 作業ブランチを作成

```bash
# 新しいブランチを作成して切り替え
git switch -c feature/your-feature-name

# または、特定のコミットから作成
git switch -c bugfix/bug-fix-name <commit-hash>
```

#### 3. 変更を論理的な単位でコミット

```bash
# ファイルをステージング
git add <files>

# コミット（後述のコミットメッセージ規約に従う）
git commit -m "feat: add new feature description"
```

#### 4. リモートにプッシュ

```bash
# 初回プッシュ（upstream設定）
git push -u origin feature/your-feature-name

# 2回目以降
git push
```

---

## Gitブランチ操作

### ブランチ切り替え操作

#### `git switch` と `git checkout` の使い分け

**原則**: `git switch` を優先的に使用してください。

| コマンド | 用途 | 推奨度 |
|---------|------|--------|
| `git switch` | ブランチ切り替え専用 | ⭐⭐⭐ 推奨 |
| `git checkout` | ブランチ切り替え + ファイル復元（多機能） | △ 特定用途のみ |

**`git switch` の使用例**:

```bash
# 既存ブランチに切り替え
git switch main
git switch feature/my-feature

# 新しいブランチを作成して切り替え
git switch -c feature/new-feature

# リモートブランチをトラッキングして切り替え
git switch -c feature/remote-feature origin/feature/remote-feature

# 直前のブランチに戻る
git switch -
```

**`git checkout` を使う場合（特定用途のみ）**:

```bash
# ファイルを最新のコミット状態に復元
git checkout -- <file>

# 特定のコミットのファイルを取得
git checkout <commit-hash> -- <file>

# 注意: ブランチ切り替えには git switch を使用
```

**なぜ `git switch` を推奨するか**:

- ブランチ操作とファイル復元が明確に分離
- より直感的で誤操作が少ない
- Git 2.23以降の新しい標準コマンド

### Git技術的なファイル操作

ファイルの移動、削除、名前変更を行う際は、**Gitの履歴追跡を維持する**ために、以下のコマンドを必ず使用してください。

**重要**: プロジェクト構造の保護やディレクトリ作成に関するルールは [ai-project-structure-core.instructions.md](./ai-project-structure-core.instructions.md) を参照してください。

#### ファイル移動・名前変更

**必須**: `git mv` コマンドを使用

```bash
# ファイルを移動（履歴を保持）
git mv old/path/file.py new/path/file.py

# ファイル名を変更（履歴を保持）
git mv old_name.py new_name.py

# ディレクトリごと移動
git mv src/old_module/ src/new_module/
```

**理由**:
- Git履歴が保持され、ファイルの変更履歴を追跡可能
- `git log --follow` でリネーム後もファイル履歴を参照可能
- コードレビューで変更内容が明確になる

#### ファイル削除

**必須**: `git rm` コマンドを使用

```bash
# ファイルを削除
git rm file.py

# ディレクトリを削除
git rm -r directory/

# ファイルをGit管理から外すが実ファイルは残す
git rm --cached file.py
```

**理由**:
- 削除履歴がGitに記録される
- 削除理由をコミットメッセージで説明可能
- 必要に応じてファイルを復元可能

#### 推奨されない操作（禁止）

以下のコマンドは**使用しないでください**:

```bash
# ❌ 禁止: 通常のmvコマンド
mv old_file.py new_file.py
# 理由: Git履歴が途切れる

# ❌ 禁止: 通常のrmコマンド
rm file.py
# 理由: Git管理から削除されず不整合が発生

# ❌ 禁止: 直接ファイルシステム操作
mv src/utils/ src/utilities/
# 理由: Gitが変更を追跡できない
```

**例外**: `.gitignore` で除外されているファイル（一時ファイル、ビルド成果物など）は通常のコマンドで削除可能。

---

#### 大量ファイル操作時の注意

機械学習プロジェクトでは、大量のファイル移動・削除が発生することがあります。以下の点に注意してください。

- データファイルの整理時も`git mv` を使用
- 実験結果の移動時も`git mv` を使用し、履歴を保持
- 大量のファイルを一度に操作する場合、コミットを小分けにして履歴を明確に
- スクリプトで自動化する場合も、Gitコマンドを使用

---

## コミットメッセージの形式

### コミットメッセージ規約

このプロジェクトでは **Conventional Commits** 準拠のコミットメッセージを使用します。

#### 基本形式

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

#### `<type>` の種類

| タイプ | 説明 | 例 |
|--------|------|-----|
| `feat` | 新機能の追加 | `feat: add defect detection model` |
| `fix` | バグ修正 | `fix: correct CSV parsing error` |
| `model` | モデル開発・改善 | `model: implement lightgbm classifier` |
| `train` | 訓練パイプライン変更 | `train: add cross-validation` |
| `eval` | 評価・検証の追加・変更 | `eval: add confusion matrix calculation` |
| `data` | データ処理・前処理の変更 | `data: add feature scaling` |
| `docs` | ドキュメントのみの変更 | `docs: update API specification` |
| `style` | コードの動作に影響しないスタイル変更 | `style: format Python files with black` |
| `refactor` | バグ修正や機能追加を伴わないリファクタリング | `refactor: extract validation logic` |
| `perf` | パフォーマンス改善 | `perf: optimize model inference speed` |
| `test` | テストの追加・修正 | `test: add unit tests for DataValidator` |
| `build` | ビルドシステム・外部依存関係の変更 | `build: upgrade lightgbm to 4.0.0` |
| `ci` | CI設定ファイル・スクリプトの変更 | `ci: add GitHub Actions workflow` |
| `chore` | その他の変更（ビルドプロセス、補助ツールなど） | `chore: update .gitignore` |
| `revert` | 以前のコミットを取り消し | `revert: revert commit abc123` |

#### `<scope>` の指定（オプション）

スコープは変更の影響範囲を示します:

```
feat(trainer): add cross-validation support
fix(validator): handle missing column error
docs(api): update inference endpoint documentation
```

**スコープ例**:
- `trainer`: モデル訓練関連
- `validator`: データ検証関連
- `inference`: 推論関連
- `api`: API関連
- `config`: 設定ファイル
- `ui`: UIコンポーネント

#### `<description>` のルール

- **英語で記述**（プロジェクト標準）
- **小文字で開始**
- **命令形を使用**（"add" not "added", "fix" not "fixed"）
- **ピリオドで終わらない**
- **50文字以内を目標**
- **変更内容を簡潔に**

**良い例**:
```
feat: add ONNX model export functionality
fix: prevent null pointer in data loader
docs: clarify model training parameters
```

**悪い例**:
```
feat: Added new feature.  # 過去形、ピリオド付き
fix: Fix bug  # 曖昧
docs: update docs  # 不明瞭
```

#### `<body>` の記述（オプション）

複雑な変更の場合、本文で詳細を説明します:

```
feat: add automated model retraining pipeline

This commit introduces a scheduled retraining pipeline that:
- Monitors model performance metrics
- Triggers retraining when accuracy drops below threshold
- Automatically deploys improved models

Implementation uses APScheduler for scheduling and
includes comprehensive logging.
```

**本文のルール**:
- 概要行から1行空ける
- 72文字で折り返す
- 何を変更したかだけでなく、**なぜ**変更したかを説明
- 複数の段落に分けて説明可能

#### `<footer>` の記述（オプション）

重大な変更やIssue参照を記載:

```
feat: migrate to new configuration format

BREAKING CHANGE: Configuration file format has changed from JSON to YAML.
Users must migrate existing config/app_settings.json to config/app_settings.yaml.

Closes #123
Refs #456
```

**footerの用途**:
- `BREAKING CHANGE:` - 破壊的変更の説明
- `Closes #123` - Issueのクローズ
- `Refs #456` - 関連Issueの参照
- `Co-authored-by:` - 共同作成者の記載

### 改行とエスケープ規約

ターミナルでコミットメッセージを作成する際の注意点:

#### シングルラインメッセージ

```bash
# 基本形式
git commit -m "feat: add new feature"

# スコープ付き
git commit -m "fix(validator): handle empty dataframe"
```

#### マルチラインメッセージ

**方法1**: エディタを使用（推奨）

```bash
# デフォルトエディタが開く
git commit

# 特定のエディタを使用
git commit -e
```

**方法2**: Here-Doc を使用

```bash
git commit -F - <<`MSG`
feat: add model retraining pipeline

This commit introduces automated retraining:
- Monitors performance metrics
- Triggers retraining on degradation

Closes #123
```

**方法3**: 改行を含む文字列（bashの場合）

```bash
git commit -m "feat: add model retraining pipeline

This commit introduces automated retraining:
- Monitors performance metrics
- Triggers retraining on degradation

Closes #123"
```

### コミットメッセージの例

#### 新機能追加

```
feat: add real-time anomaly detection

Implement sliding window approach for real-time detection:
- Use z-score method with configurable threshold
- Buffer last N data points for baseline calculation
- Emit alerts when anomaly is detected

Performance: Processes 1000 data points in <100ms
```

#### バグ修正

```
fix: prevent division by zero in defect rate calculation

Add validation to check if total_count is zero before calculation.
Returns 0.0 for defect rate when no products are counted.

Closes #234
```

#### ドキュメント更新

```
docs: add training pipeline setup guide

Include step-by-step instructions for:
- Environment setup
- Data preparation
- Model training execution
- ONNX export process
```

#### リファクタリング

```
refactor: extract data validation into separate class

Move validation logic from DataLoader to new DataValidator class:
- Improves separation of concerns
- Enables unit testing of validation independently
- Makes validation reusable across components
```

#### 破壊的変更

```
feat!: change API response format to JSON

BREAKING CHANGE: Inference API now returns JSON instead of CSV.

Before:
  timestamp,value,prediction
  2025-12-06T10:00:00,42.5,0

After:
  {"timestamp": "2025-12-06T10:00:00", "value": 42.5, "prediction": 0}

Clients must update their parsers to handle JSON format.

Refs #567
```

---

## Git管理対象

### バージョン管理すべきファイル

以下のファイルは必ず Git で管理します:

**ソースコード**:
- `*.py`, `*.java`, `*.js`, `*.ts` など
- すべてのアプリケーションコード

**設定ファイル**:
- `config/*.json` - アプリケーション設定
- `pyproject.toml`, `pom.xml` - プロジェクト設定
- `requirements.txt`, `setup.py` - Python依存関係
- `.github/workflows/*.yml` - CI/CD設定

**ドキュメント**:
- `README.md`, `docs/*.md` - すべてのドキュメント
- `docs/api_specification.md` - API仕様
- `docs/design_spec.md` - 設計仕様

**テストコード**:
- `*Test.java`, `test_*.py` - ユニットテスト
- `tests/` - テストディレクトリ

**インストラクションファイル**:
- `.github/instructions/*.instructions.md` - AI向けガイドライン

### Git管理対象外（`.gitignore`で除外）

以下のファイルは Git で管理しません:

**ビルド成果物**:
- `*.class`, `*.pyc`, `__pycache__/`
- `target/` (Java)
- `dist/`, `build/`

**機械学習モデル**:
- `models/current/*.onnx` - 学習済みモデル（大容量）
- `models/archive/*.onnx` - アーカイブモデル
- **理由**: サイズが大きく、Git LFSや専用ストレージで管理すべき

**実験結果・ログ**:
- `experiments/*/` - 実験ログ
- `mlruns/` - MLflow実験データ
- `*.log` - ログファイル

**データファイル**:
- `data/input/*.csv` - 入力データ
- `data/output/*.csv` - 出力データ
- **理由**: データは外部ストレージで管理し、READMEで取得方法を記載

**IDE・エディタ設定（個人設定）**:
- `.vscode/` (チーム共有する場合は例外)
- `.idea/`
- `*.swp`, `*.swo`

**OS生成ファイル**:
- `.DS_Store` (macOS)
- `Thumbs.db` (Windows)

**環境変数・機密情報**:
- `.env` - 環境変数ファイル
- `*.key`, `*.pem` - 秘密鍵
- **重要**: 絶対にコミットしない

---

## データとプライバシー

### 機密情報の取り扱い

**絶対にコミットしてはいけないもの**:

1. **認証情報**:
   - APIキー、トークン
   - データベースパスワード
   - 秘密鍵、証明書

2. **個人情報**:
   - 顧客データ
   - 従業員情報
   - 生産データ（実データ）

3. **機密設定**:
   - 本番環境の接続情報
   - 内部IPアドレス、ポート番号

4. **データファイル（機密性・サイズの観点）**:
   - **実データ**: 本番環境から取得した実際の生産データ
   - **サンプルデータ**: 実データから抽出したサンプル（個人情報含む場合）
   - **アノテーションデータ**: 手動でラベル付けした大容量データ
   - **理由**: プライバシー保護、リポジトリサイズ肥大化防止
   - **代替策**: データストレージ（S3、GCS等）で管理し、READMEにアクセス方法を記載

### 機密情報の管理方法

**環境変数を使用**:

```python
# ❌ 禁止: ハードコード
api_key = "sk-1234567890abcdef"

# ✅ 推奨: 環境変数
import os
api_key = os.environ.get("API_KEY")
```

**設定ファイルのテンプレート化**:

```bash
# テンプレートをバージョン管理
config/app_settings.template.json

# 実際の設定は.gitignoreで除外
config/app_settings.local.json
```

**万が一コミットしてしまった場合**:

```bash
# 1. 即座にキーを無効化
# 2. 履歴から削除（慎重に実行）
git filter-branch --force --index-filter \
  "git rm --cached --ignore-unmatch path/to/secret/file" \
  --prune-empty --tag-name-filter cat -- --all

# 3. チームに通知
```

---

## プルリクエスト（PR）

### PRの作成プロセス

#### 1. 作業完了の確認

```bash
# すべての変更がコミット済みか確認
git status

# テストが通過するか確認
pytest  # Python
mvn test  # Java

# Lintエラーがないか確認
ruff check .  # Python
```

#### 2. 最新のmainにリベース（推奨）

```bash
# mainの最新を取得
git switch main
git pull origin main

# 作業ブランチに戻る
git switch feature/your-feature

# リベース
git rebase main

# コンフリクトがある場合は解決
# ... (コンフリクト解決)
git rebase --continue

# リモートに強制プッシュ（既にプッシュ済みの場合）
git push --force-with-lease
```

#### 3. PRの作成

GitHub上でPRを作成し、以下を記載:

**PRタイトル**:
- コミットメッセージと同じ形式
- 例: `feat: add automated model retraining pipeline`

**PR説明テンプレート**:

```markdown
## 変更内容
<!-- 何を変更したか簡潔に -->

## 変更理由
<!-- なぜこの変更が必要か -->

## 影響範囲
<!-- どの部分に影響するか -->

## テスト
<!-- どのようにテストしたか -->
- [ ] ユニットテスト追加
- [ ] 手動テスト実施
- [ ] 既存テストすべて通過

## チェックリスト
- [ ] コードはスタイルガイドに準拠
- [ ] ドキュメントを更新
- [ ] 破壊的変更がある場合、READMEに記載
- [ ] コミットメッセージはConventional Commits準拠

## 関連Issue
Closes #123
Refs #456
```

### PRレビューのガイドライン

ML/AIプロジェクトでは、以下の4つの観点からレビューを実施します。

#### 1. 技術レビュー

**チェックポイント**:
- **機能性**: 意図した動作をするか
- **コード品質**: 読みやすく保守可能か
- **テスト**: 適切なテストがあるか
- **パフォーマンス**: 性能上の問題がないか
- **セキュリティ**: 脆弱性がないか
- **再現性**: 乱数シードが固定されているか

#### 2. 実験レビュー（ML/AI特有）

ML/AI実験のレビューについては、[git-workflow-ml-experiments.instructions.md](./git-workflow-ml-experiments.instructions.md)を参照してください。

**主なチェックポイント**:
- **実験設計**: 実験の目的と仮説が明確か
- **評価指標**: 適切な評価指標が選択されているか
- **ベースライン比較**: 既存モデルとの比較があるか
- **結果の妥当性**: 結果が統計的に有意か
- **過学習チェック**: train/validationの性能差は適切か
- **実験記録**: 実験条件と結果が記録されているか

#### 3. 設定レビュー

**チェックポイント**:
- **ハイパーパラメータ**: 設定値が妥当か、根拠があるか
- **依存関係**: requirements.txtやpyproject.tomlが更新されているか
- **環境変数**: 機密情報がハードコードされていないか
- **設定ファイル**: config/配下のファイルが適切に更新されているか
- **バージョン互換性**: ライブラリバージョンの変更が他に影響しないか

#### 4. ドキュメントレビュー（完整性）

**チェックポイント**:
- **README更新**: 機能追加・変更がREADMEに反映されているか
- **API仕様**: 新しいAPIのドキュメントがあるか
- **インラインコメント**: 複雑なロジックに説明があるか
- **Docstring**: すべての公開関数・クラスにdocstringがあるか
- **変更履歴**: 破壊的変更がCHANGELOGに記載されているか
- **実験ノート**: 実験結果と考察が記録されているか

**レビューコメントの例**:

```markdown
# 承認
LGTM! (Looks Good To Me)

# 軽微な指摘
nit: 変数名を `data` から `input_data` に変更すると意図が明確になります

# 重要な指摘
⚠️ この変更はnullチェックが不足しており、NullPointerExceptionが発生する可能性があります。
```

### マージ方法

#### マージ戦略の選択

| 戦略 | 説明 | 使用ケース |
|------|------|-----------|
| **Merge commit** | マージコミットを作成 | 複数コミットの履歴を保持したい場合 |
| **Squash and merge** | すべてのコミットを1つに圧縮 | PRのコミット履歴を整理したい場合（推奨） |
| **Rebase and merge** | リベースしてマージ | 線形な履歴を維持したい場合 |

**このプロジェクトでは**: **Squash and merge** を推奨

#### マージ前の最終チェック

```bash
# CIが通過しているか確認
# GitHub Actions のステータスを確認

# コンフリクトがないか確認
# GitHub PR画面で確認

# 少なくとも1人のレビュアーの承認を得る
```

---

## コンフリクト解決

### コンフリクトが発生する状況

```bash
# mainの最新を取得
git switch main
git pull origin main

# 作業ブランチをリベース
git switch feature/your-feature
git rebase main
# または
git merge main

# コンフリクトが発生
# Auto-merging file.py
# CONFLICT (content): Merge conflict in file.py
```

### コンフリクト解決手順

#### 1. コンフリクトの確認

```bash
# コンフリクトしているファイルを確認
git status

# コンフリクト箇所を確認
cat file.py
```

#### 2. コンフリクトマーカーの理解

```python
<<<<<<< HEAD (または現在のブランチ)
# あなたの変更
def calculate_rate(total, defect):
    return (defect / total) * 100
=======
# mainブランチの変更
def calculate_rate(total_count, defect_count):
    if total_count == 0:
        return 0.0
    return (defect_count / total_count) * 100
>>>>>>> main (またはマージ元のブランチ)
```

#### 3. コンフリクトの解決

```python
# マーカーを削除し、適切なコードを残す
# 両方の良い部分を統合
def calculate_rate(total_count, defect_count):
    """Calculate defect rate as percentage."""
    if total_count == 0:
        return 0.0
    return (defect_count / total_count) * 100
```

#### 4. 解決をマーク

```bash
# ファイルをステージング
git add file.py

# すべてのコンフリクトが解決されたら
git rebase --continue
# または（mergeの場合）
git commit
```

### 複雑なコンフリクトの対処

**取り消してやり直す**:

```bash
# リベース中止
git rebase --abort

# マージ中止
git merge --abort
```

**特定の変更を優先**:

```bash
# 自分の変更を優先（ours）
git checkout --ours file.py

# 相手の変更を優先（theirs）
git checkout --theirs file.py

# ステージング
git add file.py
```

---

## トラブルシューティング

### よくある問題と解決方法

#### 問題1: 間違ったブランチでコミットしてしまった

```bash
# コミットを取り消し（変更は保持）
git reset --soft HEAD~1

# 正しいブランチに切り替え
git switch correct-branch

# 再度コミット
git commit -m "feat: correct branch commit"
```

#### 問題2: プッシュ後にコミットメッセージを修正したい

```bash
# 最新コミットのメッセージを修正
git commit --amend -m "feat: corrected commit message"

# 強制プッシュ（注意: 共有ブランチでは避ける）
git push --force-with-lease
```

#### 問題3: 大きなファイルを誤ってコミットした

```bash
# 最新コミットから削除
git rm --cached large_file.dat
git commit --amend --no-edit

# .gitignoreに追加
echo "large_file.dat" >> .gitignore
git add .gitignore
git commit -m "chore: add large file to gitignore"
```

#### 問題4: リベース中に混乱した

```bash
# リベースを中止して最初からやり直す
git rebase --abort

# または、現在の状態を確認
git status
git log --oneline --graph --all
```

---

## コードレビューのベストプラクティス

### レビュアー向け

**効果的なレビューコメント**:

```markdown
# 具体的な提案
💡 `for` ループの代わりにリスト内包表記を使うと可読性が向上します:
```python
result = [transform(x) for x in data if x > 0]
```

# 質問形式
❓ この関数が空のリストを受け取った場合の動作は意図通りですか?

# 称賛
👍 エラーハンドリングが適切で、わかりやすいエラーメッセージになっています!
```

**レビューの優先順位**:

1. **Critical**: セキュリティ、バグ、データ損失の可能性
2. **Important**: パフォーマンス、保守性、設計
3. **Nice to have**: スタイル、命名、コメント

### PR作成者向け

**レビューを受けやすくする工夫**:

1. **小さなPR**: 300行以下を目安
2. **明確な説明**: 背景と意図を記載
3. **セルフレビュー**: 提出前に自分で確認
4. **コメント追加**: 複雑な箇所に説明を追加

**レビューコメントへの対応**:

```markdown
# 対応完了
✅ 修正しました。`calculate_rate`関数にnullチェックを追加しました。

# 対応しない理由を説明
🤔 ご指摘ありがとうございます。今回はパフォーマンス上の理由で現在の実装を維持しますが、
   将来的な最適化として Issue #789 を作成しました。

# 追加の説明が必要
❓ この部分の意図がわからなかったので、もう少し詳しく教えていただけますか?
```

---

## まとめ

### 重要なポイントの再確認

1. **ブランチ命名**: `<type>/<description>` 形式を厳守
2. **コミットメッセージ**: Conventional Commits準拠
3. **ファイル操作**: `git mv`, `git rm` を使用（履歴保持）
4. **機密情報**: 絶対にコミットしない
5. **PR**: 小さく、明確に、テスト済み
6. **レビュー**: 建設的で具体的なコメント

### クイックリファレンス

```bash
# 新規ブランチ作成
git switch -c feature/new-feature

# コミット
git add .
git commit -m "feat: add new feature"

# プッシュ
git push -u origin feature/new-feature

# ファイル移動（履歴保持）
git mv old.py new.py

# 最新mainと同期
git switch main && git pull origin main
git switch - && git rebase main

# コンフリクト解決
git add <resolved-files>
git rebase --continue
```