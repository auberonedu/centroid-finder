"use client";
import { useEffect, useState } from "react";
import { Box, List, Button, CircularProgress, Typography } from "@mui/material";
import VideoItem from "../components/VideoItem";

const PAGE_SIZE = 10;

export default function VideoList() {
  const [videos, setVideos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);

  useEffect(() => {
    fetch("http://localhost:3001/videos")
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch videos");
        return res.json();
      })
      .then((data) => {
        setVideos(data.videos);
        setError(null);
      })
      .catch((err) => {
        console.error(err);
        setError("Failed to load video list");
      })
      .finally(() => setLoading(false));
  }, []);

  const handleNext = () => setPage((p) => p + 1);
  const handlePrev = () => setPage((p) => Math.max(0, p - 1));

  const startIndex = page * PAGE_SIZE;
  const paginatedVideos = videos.slice(startIndex, startIndex + PAGE_SIZE);

  if (loading) return <CircularProgress />;
  if (error) return <Typography color="error">{error}</Typography>;

  return (
    <Box>
      <List>
        {paginatedVideos.map((video) => (
          <VideoItem key={video.id} video={video} />
        ))}
      </List>
      <Box display="flex" justifyContent="space-between" mt={2}>
        <Button disabled={page === 0} onClick={handlePrev}>Previous</Button>
        <Button disabled={startIndex + PAGE_SIZE >= videos.length} onClick={handleNext}>
          Next
        </Button>
      </Box>
    </Box>
  );
}