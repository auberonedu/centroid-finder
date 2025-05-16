# Planning
- New entry point
    - new Main calls old main on each video frame
- (maybe?) Handle each frame as it's created
    - Consider tradeoffs of time and size
- Convert frames back into a video for validation


[new main] -> [Data Tracker - create CSV, call centroid finder to modify CSV] -> [largest centroid finder - take photo, return largest centroid] && [Video Processor - produce multiple frames] 

Link to flowchart: https://docs.google.com/drawings/d/1PYy6h4iulJ7FepGHRRITSQBkAGDCtvwggX7IKSOrPlc/edit

Link to centroid flowchard: https://docs.google.com/drawings/d/1APfHESu6xwbwyQqTZXW2G4OGoleH2Nut88yQMnYuCtY/edit?usp=sharing

<!-- To view the diagram below in preview, add Markdown Preview Mermaid Support extension -->
```mermaid
graph LR
    MainApp:::new --> 
    VideoProcessor:::new --> LargestCentroid/ImageSummaryApp:::prev --> EuclideanColorDistance:::prev & DistanceImageBinarizer:::prev & BinarizingImageGroupFinder:::prev

    classDef new fill:#922, stroke:#fff, stroke-width:3px;
    classDef prev fill:#229, stroke:#fff, stroke-width:2px;
```

```mermaid
classDiagram
    class Main["Main App"] {
    }

    class Video["VideoProcessor"]{
        +String filePath
        +int color
        +int threshhold
        +File csv
        +int framesPerSec = empty
        +int secondIncrement = 1
        +extractFrames(videoPath, outputDir, fps)
        -frameToData(frame)
        +getFileAt(timeStamp)
    }


    classDef default fill:#a22,stroke:#fff,stroke-width:2px;
```

<!-- 
    class DataTracker["Data Tracker"] {
        +int color
        +int threshhold
        +String filePath
        +File CSV
        +VideoProcessor processor
        -processVideoData(data, CSV, videoProcessor)
    }
    -->





