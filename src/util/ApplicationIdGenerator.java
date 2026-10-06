package util;

import java.security.SecureRandom;

/**
 * Generates application reference numbers in the established format:
 * four digits followed by two upper-case letters (e.g. 4821KD).
 *
 * The previous implementation used an in-memory counter that restarted at
 * 0001AA on every launch, colliding with existing records. IDs are now random
 * (6.76 million combinations) and callers verify uniqueness against the
 * database before inserting (see Model.DBSearch#submitNewApplication).
 */
public final class ApplicationIdGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private ApplicationIdGenerator() {
    }

    public static String generateFormattedString() {
        int number = RANDOM.nextInt(10_000);
        char letter1 = (char) ('A' + RANDOM.nextInt(26));
        char letter2 = (char) ('A' + RANDOM.nextInt(26));
        return String.format("%04d%c%c", number, letter1, letter2);
    }
}
