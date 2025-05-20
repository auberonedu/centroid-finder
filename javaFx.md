The goal is to use Java Fx to intake a video, be able to evaluate the size of the salamender, derive enough infromation to produce a x and y coordinates of the largest salamender at any given time fora duration of the video,

- This should include information on how to read files with JavaFX
https://openjfx.io/javadoc/21/javafx.media/javafx/scene/media/Media.html

What we actually need is to ability to analyze each video, frame by frame to be able to disect the neccesary infromation. Not sure if JavaFX is good for that but it seems OpenCV can, 

https://www.reddit.com/r/learnpython/comments/15srvwo/how_is_a_video_file_is_read_frame_by_frame/


https://www.geeksforgeeks.org/python-program-extract-frames-using-opencv/

https://stackoverflow.com/questions/33650974/opencv-python-read-specific-frame-using-videocapture


Seems videoCapture() in openCV might be the way? 

import cv2
cap = cv2.VideoCapture(videopath)
cap.set(cv2.CAP_PROP_POS_FRAMES, frame_number-1)
res, frame = cap.read()

frame_number is an integer in the range 0 to the number of frames in the video.
Notice: you should set frame_number-1 to force reading frame frame_number. It's not documented well but that is how the VideoCapture module behaves.

One may obtain amount of frames by:

amount_of_frames = cap.get(cv2.CAP_PROP_FRAME_COUNT)


Something like to word above for inspiration. From the stackOverFlow

