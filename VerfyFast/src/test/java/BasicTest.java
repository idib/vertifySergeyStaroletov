import com.verify.NearestPoints;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasicTest {
    @Test
    void justAnExample() {
        NearestPoints nearestPoints = new NearestPoints();
        assertEquals(nearestPoints.addPoint(1L,1,2), true);
    }
}
