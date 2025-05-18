package io.github.alstondsouza1.centroidFinder;

import java.io.File;
import java.io.FileNotFoundException;

public class CommandLineParser {
    private final String inputPath;
    private final String outputCsv;
    private final int targetColor;
    private final int threshold;


    
    private byte videoState = 0;
    private final byte video_OK = 0b000;
    private final byte video_EXTENSION = 0b001;
    private final byte video_LONG = 0b010;
    private final byte video_NOT_FOUND = 0b100;

    /*
     * 0b00 = OK
     * 0b01 = Incorrect extension.
     * 0b10 = Filename too long.
     * 0b100 = File not found.
     */

    // constructor that takes and validates command line arguments
    public CommandLineParser(String[] args) {
        if (args.length != 4) {
            throw new IllegalArgumentException("Usage: java -jar videoprocessor.jar inputPath outputCsv targetColor threshold");
        }
        this.inputPath = args[0];
        this.outputCsv = args[1];
        this.targetColor = Integer.parseInt(args[2], 16); // parse color from hex
        this.threshold = Integer.parseInt(args[3]); // parse threshold as int
    }

    public String getInputPath() {
        return inputPath;
    }

    public String getOutputCsv() {
        return outputCsv;
    }

    public int getTargetColor() {
        return targetColor;
    }

    public int getThreshold() {
        return threshold;
    }

    public byte getVideoState() {
        return videoState;
    }

    /*private String checkVideoArg(String arg) {
        //Not sure if throws Exception is a good practice, but I'll go with this. -Raymond
        String RET = new String(arg);
        if(!arg.endsWith(".mp4")) {
            this.videoState = (byte) (this.videoState | video_EXTENSION);
        }

        char slash = 0;
        int fileNameLength = 0;
        for(int i = 0; slash != '/'; i++) {
            slash = arg.charAt(arg.length()-1-i);
            fileNameLength++;
        }
        if(fileNameLength > 256) {
            this.videoState = (byte) (this.videoState | video_LONG);
        }


        File fakeFile = new File(arg);
        if(!fakeFile.exists()) {
            this.videoState = (byte) (this.videoState | video_NOT_FOUND);
        }
        return RET;
    }*/
}