"use client";
import { useState, useEffect } from "react";
import { Box, Button, Typography, LinearProgress } from "@mui/material";
import CompletedJobs from "./CompletedJobs";
import withVideoProcessing from "./withVideoProcessing";

const StartProcess = ({ filename, color, threshold, status, error, jobId, start }) => {
  const [completedJobs, setCompletedJobs] = useState([]);
  const [jobError, setJobError] = useState("");

  // Fetch completed jobs on initial load
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

  // Poll job status if a job is active
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
          clearInterval(interval);
        }
      } catch (err) {
        console.error("Failed to check job status:", err);
        setJobError("Network error while checking job status.");
        clearInterval(interval);
      }
    }, 2000);

    return () => clearInterval(interval);
  }, [jobId, filename]);

  // Delete a single job from the server
  const handleDeleteJob = async (jobIdToDelete) => {
    try {
      const res = await fetch(`http://localhost:3000/api/completed/${jobIdToDelete}`, {
        method: "DELETE",
      });

      if (!res.ok) {
        throw new Error("Failed to delete job from server");
      }

      setCompletedJobs((prev) => prev.filter((job) => job.jobId !== jobIdToDelete));
    } catch (err) {
      console.error("Error deleting job:", err);
    }
  };

  // Clear all jobs after confirmation
  const handleClearAll = async () => {
    const confirmed = window.confirm(
      "Are you sure you want to delete all completed jobs? This will permanently remove all CSV files."
    );

    if (!confirmed) return;

    try {
      const res = await fetch("http://localhost:3000/api/completed", {
        method: "DELETE",
      });

      if (!res.ok) {
        throw new Error("Failed to clear completed jobs on server");
      }

      setCompletedJobs([]);
    } catch (err) {
      console.error("Error clearing completed jobs:", err);
    }
  };

  return (
    <Box sx={{ marginTop: 6, textAlign: "center" }}>
      {/* Start Processing Button */}
      <Button
        variant="contained"
        onClick={() => {
          setJobError(""); // clear previous error
          start(filename, color, threshold);
        }}
        disabled={status === "processing"}
        sx={{ backgroundColor: "lightblue", color: "black" }}
      >
        {status === "processing" ? "Processing..." : "Start Process"}
      </Button>

      {/* Loading Indicator */}
      {status === "processing" && (
        <Box sx={{ width: "100%", mt: 2 }}>
          <LinearProgress />
        </Box>
      )}

      {/* Error Message from withVideoProcessing */}
      {error && (
        <Typography sx={{ mt: 2, color: "red" }}>{error}</Typography>
      )}

      {/* Job Failure Message from polling */}
      {jobError && (
        <Typography sx={{ mt: 2, color: "red" }}>
          ⚠️ Job Failed: {jobError}
        </Typography>
      )}

      {/* Success Message */}
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
