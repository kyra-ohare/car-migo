import { Autocomplete } from '@mui/material';
import styled from 'styled-components';

export const SearchContainer = styled.div`
  display: flex;
  width: 80vw;
  justify-content: space-between;
  margin: 16px;
`;

export const FloatingAutocomplete = styled(Autocomplete)`
  label.Mui-focused {
    color: #1976d2; // Change the focused label color
  }
  .MuiInputBase-root {
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2); // Add shadow
    border-radius: 8px; // Optional: Add rounded corners
    color: 'black'; // Text color
    background-color: 'white'; // Background color
  }
` as typeof Autocomplete;
