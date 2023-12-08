import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";
import { styled } from "@mui/material";

const FloatingDatePicker = styled(DatePicker)`
  label.Mui-focused {
    color: #1976d2; // Change the focused label color
  }
  .MuiInputBase-root {
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2); // Add shadow
    border-radius: 8px; // Optional: Add rounded corners
    color: 'black', // Text color
    backgroundColor: 'white', // Background color
}
`;

export default function BasicDatePicker(props: any) {
  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <FloatingDatePicker {...props} />
    </LocalizationProvider>
  );
}
