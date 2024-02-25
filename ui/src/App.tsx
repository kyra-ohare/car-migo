import { useEffect } from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { CssBaseline } from '@mui/material';
import './App.css';
import { useAuthStore } from './hooks/useAuthStore';
import { useTokens } from './hooks/useTokens';
import {
  NavBar,
  AuthenticatedRoutes,
  UnauthenticatedRoutes,
} from './components';

export default function App() {
  const { isAuthorized } = useAuthStore();
  const { checkLocalStorageTokens } = useTokens();
  const queryClient = new QueryClient();

  useEffect(() => {
    checkLocalStorageTokens();
  }, []);

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
