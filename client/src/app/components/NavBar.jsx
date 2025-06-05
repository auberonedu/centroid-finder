"use client";
import Link from "next/link";
import { AppBar, Toolbar, Button, Box } from "@mui/material";

export default function NavBar() {
  return (
    <AppBar position="static">
      <Toolbar sx={{ display: "flex", justifyContent: "space-around" }}>
        <Button color="inherit" component={Link} href="/videos">
          Video List
        </Button>
        <Button color="inherit" component={Link} href="/preview">
          Preview Page
        </Button>
        <Button color="inherit" component={Link} href="/results/example-job-id">
          Results (Demo)
        </Button>
      </Toolbar>
    </AppBar>
  );
}
