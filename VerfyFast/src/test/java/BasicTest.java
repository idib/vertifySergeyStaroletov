import com.verify.NearestPoints;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasicTest {
    @Test
    void justAnExample() {
        NearestPoints nearestPoints = new NearestPoints();
        assertEquals(true, nearestPoints.addPoint(1L, 1, 2));
    }

    @Test
    void checkBorders() {
        NearestPoints nearestPoints = new NearestPoints();
        assertAll("",
                () -> assertEquals(false, nearestPoints.checkDegrees(-181)),
                () -> assertEquals(false, nearestPoints.checkDegrees(-180.1)),
                () -> assertEquals(true, nearestPoints.checkDegrees(-180)),
                () -> assertEquals(true, nearestPoints.checkDegrees(180))
        );
    }
}
