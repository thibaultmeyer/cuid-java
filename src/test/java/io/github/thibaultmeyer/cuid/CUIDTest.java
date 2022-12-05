package io.github.thibaultmeyer.cuid;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.MethodName.class)
final class CUIDTest {

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
    void randomCUID() {

        // Act
        final CUID cuid = CUID.randomCUID();

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
            cuidSet.add(CUID.randomCUID());
        }

        // Assert
        Assertions.assertEquals(500000, cuidSet.size());
    }

    @Test
    void speedVersusUUID() {

        System.gc();
        for (int i = 0; i < 10; i += 1) {
            UUID.randomUUID();
        }

        final List<UUID> uuidList = new ArrayList<>();
        final long start2 = System.nanoTime();
        for (int i = 0; i < 1_000_000; i += 1) {
            uuidList.add(UUID.randomUUID());
        }
        final long end2 = System.nanoTime();
        System.err.println("1,000,000 UUID have been generated in " + (end2 - start2) / 1000000 + " ms");
        uuidList.clear();

        System.gc();
        for (int i = 0; i < 10; i += 1) {
            CUID.randomCUID();
        }

        final List<CUID> cuidList = new ArrayList<>();
        final long start = System.nanoTime();
        for (int i = 0; i < 1_000_000; i += 1) {
            cuidList.add(CUID.randomCUID());
        }
        final long end = System.nanoTime();
        System.err.println("1,000,000 CUID have been generated in " + (end - start) / 1000000 + " ms");
        cuidList.clear();
    }
}
