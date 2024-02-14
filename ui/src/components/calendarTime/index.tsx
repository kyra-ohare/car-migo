import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DateTimePicker } from "@mui/x-date-pickers";
import { TextField, styled } from "@mui/material";
import dayjs from "dayjs";
import { date } from "yup";

const FloatingDateTimePicker = styled(DateTimePicker)`
  label.Mui-focused {
    color: #1976d2; // Change the focused label color
  }
  .MuiInputBase-root {
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2); // Add shadow
    border-radius: 8px; // Optional: Add rounded corners
    color: "black"; // Text color
    background-color: "white"; // Background color
  }
` as typeof DateTimePicker;
export default function BasicDateTimePicker(props: any) {
  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <FloatingDateTimePicker
        value={props.value ? dayjs(props.value) : null}
        onChange={(value: any) =>
          props.onChange(props.name, dayjs(value).toISOString() || "", true)
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
