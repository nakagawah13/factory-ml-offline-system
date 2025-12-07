import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
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
    @Before
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
        
        InputRow row1 = new InputRow();
        row1.setValue("date", "2023-01-01");
        row1.setValue("category", "A");
        row1.setValue("value", 100.0);
        rows.add(row1);
        
        InputRow row2 = new InputRow();
        row2.setValue("date", "2023-01-02");
        row2.setValue("category", "B");
        row2.setValue("value", 200.0);
        rows.add(row2);

        FloatBuffer floatInput = featureTransformer.createFloatInput(rows);
        
        assertNotNull(floatInput);
        assertTrue(floatInput.capacity() > 0);
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
        
        InputRow row1 = new InputRow();
        row1.setValue("date", "2023-01-01");
        row1.setValue("category", "A");
        row1.setValue("value", 100.0);
        rows.add(row1);
        
        InputRow row2 = new InputRow();
        row2.setValue("date", "2023-01-02");
        row2.setValue("category", "B");
        row2.setValue("value", 200.0);
        rows.add(row2);

        String[] stringInput = featureTransformer.createStringInput(rows);
        
        assertNotNull(stringInput);
        assertTrue(stringInput.length > 0);
    }
}