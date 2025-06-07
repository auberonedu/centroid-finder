// VideoChooserPage.jsx

"use client";
import { useEffect, useState } from "react";
import Link from "next/link";
import {
  Typography,
  List,
  ListItem,
  ListItemText,
  CircularProgress,
  Paper,
  Box,
} from "@mui/material";

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

  console.log("👀 videos array in render:", videos);
  console.log("🎬 video count:", videos?.length);

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

      {/* Optional debug output */}
      {/* <pre>{JSON.stringify(videos, null, 2)}</pre> */}

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
              <ListItem key={video.name} disablePadding>
                <Link
                  href={`/preview?filename=${encodeURIComponent(video.name)}`}
                  style={{ textDecoration: "none", color: "inherit" }}
                >
                  <Box
                    sx={{
                      display: "block",
                      width: "100%",
                      padding: 2,
                      textAlign: "center",
                      "&:hover": {
                        backgroundColor: "#f0f0f0",
                      },
                    }}
                  >
                    <ListItemText
                      primary={video.name}
                      secondary={`Duration: ${Math.round(video.duration)}s`}
                    />
                  </Box>
                </Link>
              </ListItem>
            ))}
          </List>
        </Paper>
      )}
    </Box>
  );
}
