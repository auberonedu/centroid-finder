1. Review the Salamander API

Understand the expected inputs/outputs.

Identify required endpoints (e.g. start job, get status, get results).

2. Set Up Express Structure

Create a router for defining endpoints.

Create controller functions for route logic.

Follow a traditional Express layout (router + controller separation).

3. Plan Job Tracking Strategy

Pass a jobId as an argument when calling the Java JAR.

Have the JAR output results (e.g. CSV) named with the jobId.

4. Implement Job Status Tracking

Avoid using environment variables for tracking status (they’re not shared across processes).

Use a shared text/JSON file per job to mark status as "in-progress", "finished", etc.

5. Continuously Test with Postman

Create and share a set of Postman requests to simulate API calls.

Use them for both input testing and validating outputs as you build.