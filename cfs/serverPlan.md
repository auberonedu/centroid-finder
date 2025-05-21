## What we understand of the API
- It looks like the API will provide us the parameters required for our front end development.
- It will tell us if there are errors using the error codes, we can use this on console.log or console.error as we develop.
- All the parameters gives us a description that helps us understand the context and use of them.
- The job ID may be useful, especially to stop duplicate jobs. So the same video won't be processed more than once.
- The query will be useful when we create the option to choose the video, we can make test cases to make sure that's being processed.

## Overall Plan
- Application accepts video processing information and job request.
- Runs the API through the JAR.
- Return results asynchronously.
- Host the results for ease of access.
- Tracks the job state nad evn configuration

### Using Express
- Server gets teh `/process-video` path to video saved locally.
- Generate a `jobId` through (UUID).
- `child-process` used to run JAR (in background).
- JAR saves results in `results/`
- Job result will be accessed through the job ID.

### Architecture
```
Client (Postman)
        |
        v
[Express Server (Index.js)]
        |
        |---> [Video Directory (local)] — receives input videos
        |
        |---> [Java JAR via child_process] — processes videos
        |
        |---> [Output Directory (public)] — stores results
        |
        |---> [Job Tracker (in-memory or file-based/db)]
