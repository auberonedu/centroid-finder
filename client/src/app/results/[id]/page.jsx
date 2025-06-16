"use client";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import {
  Box,
  Typography,
  CircularProgress,
  LinearProgress,
  Alert,
  Button,
} from "@mui/material";

export default function ResultsPage() {
  const { id: jobId } = useParams();
  const [status, setStatus] = useState("loading");
  const [outputUrl, setOutputUrl] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!jobId) return;

    const pollInterval = setInterval(async () => {
      try {
        const res = await fetch(`http://localhost:3001/videos/status/${jobId}`);
        if (!res.ok) throw new Error("Failed to fetch status");
        const data = await res.json();

        if (data.status === "completed") {
          clearInterval(pollInterval);
          setStatus("completed");
          setOutputUrl(`http://localhost:3001/output/${jobId}.csv`);
        } else if (data.status === "failed") {
          clearInterval(pollInterval);
          setStatus("failed");
          setError(data.error || "Unknown error occurred.");
        } else {
          setStatus("processing");
        }
      } catch (err) {
        clearInterval(pollInterval);
        setStatus("failed");
        setError(err.message || "Unknown error occurred.");
      }
    }, 1000);

    return () => clearInterval(pollInterval);
  }, [jobId]);

  return (
    <Box sx={{ p: 4, textAlign: "center" }}>
      <Typography variant="h4" gutterBottom>
        Job Status: {jobId}
      </Typography>

      {status === "loading" && <CircularProgress />}
      {status === "processing" && (
        <>
          <Typography variant="body1" sx={{ mb: 2 }}>
            Your video is being processed...
          </Typography>
          <LinearProgress />
        </>
      )}
      {status === "completed" && (
        <>
          <Typography variant="h6" sx={{ mb: 2 }}>
            ✅ Video processing complete!
          </Typography>
          <Button
            variant="contained"
            color="success"
            href={outputUrl}
            download
            sx={{ mt: 2 }}
          >
            ⬇️ Download CSV
          </Button>
          <Button
            variant="outlined"
            sx={{ mt: 2, ml: 2 }}
            href="/videos/history"
          >
            📁 View All CSVs
          </Button>
        </>
      )}
      {status === "failed" && (
        <Alert severity="error" sx={{ mt: 2 }}>
          ❌ Processing failed: {error}
        </Alert>
      )}

      <Button
        variant="outlined"
        sx={{ mt: 4 }}
        onClick={() => (window.location.href = "/videos")}
      >
        🔁 Process Another Video
      </Button>
    </Box>
  );
}