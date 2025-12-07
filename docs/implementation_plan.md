# 一時ディレクトリ削除実装計画

## 概要

このドキュメントは、空の一時ディレクトリ `docs/instructions_tmp/` を削除する計画です。

**Issue**: #16 - chore: remove empty temporary directory docs/instructions_tmp/
**ブランチ**: `chore/remove-empty-temporary-directory`
**作成日**: 2025-12-07
**優先度**: Low
**見積時間**: 5分

---

## 背景と問題

### 現状の問題

`docs/instructions_tmp/` という空のディレクトリが残存しており、プロジェクト構造を不明瞭にしています。

**現状**:
```bash
$ ls -la docs/instructions_tmp/
total 8
drwxr-xr-x 2 nakagawa6 nakagawa6 4096 Dec  7 00:59 .
drwxr-xr-x 3 nakagawa6 nakagawa6 4096 Dec  7 18:30 ..
```

完全に空のディレクトリです。

### プロジェクト構造保護ガイドラインとの関連

`.github/instructions/ai-project-structure-core.instructions.md` では以下のように禁止されています:

```
❌ 絶対禁止
temp/, tmp/, test_folder/, experiment/, sandbox/
```

`instructions_tmp/` は `_tmp` という名前から一時的な作業用ディレクトリと推測され、このガイドラインに違反しています。

---

## 目的

空の一時ディレクトリを削除し、プロジェクト構造を明確化:

1. **docs/instructions_tmp/**: 削除
2. **プロジェクト構造の一貫性**: ガイドライン準拠
3. **クリーンアップ**: 不要なディレクトリの除去

---

## 影響を受けるファイルとディレクトリ

### 削除対象

| パス | 現状 | 理由 |
|------|------|------|
| `docs/instructions_tmp/` | 空ディレクトリ | 一時ディレクトリとして不適切、ガイドライン違反 |

### 影響範囲

**影響なし**: このディレクトリは完全に空であり、Git追跡もされていないため、削除による影響はありません。

---

## 実装タスク

### Task 1: 一時ディレクトリの削除 (2分)

**目的**: 空の一時ディレクトリ `docs/instructions_tmp/` を削除

**実装コマンド**:
```bash
# ディレクトリ削除（Git追跡されていないため rm -rf を使用）
rm -rf docs/instructions_tmp/
```

**検証方法**:
- `ls -la docs/` で `instructions_tmp/` が存在しないことを確認

---

### Task 2: implementation_plan.md の更新 (2分)

**目的**: このドキュメントをIssue #16の内容に更新

**実装内容**:
- Issue #16に対応した内容に書き換え
- 背景、目的、タスクを適切に記載

**検証方法**:
- ドキュメントがIssue #16の内容と一致していることを確認

---

### Task 3: コミットとプッシュ (1分)

**目的**: 変更をコミットし、リモートブランチにプッシュ

**実装コマンド**:
```bash
# 変更をステージング（implementation_plan.mdの変更のみ）
git add docs/implementation_plan.md

# コミット
git commit -m "chore: remove empty temporary directory

- Remove docs/instructions_tmp/ (empty directory)
- Update implementation_plan.md for Issue #16
- Comply with project structure guidelines

Closes #16"

# プッシュ
git push -u origin chore/remove-empty-temporary-directory
```

**検証方法**:
- コミットが正常に作成される
- リモートブランチにプッシュされる

## タスク進捗トラッキング

| Task | 内容 | 見積 | 状態 | 備考 |
|------|------|------|------|------|
| T-001 | 一時ディレクトリの削除 | 2分 | ✅ Done | rm -rf で削除完了 |
| T-002 | implementation_plan.md更新 | 2分 | ✅ Done | Issue #16対応に書き換え |
| T-003 | コミットとプッシュ | 1分 | 🔵 In Progress | 次のステップ |

**状態凡例**:
- ⚪ Not Started（未着手）
- 🔵 In Progress（進行中）
- ✅ Done（完了）
- ⏸️ Blocked（ブロック中）
- ❌ Cancelled（キャンセル）

## 総作業時間見積

| フェーズ | 見積時間 |
|---------|---------|
| Task 1: 一時ディレクトリ削除 | 2分 |
| Task 2: ドキュメント更新 | 2分 |
| Task 3: コミット・プッシュ | 1分 |
| **合計** | **5分** |

## 次のステップ

実装計画に従い、以下の順序でタスクを実施:

1. ✅ Task 1: 一時ディレクトリの削除
2. ✅ Task 2: implementation_plan.md更新
3. 🔵 Task 3: コミットとプッシュ

## 参考資料

- [Issue #16](https://github.com/nakagawah13/factory-ml-offline-system/issues/16)
- [ai-project-structure-core.instructions.md](../.github/instructions/ai-project-structure-core.instructions.md)
- [git-workflow.instructions.md](../.github/instructions/git-workflow.instructions.md)
