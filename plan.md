# Centroid Finder Video Part 2

## Wave 1: Planning

## Goal
we are creating a command line tool that processes mp4 videos and finds the largest centroid (connected groups of pixels close to a target color) in each frame. The output will be a CSV file that list the time (in seconds) and (x, y) location of that centroid. If no centroid is found in a frame, the output will be -1, -1

we will reuse code from the orginal Centroid Finder project and built around it using object-oriented design

---------

### System Components

## 1. VideoProcessorApp (main clas)

- entry point of the application
- parses the command line arguments using `CommandLineParser`
- coordinates: video reading - (`VideoReader`), processing each frame - (`FrameProcessor`), and writing output to CSV - (`CsvWriter`)


## 2. CommandLineParser

- validates and extracts inputs:
    - input video path
    - output CSV path
    - target color
    - color threshold
- converts the target color into a format
- may include error checking for `.mp4` and `.csv` file


## 3. VideoReader

- uses JCodec to open the mp4 video
- extracts one frame per second
- keep track of the timestamp in seconds for each frame


## 4. FrameProcessor

- accepts a BufferedIn=mage frame
- uses existing code:
    - distanceImageBinarizer to binarize the frame
    - dfsBinaryGroupFinder through BinarizingImageGroupFinder to find white pixel groups
- returns the (x, y) centroid of the largest group


## 5. CsvWriter

- opens the output CSV file
- writes one row per frame with
    - time in seconds
    - x coordinate
    - y coordinate

---------

### Notes

- constants will be used for configuration
- all new classes will be under -100 lines and broken into smaller methods
- comments will be added for any logic
- following name conventions and java best practices


### Testing Plan

- `CommandLineParser`: argument length, color parsing and threshold parsing
- `VideoReader`: frame rate extracting and first frame loading
- `FrameProcessor`: corrects centroid returns or fallback to (-1, -1)
- `CsvWriter`: file creation and formatted output

tests will be managed through Maven and the Surefire plugin

## Flow
VideoProcessorApp
↓
CommandLineParser → VideoReader → FrameProcessor → CsvWriter
↘
BinarizingImageGroupFinder
↘
DistanceImageBinarizer + DfsBinaryGroupFinder

Wave 4: Validation
To validate the functionality, I tested with the video ensantina.mp4. I confirmed the output in mander.csv and checked the centroid points visually through the frontend React interface. The coordinates aligned with the expected salamander positions. If no centroid was detected, the output correctly logged -1, -1.

## Command Line Used

- java -jar target/centroid-1.0-SNAPSHOT-jar-with-dependencies.jar sample.mp4 output.csv FF0000 50

- java -jar target/centroid-1.0-SNAPSHOT-jar-with-dependencies.jar ensantina.mp4 mander.csv FF0000 100 