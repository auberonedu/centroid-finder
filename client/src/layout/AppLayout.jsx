import { Container, Box } from "@mui/material";

export default function AppLayout({ children }) {
  return (
    <Container maxWidth="md">
      <Box sx={{ paddingTop: 4 }}>{children}</Box>
    </Container>
  );
}
