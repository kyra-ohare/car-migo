import { Autocomplete } from '@mui/material';
import styled from 'styled-components';

export const StyledSearchContainer = styled.div`
  display: flex;
  width: 80vw;
  justify-content: space-between;
  margin: 16px;
`;

export const StyledAutocomplete = styled(Autocomplete)`
  label.Mui-focused {
    color: #1976d2;
  }
  .MuiInputBase-root {
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
    border-radius: 8px;
    color: #000000;
    background-color: #ffffff;
  }
` as typeof Autocomplete;
