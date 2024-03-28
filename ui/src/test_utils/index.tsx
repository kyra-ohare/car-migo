import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DefaultTheme, ThemeProvider } from 'styled-components';
import { AxiosResponse } from 'axios';
import '@testing-library/jest-dom';
import {
  Queries,
  RenderResult,
  render as rtlRender,
} from '@testing-library/react';
import {
  createTheme,
  CssBaseline,
  ThemeProvider as MuiThemeProvider,
} from '@mui/material';
import { NavBar } from '../components';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
    },
  },
});

const exampleTheme = createTheme();

function render(
  ui: React.ReactElement<unknown>,
  { ...options } = {}
): RenderResult<Queries, HTMLElement> {
  const Wrapper: React.JSXElementConstructor<{ children: React.ReactNode }> = ({
    children,
  }) => (
    <Router>
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <QueryClientProvider client={queryClient}>
          <MuiThemeProvider theme={exampleTheme}>
            <ThemeProvider theme={exampleTheme as unknown as DefaultTheme}>
              <CssBaseline enableColorScheme>
                <NavBar>{children}</NavBar>
              </CssBaseline>
            </ThemeProvider>
          </MuiThemeProvider>
        </QueryClientProvider>
      </LocalizationProvider>
    </Router>
  );

  return rtlRender(ui, { wrapper: Wrapper, ...options });
}

const TestUtils = {
  render,
};

export default TestUtils;
