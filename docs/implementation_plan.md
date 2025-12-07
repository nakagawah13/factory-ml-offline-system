# ディレクトリ構造修正実装計画

## 概要

このドキュメントは、models/とdata/配下の不正なファイル/ディレクトリ構造を修正する計画です。

**注意**: 本PR (#27) では models/ ディレクトリのみを対応します。data/ ディレクトリは既に正常な状態であるため、変更対象外です。

**Issue**: #15 - models/とdata/のディレクトリ構造修正
**ブランチ**: `fix/directory-structure-models-data`
**作成日**: 2025-12-07
**優先度**: High
**見積時間**: 30分

---

## 背景と問題

### 現状の問題

`models/archive`と`models/current`がテキストファイルとして存在しており、本来はディレクトリであるべき構造が破綻しています。

**現状**:
```bash
$ file models/archive models/current
models/archive: UTF-8 Unicode text
models/current: UTF-8 Unicode text
```

これにより以下の問題が発生:

1. **モデル保存不可**: 訓練パイプラインがモデルを保存できない
2. **Java統合テスト不可**: InferenceServiceがモデルをロードできない
3. **すべての実装をブロック**: Issue #10, #12, #24等が影響を受ける

**data/配下の状況**:
- `data/input`: 既にディレクトリとして正常（このPRでは変更なし）
- `data/output`: 既にディレクトリとして正常（このPRでは変更なし）

### システム全体への影響

- **訓練パイプライン**: `ModelTrainer.run()`がモデルを保存できない
- **Java統合テスト**: `InferenceService`がONNXモデルをロードできない
- **Issue #12, #24**: 統合テストが実施不可能

---

## 目的

ディレクトリ構造を正常化し、以下を実現:

1. **models/archive**: ディレクトリとして作成、`.gitkeep`で空ディレクトリを管理
2. **models/current**: ディレクトリとして作成、`.gitkeep`で空ディレクトリを管理
3. **Git履歴保持**: `git rm` + `git add`で正しく管理
4. **構造検証**: 変更後にディレクトリが正しく作成されているか確認

---

## 影響を受けるファイルとディレクトリ

### 修正対象

| パス | 現状 | 修正後 |
|------|------|--------|
| `models/archive` | ファイル（UTF-8テキスト） | ディレクトリ + `.gitkeep` |
| `models/current` | ファイル（UTF-8テキスト） | ディレクトリ + `.gitkeep` |

**このPRのスコープ外（変更なし）**:

| パス | 現状 | 備考 |
|------|------|------|
| `data/input` | ✅ ディレクトリ（正常） | Issue #15の計画に含まれるが、既に正常なため修正不要 |
| `data/output` | ✅ ディレクトリ（正常） | Issue #15の計画に含まれるが、既に正常なため修正不要 |

### 既存ファイルの内容確認

修正前に既存ファイルの内容を記録:

**models/archive** (211 bytes):
```
# Archive Directory

This directory contains archived machine learning models.
Models are automatically moved here when a new model is deployed.

Naming convention: model_YYYYMMDD_HHMMSS.onnx
```

**models/current** (340 bytes):
```
# Current Model Directory

This directory contains the currently active machine learning model.
The Java application loads the model from this location.

Expected files:
- model.onnx: Current ONNX model
- model.joblib: Scikit-learn pipeline (for Python inference)
- metadata.json: Model version and training information
```

→ これらの説明を`models/README.md`に統合済み

---

## 実装タスク

### Task 1: 既存ファイルの削除 (5分)

**目的**: テキストファイルとして存在する`models/archive`と`models/current`を削除

**実装コマンド**:
```bash
git rm models/archive models/current
```

**検証方法**:
- `git status`で削除がステージングされていることを確認
- ファイルが物理的に削除されていることを確認

---

### Task 2: ディレクトリの作成と.gitkeep追加 (5分)

**目的**: `models/archive`と`models/current`をディレクトリとして作成し、空ディレクトリ管理用の`.gitkeep`を追加

**実装コマンド**:
```bash
# ディレクトリ作成
mkdir -p models/archive models/current

# .gitkeep作成
touch models/archive/.gitkeep
touch models/current/.gitkeep

# Gitに追加
git add models/archive/.gitkeep models/current/.gitkeep
```

**検証方法**:
- `models/archive`がディレクトリとして存在
- `models/current`がディレクトリとして存在
- 各ディレクトリ内に`.gitkeep`が存在
- `git status`で追加がステージングされていることを確認

---

### Task 3: 構造検証とドキュメント確認 (5分)

**目的**: 変更が正しく適用されているか確認し、models/README.mdの作成を検討

**実装内容**:

1. 構造確認:
   ```bash
   # ディレクトリ構造の確認
   file models/archive models/current
   
   # ツリー表示
   tree models/
   ```

2. (オプション) models/README.md作成:
   - 削除した2つのファイルの内容を統合
   - archive/とcurrent/の役割を説明
   - 命名規則とバージョニングポリシーを記載

**検証方法**:
- `file models/archive`が`directory`を返す
- `file models/current`が`directory`を返す
- `ls -la models/archive/`で`.gitkeep`のみが存在
- `ls -la models/current/`で`.gitkeep`のみが存在

---

### Task 4: コミットとプッシュ (5分)

**目的**: 変更をコミットし、リモートブランチにプッシュ

**実装コマンド**:
```bash
# コミット
git commit -m "fix: convert models/archive and models/current from files to directories

- Remove text files that were incorrectly used as directories
- Create proper directory structure with .gitkeep files
- This fixes model save/load functionality in training pipeline
- This unblocks Java-Python integration testing (Issue #12)

Closes #15"

# プッシュ
git push -u origin fix/directory-structure-models-data
```

**検証方法**:
- コミットが正常に作成される
- リモートブランチにプッシュされる
- GitHub上でPR作成準備が整う

### Task 5: PR作成 (10分)

**目的**: GitHubでPRを作成し、レビュー依頼

**実装コマンド**:
```bash
gh pr create \
  --title "fix: convert models/archive and models/current to directories" \
  --body-file .github/pr_body_template.md \
  --base main
```

**PR本文のポイント**:
- 変更概要: ファイル→ディレクトリへの変換
- 変更理由: モデル保存機能の有効化
- 影響範囲: models/配下のみ、data/は変更なし
- テスト: 構造検証完了
- リスク: なし（既存機能への影響なし）

**検証方法**:
- PRが正常に作成される
- Issue #15が自動でリンクされる
- CI/CDが正常に実行される

## タスク進捗トラッキング

| Task | 内容 | 見積 | 状態 | 備考 |
|------|------|------|------|------|
| T-001 | 既存ファイルの削除 | 5分 | ✅ Done | git rm で削除完了 |
| T-002 | ディレクトリ作成と.gitkeep追加 | 5分 | ✅ Done | mkdir, touch, git add で完了 |
| T-003 | 構造検証 | 5分 | ✅ Done | file, ls -la で確認完了 |
| T-004 | コミットとプッシュ | 5分 | ✅ Done | 2コミット、強制プッシュ完了 |
| T-005 | PR作成 | 10分 | ✅ Done | PR #27 作成・本文更新完了 |

**状態凡例**:
- ⚪ Not Started（未着手）
- 🔵 In Progress（進行中）
- ✅ Done（完了）
- ⏸️ Blocked（ブロック中）
- ❌ Cancelled（キャンセル）

## 総作業時間見積

| フェーズ | 見積時間 |
|---------|---------|
| Task 1: 既存ファイル削除 | 5分 |
| Task 2: ディレクトリ作成 | 5分 |
| Task 3: 構造検証 | 5分 |
| Task 4: コミット・プッシュ | 5分 |
| Task 5: PR作成 | 10分 |
| **合計** | **30分** |

## 次のステップ

実装計画に従い、以下の順序でタスクを実施（すべて完了済み）:

1. ✅ Task 1: 既存ファイルの削除
2. ✅ Task 2: ディレクトリ作成と.gitkeep追加
3. ✅ Task 3: 構造検証
4. ✅ Task 4: コミットとプッシュ
5. ✅ Task 5: PR作成

## 参考資料

- [Issue #15](https://github.com/nakagawah13/factory-ml-offline-system/issues/15)
- [ai-project-structure-core.instructions.md](../.github/instructions/ai-project-structure-core.instructions.md)
- [git-workflow.instructions.md](../.github/instructions/git-workflow.instructions.md)
