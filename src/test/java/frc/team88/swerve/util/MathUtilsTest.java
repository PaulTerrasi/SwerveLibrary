package frc.team88.swerve.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static frc.team88.swerve.TestUtils.assertDoubleEquals;

import org.junit.jupiter.api.Test;

public class MathUtilsTest {

    @Test
    public void testDoubleEqualsTrue() {
        assertTrue(MathUtils.doubleEquals((1. / 10_000.) * 10., (10. / 1_000_000.) * 100.));
    }

    @Test
    public void testDoubleEqualsFalse() {
        assertFalse(MathUtils.doubleEquals(1., 1.001));
    }

    @Test
    public void testLimitChangeIncreaseUnrestricted() {
        assertDoubleEquals(10., MathUtils.limitChange(8., 10., 4.));
    }

    @Test
    public void testLimitChangeIncreaseRestricted() {
        assertDoubleEquals(12., MathUtils.limitChange(8., 16., 4.));
    }

    @Test
    public void testLimitChangeDecreaseUnrestricted() {
        assertDoubleEquals(8., MathUtils.limitChange(10., 8., 6.));
    }

    @Test
    public void testLimitChangeDecreaseRestricted() {
        assertDoubleEquals(4., MathUtils.limitChange(10., 2., 6.));
    }
}
