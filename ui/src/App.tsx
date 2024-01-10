import "./App.css";
import { Routes, Route, Navigate } from "react-router-dom";
import { CssBaseline } from "@mui/material";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { useEffect } from "react";
import { setBearerToken } from "./integration/instance";
import NavBar from "./components/navigation";
import navigation from "./constants/navigation";
import {
  ConfirmEmail,
  Homepage,
  ForgotPassword,
  Playground,
  Profile,
  SignIn,
  SignUp,
} from "./pages";
import bearerStore from "./utils/bearerStore";
import { useAuthStore } from "./utils/authStore";
import useTokens from "./utils/tokenStore";

function App() {
  const { bearer } = bearerStore();

  const { isAuthorized } = useAuthStore();
  const { checkLocalStorageTokens } = useTokens();

  useEffect(() => {
    checkLocalStorageTokens();
  }, []);

  useEffect(() => {
    // useEffect keeps an eye on data changes.
    if (bearer) {
      setBearerToken(bearer);
    }
    setBearerToken("");
    console.log("token from App.tsx", bearer);
  }, [bearer]); // passing an empty array because I want it to render only once.

  const AuthenticatedRoutes = (
    <Routes>
      <Route path={navigation.CONFIRM_EMAIL_PAGE} element={<ConfirmEmail />} />
      <Route path={navigation.HOME_PAGE} element={<Homepage />} />
      <Route path={navigation.PROFILE_PAGE} element={<Profile />} />
      <Route path={navigation.PLAYGROUND} element={<Playground />} />
      <Route
        path={navigation.FORGOT_PASSWORD_PAGE}
        element={<ForgotPassword />}
      />
      {/* star in Route path below is the default behaviour */}
      <Route
        path="*"
        element={<Navigate to={navigation.HOME_PAGE} replace />}
      />
    </Routes>
  );

  const UnauthenticatedRoutes = (
    <Routes>
      <Route path={navigation.SIGN_IN_PAGE} element={<SignIn />} />
      <Route path={navigation.SIGN_UP_PAGE} element={<SignUp />} />
      <Route
        path="*"
        element={<Navigate to={navigation.SIGN_IN_PAGE} replace />}
      />
    </Routes>
  );

  const queryClient = new QueryClient(); // interacts with a cache. https://tanstack.com/query/v4/docs/react/reference/QueryClient

  return (
    <QueryClientProvider client={queryClient}>
      <NavBar>
        <CssBaseline />
        {isAuthorized === true && AuthenticatedRoutes}
        {isAuthorized === false && UnauthenticatedRoutes}
      </NavBar>
    </QueryClientProvider>
  );
}

export default App;
