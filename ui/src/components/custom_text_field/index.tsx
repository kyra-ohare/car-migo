import { TextField, styled } from '@mui/material';

const FloatingTextField = styled(TextField)`
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

export default function CustomTextField(props: any) {
  return <FloatingTextField fullWidth sx={{ mt: '20px' }} {...props} />;
}
