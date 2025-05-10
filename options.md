1. Xuggler

Pros:

- Able to encode, decode and real audio and video files
- Raw frames that can be processed as buffered images
- Great for modifying media streams and extracting metadata

Cons:

- No longer kept up to date
- Compatibility problems with more revent java versions can result from the complex setup process
- Native libraries are needed

2. JCodec

Pros:

- No native dependencies, just a pure java library that is cross platform and simple to integrate
- Able to decode pictures and MP4 sequences
- Simple API to grab video frames as Buffered Image

Cons:

- Limited compatability with most MP4 formats
- Slower for larger files than native libraies
- There is little support of encoding

3. JavaCV

Pros:

- Complete access to the robust and adaptable capabilities of Opencv AND ffMPEG
- Kept up to date
- Great for tasks involving computer vision and other complex processing

Cons:

- Native binaries are needed
- Greater learning curve because of its complexity
- Maven has more dependencies to handle