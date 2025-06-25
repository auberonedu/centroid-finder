import { useState } from "react";

const withVideoProcessing = (WrappedComponent) => {
  return function WithVideoProcessingComponent(props) {
    const [status, setStatus] = useState("idle");
    const [error, setError] = useState("");
    const [jobId, setJobId] = useState(null);

    const start = async (filename, color, threshold) => {
      setError("");
      setStatus("processing");

      try {
        const res = await fetch(
          `http://localhost:3000/process/${filename}?targetColor=${color.slice(1)}&threshold=${threshold}`,
          {
            method: "POST",
            headers: { "Content-Type": "application/json" },
          }
        );

        if (!res.ok) {
          const errData = await res.json();
          throw new Error(errData.error || "Failed to start job");
        }

        const { jobId } = await res.json();
        setJobId(jobId);
      } catch (err) {
        setError(err.message);
        setStatus("idle");
      }
    };

    // ✅ NEW: Reset function
    const reset = () => {
      setStatus("idle");
      setError("");
      setJobId(null);
    };

    return (
      <WrappedComponent
        {...props}
        status={status}
        error={error}
        jobId={jobId}
        start={start}
        reset={reset} // ⬅️ pass to wrapped component
      />
    );
  };
};

export default withVideoProcessing;
