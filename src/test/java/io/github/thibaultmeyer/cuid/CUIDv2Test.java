package io.github.thibaultmeyer.cuid;

import io.github.thibaultmeyer.cuid.exception.CUIDGenerationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.HashSet;
import java.util.Set;

@TestMethodOrder(MethodOrderer.MethodName.class)
final class CUIDv2Test {

    @Test
    void fromString() {

        // Arrange
        final String cuidAsString = "n1ht3jch1r23dy9ramd6ts16";

        // Act
        final CUID cuid = CUID.fromString(cuidAsString);

        // Assert
        Assertions.assertNotNull(cuid);
        Assertions.assertEquals(24, cuid.toString().length());
        Assertions.assertEquals("n1ht3jch1r23dy9ramd6ts16", cuid.toString());
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
    void randomCUIDv2() {

        // Act
        final CUID cuid = CUID.randomCUID2();

        // Assert
        Assertions.assertNotNull(cuid);
        Assertions.assertEquals(24, cuid.toString().length());
    }

    @Test
    void randomCUIDv2BigLength() {

        // Act
        final CUID cuid = CUID.randomCUID2(32);

        // Assert
        Assertions.assertNotNull(cuid);
        Assertions.assertEquals(32, cuid.toString().length());
    }

    @Test
    void compareToNotSame() {

        // Arrange
        final CUID cuidOne = CUID.fromString("g346rykdwn4m117cupchv9m6");
        final CUID cuidTwo = CUID.fromString("o7ti2h84195cdvxmbx9vb2gg");

        // Act
        final int result = cuidOne.compareTo(cuidTwo);

        // Assert
        Assertions.assertEquals(-8, result);
    }

    @Test
    void compareToNotSameNull() {

        // Arrange
        final CUID cuidOne = CUID.fromString("f23nsqjlsmd1oo0kooedsg07");

        // Act
        final int result = cuidOne.compareTo(null);

        // Assert
        Assertions.assertEquals(-1, result);
    }

    @Test
    void compareToSame() {

        // Arrange
        final CUID cuidOne = CUID.fromString("z976prixkgxs0u13x7g67fo3");
        final CUID cuidTwo = CUID.fromString("z976prixkgxs0u13x7g67fo3");

        // Act
        final int result = cuidOne.compareTo(cuidTwo);

        // Assert
        Assertions.assertEquals(0, result);
    }

    @Test
    void parameterizedLength() {

        // Act
        final CUID cuid = CUID.randomCUID2(2);

        // Assert
        Assertions.assertNotNull(cuid);
        Assertions.assertEquals(2, cuid.toString().length());
    }

    @Test
    void parameterizedLengthInvalidSize() {

        // Act
        final CUIDGenerationException exception = Assertions.assertThrows(
            CUIDGenerationException.class,
            () -> CUID.randomCUID2(-1));

        // Assert
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("CUID generation failure: the length must be at least 1", exception.getMessage());
    }

    @Test
    void unicityOver500000() {

        // Act
        final Set<CUID> cuidSet = new HashSet<>();
        for (int i = 0; i < 500000; i += 1) {
            cuidSet.add(CUID.randomCUID2());
        }

        // Assert
        Assertions.assertEquals(500000, cuidSet.size());
    }
}
