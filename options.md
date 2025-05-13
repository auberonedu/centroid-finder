# Video Processing Libraries
For this project, we have considered several different Java video processing libraries, listed below.

## Overview
- JCodec
- JavaCV
- VLCJ

## [JCodec](http://jcodec.org/)
A library that implements video and audio codecs.
### Pros
- Lightweight
- No extra dependencies
- Easy to set up  
- Designed for this case use (converting video to frame images)
### Cons  
- Slow
- Minimal documentation
## [JavaCV](https://docs.opencv.org/4.x/)
Contains wrappers of commonly used computer vision libraries with utility classes to make them easier to use.
### Pros  
- LOTS of visual processing options!
- Faster than JCodec
- Lots of documentation available
### Cons  
- Contains way more functionality than we need
- Steep learning curve

## [Xuggle/Xuggler](https://github.com/artclarke/xuggle-xuggler?tab=readme-ov-file)
A library that is a wrapper around FFmpeg for decoding multimedia files. This includes processing frames of a video.
### Pros  
- Supports lots of video formats without needing to implement specific logic
- Java Friendly 👍
- Sometimes lightweight - for simple operations 
### Cons  
- Not actively maintained
- Complex setup - need to set up native libraries on each machine