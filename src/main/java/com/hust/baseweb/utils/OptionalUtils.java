package com.hust.baseweb.utils;

import java.util.Optional;

/**
 * @author Le Anh Tuan
 */
public class OptionalUtils {

    /**
     * Convert string to integer.
     *
     * @param s a {@code String} containing the int representation to be converted
     * @return the integer value represented by the argument in decimal
     */
    public static Optional<Integer> strToInt(String s) {
        try {
            return Optional.of(Integer.parseInt(s));
        } catch (NumberFormatException nfe) {
            return Optional.empty();
        }
    }

}
