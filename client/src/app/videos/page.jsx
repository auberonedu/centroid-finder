"use client";
import { useEffect, useState } from "react";
import { Typography, List, CircularProgress, Paper, Box } from "@mui/material";
import VideoItem from "./VideoItem"; // adjust the path if needed

export default function VideoChooserPage() {
  const [videos, setVideos] = useState([]);

  useEffect(() => {
    const fetchVideos = async () => {
      try {
        const res = await fetch("http://localhost:3001/videos");
        if (!res.ok) throw new Error("Server response not OK");
        const data = await res.json();
        setVideos(data.videos);
      } catch (err) {
        console.error("Error fetching videos:", err.message);
        setTimeout(fetchVideos, 1000); // retry after 1 second
      }
    };

    fetchVideos();
  }, []);

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        textAlign: "center",
        mt: 6,
      }}
    >
      <Typography variant="h3" gutterBottom>
        Video Chooser Page
      </Typography>

      <Typography variant="body1" sx={{ mb: 4 }}>
        Please select the video you want to process from the list below:
      </Typography>

      {videos.length === 0 ? (
        <CircularProgress />
      ) : (
        <Paper
          elevation={3}
          sx={{
            maxHeight: 400,
            overflowY: "auto",
            width: "100%",
            maxWidth: 600,
            padding: 2,
          }}
        >
          <List>
            {videos.map((video) => (
              <VideoItem key={video.name} video={video} />
            ))}
          </List>
        </Paper>
      )}
    </Box>
  );
}