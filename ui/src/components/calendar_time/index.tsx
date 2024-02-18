import dayjs from 'dayjs';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers';
import { FloatingDateTimePicker } from './styled';

export default function BasicDateTimePicker(props: any) {
  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <FloatingDateTimePicker
        value={props.value ? dayjs(props.value) : null}
        onChange={(value: any) =>
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
