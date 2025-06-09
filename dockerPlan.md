## 1. Base Docker Image

We'll use a multi-stage build to keep the image small and efficient:

* Start from an `openjdk` base image (ex, `openjdk:21-jdk-slim`) for Java processing.
* Install Node.js manually using NodeSource or use a base image like `node:20-slim` and install Java into it.

This will allow both Java and Node.js to run in the same container. The slim variants help reduce image size.

## 2. Installing Dependencies

* Copy over the backend Java JAR file and Node.js project files.
* Install Node dependencies using `npm install`.
* Ensure Java is available for executing the `.jar` batch job from the Node API.

## 3. Exposing the API

* The Express app runs on port `3000` inside the container.
* We'll expose port `3000` and map it to `3001` on the host machine using Docker run or Compose.

```Dockerfile
EXPOSE 3000
```

And run the container with:

```bash
docker run -p 3001:3000 my-app
```

## 4. Accessing Files (Video Input & Results)

* Use environment variables to specify the paths to the input videos and result files (ex, `VIDEOS_DIR`, `RESULTS_DIR`).
* Mount host directories into the container with `-v`:

```bash
docker run -p 3001:3000 \
  -v $(pwd)/videos:/app/videos \
  -v $(pwd)/results:/app/results \
  -e VIDEOS_DIR=/app/videos \
  -e RESULTS_DIR=/app/results \
  my-app
```

## 5. Testing the Dockerfile and Image

* Run the container locally using `docker build` and `docker run`.
* Use Postman or browser to test API endpoints like:

  * `http://localhost:3001/videos`
  * `http://localhost:3001/process/:filename`
* Check volume mounts by verifying the files are written to the host `results` folder.

## 6. Optimization Tips

* Use multi-stage builds to separate dependency installation from runtime execution.
* Add a `.dockerignore` to exclude `node_modules`, logs, and local dev files.
* Only copy necessary files.
* Leverage Docker layer caching by ordering Dockerfile instructions from least to most frequently changed.

---
