Example of what commands to run to make the project work


RUN THIS: mvn clean compile assembly:single

-   This is a Maven command that:

        clean — Deletes the target/ directory (removes previous builds).

        compile — Compiles your Java source code (converts .java to .class).

        assembly:single — Bundles everything (your compiled .class files + all dependencies) into one .jar file using the Maven Assembly Plugin.

        You get a self-contained JAR file: target/centroidfinder-1.0-SNAPSHOT-jar-with-dependencies.jar

RUN THIS: java -jar target/centroidfinder-1.0-SNAPSHOT-jar-with-dependencies.jar ensantina.mp4 ensantina_tracking.csv 5a020c 60 
          time java -jar target/centroidfinder-1.0-SNAPSHOT-jar-with-dependencies.jar shortTest.mp4 ensantina_tracking.csv 5a020c 60 

-   What it does:
        It runs your main() method (in VideoProcessorApp.java) and passes in four arguments:

        ensantina.mp4 → Input video file path

        ensantina_tracking.csv → Output CSV filename

        5a020c → Target color in hex (RRGGBB)

        60 → Threshold for how close a pixel must be to the target color
