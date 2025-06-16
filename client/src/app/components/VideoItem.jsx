"use client";
import { useState } from "react";
import {
  Box,
  Typography,
  ListItem,
  ListItemText,
  Avatar,
} from "@mui/material";
import Link from "next/link";

export default function VideoItem({ video }) {
  const [hovered, setHovered] = useState(false);

  const thumbnailUrl = video.thumbnail
    ? `http://localhost:3001/${video.thumbnail}`
    : null;

  return (
    <ListItem
      disablePadding
      onMouseEnter={() => setHovered(true)}
      onMouseLeave={() => setHovered(false)}
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
          {}
          {thumbnailUrl ? (
            <Avatar
              src={thumbnailUrl}
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
              No Preview
            </Avatar>
          )}

          {}
          <Box sx={{ textAlign: "left", flex: 1 }}>
            <Typography variant="body1">{video.name}</Typography>
            {hovered && (
              <Typography variant="body2" color="text.secondary">
                Duration: {Math.round(video.duration)}s | Created:{" "}
                {new Date(video.createdAt).toLocaleDateString()}
              </Typography>
            )}
          </Box>
        </Box>
      </Link>
    </ListItem>
  );
}