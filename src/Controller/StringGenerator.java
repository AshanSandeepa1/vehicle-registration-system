package Controller;

import util.ApplicationIdGenerator;

/**
 * Backward-compatible wrapper for generating application identifiers.
 * 
 * @author Ashan
 */
public class StringGenerator {

    public static String generateFormattedString() {
        return ApplicationIdGenerator.generateFormattedString();
    }
}
