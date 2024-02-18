import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers';
import { FloatingDatePicker } from './styled';

export default function BasicDatePicker(props: any) {
  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <FloatingDatePicker {...props} />
    </LocalizationProvider>
  );
}
