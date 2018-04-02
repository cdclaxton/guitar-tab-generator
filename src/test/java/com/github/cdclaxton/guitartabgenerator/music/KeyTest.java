package com.github.cdclaxton.guitartabgenerator.music;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyTest {

    @Test
    void testValidKeys() throws InvalidKeyException {
        assertEquals("A", new Key("A").getKey());
        assertEquals("A#", new Key("A#").getKey());
        assertEquals("B", new Key("B").getKey());
        assertEquals("Am", new Key("Am").getKey());
        assertEquals("A#m", new Key("A#m").getKey());
        assertEquals("Bm", new Key("Bm").getKey());
    }

    @Test
    void testInvalidKeys() {
        assertThrows(InvalidKeyException.class, () -> new Key("Hello"));
        assertThrows(InvalidKeyException.class, () -> new Key("A#b"));
        assertThrows(InvalidKeyException.class, () -> new Key("Hm"));
    }

}