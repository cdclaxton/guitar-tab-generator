package com.github.cdclaxton.guitartabgenerator.app;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    @Test
    void testReadConfig() throws IOException {
        String path = this.getClass().getResource("/config.properties").getFile();
        Config config = new Config(path);

        assertEquals(100, config.getPageWidth());
        assertEquals(20, config.getMaxFret());
    }

}