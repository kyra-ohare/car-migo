import React from 'react';
import '@testing-library/jest-dom';
import { DefaultTheme, ThemeProvider } from 'styled-components';
import {
  Queries,
  RenderResult,
  render as rtlRender,
} from '@testing-library/react';
import { AxiosResponse } from 'axios';
import { BrowserRouter as Router } from 'react-router-dom';
import {
  createTheme,
  CssBaseline,
  ThemeProvider as MuiThemeProvider,
} from '@mui/material';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
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

// eslint-disable-next-line func-names
export const buildAxiosResponse = function <T>(
  data: T,
  status: number
): AxiosResponse<T> {
  const axiosResponse: AxiosResponse<T> = {
    data,
    status,
    statusText: status.toString(),
    headers: {},
    config: {
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      //@ts-expect-error
      headers: {},
    },
  };
  return axiosResponse;
};

const TestUtils = {
  render,
  buildAxiosResponse,
};

export default TestUtils;
