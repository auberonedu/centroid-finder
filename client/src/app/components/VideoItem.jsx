"use client";
import { useState, useRef } from "react";
import {
  Box,
  Typography,
  ListItem,
  ListItemText,
  CircularProgress,
  Avatar,
} from "@mui/material";
import Link from "next/link";

export default function VideoItem({ video }) {
  const [metadata, setMetadata] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const timeoutRef = useRef(null);

  const handleMouseEnter = () => {
    if (metadata || loading) return;

    // Debounce for smoother UX
    timeoutRef.current = setTimeout(async () => {
      setLoading(true);
      try {
        const res = await fetch(
          `http://localhost:3001/videos/file/${encodeURIComponent(video.name)}`
        );
        if (!res.ok) throw new Error("Failed to fetch metadata");
        const data = await res.json();
        setMetadata(data);
      } catch (err) {
        console.error(err);
        setError("Metadata not available");
      } finally {
        setLoading(false);
      }
    }, 300); // delay to avoid instant triggers
  };

  const handleMouseLeave = () => {
    clearTimeout(timeoutRef.current);
  };

  return (
    <ListItem
      disablePadding
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
    >
      <Link
        href={`/preview?filename=${encodeURIComponent(video.name)}`}
        style={{ textDecoration: "none", color: "inherit", width: "100%" }}
      >
        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            gap: 2,
            padding: 2,
            "&:hover": {
              backgroundColor: "#f0f0f0",
            },
          }}
        >
          {/* Thumbnail */}
          {metadata?.thumbnail ? (
            <Avatar
              src={`http://localhost:3001/${metadata.thumbnail}`}
              alt="thumbnail"
              variant="square"
              sx={{ width: 80, height: 45 }}
            />
          ) : (
            <Avatar
              variant="square"
              sx={{
                width: 80,
                height: 45,
                backgroundColor: "#ccc",
                fontSize: 12,
              }}
            >
              {loading ? <CircularProgress size={20} /> : "No Preview"}
            </Avatar>
          )}

          {/* Text Info */}
          <Box sx={{ textAlign: "left", flex: 1 }}>
            <ListItemText
              primary={video.name}
              secondary={
                loading ? (
                  "Loading metadata..."
                ) : error ? (
                  error
                ) : metadata ? (
                  `Duration: ${Math.round(metadata.duration)}s | Created: ${new Date(
                    metadata.createdAt
                  ).toLocaleDateString()}`
                ) : (
                  "Hover to load metadata"
                )
              }
            />
          </Box>
        </Box>
      </Link>
    </ListItem>
  );
}
