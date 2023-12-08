import { Dispatch, SetStateAction } from "react";
import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";
import { locations } from "../../constants/location";
import { IDropdownOptions } from ".";
import { Paper, styled } from "@mui/material";

interface ILocationDropdown {
  id: string;
  label: string;
  selectedLocation: IDropdownOptions | undefined;
  setSelectedLocation: Dispatch<SetStateAction<IDropdownOptions | undefined>>;
}

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

export default function LocationDropdown(props: ILocationDropdown) {
  return (
    <FloatingAutocomplete
      disablePortal
      id={props.id}
      value={props.selectedLocation}
      onChange={(_event, value) => {
        props.setSelectedLocation(value!);
      }}
      options={locations}
      sx={{ width: 300, mr: 0.5 }}
      renderInput={(params) => <TextField {...params} label={props.label} />}
    />
  );
}

// the exclamation at the end of an expression means the expression will only get executed if the value is NOT null.
