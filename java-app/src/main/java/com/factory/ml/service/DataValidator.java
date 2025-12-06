package com.factory.ml.service;

import com.factory.ml.model.ValidationError;
import com.factory.ml.model.Schema;

import java.util.ArrayList;
import java.util.List;

public class DataValidator {
    public List<ValidationError> validate(List<String[]> csvRows, Schema schema) {
        List<ValidationError> errors = new ArrayList<>();

        for (int i = 0; i < csvRows.size(); i++) {
            String[] row = csvRows.get(i);
            // バリデーションロジックをここに実装
            // 例: 型チェック、必須フィールドの確認など
            // エラーが見つかった場合は、ValidationErrorを作成してerrorsリストに追加
        }

        return errors;
    }
}