Making this to evaluate different pros and cons for video libraries

https://stackoverflow.com/questions/13011397/video-processing-library-for-java
-   https://marvinproject.net/en/index.html
-   Xugler
    - No longer maintaied and difficult with modern java they say
-   OpenCV
    - They say its tricky to set up 
    -   Real powerful and has real time capabilites apperantly 
    - Big community so should be some good F & Q's
-   Marvin Framework
    -   Easy to use allegadly, although it is less flexible then OpenCV. Good for quick stuff






1. JavaFX Media API

Pros

Built into JavaFX, so no extra native binaries are required.

Easy to play MP4/H.264 videos and retrieve basic metadata (duration, resolution).

Seamlessly integrates with JavaFX UI components (MediaView).

Cons

Limited codec support: primarily H.264/AAC in MP4 containers.

Not optimized for low‐level frame extraction or batch processing of frames.

Requires JavaFX runtime setup (can be a bit heavy if you don’t already use JavaFX).

2. VLCJ

Pros

Leverages VLC’s comprehensive codec support, can handle nearly any format.

Supports advanced features like streaming, filters, and subtitles.

Offers snapshot and frame capture APIs.

Cons

Depends on native VLC libraries, increasing distribution complexity.

Larger footprint and heavier startup time.

Maven integration can be more involved due to native library loading.

3. JCodec

Pros

100% pure Java implementation—no native code or JNI dependencies.

Can decode/encode H.264, extract raw frame data as BufferedImages.

Lightweight and easy to add to any Maven project.

Cons

Performance lags behind native solutions (not ideal for real‐time or high‐res processing).

Limited container format support beyond MP4/MOV.

May lack advanced features like streaming or hardware acceleration.


