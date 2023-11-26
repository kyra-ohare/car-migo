import React, { Dispatch, SetStateAction } from "react";
import { Autocomplete, TextField, InputAdornment } from "@mui/material";
import { numbers } from "../../constants/numbers";
import PersonIcon from "@mui/icons-material/Person";
import { IDropdownOptions } from ".";

interface IPassengerNumberDropdown {
  id: string;
  label: string;
  selectedPassengerNumber: IDropdownOptions | undefined;
  setSelectedPassengerNumber: Dispatch<
    SetStateAction<IDropdownOptions | undefined>
  >;
}

export default function PassengerNumberDropdown(
  props: IPassengerNumberDropdown
) {
  return (
    <TextField
    // {...params}
    label={props.label}
    type="number"
    InputLabelProps={{
      shrink: true,
    }}
  />
    // <Autocomplete
    //   disablePortal
    //   id={props.id}
    //   value={props.selectedPassengerNumber}
    //   onChange={(_event, value) => {
    //     props.setSelectedPassengerNumber(value!);
    //   }}
    //   options={numbers}
    //   sx={{ width: 200 }}
    //   renderInput={(params) => (
    //     <TextField
    //       {...params}
    //       label={props.label}
          
    //       InputProps={{
    //         ...params.InputProps,
    //         startAdornment: (
    //           <InputAdornment position="start" sx={{ marginRight: "2px" }}>
    //             <PersonIcon />
    //           </InputAdornment>
    //         ),
    //         disableUnderline: true,
    //       }}
    //     />
    //   )}
    // />
  );
}
