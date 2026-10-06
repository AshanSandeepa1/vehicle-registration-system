package util;

/**
 * Utility class to generate formatted application numbers (e.g. 0001AA).
 */
public class ApplicationIdGenerator {

    private static int counter = 0;

    /**
     * Generates a sequential formatted string with numeric prefix and letter suffix.
     * Example: 0001AA, 0002AB, etc.
     */
    public static synchronized String generateFormattedString() {
        counter++;
        String formattedNumber = String.format("%04d", counter);
        char letter1 = (char) ('A' + ((counter - 1) / 26) % 26);
        char letter2 = (char) ('A' + (counter - 1) % 26);
        return formattedNumber + letter1 + letter2;
    }

    public static synchronized void resetCounter() {
        counter = 0;
    }
}

