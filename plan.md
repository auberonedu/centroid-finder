Liban 5.19

The plan

1 Find the right video library (openCV)
-   It needs to be able to process videos frame by frame
-   We need to be able to process our logic on each frame

1.1 DOWNLOAD OPENCV 
- 

2 Take the video in and process it in the main file - This will also contain a way to pass in command line args such as java -jar videoprocessor.jar inputPath outputCsv targetColor threshold 

-   While doing this
    -   We are reading in the file from VideoProcessor and then pass the video as a whole into Video Analyzer
    -   Video Analyzer than breaks it down into buffered Images which we can than process as we normaly did for pt.1 of the centroid proj. 
        -https://stackoverflow.com/questions/15670933/opencv-java-load-image-to-gui 
        
2.1 Pass it along to a helper class that will than process it down frame by frame
2.2 Pass it along to another helper class that will run the logic to analyze the biggest salamender then produce the infromation of "The first column will be the number of seconds since the beginning of the video, and the second and third columns will be the x and y coordinates of the largest found centroid at that time. " all to a csv file

3. Done? 


What Mat contains:
A Mat object contains:

Width and height (rows and columns)

Number of channels (e.g. 1 for grayscale, 3 for color)

Pixel data (stored as a byte array internally)

Type (e.g. CV_8UC3 for 8-bit unsigned 3-channel image)


How was a color selected- I took a screen shot and went to a color identifier site - 5d0213 
The threshold was selected at random to be honest. Felt like 60 was fair. I tested by running the programming and seeing how often it would default or use the background as the targeted item in the video. Didn't happen to often so i am happy with 60. 

Final Command

java -jar target/centroidfinder-1.0-SNAPSHOT-jar-with-dependencies.jar ensantina.mp4 videoResults.csv 5d0213 60
