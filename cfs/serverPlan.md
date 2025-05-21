## What we understand of the API
- It looks like the API will provide us the parameters required for our front end development.
- It will tell us if there are errors using the error codes, we can use this on console.log or console.error as we develop.
- All the parameters gives us a description that helps us understand the context and use of them.
- The job ID may be useful, especially to stop duplicate jobs. So the same video won't be processed more than once.
- The query will be useful when we create the option to choose the video, we can make test cases to make sure that's being processed.

## Plan
- Application accepts video processing information and job request.
- Runs the API through the JAR.
- Return results asynchronously.
- Host the results for ease of access.
- Tracks the job state nad evn configuration
### Express
- Server 