import { SetStateAction, useState } from "react";
import { InputAdornment } from "@mui/material";
import PersonAddAlt1RoundedIcon from "@mui/icons-material/PersonAddAlt1Rounded";
import { SearchContainer } from "./styled";
import {
  BasicDateTimePicker,
  CustomButton,
  CustomTextField,
  LocationDropdown,
} from "../../components/index";

export interface IDropdownOptions {
  value: number;
  label: string;
}

const Search = () => {
  const [selectedLeaving, setSelectedLeaving] = useState<IDropdownOptions>();
  const [selectedGoing, setSelectedGoing] = useState<IDropdownOptions>();
  const [selectedNumPassengers, setSelectedNumPassengers] = useState("");
  const handleSearch = () => {
    console.log("selectedLeaving", selectedLeaving);
    console.log("selectedGoing", selectedGoing);
    console.log("selectedNumPassengers", selectedNumPassengers);
  };

  return (
    <>
      <SearchContainer>
        <LocationDropdown
          id="leaving-from-dropdown"
          label="Leaving From"
          selectedLocation={selectedLeaving}
          setSelectedLocation={setSelectedLeaving}
        />
        <LocationDropdown
          id="going-to-dropdown"
          label="Going to"
          selectedLocation={selectedGoing}
          setSelectedLocation={setSelectedGoing}
        />
        <BasicDateTimePicker />
        <CustomTextField
          id="number-of-passengers"
          label="Number of Passengers"
          name="number-of-passengers"
          autoComplete="current-password"
          required
          type="number"
          inputProps={{ min: 1 }}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <PersonAddAlt1RoundedIcon />
              </InputAdornment>
            ),
          }}
          sx={{ mt: "0px", width: "22ch" }}
          onChange={(event: SetStateAction<string>) => {
            setSelectedNumPassengers(event.target.value);
          }}
        />
        <CustomButton label="Search" onClick={handleSearch} />
      </SearchContainer>
    </>
  );
};

export default Search;
