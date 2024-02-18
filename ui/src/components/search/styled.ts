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
    color: #1976d2;
  }
  .MuiInputBase-root {
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
    border-radius: 8px;
    color: 'black';
    background-color: 'white';
  }
` as typeof Autocomplete;
