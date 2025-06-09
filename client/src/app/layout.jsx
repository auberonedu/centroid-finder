import NavBar from "./components/NavBar";
import { Container, Box } from "@mui/material";
import "./globals.css";

export const metadata = {
  title: "Salamander Tracker",
  description: "Track centroid positions in real time",
};

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
