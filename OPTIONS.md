Research Java video processing libraries
You need to:
✅ Search online for at least 2/each of us - libraries that let Java handle MP4 videos.
In OPTIONS.md file, in your own words, list:

the name of the library,
a few pros,
a few cons.

JavaCV

JavaCV is a Java wrapper for OpenCV, which is a powerful, open-source computer vision library widely used for object detection and tracking.

Pros:
- Features for object tracking
- frame by frame analysis available 
- 
- access to image processing tools

cons
- large library
    - more to learn how to use and set up project 
-Most tutorials, examples, and docs are for C++ or Python OpenCV.

VLCJ   

VLCJ is a Java library that provides bindings to the native VLC media player, allowing you to control VLC and play audio/video from within Java application

Pros:
-simple frame grabing 
-Can be used to stream or receive live feeds with minimal configuration. 
- You'll get pixel data (e.g., in RGBA or YUV) for each frame.

cons 
- does not support computer vision, object detection, or tracking.
- VLCJ is designed for playing or streaming video, not for analysis.

JCodec
Description:
JCodec is a pure Java library for video encoding and decoding, supporting formats like H.264 and VP8. It facilitates operations such as video transcoding, resizing, and watermarking.

Pros:

Pure Java implementation, eliminating the need for native libraries.
Suitable for basic video processing tasks, including transcoding and resizing.
Useful for applications requiring lightweight video manipulation.
Cons:

Limited support for advanced video processing features.
May not be as performant as libraries leveraging native code for intensive tasks.

Xuggler
Description:
Xuggler is a Java library that provides Java bindings to the FFmpeg multimedia framework, enabling developers to decode, encode, and manipulate video and audio streams.

Pros:

Comprehensive support for various media formats and codecs.
Facilitates tasks like video transcoding, streaming, and frame extraction.
Integrates well with Java applications requiring media manipulation.
Cons:

Development has been discontinued, and the library is no longer actively maintained.
May pose compatibility issues with newer systems and Java versions.
