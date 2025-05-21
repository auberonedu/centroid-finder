Plan

Overall we want the server to handle requests to Salamander API. When a POST request is made to the server it should process the video by using the jar made previously. We also want the server to track status of jobs (Deciding whether to use database or filesystem). We will also want to be able to return csv file with the information we get from processing the video and maybe return the beginning frame for users.

We will achieve this by building out routes for each task that we would like our server to be able to do, such as take POST requests, run the jar for videos from the request, track jobs, and return output.

After we build the routes we will do some testing to make sure the routes function as we intended them to do. 