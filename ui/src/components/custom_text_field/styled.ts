import { TextField, styled } from '@mui/material';

export const StyledTextField = styled(TextField)`
  label.Mui-focused {
    color: #1976d2;
  }
  .MuiInputBase-root {
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
    border-radius: 8px;
    color: #000000,
    backgroundColor: #ffffff,
  }
`;
