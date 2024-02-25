import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers';
import { StyledDatePicker } from './styled';
import { IBasicDatePickerProps } from '../../interfaces';

export default function BasicDatePicker(props: IBasicDatePickerProps) {
  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <StyledDatePicker {...props} />
    </LocalizationProvider>
  );
}
