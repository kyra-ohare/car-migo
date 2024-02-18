import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DatePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { styled } from '@mui/material';

const FloatingDatePicker = styled(DatePicker)`
  label.Mui-focused {
    color: #1976d2;
  }
  .MuiInputBase-root {
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
    border-radius: 8px;
    color: 'black',
    backgroundColor: 'white',
}
`;

export default function BasicDatePicker(props: any) {
  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <FloatingDatePicker {...props} />
    </LocalizationProvider>
  );
}
