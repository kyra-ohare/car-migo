import React, { useState } from "react";
import { Button, TextField } from "@mui/material";
import { SearchContainer } from "./styled";
import {
  BasicDateTimePicker,
  LocationDropdown,
  PassengerNumberDropdown,
} from "../../components/index";

export interface IDropdownOptions {
  value: number;
  label: string;
}

const Search = () => {
  const [selectedLeaving, setSelectedLeaving] = useState<IDropdownOptions>();
  const [selectedGoing, setSelectedGoing] = useState<IDropdownOptions>();
  const [selectedNumPassengers, setSelectedNumPassengers] =
    useState<IDropdownOptions>();
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
        <PassengerNumberDropdown
          id="number-of-passengers"
          label="Number of Passengers"
          selectedPassengerNumber={selectedNumPassengers}
          setSelectedPassengerNumber={setSelectedNumPassengers}
        />
        <Button variant="contained" onClick={handleSearch}>
          Search
        </Button>
      </SearchContainer>
    </>
  );
};

export default Search;
