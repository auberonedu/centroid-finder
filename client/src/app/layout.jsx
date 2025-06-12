import NavBar from "./components/NavBar";
import Head from "next/head";
import { Container, Box } from "@mui/material";
import ClientLayout from "./components/ClientLayout";
import "./globals.css";

export const metadata = {
  title: "Salamander Tracker",
  description: "Track centroid positions in real time",
  icons: {
    icon: "/favicon.ico",
  },
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <Head>
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <body>
        <ClientLayout>
          <Container maxWidth="md">
            <Box sx={{ paddingTop: 4 }}>{children}</Box>
          </Container>
        </ClientLayout>
      </body>
    </html>
  );
}
