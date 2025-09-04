"use client";
import { useEffect, useRef, useState } from "react";
import { useParams } from "next/navigation";
import { Slider, Container, Box, Typography, Checkbox, TextField} from "@mui/material";
import StartProcess from "./StartProcess";
import { AreaSelector, IArea } from '@bmunozg/react-image-area';

export default function PreviewVideo({ params }) {
  // Get filename from URL
  const { filename } = useParams();
  // States for color, threshold, and areas
  const [color, setColor] = useState("#000000");
  const [threshold, setThreshold] = useState(100);
  const [areas, setAreas] = useState([]);
  const [areaToggle, setAreaToggle] = useState(false);
  const [areaNames, setAreaNames] = useState(['1', '2']);
  // Refs for canvas and image
  const canvasRef = useRef(null);
  const imgRef = useRef(null);

  // Thumbnail URL
  const thumbnailUrl = `http://localhost:3000/thumbnail/${encodeURIComponent(
    filename
  )}`;

  // Load thumbnail image
  useEffect(() => {
    const img = new Image();
    img.crossOrigin = "Anonymous";
    img.src = thumbnailUrl;

    img.onload = () => {
      imgRef.current = img;
      drawBinarized(img, color, threshold);
    };
  }, [thumbnailUrl]);

  // Update binarized image when color/threshold changes
  useEffect(() => {
    if (imgRef.current) {
      drawBinarized(imgRef.current, color, threshold);
    }
  }, [color, threshold]);

  // convert the image to binary based on color and threshold
  const drawBinarized = (img, targetColor, threshold) => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext("2d");

    // Scale image
    const scale = 300 / img.width;
    const width = img.width * scale;
    const height = img.height * scale;

    canvas.width = width;
    canvas.height = height;

    // Draw and process the image
    ctx.drawImage(img, 0, 0, width, height);
    const imageData = ctx.getImageData(0, 0, width, height);
    const data = imageData.data;

    // target color values
    const rT = parseInt(targetColor.slice(1, 3), 16);
    const gT = parseInt(targetColor.slice(3, 5), 16);
    const bT = parseInt(targetColor.slice(5, 7), 16);

    // Process each individual pixel
    for (let i = 0; i < data.length; i += 4) {
      const r = data[i];
      const g = data[i + 1];
      const b = data[i + 2];

      const diff = Math.sqrt((r - rT) ** 2 + (g - gT) ** 2 + (b - bT) ** 2);
      const value = diff < threshold ? 0 : 255;
      data[i] = data[i + 1] = data[i + 2] = value;
    }

    ctx.putImageData(imageData, 0, 0);
  };

  // handle state change of area name
  const handleNameChange = (index, newVal) => {
    setAreaNames(prev => {
      const newNames = [...prev];
      newNames[index] = newVal;
      return newNames;
    })
  }

  // render area name in area selector
  const renderAreaNames = (areaProps) => {
    if (!areaProps.isChanging) {
        return (
            <div key={areaProps.areaNumber}>
                <Typography>{areaNames[areaProps.areaNumber - 1]}</Typography>
            </div>
        );
    }
  };

  // Card style
  const cardStyle = {
    padding: 2,
    borderRadius: 2,
    backgroundColor: "#fff",
    boxShadow: 1,
    textAlign: "center",
    maxWidth: 320,
    flex: "1 1 320px",
  };

  // Original Image thumbnail
  const OgImage = () => {
    return(
    <img
      src={thumbnailUrl}
      alt="Original thumbnail"
      style={{
        width: "300px",
        borderRadius: 8,
        border: "1px solid #ccc",
      }}
    /> )
  }

  return (
    <Container maxWidth="md" sx={{ py: 5, px: 3, bgcolor: "#f9f9f9", borderRadius: 2, boxShadow: 2, marginTop: 2 }}>
      <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center", gap: 5, marginBottom: 2 }}>
        {/* Title */}
        <Typography variant="h4" sx={{ fontWeight: 600, color: "#1976d2" }}>
          Preview: {filename}
        </Typography>

        {/* Preview cards */}
        <Box sx={{ display: "flex", gap: 4, flexWrap: "wrap", justifyContent: "center", width: "100%" }}>
          
          {/* Original image */}
          <Box sx={cardStyle}>
            <Typography variant="h6" gutterBottom>
              Original
            </Typography>
            {areaToggle &&
              <AreaSelector
                areas={areas}
                onChange={(areas) => setAreas(areas)}
                debug
                customAreaRenderer={renderAreaNames}
                maxAreas={2}
                unit="percentage"
              >
                <OgImage />
              </AreaSelector>
            }
            {!areaToggle && <OgImage />}
          </Box>

          {/* Binarized image */}
          <Box sx={cardStyle}>
            <Typography variant="h6" gutterBottom>
              Binarized
            </Typography>
            <canvas
              ref={canvasRef}
              style={{
                width: "300px",
                borderRadius: 8,
                border: "1px solid #ccc",
              }}
            />
          </Box>
        </Box>

        {/* Controls */}
        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            flexWrap: "wrap",
            gap: 2,
            padding: 3,
            borderRadius: 2,
            backgroundColor: "#fff",
            boxShadow: 1,
            justifyContent: "center",
          }}
        >
          {/* Color picker */}
          <Typography>Pick Target Color:</Typography>
          <input
            type="color"
            
            value={color}
            onChange={(e) => setColor(e.target.value)}
            style={{
              width: 40,
              height: 40,
              border: "none",
              borderRadius: 4,
              cursor: "pointer",
            }}
          />
          {/* Threshold slider */}
          <Typography sx={{ marginLeft: 2 }}>Threshold:</Typography>
          <Slider
            value={threshold}
            min={0}
            max={255}
            onChange={(e, newValue) => setThreshold(newValue)}
            sx={{ width: 250 }}
          />

          {/* Area Selector */}
          <Typography sx={{ marginLeft: 2 }}>Set Regions:</Typography>
          <Checkbox
            onChange={(e) => setAreaToggle(prev => !prev)}
          />
          {/* Area Names inputs */}
          {areaToggle && 
            <div>
              {areaNames.map((value, index) => (
                <TextField
                  key={index} 
                  value={value}
                  label={'Region ' + (index + 1) }
                  onChange={(e) => handleNameChange(index, e.target.value)}
                  size="small"
                  sx={{ marginLeft: 2 }}
                />
              ))}
            </div>
          }


        </Box>
      </Box>

      {/* Process button */}
      {areaToggle ? <StartProcess filename={filename} color={color} threshold={threshold} areaValues={areas} areaNames={areaNames} /> :
        <StartProcess filename={filename} color={color} threshold={threshold} areaValues={[]} areaNames={[]} />}
      
    </Container>
  );
}
