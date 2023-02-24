package io.github.thibaultmeyer.cuid;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.HashSet;
import java.util.Set;

@TestMethodOrder(MethodOrderer.MethodName.class)
final class CUIDv1Test {

    @Test
    void fromString() {

        // Arrange
        final String cuidAsString = "cl9gts1kw00393647w1z4v2tc";

        // Act
        final CUID cuid = CUID.fromString(cuidAsString);

        // Assert
        Assertions.assertNotNull(cuid);
        Assertions.assertEquals(25, cuid.toString().length());
        Assertions.assertEquals("cl9gts1kw00393647w1z4v2tc", cuid.toString());
    }

    @Test
    void fromStringInvalid() {

        // Arrange
        final String cuidAsString = "invalid-cuid";

        // Act
        final IllegalArgumentException exception = Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> CUID.fromString(cuidAsString));

        // Assert
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("CUID string is invalid: 'invalid-cuid'", exception.getMessage());
    }

    @Test
    void isValid() {

        // Arrange
        final String cuidAsString = "cl9gts1kw00393647w1z4v2tc";

        // Act
        final boolean isValid = CUID.isValid(cuidAsString);

        // Assert
        Assertions.assertTrue(isValid);
    }

    @Test
    void isValidInvalid() {

        // Arrange
        final String cuidAsString = "not-a-cuid";

        // Act
        final boolean isValid = CUID.isValid(cuidAsString);

        // Assert
        Assertions.assertFalse(isValid);
    }

    @Test
    void randomCUID() {

        // Act
        final CUID cuid = CUID.randomCUID1();

        // Assert
        Assertions.assertNotNull(cuid);
        Assertions.assertEquals(25, cuid.toString().length());
    }

    @Test
    void compareToNotSame() {

        // Arrange
        final CUID cuidOne = CUID.fromString("cl9gts1kw00393647w1z4v2tc");
        final CUID cuidTwo = CUID.fromString("cl9gts1kw00393647ee45bn56");

        // Act
        final int result = cuidOne.compareTo(cuidTwo);

        // Assert
        Assertions.assertEquals(18, result);
    }

    @Test
    void compareToNotSameNull() {

        // Arrange
        final CUID cuidOne = CUID.fromString("cl9gts1kw00393647w1z4v2tc");

        // Act
        final int result = cuidOne.compareTo(null);

        // Assert
        Assertions.assertEquals(-1, result);
    }

    @Test
    void compareToSame() {

        // Arrange
        final CUID cuidOne = CUID.fromString("cl9gts1kw00393647w1z4v2tc");
        final CUID cuidTwo = CUID.fromString("cl9gts1kw00393647w1z4v2tc");

        // Act
        final int result = cuidOne.compareTo(cuidTwo);

        // Assert
        Assertions.assertEquals(0, result);
    }

    @Test
    void unicityOver500000() {

        // Act
        final Set<CUID> cuidSet = new HashSet<>();
        for (int i = 0; i < 500000; i += 1) {
            cuidSet.add(CUID.randomCUID1());
        }

        // Assert
        Assertions.assertEquals(500000, cuidSet.size());
    }
}
