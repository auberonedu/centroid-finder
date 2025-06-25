"use client";
import { useState, useEffect } from "react";
import { Box, Button, Typography, LinearProgress } from "@mui/material";
import CompletedJobs from "./CompletedJobs";
import withVideoProcessing from "./withVideoProcessing";

const StartProcess = ({
  filename,
  color,
  threshold,
  status,
  error,
  jobId,
  start,
  reset
}) => {
  const [completedJobs, setCompletedJobs] = useState([]);
  const [jobError, setJobError] = useState("");

  // Fetch completed jobs initially
  useEffect(() => {
    const fetchCompleted = async () => {
      try {
        const res = await fetch("http://localhost:3000/api/completed");
        if (!res.ok) throw new Error("Failed to fetch completed jobs");
        const data = await res.json();
        setCompletedJobs(data);
      } catch (err) {
        console.error("Error loading completed jobs:", err);
      }
    };

    fetchCompleted();
  }, []);

  // Poll job status
  useEffect(() => {
    if (!jobId) return;

    const interval = setInterval(async () => {
      try {
        const res = await fetch(`http://localhost:3000/process/${jobId}/status`);
        const data = await res.json();

        if (data.status === "done") {
          const newJob = { jobId, filename };
          setCompletedJobs((prev) => {
            if (prev.some((job) => job.jobId === jobId)) return prev;
            return [...prev, newJob];
          });
          clearInterval(interval);
        } else if (data.status === "error") {
          setJobError(data.error || "An unknown error occurred.");
          reset(); // <-- allow retry by resetting HOC state
          clearInterval(interval);
        }
      } catch (err) {
        console.error("Failed to check job status:", err);
        setJobError("Network error while checking job status.");
        reset(); // <-- reset on fetch failure
        clearInterval(interval);
      }
    }, 2000);

    return () => clearInterval(interval);
  }, [jobId, filename, reset]);

  // Delete a single job
  const handleDeleteJob = async (jobIdToDelete) => {
    try {
      const res = await fetch(`http://localhost:3000/api/completed/${jobIdToDelete}`, {
        method: "DELETE",
      });
      if (!res.ok) throw new Error("Failed to delete job");
      setCompletedJobs((prev) => prev.filter((job) => job.jobId !== jobIdToDelete));
    } catch (err) {
      console.error("Error deleting job:", err);
    }
  };

  // Clear all jobs
  const handleClearAll = async () => {
    const confirmed = window.confirm(
      "Are you sure you want to delete all completed jobs? This will permanently remove all CSV files."
    );
    if (!confirmed) return;

    try {
      const res = await fetch("http://localhost:3000/api/completed", {
        method: "DELETE",
      });
      if (!res.ok) throw new Error("Failed to clear jobs");
      setCompletedJobs([]);
    } catch (err) {
      console.error("Error clearing jobs:", err);
    }
  };

  return (
    <Box sx={{ marginTop: 6, textAlign: "center" }}>
      {/* Start Button */}
      <Button
        variant="contained"
        onClick={() => {
          setJobError(""); // clear old errors
          start(filename, color, threshold);
        }}
        disabled={status === "processing"}
        sx={{ backgroundColor: "lightblue", color: "black" }}
      >
        {status === "processing" ? "Processing..." : "Start Process"}
      </Button>

      {/* Loading Bar */}
      {status === "processing" && !jobError && (
        <Box sx={{ width: "100%", mt: 2 }}>
          <LinearProgress />
        </Box>
      )}

      {/* Error from start() */}
      {error && (
        <Typography sx={{ mt: 2, color: "red" }}>{error}</Typography>
      )}

      {/* Error from polling */}
      {jobError && (
        <Typography sx={{ mt: 2, color: "red" }}>
          ⚠️ Job Failed: {jobError}
        </Typography>
      )}

      {/* Success */}
      {status === "done" && (
        <Box sx={{ mt: 2 }}>
          <Typography>✅ Process complete!</Typography>
        </Box>
      )}

      {/* Completed Jobs */}
      <CompletedJobs
        jobs={completedJobs}
        onDelete={handleDeleteJob}
        onClearAll={handleClearAll}
      />
    </Box>
  );
};

export default withVideoProcessing(StartProcess);
