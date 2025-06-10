"use client";
import NavBar from "./NavBar";

export default function ClientLayout({ children }) {
  return (
    <>
      <NavBar />
      {children}
    </>
  );
}