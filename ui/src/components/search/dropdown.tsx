import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";
import { locations } from "../../constants/location";
import { styled } from "@mui/material";
import { useFormikContext } from 'formik';

const FloatingAutocomplete = styled(Autocomplete)`
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

export default function LocationDropdown(props: any) {
  // const context = useFormikContext<any>();
  console.log("LocationDropdown", "props", props);

  return (
    <FloatingAutocomplete
      disablePortal
      id={props.id}
      // value={props.selectedLocation}
      // onChange={(_event, value) => {
      //   props.setSelectedLocation(value);
      // }}
      value={props.value}
      onChange={props.onChange}
      options={locations}
      sx={{ width: 300, mr: 0.5 }}
      renderInput={(params) => (
        <TextField
          {...params}
          label={props.label}
          // required
          name={props.name}
          // value={context.values.locationIdTo}
          // onChange={context.handleChange}
          error={props.formikTouched && Boolean(props.formikErrors)}
          helperText={props.formikTouched && props.formikErrors}
        />
      )}
    />
  );
}

// the exclamation at the end of an expression means the expression will only get executed if the value is NOT null.
