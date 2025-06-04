# Docker Plan for Salamander Full-Stack Application

## Overall Goal:

The goal is to containerize our **Salamander Full-Stack Application**, with each component running in its own container to follow best OOP practices. These images will be published to GHCR (GitHub Container Registry), so anyone can run the application using `docker compose up` or by pulling individual images and running `docker run`.

## Implementation Strategy:

- **Structure**: The app will be split into **three separate containers**, each built from its own image:
  1. **Frontend** – React application (in its own GitHub repo)
  2. **Backend** – Express.js API
  3. **Java Processor** – A `.jar`-based Java job for video processing

- **Docker Compose** will be used to coordinate and network the containers, both for local development and deployment. This way, services can talk to each other using service names like `backend` and `frontend`.

### What base Docker image will you use?

We’re currently considering the following:
- `node:24-slim` or `node:24-alpine` for the frontend and backend
- `openjdk:25-slim` for the Java processor

### How will you make sure that both Node.js and Java can run?

Since each component is isolated in its own container, we’ll use different base images per component. There’s no need to combine Node and Java in one image as Compose can handle coordination.

### How will you test your Dockerfiles and images?

We’ll:
- Build and run containers locally using `docker compose up`
- Use **Postman** to send requests to API endpoints and validate responses
- Mount local directories as volumes to test input/output behavior for the video processor

### How will you make sure the endpoints are available outside the containers?

We’re still looking into this, but for now we plan to expose the necessary internal ports to the host using `docker-compose.yml`. This will allow services like the frontend and backend to be accessed through `localhost` during development. Internally, the containers will communicate with each other through the Docker network using their service names.

### How will your code know where to access the video/results directory?

We’re looking into using **environment variables** and **volume mounts** to tell the containers where to read/write files.

### How can you make your Docker images small, cacheable, and quick to update?

We're currently looking into this, but for now planning to:
- Choose smaller base images (e.g. `*-alpine`)
- Add `.dockerignore` files to exclude things like `node_modules`, `.git`, and local test files
- Install only production dependencies (`npm ci --omit=dev`)

## Testing and Validation:

We'll use **Postman** and manual testing to:
- Confirm backend routes (like `/upload`, `/status`) respond correctly
- Verify video processing jobs are triggered and handled as expected
- Check that output files are generated in the right directory and match expected format