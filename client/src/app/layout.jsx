"use client";

import NavBar from "./components/NavBar"; // adjust if path is different
import { Container, Box } from "@mui/material";
import "./globals.css"; // keep this if it exists

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body>
        <NavBar />
        <Container maxWidth="md">
          <Box sx={{ paddingTop: 4 }}>
            {children}
          </Box>
        </Container>
      </body>
    </html>
  );
}
