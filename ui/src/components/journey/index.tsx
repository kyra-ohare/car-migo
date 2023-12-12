import { useState } from "react";
import {
  Card,
  CardContent,
  Typography,
  CardActions,
  Button,
  Box,
} from "@mui/material";
import { styled } from "@mui/system";

export interface IJourney {
  id: number;
  departure: string;
  destination: string;
  maxPassengers: number;
  availability: number;
  date: string;
  time: string;
}

const StyledCard = styled(Card)`
  box-shadow: 05px 6px 5px rgba(0, 0, 0, 0.2); /* Customize the shadow */
  padding: 16px; /* Add padding around the card */
`;

export default function Journey(data: IJourney) {
  const [journeys, setJourneys] = useState<IJourney[]>([]);

  const result: IJourney[] = [
    {
      id: 5,
      departure: "Newry",
      destination: "Rostrevor",
      maxPassengers: 3,
      date: "2022-12-02",
      time: "08:15",
    },
    {
      id: 6,
      departure: "Newry",
      destination: "Rostrevor",
      maxPassengers: 3,
      date: "2022-12-03",
      time: "08:00",
    },
  ];

  const handleResult = () => {
    setJourneys(result);
  };

  return (
    <Box onSubmit={handleResult}>
      <Typography variant="h6" gutterBottom>
        {data.departure} to {data.destination}
      </Typography>
      {/* <MyCard {...data} /> */}
      {journeys.map((data, index) => (
        <MyCard
          key={index}
          id={data.id}
          departure={data.departure}
          destination={data.destination}
          maxPassengers={data.maxPassengers}
          date={data.date}
          time={data.time}
        />
      ))}
    </Box>
  );
}

function MyCard(data: IJourney) {
  return (
    <StyledCard>
      <Card raised={true}>
        <CardContent>
          <Typography variant="body2">When? {data.date}</Typography>
          <Typography variant="body2">What time? {data.time}</Typography>
          {/* Add more data details as needed*/}
        </CardContent>
        <CardActions>
          <Button size="small" color="primary">
            Book Now
          </Button>
          {/* Add more action buttons if required */}
        </CardActions>
      </Card>
    </StyledCard>
  );
}
