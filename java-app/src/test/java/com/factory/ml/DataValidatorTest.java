import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.factory.ml.service.DataValidator;
import com.factory.ml.model.ValidationError;

import java.util.ArrayList;
import java.util.List;

public class DataValidatorTest {

    private DataValidator dataValidator;

    @BeforeEach
    public void setUp() {
        dataValidator = new DataValidator();
    }

    @Test
    public void testValidateValidData() {
        List<String[]> validData = new ArrayList<>();
        validData.add(new String[]{"2023-01-01", "A", "100"});
        validData.add(new String[]{"2023-01-02", "B", "200"});

        List<ValidationError> errors = dataValidator.validate(validData, /* schema object */);
        assertTrue(errors.isEmpty(), "Validation should pass for valid data");
    }

    @Test
    public void testValidateInvalidData() {
        List<String[]> invalidData = new ArrayList<>();
        invalidData.add(new String[]{"2023-01-01", "A", "invalid_number"});
        invalidData.add(new String[]{"2023-01-02", "Invalid_Category", "200"});

        List<ValidationError> errors = dataValidator.validate(invalidData, /* schema object */);
        assertFalse(errors.isEmpty(), "Validation should fail for invalid data");
    }

    @Test
    public void testValidateEmptyData() {
        List<String[]> emptyData = new ArrayList<>();

        List<ValidationError> errors = dataValidator.validate(emptyData, /* schema object */);
        assertFalse(errors.isEmpty(), "Validation should fail for empty data");
    }
}