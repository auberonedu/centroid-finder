// pages/preview.js
import { useEffect, useRef, useState } from "react";
import { useRouter } from "next/router";
import {
  Typography,
  Box,
  Button,
  Slider,
  Paper,
  Stack,
  Snackbar,
  Alert,
} from "@mui/material";

export default function VideoPreviewPage() {
  const router = useRouter();
  const { filename } = router.query;

  const [selectedColor, setSelectedColor] = useState(null);
  const [threshold, setThreshold] = useState(50);
  const [timeFrame, setTimeFrame] = useState("all");
  const [selectingColor, setSelectingColor] = useState(false);

  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMsg, setSnackbarMsg] = useState("");

  const canvasRef = useRef(null);
  const binaryCanvasRef = useRef(null);

  const staticFrameSrc = filename
    ? `http://localhost:3001/frames/${encodeURIComponent(filename)}.jpg`
    : "";

  // Draw static frame and sync both canvases
  useEffect(() => {
    if (!staticFrameSrc || !canvasRef.current || !binaryCanvasRef.current)
      return;

    const canvas = canvasRef.current;
    const binaryCanvas = binaryCanvasRef.current;
    const ctx = canvas.getContext("2d");

    console.log("📸 staticFrameSrc set to:", staticFrameSrc);

    const img = new Image();
    img.crossOrigin = "anonymous";
    img.src = staticFrameSrc;

    img.onload = () => {
      console.log("✅ Image loaded for canvas");
      const { width, height } = img;

      canvas.width = width;
      canvas.height = height;
      binaryCanvas.width = width;
      binaryCanvas.height = height;

      ctx.drawImage(img, 0, 0);
    };

    img.onerror = (err) => {
      console.error("❌ Failed to load image:", staticFrameSrc, err);
    };
  }, [staticFrameSrc]);

  // Handle color selection
  const handleCanvasClick = (e) => {
    if (!selectingColor) return;

    const canvas = canvasRef.current;
    const rect = canvas.getBoundingClientRect();
    const ctx = canvas.getContext("2d");

    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;

    const pixel = ctx.getImageData(x, y, 1, 1).data;
    const [r, g, b] = pixel;
    const hex = `#${[r, g, b]
      .map((val) => val.toString(16).padStart(2, "0"))
      .join("")}`;

    setSelectedColor({ r, g, b, hex });
    setSelectingColor(false);
  };

  // Binarization preview
  useEffect(() => {
    if (!selectedColor || !staticFrameSrc || !binaryCanvasRef.current) return;

    const canvas = binaryCanvasRef.current;
    const ctx = canvas.getContext("2d");

    const img = new Image();
    img.crossOrigin = "anonymous";
    img.src = staticFrameSrc;

    img.onload = () => {
      const { width, height } = img;

      const tempCanvas = document.createElement("canvas");
      const tempCtx = tempCanvas.getContext("2d");
      tempCanvas.width = width;
      tempCanvas.height = height;
      tempCtx.drawImage(img, 0, 0);

      const imageData = tempCtx.getImageData(0, 0, width, height);
      const data = imageData.data;

      for (let i = 0; i < data.length; i += 4) {
        const r = data[i];
        const g = data[i + 1];
        const b = data[i + 2];

        const dist = Math.sqrt(
          (r - selectedColor.r) ** 2 +
            (g - selectedColor.g) ** 2 +
            (b - selectedColor.b) ** 2
        );

        const binary = dist <= threshold ? 0 : 255;
        data[i] = data[i + 1] = data[i + 2] = binary;
      }

      ctx.putImageData(imageData, 0, 0);
    };
  }, [selectedColor, threshold, staticFrameSrc]);

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        mt: 4,
      }}
    >
      <Button
        onClick={() => router.push("/videos")}
        sx={{ alignSelf: "flex-start", mb: 2 }}
      >
        ⬅ Back to Video List
      </Button>

      <Typography variant="h3" gutterBottom>
        Video Preview Page
      </Typography>

      <Paper elevation={3} sx={{ p: 2, mt: 2, mb: 2 }}>
        <canvas
          ref={canvasRef}
          width={600}
          height={400} // Can be any default, will update after load
          onClick={handleCanvasClick}
          style={{
            width: "100%",
            maxWidth: 600,
            borderRadius: 8,
            cursor: selectingColor ? "crosshair" : "default",
            border: "1px solid #ccc", // Optional: add border to visualize canvas bounds
          }}
        />
      </Paper>

      <Stack direction="row" spacing={2} alignItems="center" sx={{ mb: 3 }}>
        <Button variant="contained" onClick={() => setSelectingColor(true)}>
          Target Color Selection
        </Button>
        <Box
          sx={{
            width: 40,
            height: 40,
            backgroundColor: selectedColor ? selectedColor.hex : "#ccc",
            border: "1px solid #999",
            borderRadius: 2,
          }}
        />
      </Stack>

      <Box sx={{ width: 300, mb: 4 }}>
        <Typography gutterBottom>
          Threshold: <strong>{threshold}</strong>
        </Typography>
        <Slider
          min={0}
          max={150}
          value={threshold}
          onChange={(e, newVal) => setThreshold(newVal)}
        />
      </Box>

      <Paper elevation={2} sx={{ width: "100%", maxWidth: 600, mb: 4 }}>
        <canvas
          ref={binaryCanvasRef}
          width={600}
          height={400}
          style={{
            width: "100%",
            maxWidth: 600,
            borderRadius: 8,
            display: "block",
            border: "1px solid #ccc",
          }}
        />
      </Paper>

      <Stack direction="row" spacing={2} sx={{ mb: 4 }}>
        {["all", "0.5s", "1.0s", "10s"].map((label) => (
          <Button
            key={label}
            variant={timeFrame === label ? "contained" : "outlined"}
            onClick={() => setTimeFrame(label)}
          >
            {label}
          </Button>
        ))}
      </Stack>

      <Button
        variant="contained"
        size="large"
        sx={{ mt: 2 }}
        disabled={!selectedColor}
        onClick={async () => {
          if (!selectedColor || !threshold || !timeFrame || !filename) {
            setSnackbarMsg("Missing required fields!");
            setSnackbarOpen(true);
            return;
          }

          const payload = {
            filename,
            color: {
              r: selectedColor.r,
              g: selectedColor.g,
              b: selectedColor.b,
            },
            threshold,
            interval: timeFrame,
          };

          try {
            const res = await fetch("http://localhost:3001/process", {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(payload),
            });

            if (!res.ok) throw new Error("Failed to process video");

            const result = await res.json();
            router.push(`/results/${result.jobId}`);
          } catch (err) {
            console.error(err);
            setSnackbarMsg("There was a problem submitting the video.");
            setSnackbarOpen(true);
          }
        }}
      >
        Process Video With These Settings
      </Button>

      <Snackbar
        open={snackbarOpen}
        autoHideDuration={4000}
        onClose={() => setSnackbarOpen(false)}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
      >
        <Alert
          onClose={() => setSnackbarOpen(false)}
          severity={snackbarMsg.includes("problem") ? "error" : "info"}
          sx={{ width: "100%" }}
        >
          {snackbarMsg}
        </Alert>
      </Snackbar>
    </Box>
  );
}
