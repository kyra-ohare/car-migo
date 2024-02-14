import TextField from "@mui/material/TextField";
import { locations } from "../../constants/location";
import { FloatingAutocomplete } from "./styled";

export default function LocationDropdown(props: any) {
  return (
    <FloatingAutocomplete
      disablePortal
      id={props.id}
      value={props.value.value}
      isOptionEqualToValue={(
        option: { label: string; value: any },
        value: { value: any }
      ) => option?.value === value?.value}
      onChange={(_event, value) =>
        props.onChange(props.name, value?.value || "")
      }
      options={locations}
      sx={{ width: 300, mr: 0.5 }}
      renderInput={(params) => (
        <TextField
          {...params}
          label={props.label}
          name={props.name}
          error={props.formikTouched && Boolean(props.formikErrors)}
          helperText={props.formikTouched && props.formikErrors}
        />
      )}
    />
  );
}

// the exclamation at the end of an expression means the expression will only get executed if the value is NOT null.
