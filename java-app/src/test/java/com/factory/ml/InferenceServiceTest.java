import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.factory.ml.service.InferenceService;
import com.factory.ml.model.InferenceResult;

public class InferenceServiceTest {

    private InferenceService inferenceService;

    @BeforeEach
    public void setUp() {
        inferenceService = new InferenceService();
    }

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

    @Test
    public void testPredictWithGrayZone() {
        float[] floatInput = {0.5f, 0.5f, 0.5f}; // Example input that may fall into gray zone
        String[] stringInput = {"C", "D"};

        InferenceResult result = inferenceService.predict(floatInput, stringInput, false);
        
        assertNotNull(result);
        assertTrue(result.getProbabilities()[0] >= 0.4 && result.getProbabilities()[0] <= 0.6);
    }

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