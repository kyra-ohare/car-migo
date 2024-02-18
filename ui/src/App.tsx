import { useEffect } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { CssBaseline } from '@mui/material';
import './App.css';
import {
  ConfirmEmail,
  Homepage,
  ForgotPassword,
  Playground,
  Profile,
  SignIn,
  SignUp,
} from './pages';
import NavBar from './components/navigation';
import navigation from './constants/navigation';
import { useAuthStore } from './utils/authStore';
import { useTokens } from './utils/tokenStore';

export default function App() {
  const { isAuthorized } = useAuthStore();
  const { checkLocalStorageTokens } = useTokens();

  useEffect(() => {
    checkLocalStorageTokens();
  }, []);

  const AuthenticatedRoutes = (
    <Routes>
      <Route
        path='*'
        element={<Navigate to={navigation.HOME_PAGE} replace />}
      />
      <Route path='/' element={<Homepage />} />
      <Route path={navigation.HOME_PAGE} element={<Homepage />} />
      <Route path={navigation.PROFILE_PAGE} element={<Profile />} />
      <Route path={navigation.PLAYGROUND} element={<Playground />} />
    </Routes>
  );

  const UnauthenticatedRoutes = (
    <Routes>
      <Route
        path='*'
        element={<Navigate to={navigation.HOME_PAGE} replace />}
      />
      <Route path='/' element={<Homepage />} />
      <Route path={navigation.HOME_PAGE} element={<Homepage />} />
      <Route path={navigation.SIGN_UP_PAGE} element={<SignUp />} />
      <Route path={navigation.SIGN_IN_PAGE} element={<SignIn />} />
      <Route path={navigation.CONFIRM_EMAIL_PAGE} element={<ConfirmEmail />} />
      <Route path={navigation.PLAYGROUND} element={<Playground />} />
      <Route
        path={navigation.FORGOT_PASSWORD_PAGE}
        element={<ForgotPassword />}
      />
    </Routes>
  );

  const queryClient = new QueryClient();

  return (
    <QueryClientProvider client={queryClient}>
      <NavBar>
        <CssBaseline />
        {isAuthorized === true && AuthenticatedRoutes}
        {isAuthorized === true ||
          (isAuthorized === false && UnauthenticatedRoutes)}
      </NavBar>
    </QueryClientProvider>
  );
}

