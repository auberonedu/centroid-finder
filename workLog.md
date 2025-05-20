Starting this worklog to keep track of things - Liban 


I was able to build the maven project following the project structure from the mavenValidate professor Auberon highlighted. 

Used AI to learn how to convert the console command into maven 


javac -cp lib/junit-platform-console-standalone-1.12.0.jar src/*.java && java -cp src ImageSummaryApp sampleInput/squares.jpg FFA200 164

TO 

mvn exec:java -Dexec.args="sampleInput/squares.jpg FFA200 164"


Video assigment due on 5/19

1 - Download OpenCV 
https://github.com/opencv/opencv/releases
mvn install:install-file -Dfile="C:/Users/liban/Documents/opencv/build/java/opencv-4110.jar" -DgroupId=org.opencv -DartifactId=opencv -Dversion=4.11.0 -Dpackaging=jar
