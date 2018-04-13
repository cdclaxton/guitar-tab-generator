package com.github.cdclaxton.guitartabgenerator.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WebBrowserLauncher {

    private static Logger logger = LoggerFactory.getLogger(WebBrowserLauncher.class);

    /**
     * Launch a web browser to show the url. Windows only.
     *
     * @param url URL to open in web browser.
     */
    public static void launch(String url) {
        logger.info("Opening URL: " + url);
        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec( "rundll32 url.dll,FileProtocolHandler " + url);
        } catch (IOException e) {
            logger.error("Couldn't launch browser for URL: " + url);
            e.printStackTrace();
        }
    }

}
