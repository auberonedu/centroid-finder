"use client";
import { useEffect, useState } from "react";
import {
  Box,
  Typography,
  List,
  ListItem,
  ListItemText,
  Divider,
} from "@mui/material";

export default function CsvHistoryPage() {
  const [csvList, setCsvList] = useState([]);

  useEffect(() => {
    fetch("http://localhost:3001/videos/status")
      .then((res) => res.json())
      .then((data) => {
        if (Array.isArray(data.csvFiles)) {
          setCsvList(data.csvFiles.sort((a, b) => b.localeCompare(a)));
        }
      })
      .catch((err) => {
        console.error("Error fetching CSV list:", err);
      });
  }, []);

  return (
    <Box sx={{ p: 4 }}>
      <Typography variant="h4" gutterBottom>
        🗂 All Completed CSVs
      </Typography>
      <Divider sx={{ my: 2 }} />
      {csvList.length === 0 ? (
        <Typography variant="body2" color="text.secondary">
          No past results found.
        </Typography>
      ) : (
        <List>
          {csvList.map((file) => (
            <ListItem key={file}>
              <ListItemText
                primary={
                  <a
                    href={`http://localhost:3001/output/${file}`}
                    target="_blank"
                    rel="noopener noreferrer"
                    download
                  >
                    {file}
                  </a>
                }
                secondary={`Click to download ${file}`}
              />
            </ListItem>
          ))}
        </List>
      )}
    </Box>
  );
}