package io.github.thibaultmeyer.cuid;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.MethodName.class)
final class PerformanceTest {

    @BeforeAll
    static void beforeAll() {

        System.gc();
    }

    @Test
    void speedUUID() {

        for (int i = 0; i < 10; i += 1) {
            UUID.randomUUID();
        }

        final List<UUID> uuidList = new ArrayList<>();
        final long start = System.nanoTime();
        for (int i = 0; i < 1_000_000; i += 1) {
            uuidList.add(UUID.randomUUID());
        }
        final long end = System.nanoTime();

        System.err.println("1,000,000 UUID have been generated in " + (end - start) / 1_000_000 + " ms");
        Assertions.assertEquals(1_000_000, uuidList.size());
    }

    @Test
    void speedCUIDv1() {

        for (int i = 0; i < 10; i += 1) {
            CUID.randomCUID1();
        }

        final List<CUID> cuidList = new ArrayList<>();
        final long start = System.nanoTime();
        for (int i = 0; i < 1_000_000; i += 1) {
            cuidList.add(CUID.randomCUID1());
        }
        final long end = System.nanoTime();

        System.err.println("1,000,000 CUIDv1 have been generated in " + (end - start) / 1_000_000 + " ms");
        Assertions.assertEquals(1_000_000, cuidList.size());
    }

    @Test
    void speedCUIDv2Standard() {

        for (int i = 0; i < 10; i += 1) {
            CUID.randomCUID2();
        }

        final List<CUID> cuidList = new ArrayList<>();
        final long start = System.nanoTime();
        for (int i = 0; i < 1_000_000; i += 1) {
            cuidList.add(CUID.randomCUID2());
        }
        final long end = System.nanoTime();

        System.err.println("1,000,000 CUIDv2 have been generated in " + (end - start) / 1_000_000 + " ms");
        Assertions.assertEquals(1_000_000, cuidList.size());
    }

    @Test
    void speedCUIDv2Big() {

        for (int i = 0; i < 10; i += 1) {
            CUID.randomCUID2(32);
        }

        final List<CUID> cuidList = new ArrayList<>();
        final long start = System.nanoTime();
        for (int i = 0; i < 1_000_000; i += 1) {
            cuidList.add(CUID.randomCUID2(32));
        }
        final long end = System.nanoTime();

        System.err.println("1,000,000 CUIDv2 have been generated in " + (end - start) / 1_000_000 + " ms");
        Assertions.assertEquals(1_000_000, cuidList.size());
    }
}
