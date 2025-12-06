from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score

class MetricsCalculator:
    def __init__(self, y_true, y_pred):
        self.y_true = y_true
        self.y_pred = y_pred

    def calculate_accuracy(self):
        return accuracy_score(self.y_true, self.y_pred)

    def calculate_precision(self):
        return precision_score(self.y_true, self.y_pred, average='weighted')

    def calculate_recall(self):
        return recall_score(self.y_true, self.y_pred, average='weighted')

    def calculate_f1_score(self):
        return f1_score(self.y_true, self.y_pred, average='weighted')

    def get_metrics(self):
        return {
            'accuracy': self.calculate_accuracy(),
            'precision': self.calculate_precision(),
            'recall': self.calculate_recall(),
            'f1_score': self.calculate_f1_score()
        }