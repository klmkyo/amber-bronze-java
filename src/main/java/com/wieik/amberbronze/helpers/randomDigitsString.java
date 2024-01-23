package com.wieik.amberbronze.helpers;

/**
 * This class provides a method to generate a random string of digits.
 */
public class randomDigitsString {
    /**
     * Generates a random string of digits with the specified length.
     *
     * @param length the length of the generated string
     * @return a random string of digits
     */
    public static String generate(int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += (int) (Math.random() * 10);
        }
        return result;
    }
}
