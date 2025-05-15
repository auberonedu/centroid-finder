# Planning
- New entry point
    - new Main calls old main on each video frame
- (maybe?) Handle each frame as it's created
    - Consider tradeoffs of time and size
- Convert frames back into a video for validation


[new main] -> [Data Tracker - create CSV, call centroid finder to modify CSV] -> [largest centroid finder - take photo, return largest centroid] && [Video Processor - produce multiple frames] 

Link to flowchart: https://docs.google.com/drawings/d/1PYy6h4iulJ7FepGHRRITSQBkAGDCtvwggX7IKSOrPlc/edit

<!-- To view the diagram below in preview, add Markdown Preview Mermaid Support extension -->
```mermaid
graph LR
    MainApp:::new --> DataTracker:::new --> VideoProcessor:::new 
    
    DataTracker --> LargestCentroid/ImageSummaryApp:::prev --> EuclideanColorDistance:::prev & DistanceImageBinarizer:::prev & BinarizingImageGroupFinder:::prev

    classDef new fill:#922, stroke:#fff, stroke-width:3px;
    classDef prev fill:#229, stroke:#fff, stroke-width:2px;
```

```mermaid
classDiagram
    class Main["Main App"] {
    }
    class DataTracker["Data Tracker"] {
        +int color
        +int threshhold
        +String filePath
        +createCSV()
        +addData(data, CSV)
    }
    class Video["VideoProcessor"]{
        +String filePath
        +int timeStamp
        +File outPut
        +getFileAt(timeStamp)
    }


    classDef default fill:#a22,stroke:#fff,stroke-width:2px;
```





