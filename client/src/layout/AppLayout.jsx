import { AppBar, Toolbar, Typography, Button, Box } from "@mui/material";
import Link from "next/link";

export default function AppLayout({ children }) {
  return (
    <>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            Salamander Tracker
          </Typography>
          <Button color="inherit" component={Link} href="/videos">
            Video List
          </Button>
        </Toolbar>
      </AppBar>
      <Box sx={{ padding: 4 }}>{children}</Box>
    </>
  );
}
