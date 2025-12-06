public class ValidationError {
    private String columnName;
    private String errorMessage;
    private int rowIndex;

    public ValidationError(String columnName, String errorMessage, int rowIndex) {
        this.columnName = columnName;
        this.errorMessage = errorMessage;
        this.rowIndex = rowIndex;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "columnName='" + columnName + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", rowIndex=" + rowIndex +
                '}';
    }
}