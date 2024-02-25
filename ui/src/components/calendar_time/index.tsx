import dayjs, { Dayjs } from 'dayjs';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers';
import { StyledDateTimePicker } from './styled';
import { IBasicDateTimePickerProps } from '../../interfaces';

export default function BasicDateTimePicker(props: IBasicDateTimePickerProps) {
  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <StyledDateTimePicker
        value={props.value ? dayjs(props.value) : null}
        onChange={(value: string | number | Date | Dayjs | null | undefined) =>
          props.onChange(props.name, dayjs(value).toISOString() || '', true)
        }
        label={props.label}
        slotProps={{
          textField: {
            error: props.formikTouched && Boolean(props.formikErrors),
            helperText: props.formikTouched && props.formikErrors,
          },
        }}
      />
    </LocalizationProvider>
  );
}
