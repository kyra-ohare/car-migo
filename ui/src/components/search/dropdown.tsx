import { Dispatch, SetStateAction } from "react";
import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";
import { locations } from "../../constants/location";
import { IDropdownOptions } from ".";

interface ILocationDropdown {
  id: string;
  label: string;
  selectedLocation: IDropdownOptions | undefined;
  setSelectedLocation: Dispatch<SetStateAction<IDropdownOptions | undefined>>;
}

export default function LocationDropdown(props: ILocationDropdown) {
  return (
    <Autocomplete
      disablePortal
      id={props.id}
      value={props.selectedLocation}
      onChange={(_event, value) => {
        props.setSelectedLocation(value!);
      }}
      options={locations}
      sx={{ width: 300, color: "white"}}
      renderInput={(params) => <TextField {...params} label={props.label} />}
    />
  );
}

// the exclamation at the end of an expression means the expression will only get executed if the value is NOT null.
