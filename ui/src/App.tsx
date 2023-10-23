import "./App.css";
import { Routes, Route, Navigate } from "react-router-dom";
import Homepage from "./pages/home";
import Profile from "./pages/profile";
import { CssBaseline } from "@mui/material";
import NavBar from "./components/navigation";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { useEffect } from "react";
import { setBearerToken } from "./integration/instance";

function App() {
  useEffect(() => { // useEffect keeps an eye on data changes.
    setBearerToken(
      "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYWtlLnN1bGx5QGV4YW1wbGUuY29tIiwiZXhwIjoxNjk4MDk0MDIzLCJpYXQiOjE2OTgwNTgwMjN9.p5DEG51YM6LQzCMY00m-AEstAaM4HM4EFQ-PWYh2BWw"
    );
  }, []); // passing an empty array because i want it to render only once.

  const AuthenticatedRoutes = () => {
    return (
      <Routes>
        <Route path="/home" element={<Homepage />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="*" element={<Navigate to="/home" replace />} />
      </Routes>
    );
  };

  const queryClient = new QueryClient();

  return (
    <QueryClientProvider client={queryClient}>
      <NavBar>
        <CssBaseline />
        <AuthenticatedRoutes />
      </NavBar>
    </QueryClientProvider>
  );
}

export default App;
