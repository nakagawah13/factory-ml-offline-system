import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.factory.ml.service.FeatureTransformer;
import com.factory.ml.model.InputRow;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for FeatureTransformer service.
 * 
 * Tests feature transformation logic for converting InputRow data into
 * ONNX-compatible float and string tensor formats.
 * 
 * @see FeatureTransformer
 */
public class FeatureTransformerTest {

    private FeatureTransformer featureTransformer;

    /**
     * Sets up test fixtures before each test.
     * 
     * Initializes a new FeatureTransformer instance.
     */
    @BeforeEach
    public void setUp() {
        featureTransformer = new FeatureTransformer();
    }

    /**
     * Tests creation of float buffer input for ONNX.
     * 
     * Verifies that InputRow data is correctly transformed into a FloatBuffer
     * with the expected capacity and values.
     */
    @Test
    public void testCreateFloatInput() {
        List<InputRow> rows = new ArrayList<>();
        rows.add(new InputRow("2023-01-01", "A", 100.0));
        rows.add(new InputRow("2023-01-02", "B", 200.0));

        FloatBuffer floatInput = featureTransformer.createFloatInput(rows);
        
        assertNotNull(floatInput);
        assertEquals(4, floatInput.capacity()); // 2 rows * 2 numerical features
        assertEquals(100.0, floatInput.get(0));
        assertEquals(200.0, floatInput.get(2));
    }

    /**
     * Tests creation of string array input for ONNX.
     * 
     * Verifies that InputRow data is correctly transformed into a String array
     * containing categorical features.
     */
    @Test
    public void testCreateStringInput() {
        List<InputRow> rows = new ArrayList<>();
        rows.add(new InputRow("2023-01-01", "A", 100.0));
        rows.add(new InputRow("2023-01-02", "B", 200.0));

        String[] stringInput = featureTransformer.createStringInput(rows);
        
        assertNotNull(stringInput);
        assertEquals(2, stringInput.length); // 2 rows
        assertEquals("A", stringInput[0]);
        assertEquals("B", stringInput[1]);
    }
}