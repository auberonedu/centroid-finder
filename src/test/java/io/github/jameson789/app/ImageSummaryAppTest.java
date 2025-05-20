package io.github.jameson789.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class ImageSummaryAppTest {

    private static final String TEST_VIDEO = "src/test/resources/test.mp4";
    
    @BeforeEach
    void setUp() {
        // Clean up any existing output files
        new File("frame_centroids.csv").delete();
        deleteDirectory(new File("binarized_frames"));
    }
    
    @AfterEach
    void tearDown() {
        // Clean up output files after tests
        new File("frame_centroids.csv").delete();
        deleteDirectory(new File("binarized_frames"));
    }

    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
    
    @Test
    void testInvalidArguments(){
        String[] args = {"ensantina.mp4", "FF0000"}; 
        assertThrows(IllegalArgumentException.class, () -> ImageSummaryApp.main(args));
    }
}
