import { SetStateAction, useState } from "react";
import {
  Alert,
  AlertTitle,
  Box,
  Card,
  CardActions,
  CardContent,
  InputAdornment,
  Typography,
  styled,
} from "@mui/material";
import PersonAddAlt1RoundedIcon from "@mui/icons-material/PersonAddAlt1Rounded";
import { SearchContainer } from "./styled";
import {
  BasicDateTimePicker,
  CustomButton,
  CustomTextField,
  // Journey,
  LocationDropdown,
} from "../../components/index";
import { IJourney } from "../journey";
import ArrowForwardOutlined from "@mui/icons-material/ArrowForwardOutlined";

export interface IDropdownOptions {
  value: number;
  label: string;
}

const result: IJourney[] = [
  {
    id: 5,
    departure: "Newry",
    destination: "Rostrevor",
    maxPassengers: 3,
    availability: 2,
    date: "2022-12-02",
    time: "08:15",
  },
  {
    id: 6,
    departure: "Newry",
    destination: "Rostrevor",
    maxPassengers: 3,
    availability: 3,
    date: "2022-12-03",
    time: "08:00",
  },
];

export default function Search() {
  const [selectedLeaving, setSelectedLeaving] = useState<IDropdownOptions>();
  const [selectedGoing, setSelectedGoing] = useState<IDropdownOptions>();
  const [selectedNumPassengers, setSelectedNumPassengers] = useState("");
  const [showResults, setShowResults] = useState(false);
  const [journeys, setJourneys] = useState<IJourney[]>([]);
  const [showAlert, setShowAlert] = useState(true);

  const handleSearch = () => {
    setShowResults(true);

    if (result.length === 0) {
      setShowAlert(true);
    } else {
      console.log("selectedLeaving", selectedLeaving);
      console.log("selectedGoing", selectedGoing);
      console.log("selectedNumPassengers", selectedNumPassengers);
      setJourneys(result);
    }
  };

  const resultState = (data: React.SetStateAction<boolean>) => {
    setShowResults(data);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
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
      {showResults && journeys ? (
        journeys[0] ? (
          <Journey
            results={journeys}
            departure={journeys[0].departure}
            destination={journeys[0].destination}
            state={resultState}
          />
        ) : (
          <>
            {showAlert && (
              <Box
                sx={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                }}
              >
                <Alert severity="error" variant="filled">
                  <AlertTitle>
                    <b>Oh no!</b>
                  </AlertTitle>
                  <b>No rides for selected locations or dates. </b>
                  <CustomButton label="Close" onClick={handleCloseAlert} />
                </Alert>
              </Box>
            )}
          </>
        )
      ) : (
        <></>
      )}
    </>
  );
}

const Journey = (props: any) => {
  const handleCloseResults = () => {
    props.state(false);
  };

  return (
    <Box sx={{ backgroundColor: "#f0f0f0", padding: 5 }}>
      <Typography variant="h6" sx={{ mb: "15px", display: "inline-flex" }}>
        <b>
          {props.departure}
          <ArrowForwardOutlined />
          {props.destination}
        </b>
      </Typography>
      {props.results.map((data: IJourney) => (
        <MyCard key={data.id} data={data} />
      ))}
      <Box display="flex" justifyContent="flex-end">
        <CustomButton label="Close results" onClick={handleCloseResults} />
      </Box>
    </Box>
  );
};

const StyledCard = styled(Card)`
  box-shadow: 05px 6px 5px rgba(0, 0, 0, 0.2); /* Customize the shadow */
  padding: 5px; /* Add padding around the card */
  transition: transform 0.2s ease-in-out; /* Add a smooth transition */
  margin-bottom: 15px;
  width: 50%;

  &:hover {
    transform: translateY(-5px); /* Apply a slight lift on hover */
  }
`;

function MyCard(props: any) {
  const {
    id,
    departure,
    destination,
    maxPassengers,
    availability,
    date,
    time,
  } = props.data;
  return (
    <Box display="flex" justifyContent="center" alignItems="center">
      <StyledCard raised={true}>
        <CardContent>
          <Typography variant="body1">
            <b>When?</b> {date}
          </Typography>
          <Typography variant="body1">
            <b>What time?</b> {time}
          </Typography>
          <Typography variant="body1">
            <b>Availability:</b> {availability}
          </Typography>
        </CardContent>
        <CardActions style={{ display: "flex", justifyContent: "flex-end" }}>
          <CustomButton label="Book now" />
        </CardActions>
      </StyledCard>
    </Box>
  );
}
