import { DateTimePicker } from '@mui/x-date-pickers';
import { styled } from '@mui/material';

export const FloatingDateTimePicker = styled(DateTimePicker)`
  label.Mui-focused {
    color: #1976d2;
  }
  .MuiInputBase-root {
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
    border-radius: 8px;
    color: 'black';
    background-color: 'white';
  }
` as typeof DateTimePicker;
