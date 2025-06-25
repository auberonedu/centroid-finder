import { useState, useEffect } from 'react';
import { Alert } from '@mui/material';

// HOC for video processing
const withVideoProcessing = (WrappedComponent) => {
  return function WithVideoProcessingComponent(props) {
    const [status, setStatus] = useState("idle");
    const [error, setError] = useState("");
    const [jobId, setJobId] = useState(null);

    // Start video processing
    const start = async (filename, color, threshold) => {
      setError("");
      setStatus("processing");

      try {
        const res = await fetch(
          `http://localhost:3000/process/${filename}?targetColor=${color.slice(1)}&threshold=${threshold}`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        if (!res.ok) {
          const { error } = await res.json();
          throw new Error(error);
        }

        const data = await res.json();
        setJobId(data.jobId);
      } catch (err) {
        setError(err.message);
        setStatus("idle");
      }
    };

    // Poll for job status once jobId is set
    useEffect(() => {
      if (!jobId) return;

      const interval = setInterval(async () => {
        try {
          const res = await fetch(`http://localhost:3000/process/${jobId}/status`);
          const data = await res.json();

          if (data.status === "done") {
            setStatus("done");
            clearInterval(interval);
          }
        } catch (err) {
          console.error("Polling failed:", err);
          setError("Failed to check job status");
          setStatus("idle");
          clearInterval(interval);
        }
      }, 2000);

      return () => clearInterval(interval);
    }, [jobId]);

    return (
      <>
        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}
        <WrappedComponent
          {...props}
          status={status}
          error={error}
          jobId={jobId}
          start={start}
        />
      </>
    );
  };
};

export default withVideoProcessing;
