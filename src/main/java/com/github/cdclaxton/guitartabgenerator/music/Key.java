package com.github.cdclaxton.guitartabgenerator.music;

import java.util.Arrays;
import java.util.HashSet;

public class Key {

    private String key;
    private static final String validMajorKeys = "Ab|A|A#|Bb|B|C|C#|Db|D|D#|Eb|E|F|F#|Gb|G|G#";
    private static final String validMinorKeys = "Abm|Am|A#m|Bbm|Bm|Cm|C#m|Dbm|Dm|D#m|Ebm|Em|Fm|F#m|Gbm|Gm|G#m";

    /**
     * Instantiate a key from its string representation.
     *
     * @param key Key.
     * @throws InvalidKeyException
     */
    public Key(String key) throws InvalidKeyException {
        if (isValid(key)) {
            this.key = key;
        } else {
            throw new InvalidKeyException("Key " + key + " is not valid");
        }
    }

    /**
     * Is the string representation of the key valid?
     *
     * @param key Key to check.
     * @return True if the key is valid.
     */
    public static boolean isValid(String key) {
        return isMajorKey(key) || isMinorKey(key);
    }

    /**
     * Is the key a major key?
     *
     * @param key Key.
     * @return Major key?
     */
    public static boolean isMajorKey(String key) {
        String[] majorKeys = Key.validMajorKeys.split("\\|");
        HashSet<String> setMajorKeys = new HashSet<String>(Arrays.asList(majorKeys));
        return setMajorKeys.contains(key);
    }

    /**
     * Is the key a minor key?
     *
     * @param key Key.
     * @return Minor key?
     */
    public static boolean isMinorKey(String key) {
        String[] minorKeys = Key.validMinorKeys.split("\\|");
        HashSet<String> setMinorKeys = new HashSet<String>(Arrays.asList(minorKeys));
        return setMinorKeys.contains(key);
    }

    public String getKey() {
        return key;
    }
}
