package io.github.thibaultmeyer.cuid;

import io.github.thibaultmeyer.cuid.exception.CUIDGenerationException;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * Collision-resistant ID optimized for horizontal scaling and performance.
 *
 * @see <a href="https://usecuid.org/">CUID official website</a>
 * @since 1.0.0
 */
public final class CUID implements Serializable, Comparable<CUID> {

    // Explicit serialVersionUID for interoperability.
    private static final long serialVersionUID = -2441709761088574861L;

    // Base to use
    private static final int NUMBER_BASE = 36;

    /**
     * CUID internal value holder.
     */
    private final String value;

    /**
     * Creates a new instance.
     *
     * @param value A valid CUID value
     * @since 1.0.0
     */
    private CUID(final String value) {

        this.value = value;
    }

    /**
     * Generates a new random CUID (Version 2).
     *
     * @return Newly generated CUID (Version 2)
     * @since 2.0.0
     */
    public static CUID randomCUID2() {

        return randomCUID2(CUIDv2.LENGTH_STANDARD);
    }

    /**
     * Generates a new random CUID (Version 2).
     *
     * @param length requested CUID length
     * @return Newly generated CUID (Version 2)
     * @since 2.0.1
     */
    public static CUID randomCUID2(final int length) {

        if (length <= 0) {
            throw new CUIDGenerationException("the length must be at least 1");
        }

        final String time = Long.toString(System.currentTimeMillis(), NUMBER_BASE);
        final char firstLetter = CUIDv2.ALPHABET_ARRAY[safeAbs((int) (Common.nextFloatValue() * CUIDv2.ALPHABET_ARRAY.length))];
        final String hash = CUIDv2.computeHash(
            time + CUIDv2.createEntropy(length) + CUIDv2.nextCounterValue() + Common.MACHINE_FINGERPRINT,
            length);

        return new CUID(firstLetter + hash.substring(1, length));
    }

    /**
     * Generates a new random CUID (Version 1).
     *
     * @return Newly generated CUID (Version 1)
     * @since 2.0.0
     */
    public static CUID randomCUID1() {

        final String timestamp = Long.toString(System.currentTimeMillis(), NUMBER_BASE);
        final String counter = Common.padWithZero(Integer.toString(CUIDv1.nextCounterValue(), NUMBER_BASE), CUIDv1.BLOCK_SIZE);
        final String random = CUIDv1.getRandomBlock() + CUIDv1.getRandomBlock();

        return new CUID(CUIDv1.START_CHARACTER + timestamp + counter + Common.MACHINE_FINGERPRINT + random);
    }

    /**
     * Creates a {@code CUID} from the string standard representation.
     *
     * @param cuidAsString A string that specifies a {@code CUID} (Version 1 or 2)
     * @return A {@code CUID} with the specified value
     * @throws IllegalArgumentException If the string is not conform
     * @since 1.0.0
     */
    public static CUID fromString(final String cuidAsString) {

        if (isValid(cuidAsString)) {
            return new CUID(cuidAsString);
        }

        throw new IllegalArgumentException("CUID string is invalid: '" + cuidAsString + "'");
    }

    /**
     * Checks the {@code CUID} from the string standard representation.
     *
     * @param cuidAsString A string that specifies a {@code CUID} (Version 1 or 2)
     * @return {@code true} If the string is conforms, otherwise, {@code false}
     * @since 1.0.0
     */
    public static boolean isValid(final String cuidAsString) {

        return cuidAsString != null
            && (cuidAsString.length() == CUIDv1.LENGTH_STANDARD && cuidAsString.startsWith(CUIDv1.START_CHARACTER) // Version 1
            || (!cuidAsString.isEmpty())) // Version 2
            && cuidAsString.chars()
            .filter(c -> !((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')))
            .findAny()
            .isEmpty();
    }

    /**
     * Always return non-negative value.
     *
     * @param i the integer value
     * @return a non-negative value
     * @since 2.0.3
     */
    private static int safeAbs(final int i) {

        return i == Integer.MIN_VALUE ? 0 : Math.abs(i);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    public int compareTo(final CUID cuid) {

        if (cuid == null) {
            return -1;
        }

        return this.value.compareTo(cuid.value);
    }

    /**
     * Returns the string representation.
     *
     * @return String containing the {@code CUID}
     * @since 1.0.0
     */
    @Override
    public String toString() {

        return this.value;
    }

    /**
     * Returns {@code true} if the argument is equal to current object, {@code false} otherwise.
     *
     * @param o An object
     * @since 1.0.0
     */
    @Override
    public boolean equals(final Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CUID cuid = (CUID) o;
        return Objects.equals(value, cuid.value);
    }

    /**
     * Generates a hash code.
     *
     * @return Generated hashcode
     * @since 1.0.0
     */
    @Override
    public int hashCode() {

        return Objects.hash(value);
    }

    /**
     * CUID Version 1.
     *
     * @since 1.0.0
     */
    private static final class CUIDv1 {

        // CUID configuration
        private static final int BLOCK_SIZE = 4;
        private static final int LENGTH_STANDARD = 25;
        private static final String START_CHARACTER = "c";
        private static final int DISCRETE_VALUE = (int) Math.pow(NUMBER_BASE, BLOCK_SIZE);

        // Counter
        private static int counter = 0;

        /**
         * Retrieves the counter next value.
         *
         * @return The counter next value
         * @since 1.0.0
         */
        private static synchronized int nextCounterValue() {

            counter = counter < DISCRETE_VALUE ? counter : 0;
            return counter++;
        }

        /**
         * Generates a random block of data.
         *
         * @return Newly generated block of data
         * @since 1.0.0
         */
        private static String getRandomBlock() {

            return Common.padWithZero(Integer.toString(safeAbs(Common.nextIntValue() * DISCRETE_VALUE), NUMBER_BASE), BLOCK_SIZE);
        }
    }

    /**
     * CUID Version 2.
     *
     * @since 2.0.0
     */
    private static final class CUIDv2 {

        private static final char[] ALPHABET_ARRAY = new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        // CUID configuration
        private static final int LENGTH_STANDARD = 24;

        // Counter
        private static int counter = Integer.MAX_VALUE;

        /**
         * Retrieves the counter next value.
         *
         * @return The counter next value
         */
        private static synchronized int nextCounterValue() {

            counter = counter < Integer.MAX_VALUE ? counter : safeAbs(Common.nextIntValue());
            return counter++;
        }

        /**
         * Creates an entropy string.
         *
         * @param length Length of the entropy string
         * @return String containing entropy in base {@link CUID#NUMBER_BASE}
         */
        private static String createEntropy(final int length) {

            final StringBuilder stringBuilder = new StringBuilder(length);

            while (stringBuilder.length() < length) {
                stringBuilder.append(Integer.toString(Common.nextIntValue() * NUMBER_BASE, NUMBER_BASE));
            }

            return stringBuilder.toString();
        }

        /**
         * Computes hash.
         *
         * @return String containing hash
         */
        private static String computeHash(final String content, final int saltLength) {

            final String salt = createEntropy(saltLength);
            try {
                return new BigInteger(MessageDigest.getInstance("SHA3-256").digest((content + salt).getBytes(StandardCharsets.UTF_8)))
                    .toString(NUMBER_BASE);
            } catch (final NoSuchAlgorithmException exception) {
                throw new CUIDGenerationException(exception);
            }
        }
    }

    /*
     * Holder class to defer initialization until needed.
     *
     * @since 1.0.0
     */
    private static final class Common {

        private static final int RANDOM_BUFFER_SIZE = 4096;
        private static final byte[] RANDOM_BUFFER = new byte[RANDOM_BUFFER_SIZE];
        private static final SecureRandom NUMBER_GENERATOR = new SecureRandom();
        private static final String MACHINE_FINGERPRINT = getMachineFingerprint();

        private static int randomBufferIndex = RANDOM_BUFFER_SIZE;

        /**
         * Retrieves next random integer value.
         *
         * @return A random integer
         * @since 1.0.0
         */
        private static synchronized int nextIntValue() {

            if (randomBufferIndex == RANDOM_BUFFER_SIZE) {
                Common.NUMBER_GENERATOR.nextBytes(Common.RANDOM_BUFFER);
                randomBufferIndex = 0;
            }

            return Common.RANDOM_BUFFER[randomBufferIndex++] << 24
                | (Common.RANDOM_BUFFER[randomBufferIndex++] & 0xff) << 16
                | (Common.RANDOM_BUFFER[randomBufferIndex++] & 0xff) << 8
                | (Common.RANDOM_BUFFER[randomBufferIndex++] & 0xff);
        }

        /**
         * Retrieves next random floating value.
         *
         * @return A random floating number (between `0.0` and `1.0`)
         * @since 2.0.5
         */
        private static float nextFloatValue() {

            return NUMBER_GENERATOR.nextFloat();
        }

        /**
         * Pads string with leading zero.
         *
         * @param str  The string to pad
         * @param size The size to keep
         * @return The padded string
         * @since 1.0.0
         */
        private static String padWithZero(final String str, final int size) {

            final String paddedString = "000000000" + str;
            return paddedString.substring(paddedString.length() - size);
        }

        /**
         * retrieves the machine fingerprint.
         *
         * @return The machine fingerprint
         * @since 1.0.0
         */
        private static String getMachineFingerprint() {

            final String machineName = ManagementFactory.getRuntimeMXBean().getName();
            final String[] machineNameTokenArray = machineName.split("@");
            final String pid = machineNameTokenArray[0];
            final String hostname = machineNameTokenArray[1];

            int acc = hostname.length() + NUMBER_BASE;
            for (int i = 0; i < hostname.length(); i += 1) {
                acc = acc + hostname.charAt(i);
            }

            final String idBlock = padWithZero(pid, 2);
            final String nameBlock = padWithZero(Integer.toString(acc), 2);

            return idBlock + nameBlock;
        }
    }
}
