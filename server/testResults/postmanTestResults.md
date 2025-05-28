# Video Controller

## getAllVideos
- Request = GET http://localhost:3000/api/videos
- Result = [
    "Salamander5Seconds.mp4"
    ]

## getVideoThumbnail
- Request = GET http://localhost:3000/thumbnail/Salamander5Seconds.mp4
- Result = 

![Thumbnail Screenshot](./screenshots/thumbnail-response.jpg)

# Process Controller

## startVideoProcessingJob
- Request = POST http://localhost:3000/process/Salamander5Seconds.mp4?targetColor=2D0508&threshold=180
- Result = {
    "jobId": "30b53632-0322-4af7-a872-10bb413539ad"
    } 

## getProcessingJobStatus
- Request = GET http://localhost:3000/process/30b53632-0322-4af7-a872-10bb413539ad/status
- Result 1 = {
    "status": "processing"
    }   
- Result 2 = 