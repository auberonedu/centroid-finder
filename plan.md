# Classes Architecture

- Class that handles processing how many frames there are in a video
- Class that handles checking each frame to find the largest Centroid - Part of Original Centroid Project
	- Will also return if there is no centroid found
- Class that processes coordinates of the largest Centroid - Part of Original Centroid Project
- Class that finds the frames per second of the video
- Class that checks if the centroid is staying in the same position within 'X' amount of time and the graph doesn't need to update. When it does move, we can erase visited standpoint in case it goes back to that area again.
- Class that chooses what the centroid color is