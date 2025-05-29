import { useEffect, useState } from "react";
import Link from "next/link";
import {
  Typography,
  List,
  ListItem,
  ListItemText,
  Paper,
  CircularProgress,
  Box,
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
    <Paper sx={{ padding: 4 }}>
      <Typography variant="h4" gutterBottom>
        Video Chooser Page
      </Typography>
      <Typography variant="body1" gutterBottom>
        Please select the video you want to process from the list below:
      </Typography>

      {!videos ? (
        <CircularProgress />
      ) : (
        <Box sx={{ maxHeight: 400, overflowY: "auto", mt: 2 }}>
          <List>
            {videos.map((video) => (
              <ListItem
                button
                key={video.name}
                component={Link}
                href={`/preview/${encodeURIComponent(video.name)}`}
              >
                <ListItemText
                  primary={video.name}
                  secondary={`Duration: ${Math.round(
                    video.duration
                  )}s • Created: ${new Date(
                    video.createdAt
                  ).toLocaleString()} • Modified: ${new Date(
                    video.modifiedAt
                  ).toLocaleString()}`}
                />
              </ListItem>
            ))}
          </List>
        </Box>
      )}
    </Paper>
  );
}
