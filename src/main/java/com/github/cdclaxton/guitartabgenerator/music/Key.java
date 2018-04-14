package com.github.cdclaxton.guitartabgenerator.music;

import java.util.Arrays;
import java.util.HashSet;

public final class Key {

    private final String key;
    private static final String validMajorKeys = "Ab|A|A#|Bb|B|C|C#|Db|D|D#|Eb|E|F|F#|Gb|G|G#";
    private static final String validMinorKeys = "Abm|Am|A#m|Bbm|Bm|Cm|C#m|Dbm|Dm|D#m|Ebm|Em|Fm|F#m|Gbm|Gm|G#m";

    /**
     * Instantiate a key from its string representation.
     *
     * @param key Key.
     * @throws InvalidKeyException Musical key is invalid.
     */
    public Key(String key) throws InvalidKeyException {
        if (isValid(key)) {
            this.key = key;
        } else {
            throw new InvalidKeyException("Key " + key + " is not valid");
        }
    }

    /**
     * Construct a new instance of the key.
     *
     * @param key Key to copy.
     * @return New key.
     */
    public static Key newInstance(final Key key) {
        try {
            return new Key(key.getKey());
        } catch (InvalidKeyException e) {
            throw new IllegalStateException("Key is now invalid!");
        }
    }

    /**
     * Is the string representation of the key valid?
     *
     * @param key Key to check.
     * @return True if the key is valid.
     */
    static boolean isValid(String key) {
        return isMajorKey(key) || isMinorKey(key);
    }

    /**
     * Is the key a major key?
     *
     * @param key Key.
     * @return Major key?
     */
    static boolean isMajorKey(String key) {
        String[] majorKeys = Key.validMajorKeys.split("\\|");
        HashSet<String> setMajorKeys = new HashSet<>(Arrays.asList(majorKeys));
        return setMajorKeys.contains(key);
    }

    /**
     * Is the key a minor key?
     *
     * @param key Key.
     * @return Minor key?
     */
    private static boolean isMinorKey(String key) {
        String[] minorKeys = Key.validMinorKeys.split("\\|");
        HashSet<String> setMinorKeys = new HashSet<>(Arrays.asList(minorKeys));
        return setMinorKeys.contains(key);
    }

    /**
     * Get the musical key.
     * @return Key.
     */
    public String getKey() {
        return key;
    }
}
