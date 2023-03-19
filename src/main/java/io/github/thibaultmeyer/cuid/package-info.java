/**
 * Collision-resistant ID optimized for horizontal scaling and performance.
 * <pre>{@code
 *  // Generates a random CUID (Version 1)
 *  final CUID cuid = CUID.randomCUID1();
 *  System.out.println("CUID: " + cuid);
 *
 *  // Generates a random CUID (Version 2)
 *  final CUID cuid = CUID.randomCUID2();
 *  System.out.println("CUID: " + cuid);
 *
 *  // Generates a random CUID with a custom length (Version 2)
 *  final int customLength = 8;  // Length must be, at least, 1
 *  final CUID cuid = CUID.randomCUID2(customLength);
 *  System.out.println("CUID: " + cuid);
 *
 *  // Creates a CUID from a string
 *  final CUID cuid = CUID.fromString("cl9gts1kw00393647w1z4v2tc");
 *  System.out.println("CUID: " + cuid);
 *
 *  // Verifies if string contains a valid CUID
 *  final boolean isValid = CUID.isValid("cl9gts1kw00393647w1z4v2tc");
 *  System.out.println("Is 'cl9gts1kw00393647w1z4v2tc' a valid CUID ? " + isValid);
 * }</pre>
 *
 * @since 1.0.0
 */
package io.github.thibaultmeyer.cuid;
