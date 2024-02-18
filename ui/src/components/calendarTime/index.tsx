import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DateTimePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { styled } from '@mui/material';
import dayjs from 'dayjs';

const FloatingDateTimePicker = styled(DateTimePicker)`
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
