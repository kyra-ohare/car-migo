import "./App.css";
import { Routes, Route, Navigate } from "react-router-dom";
import { CssBaseline } from "@mui/material";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { useEffect } from "react";
import { setBearerToken } from "./integration/instance";
import NavBar from "./components/navigation";
import Homepage from "./pages/home";
import Profile from "./pages/profile";
import SignIn from "./pages/sign_in";
import SignUp from "./pages/sign_up";
import MyTest from "./pages/my_test";

function App() {
  useEffect(() => {
    // useEffect keeps an eye on data changes.
    setBearerToken(
      "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXJ5LmdyZWVuQGV4YW1wbGUuY29tIiwiZXhwIjoxNzAwOTk3MDI2LCJpYXQiOjE3MDA5NjEwMjZ9.3zyC8oI_Zt4cc6nFzdX0iQsyK1-f-VCVhpvAgcT1AyM"
    );
  }, []); // passing an empty array because I want it to render only once.

  const AuthenticatedRoutes = () => {
    return (
      <Routes>
        <Route path="/home" element={<Homepage />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/sign-in" element={<SignIn />} />
        <Route path="/sign-up" element={<SignUp />} />
        <Route path="/my_test" element={<MyTest />} />
        {/* star in Route path below is the default behaviour */}
        <Route path="*" element={<Navigate to="/home" replace />} />
      </Routes>
    );
  };

  const queryClient = new QueryClient(); // interacts with a cache. https://tanstack.com/query/v4/docs/react/reference/QueryClient

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
