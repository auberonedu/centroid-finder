## Video Library Options for Java
--------------------------------------------------------------------------------------------------------------------------

# JavaCV 

- Pros:
* Can handle a wide range of video formats and operations.

* Actively maintained and widely used in computer vision projects.

* Can extract frame data, read metadata, and even do live video capture.

- Cons:
* Can be a bit complex to set up with native dependencies.

* The API is low-level, so it takes more effort to do basic things.
--------------------------------------------------------------------------------------------------------------------------

# Xuggler 

- Pros:
* Designed specifically for Java video/audio encoding and decoding.

* Lets you grab frames and video info in Java.

* Can combine video and audio into one file.

- Cons:
* No longer is updated/maintained.

* Can be tricky to install on newer systems.

* Might not work well with the latest Java versions.
--------------------------------------------------------------------------------------------------------------------------

# JCodec

- Pros:
* Easy to include, just Java code no system installs.

* Can decode MP4 and extract frames.

* Good for simple tasks like frame grabbing.

- Cons:
* Doesn’t support as many video formats.

* Slower because it's all written in Java.

* Less powerful for editing or complex features.
