# Docker Plan for Centroid Finder

This `dockerPlan.md` file outlines the strategy used to containerize and run the Centroid Finder project, which includes a Node.js server and a Java-based video processor.

---

## 1. Base Image

We use an Alpine-based Node.js image:

```Dockerfile
FROM node:20-alpine
```

This keeps the image lightweight while still supporting Node.js and npm.

---

## 2. Java Installation

OpenJDK 21 is installed to support the `.jar` video processor:

```Dockerfile
RUN apk add --no-cache openjdk21
```

---

## 3. Working Directory

We set `/app/server` as our working directory to organize server-side code:

```Dockerfile
WORKDIR /app/server
```

---

## 4. Copy Dependencies

Copy `package.json` and install Node.js dependencies:

```Dockerfile
COPY server/package*.json ./
RUN npm install
```

---

## 5. Copy Server and Processor Code

We copy the entire server and relevant parts of the processor project:

```Dockerfile
COPY server/ ./
COPY processor/target/centroid-finder-1.0-SNAPSHOT-jar-with-dependencies.jar /app/processor/
COPY processor/sampleInput /app/sampleInput
COPY processor/sampleOutput /app/sampleOutput
```

---

## 6. Expose Port

The app listens on port 3000:

```Dockerfile
EXPOSE 3000
```

---

## 7. Environment Variables

We define default paths for mounting volumes:

```Dockerfile
ENV VIDEO_DIR=/videos
ENV RESULTS_DIR=/results
ENV JAVA_JAR_PATH=/app/processor/centroid-finder-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## 8. Start Command

The container launches the server using:

```Dockerfile
CMD ["node", "server.js"]
```

---

## 9. Running the Container

To run the container with local folders mounted and environment variables set:

```bash
docker run -p 3000:3000 \
  -v "<ABSOLUTE_PATH>/sampleInput:/videos" \
  -v "<ABSOLUTE_PATH>/sampleOutput:/results" \
  -e video_directory_path=/videos \
  -e output_directory_path=/results \
  -e video_processor_jar_path=/app/processor/centroid-finder-1.0-SNAPSHOT-jar-with-dependencies.jar \
  centroid-finder
```

Replace `<ABSOLUTE_PATH>` with the full path to your project directory.

---

## 10. Notes

* This setup avoids using `.env` file inside the container for flexibility.
* You should rebuild the image (`docker build -t centroid-finder .`) every time the Java JAR or server code changes.