from skl2onnx import convert_sklearn
from skl2onnx.common.data_types import FloatTensorType, StringTensorType
import joblib
import os

def convert_model_to_onnx(model, model_path, initial_types):
    onnx_model = convert_sklearn(model, initial_types=initial_types)
    with open(model_path, "wb") as f:
        f.write(onnx_model.SerializeToString())

def load_model(model_file):
    return joblib.load(model_file)

def save_onnx_model(model, output_dir, model_name):
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)
    model_path = os.path.join(output_dir, f"{model_name}.onnx")
    initial_types = [
        ('float_input', FloatTensorType([None, model.n_features_in_])),
        ('string_input', StringTensorType([None, model.n_categorical_features_]))
    ]
    convert_model_to_onnx(model, model_path, initial_types)
    return model_path