# Javaアプリケーションコンパイルエラー修正実装計画

## 概要
このドキュメントは、Javaアプリケーションのコンパイルエラーを修正する実装計画です。

**Issue**: #10 - Javaアプリケーションのコンパイルエラー修正
**ブランチ**: `fix/java-compilation-errors`
**作成日**: 2025-12-07
**優先度**: Critical (blocker)

---

## 背景と問題

### 現状の問題

Phase 3 (Javadoc追加) 実施後のMaven型チェックで、複数のコンパイルエラーが検出されました。これらはすべて**実装の不完全性**に起因しており、Javadoc自体には構文エラーは0件です。

**主要なエラーカテゴリ**:

#### 1. ONNX Runtime API互換性問題
- `OrtInputs`, `OrtOutputs` クラスが存在しない（ONNX Runtime 1.15.0でAPI変更）
- 型の不一致: `float[]` と `double[]` の不整合

#### 2. モデルクラスのメソッド不足
- `InputRow.java`: コピーコンストラクタ、`setValue()`, `getFloatInput()`, `getStringInput()` メソッドが未実装

#### 3. サービスクラスの引数不一致
- `InferenceService`: コンストラクタ引数が必要だが、引数なしで呼び出している箇所がある
- `ModelManagerService`: 同様の問題

#### 4. JavaFX依存関係の欠落
- `AnalysisReportController.java`: `javafx.scene.web.WebView` パッケージが見つからない

**現在のONNX Runtime依存関係**:
```xml
<dependency>
    <groupId>com.microsoft.onnxruntime</groupId>
    <artifactId>onnxruntime</artifactId>
    <version>1.15.0</version>
</dependency>
```

### 影響範囲

- Javaアプリケーション全体がコンパイル不可
- アプリケーション起動不可
- Pythonトレーナーとの統合テスト実施不可

---

## 実装方針

### 優先順位付け

1. **Phase 1 (Critical)**: 依存関係とモデルクラスの修正
2. **Phase 2 (High)**: ONNX Runtime API対応とサービスクラス修正
3. **Phase 3 (Medium)**: Controllerクラスの修正
4. **Phase 4 (High)**: 統合テストと動作確認

### 技術方針

- **ONNX Runtime API**: 1.15.0の正式APIを使用（`OrtSession.run()` メソッドの戻り値型を調査）
- **型変換**: `float[]` と `double[]` 間の変換処理を追加
- **コンストラクタ**: デフォルトコンストラクタまたは引数付きコンストラクタの使い分けを明確化
- **JavaFX Web**: `javafx-web` 依存関係を追加

---

## 実装タスク

### Phase 1: 依存関係とモデルクラスの修正 (30分)

#### Task 1-1: pom.xmlへのJavaFX Web依存関係追加 (5分)

**目的**: `javafx.scene.web.WebView` を使用可能にする

**実装内容**:
```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-web</artifactId>
    <version>17.0.1</version>
</dependency>
```

**影響ファイル**:
- `java-app/pom.xml`

**検証方法**:
- `mvn dependency:tree` で依存関係が追加されていることを確認

---

#### Task 1-2: InputRow.javaへのメソッド追加 (25分)

**目的**: `SimulationService` で必要なメソッドを実装

**実装内容**:
1. **コピーコンストラクタ**:
```java
public InputRow(InputRow other) {
    this.features = new HashMap<>(other.features);
}
```

2. **setValue() メソッド**:
```java
public void setValue(String column, Object value) {
    features.put(column, value);
}
```

3. **getFloatInput() メソッド**:
```java
public FloatBuffer getFloatInput() {
    // featuresから数値型の値を抽出してFloatBufferに変換
    List<Float> floatValues = new ArrayList<>();
    for (Object value : features.values()) {
        if (value instanceof Number) {
            floatValues.add(((Number) value).floatValue());
        }
    }
    FloatBuffer buffer = FloatBuffer.allocate(floatValues.size());
    for (Float f : floatValues) {
        buffer.put(f);
    }
    buffer.flip();
    return buffer;
}
```

4. **getStringInput() メソッド**:
```java
public String[] getStringInput() {
    // featuresから文字列型の値を抽出して配列に変換
    List<String> stringValues = new ArrayList<>();
    for (Object value : features.values()) {
        if (value instanceof String) {
            stringValues.add((String) value);
        }
    }
    return stringValues.toArray(new String[0]);
}
```

**影響ファイル**:
- `java-app/src/main/java/com/factory/ml/model/InputRow.java`

**検証方法**:
- コンパイルエラーが減少することを確認
- `SimulationService.java` のエラーが解消されることを確認

---

### Phase 2: ONNX Runtime API対応とサービスクラス修正 (90分)

#### Task 2-1: ONNX Runtime APIの最新仕様調査 (20分)

**目的**: ONNX Runtime 1.15.0の正式なAPIを理解する

**実施内容**:
- ONNX Runtime公式ドキュメント確認: https://onnxruntime.ai/docs/api/java/api/
- `OrtSession.run()` メソッドのシグネチャを確認
- `OrtInputs`, `OrtOutputs` の代替APIを特定

**予想される正式API**:
```java
// 1.15.0では Map<String, OnnxTensor> を使用
Map<String, OnnxTensor> inputs = new HashMap<>();
OrtSession.Result result = session.run(inputs);
```

---

#### Task 2-2: InferenceService.javaの修正 (40分)

**目的**: ONNX Runtime 1.15.0の正式APIに対応

**実装内容**:

1. **import文の修正**:
```java
import ai.onnxruntime.*;
import java.util.HashMap;
import java.util.Map;
```

2. **predict() メソッドの修正**:
```java
public InferenceResult predict(FloatBuffer floats, String[] strings, boolean useCandidate) throws OrtException {
    OrtSession session = useCandidate ? candidateSession : currentSession;
    
    // float配列に変換
    float[] floatArray = new float[floats.remaining()];
    floats.get(floatArray);
    
    // Tensorを作成
    OnnxTensor floatTensor = OnnxTensor.createTensor(env, 
        FloatBuffer.wrap(floatArray), new long[]{1, floatArray.length});
    OnnxTensor stringTensor = OnnxTensor.createTensor(env, strings);
    
    // 入力マップを作成
    Map<String, OnnxTensor> inputs = new HashMap<>();
    inputs.put("float_input", floatTensor);
    inputs.put("string_input", stringTensor);
    
    // 推論実行
    OrtSession.Result result = session.run(inputs);
    
    // 結果の取得（型をdouble[]に対応）
    OnnxValue probValue = result.get("probabilities").get();
    float[] probabilities = convertToFloatArray(probValue);
    
    OnnxValue labelValue = result.get("label").get();
    String label = (String) labelValue.getValue();
    
    // リソース解放
    floatTensor.close();
    stringTensor.close();
    result.close();
    
    return new InferenceResult(label, probabilities);
}

private float[] convertToFloatArray(OnnxValue value) throws OrtException {
    Object obj = value.getValue();
    if (obj instanceof double[]) {
        double[] doubles = (double[]) obj;
        float[] floats = new float[doubles.length];
        for (int i = 0; i < doubles.length; i++) {
            floats[i] = (float) doubles[i];
        }
        return floats;
    } else if (obj instanceof float[]) {
        return (float[]) obj;
    }
    throw new IllegalStateException("Unexpected type: " + obj.getClass());
}
```

**影響ファイル**:
- `java-app/src/main/java/com/factory/ml/service/InferenceService.java`

**検証方法**:
- `InferenceService.java` のコンパイルエラーが解消されることを確認

---

#### Task 2-3: SimulationService.javaの修正 (30分)

**目的**: `InferenceService` コンストラクタ引数とメソッド呼び出しを修正

**実装内容**:

```java
public InferenceResult simulate(InputRow original, Map<String, Object> modifications) {
    // Create a copy of the original input row
    InputRow modifiedRow = new InputRow(original);  // コピーコンストラクタ使用
    
    // Apply modifications to the copied row
    for (Map.Entry<String, Object> entry : modifications.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();
        modifiedRow.setValue(key, value);  // setValue() メソッド使用
    }
    
    // Perform inference using the modified row
    // TODO: modelPathを設定ファイルから取得する必要がある
    // 暫定的にハードコードまたは別のコンストラクタを作成
    try {
        InferenceService inferenceService = new InferenceService("models/current/model.onnx");
        return inferenceService.predict(
            modifiedRow.getFloatInput(), 
            modifiedRow.getStringInput(), 
            false
        );
    } catch (OrtException e) {
        throw new RuntimeException("Inference failed", e);
    }
}
```

**影響ファイル**:
- `java-app/src/main/java/com/factory/ml/service/SimulationService.java`

**検証方法**:
- `SimulationService.java` のコンパイルエラーが解消されることを確認

---

### Phase 3: Controllerクラスの修正 (60分)

#### Task 3-1: TrainingTabController.javaの修正 (25分)

**目的**: サービスクラスのコンストラクタ引数とメソッド呼び出しを修正

**実装内容**:

1. **サービスクラスのインスタンス化を修正**:
```java
// 現状の問題箇所（67-68行目）
InferenceService inferenceService = new InferenceService();
ModelManagerService modelManagerService = new ModelManagerService();

// 修正後
InferenceService inferenceService = new InferenceService("models/current/model.onnx");
ModelManagerService modelManagerService = new ModelManagerService("models/current", "models/archive");
```

2. **DataValidator.validate() 呼び出しを修正**:
```java
// 現状の問題箇所（106行目、120行目）
List<ValidationError> errors = validator.validate(filePath);

// 修正後（CSVファイルを読み込んでList<String[]>に変換）
List<String[]> rows = loadCsvRows(filePath);
Schema schema = loadSchema("config/schema.json");
List<ValidationError> errors = validator.validate(rows, schema);
```

3. **ModelManagerService.trainModel() 呼び出しを修正**:
```java
// 現状の問題箇所（122行目）
boolean success = modelManagerService.trainModel(filePath);

// 修正後（trainModel() メソッドの引数を確認して修正）
// ModelManagerService.javaの実装を確認する必要がある
```

**影響ファイル**:
- `java-app/src/main/java/com/factory/ml/controller/TrainingTabController.java`

**検証方法**:
- `TrainingTabController.java` のコンパイルエラーが解消されることを確認

---

#### Task 3-2: InferenceTabController.javaの修正 (20分)

**目的**: サービスクラスのコンストラクタ引数とメソッド呼び出しを修正

**実装内容**:

1. **InferenceService のインスタンス化を修正**:
```java
// 現状の問題箇所（58行目）
InferenceService inferenceService = new InferenceService();

// 修正後
InferenceService inferenceService = new InferenceService("models/current/model.onnx");
```

2. **FeatureTransformer.loadData() 呼び出しを修正**:
```java
// 現状の問題箇所（86行目）
List<InputRow> data = featureTransformer.loadData(file);

// 修正後（loadData() メソッドの実装を確認）
// FeatureTransformer.javaの実装を確認する必要がある
```

3. **DataValidator.validate() 呼び出しを修正**:
```java
// 現状の問題箇所（87行目）
List<ValidationError> errors = validator.validate(data);

// 修正後
List<String[]> rows = convertInputRowsToStringArrays(data);
Schema schema = loadSchema("config/schema.json");
List<ValidationError> errors = validator.validate(rows, schema);
```

4. **InferenceService.predict() 呼び出しを修正**:
```java
// 現状の問題箇所（104行目）
// predict() メソッドの引数を確認して修正
```

**影響ファイル**:
- `java-app/src/main/java/com/factory/ml/controller/InferenceTabController.java`

**検証方法**:
- `InferenceTabController.java` のコンパイルエラーが解消されることを確認

---

#### Task 3-3: SimulationViewController.javaの修正 (15分)

**目的**: `InferenceService` と `InferenceResult` の型定義問題を修正

**実装内容**:

1. **import文の確認**:
```java
import com.factory.ml.service.InferenceService;
import com.factory.ml.model.InferenceResult;
```

2. **型定義の修正**:
```java
// 現状の問題箇所（37行目、83行目）
// cannot find symbol エラーの原因を特定して修正
```

**影響ファイル**:
- `java-app/src/main/java/com/factory/ml/controller/SimulationViewController.java`

**検証方法**:
- `SimulationViewController.java` のコンパイルエラーが解消されることを確認

---

### Phase 4: 統合テストと動作確認 (30分)

#### Task 4-1: コンパイル成功確認 (10分)

**目的**: すべての修正が正しく適用されていることを確認

**実施内容**:
```bash
cd java-app
mvn clean compile
```

**検証方法**:
- コンパイルエラーが0件であることを確認
- `BUILD SUCCESS` が表示されることを確認

---

#### Task 4-2: 単体テストの実行 (10分)

**目的**: 既存のテストがパスすることを確認

**実施内容**:
```bash
mvn test
```

**検証方法**:
- すべてのテストがパスすることを確認
- 失敗がある場合は原因を分析して修正

---

#### Task 4-3: アプリケーション起動確認 (10分)

**目的**: Javaアプリケーションが正常に起動することを確認

**実施内容**:
```bash
mvn javafx:run
```

**検証方法**:
- アプリケーションが起動すること
- エラーログが出力されないこと
- 基本的なUI操作が可能なこと

---

## タスク進捗トラッキング

| Task | Epic | 見積時間 | 状態 | 備考 |
|------|------|---------|------|------|
| T-1-1 | Phase 1 | 5分 | ⚪ Not Started | JavaFX Web依存関係追加 |
| T-1-2 | Phase 1 | 25分 | ⚪ Not Started | InputRowメソッド実装 |
| T-2-1 | Phase 2 | 20分 | ⚪ Not Started | ONNX Runtime API調査 |
| T-2-2 | Phase 2 | 40分 | ⚪ Not Started | InferenceService修正 |
| T-2-3 | Phase 2 | 30分 | ⚪ Not Started | SimulationService修正 |
| T-3-1 | Phase 3 | 25分 | ⚪ Not Started | TrainingTabController修正 |
| T-3-2 | Phase 3 | 20分 | ⚪ Not Started | InferenceTabController修正 |
| T-3-3 | Phase 3 | 15分 | ⚪ Not Started | SimulationViewController修正 |
| T-4-1 | Phase 4 | 10分 | ⚪ Not Started | コンパイル成功確認 |
| T-4-2 | Phase 4 | 10分 | ⚪ Not Started | 単体テスト実行 |
| T-4-3 | Phase 4 | 10分 | ⚪ Not Started | アプリケーション起動確認 |

**状態凡例**:
- ⚪ Not Started（未着手）
- 🔵 In Progress（進行中）
- ✅ Done（完了）
- ⏸️ Blocked（ブロック中）
- ❌ Cancelled（キャンセル）

## 総作業時間見積

| フェーズ | 見積時間 |
|---------|---------|
| Phase 1: 依存関係とモデルクラス修正 | 30分 |
| Phase 2: ONNX Runtime API対応 | 90分 |
| Phase 3: Controllerクラス修正 | 60分 |
| Phase 4: 統合テストと動作確認 | 30分 |
| **合計** | **3.5時間** |

## 次のステップ

実装計画に従い、以下の順序でタスクを実施:

1. ⚪ Phase 1: 依存関係とモデルクラスの修正
2. ⚪ Phase 2: ONNX Runtime API対応とサービスクラス修正
3. ⚪ Phase 3: Controllerクラスの修正
4. ⚪ Phase 4: 統合テストと動作確認

## 参考資料

- [Issue #10](https://github.com/nakagawah13/factory-ml-offline-system/issues/10)
- [ONNX Runtime Java API Documentation](https://onnxruntime.ai/docs/api/java/api/)
- [ai-code-writing.instructions.md](../.github/instructions/ai-code-writing.instructions.md)
- [git-workflow.instructions.md](../.github/instructions/git-workflow.instructions.md)
