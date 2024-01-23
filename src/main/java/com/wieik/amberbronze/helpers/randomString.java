package com.wieik.amberbronze.helpers;

/**
 * This class provides a method to generate a random string of specified length.
 */
public class randomString {
    /**
     * Generates a random string of the specified length.
     *
     * @param length the length of the random string to generate
     * @return a random string of the specified length
     */
    public static String generate(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * chars.length());
            stringBuilder.append(chars.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }
}
