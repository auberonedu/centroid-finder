package io.github.alstondsouza1.centroidFinder;

import java.io.File;
import java.io.FileNotFoundException;

public class CommandLineParser {
    private final String inputPath;
    private final String outputCsv;
    private final int targetColor;
    private final int threshold;

    // constructor that takes and validates command line arguments
    public CommandLineParser(String[] args) throws Exception {
        if (args.length != 4) {
            throw new IllegalArgumentException("Usage: java -jar videoprocessor.jar inputPath outputCsv targetColor threshold");
        }
        try {
            this.inputPath = checkVideoArg(args[0]);
        } catch (Exception e) {
            throw e;
        }
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

    public String checkVideoArg(String arg) throws Exception {
        //Not sure if throws Exception is a good practice, but I'll go with this. -Raymond
        String RET = new String(arg);
        if(arg.substring(arg.length()-5).equals(".mp4") != false) {
            throw new IllegalArgumentException("File is required to be .mp4");
        }

        char slash = 0;
        int fileNameLength = 0;
        for(int i = 0; slash != '/'; i++) {
            slash = arg.charAt(arg.length()-1-i);
            fileNameLength++;
        }
        if(fileNameLength > 256) {
            throw new IllegalArgumentException("Filename is too long:"+fileNameLength+" chars, expected <=256 ");
        }


        File fakeFile = new File(arg);
        if(!fakeFile.exists()) {
            throw new FileNotFoundException(arg + " is not found!");
        }
        return RET;
    }
}