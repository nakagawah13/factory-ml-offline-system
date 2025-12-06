import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.factory.ml.service.InferenceService;
import com.factory.ml.model.InferenceResult;

/**
 * Unit tests for InferenceService.
 * 
 * Tests model inference functionality including valid input prediction,
 * gray zone handling, and shadow testing mode.
 * 
 * @see InferenceService
 */
public class InferenceServiceTest {

    private InferenceService inferenceService;

    /**
     * Sets up test fixtures before each test.
     * 
     * Initializes a new InferenceService instance.
     */
    @BeforeEach
    public void setUp() {
        inferenceService = new InferenceService();
    }

    /**
     * Tests prediction with valid input data.
     * 
     * Verifies that inference produces a valid result with label and
     * probability scores.
     */
    @Test
    public void testPredictValidInput() {
        float[] floatInput = {1.0f, 2.0f, 3.0f}; // Example numerical features
        String[] stringInput = {"A", "B"}; // Example categorical features

        InferenceResult result = inferenceService.predict(floatInput, stringInput, false);
        
        assertNotNull(result);
        assertNotNull(result.getLabel());
        assertNotNull(result.getProbabilities());
        assertTrue(result.getProbabilities().length > 0);
    }

    /**
     * Tests prediction with gray zone input.
     * 
     * Verifies that inputs with ambiguous probability scores (around 0.5)
     * are handled correctly by the gray zone logic.
     */
    @Test
    public void testPredictWithGrayZone() {
        float[] floatInput = {0.5f, 0.5f, 0.5f}; // Example input that may fall into gray zone
        String[] stringInput = {"C", "D"};

        InferenceResult result = inferenceService.predict(floatInput, stringInput, false);
        
        assertNotNull(result);
        assertTrue(result.getProbabilities()[0] >= 0.4 && result.getProbabilities()[0] <= 0.6);
    }

    /**
     * Tests prediction with shadow testing enabled.
     * 
     * Verifies that shadow testing mode produces valid predictions alongside
     * the primary model without affecting results.
     */
    @Test
    public void testPredictWithShadowTest() {
        float[] floatInput = {1.0f, 1.0f, 1.0f}; // Example input for shadow test
        String[] stringInput = {"A", "B"};

        InferenceResult result = inferenceService.predict(floatInput, stringInput, true);
        
        assertNotNull(result);
        assertNotNull(result.getLabel());
        assertNotNull(result.getProbabilities());
        assertTrue(result.getProbabilities().length > 0);
    }
}