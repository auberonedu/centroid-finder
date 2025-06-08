//make lower level comp for video cards

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
  Stack,
} from "@mui/material";

export default function VideoChooserPage() {
  const [videos, setVideos] = useState(null);

  useEffect(() => {
    const fetchVideos = async () => {
      try {
        const res = await fetch("http://localhost:3001/videos");
        const data = await res.json();
        setVideos(data.videos);
      } catch (err) {
        console.error("Error fetching videos:", err);
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

      {!videos ? (
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
              <ListItem
                key={video.name}
                button
                component={Link}
                href={`/preview?filename=${encodeURIComponent(video.name)}`}
                sx={{
                  justifyContent: "center",
                  textAlign: "center",
                }}
              >
                <ListItemText
                  primary={video.name}
                  secondary={`Duration: ${Math.round(video.duration)}s`}
                />
              </ListItem>
            ))}
          </List>
        </Paper>
      )}
    </Box>
  );
}
