/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Ashan
 */
public class StringGenerator {
    private static int counter = 0;

    public static String generateFormattedString() {
        // Increment the counter
        counter++;

        // Format the counter to have leading zeros
        String formattedNumber = String.format("%04d", counter);

        // Generate two random uppercase letters
        char letter1 = (char) ('A' + ((counter - 1) / 26) % 26);
        char letter2 = (char) ('A' + (counter - 1) % 26);

        // Combine the formatted number and letters to get the final string
        return formattedNumber + letter1 + letter2;
    }
}
