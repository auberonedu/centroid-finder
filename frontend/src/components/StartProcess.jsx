"use client";
import { useState, useEffect } from "react";
import { Box, Button, Typography, LinearProgress } from "@mui/material";
import CompletedJobs from "./CompletedJobs";
import withVideoProcessing from "./withVideoProcessing";

const StartProcess = ({ filename, color, threshold, status, error, jobId, start }) => {
  // Initialize from localStorage on first render
  const [completedJobs, setCompletedJobs] = useState(() => {
    if (typeof window !== "undefined") {
      const saved = localStorage.getItem("completedJobs");
      return saved ? JSON.parse(saved) : [];
    }
    return [];
  });

  useEffect(() => {
    if (!jobId) return;

    const interval = setInterval(async () => {
      try {
        const res = await fetch(`http://localhost:3000/process/${jobId}/status`);
        const data = await res.json();

        if (data.status === "done") {
          const newJob = { jobId, filename };

          setCompletedJobs((prev) => {
            const updated = [...prev, newJob];
            localStorage.setItem("completedJobs", JSON.stringify(updated));
            return updated;
          });

          clearInterval(interval);
        }
      } catch (err) {
        console.error("Failed to check job status:", err);
        clearInterval(interval);
      }
    }, 2000);

    return () => clearInterval(interval);
  }, [jobId, filename]);

  return (
    <Box sx={{ marginTop: 6, textAlign: "center" }}>
      {/* Start Button */}
      <Button
        variant="contained"
        onClick={() => start(filename, color, threshold)}
        disabled={status === "processing"}
        sx={{ backgroundColor: "lightblue", color: "black" }}
      >
        {status === "processing" ? "Processing..." : "Start Process"}
      </Button>

      {/* Progress Indicator */}
      {status === "processing" && (
        <Box sx={{ width: "100%", mt: 2 }}>
          <LinearProgress />
        </Box>
      )}

      {/* Error Message */}
      {error && (
        <Typography sx={{ mt: 2, color: "red" }}>{error}</Typography>
      )}

      {/* Success Message */}
      {status === "done" && (
        <Box sx={{ mt: 2 }}>
          <Typography>✅ Process complete!</Typography>
        </Box>
      )}

      {/* Completed Jobs List */}
      <CompletedJobs jobs={completedJobs} />
    </Box>
  );
};

export default withVideoProcessing(StartProcess);
