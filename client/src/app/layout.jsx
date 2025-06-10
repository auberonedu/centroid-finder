
import { Container, Box } from "@mui/material";
import ClientLayout from "./components/ClientLayout";
import "./globals.css";

export const metadata = {
  title: "Salamander",
  description: "Tracks salamander across frames",
  icons: {
    icon: "/favicon.ico", 
  },
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body>
        <ClientLayout>
          <Container maxWidth="md">
            <Box sx={{ paddingTop: 4 }}>
              {children}
            </Box>
          </Container>
        </ClientLayout>
      </body>
    </html>
  );
}