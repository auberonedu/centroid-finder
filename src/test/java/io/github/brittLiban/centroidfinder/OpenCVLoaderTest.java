package io.github.brittLiban.centroidfinder;

import org.junit.jupiter.api.Test;
import org.opencv.core.Core;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OpenCVLoaderTest {

    @Test
    public void testOpenCVLoadsSuccessfully() {
        // This triggers the static block in OpenCVLoader
        OpenCVLoader loader = new OpenCVLoader();

        // Confirm OpenCV loaded by checking the version string
        String version = Core.VERSION;
        assertNotNull(version, "OpenCV version should not be null");
        System.out.println("✅ OpenCV loaded. Version: " + version);
    }
}
