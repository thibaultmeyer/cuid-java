package io.github.thibaultmeyer.cuid;

import java.lang.management.ManagementFactory;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * Collision-resistant ID optimized for horizontal scaling and performance.
 *
 * @see <a href="https://usecuid.org/">CUID official website</a>
 */
public final class CUID implements java.io.Serializable, Comparable<CUID> {

    // Explicit serialVersionUID for interoperability.
    private static final long serialVersionUID = -2441709761088574861L;

    // CUID configuration.
    private static final int NUMBER_BASE = 36;
    private static final int BLOCK_SIZE = 4;
    private static final int CUID_VALUE_LENGTH = 25;
    private static final String START_CHARACTER = "c";
    private static final int RANDOM_BUFFER_SIZE = 4096;

    // Counter
    private static int counter = 0;
    private static int randomBufferIndex = RANDOM_BUFFER_SIZE;

    // CUID value
    private final String value;

    /**
     * Build a new instance.
     *
     * @param value A valid CUID value
     */
    private CUID(final String value) {

        this.value = value;
    }

    /**
     * Generates a new random CUID.
     *
     * @return Newly generated CUID
     */
    public static CUID randomCUID() {

        final String timestamp = Long.toString(System.currentTimeMillis(), NUMBER_BASE);
        final String counter = padWithZero(Integer.toString(nextCounterValue(), NUMBER_BASE), BLOCK_SIZE);
        final String random = getRandomBlock() + getRandomBlock();

        return new CUID(START_CHARACTER + timestamp + counter + Holder.MACHINE_FINGERPRINT + random);
    }

    /**
     * Creates a {@code CUID} from the string standard representation.
     *
     * @param cuidAsString A string that specifies a {@code CUID}
     * @return A {@code CUID} with the specified value
     * @throws IllegalArgumentException If the string is not conform
     */
    public static CUID fromString(final String cuidAsString) {

        if (isValid(cuidAsString)) {
            return new CUID(cuidAsString);
        }

        throw new IllegalArgumentException("CUID string is invalid: '" + cuidAsString + "'");
    }

    /**
     * Retrieves the counter next value.
     *
     * @return The counter next value
     */
    private static synchronized int nextCounterValue() {

        counter = counter < Holder.DISCRETE_VALUES ? counter : 0;
        return counter++;
    }

    /**
     * Generates a random block of data.
     *
     * @return Newly generated block of data
     */
    private static String getRandomBlock() {

        return padWithZero(Integer.toString(nextIntValue() * Holder.DISCRETE_VALUES, NUMBER_BASE), BLOCK_SIZE);
    }

    /**
     * Retrieves next random integer value.
     *
     * @return A random integer
     */
    private static synchronized int nextIntValue() {

        if (randomBufferIndex == RANDOM_BUFFER_SIZE) {
            Holder.NUMBER_GENERATOR.nextBytes(Holder.RANDOM_BUFFER);
            randomBufferIndex = 0;
        }

        return Holder.RANDOM_BUFFER[randomBufferIndex++] << 24
            | (Holder.RANDOM_BUFFER[randomBufferIndex++] & 0xff) << 16
            | (Holder.RANDOM_BUFFER[randomBufferIndex++] & 0xff) << 8
            | (Holder.RANDOM_BUFFER[randomBufferIndex++] & 0xff);
    }

    /**
     * Pads string with leading zero.
     *
     * @param str  The string to pad
     * @param size The size to keep
     * @return The padded string
     */
    private static String padWithZero(final String str, final int size) {

        final String paddedString = "000000000" + str;
        return paddedString.substring(paddedString.length() - size);
    }

    /**
     * Checks the {@code CUID} from the string standard representation.
     *
     * @param cuidAsString A string that specifies a {@code CUID}
     * @return {@code true} If the string is not conform, otherwise, {@code false}
     */
    public static boolean isValid(final String cuidAsString) {

        return cuidAsString != null
            && cuidAsString.length() == CUID_VALUE_LENGTH
            && cuidAsString.startsWith(START_CHARACTER)
            && cuidAsString.chars()
            .filter(c -> !((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')))
            .count() == 0;
    }

    @Override
    public int compareTo(final CUID cuid) {

        if (cuid == null) {
            return -1;
        }

        return this.value.compareTo(cuid.value);
    }

    @Override
    public String toString() {

        return this.value;
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CUID cuid = (CUID) o;
        return Objects.equals(value, cuid.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(value);
    }

    /*
     * Holder class to defer initialization until needed.
     */
    private static final class Holder {

        static final byte[] RANDOM_BUFFER = new byte[RANDOM_BUFFER_SIZE];
        static final SecureRandom NUMBER_GENERATOR = new SecureRandom();
        static final String MACHINE_FINGERPRINT = getMachineFingerprint();
        private static final int DISCRETE_VALUES = (int) Math.pow(NUMBER_BASE, BLOCK_SIZE);

        /**
         * retrieves the machine fingerprint.
         *
         * @return The machine fingerprint
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
