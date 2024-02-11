import { Card, Box, CardContent, Typography, CardActions } from "@mui/material";
import styled from "styled-components";
import { CustomButton } from "..";

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

export default function JourneyCard(props: any) {
  const data = props.data;
  return (
    <Box display="flex" justifyContent="center" alignItems="center">
      <StyledCard raised={true}>
        <CardContent>
          <Typography variant="body1">
            <b>When?</b> {data.createdDate}
          </Typography>
          <Typography variant="body1">
            <b>What time?</b> {data.createdDate}
          </Typography>
          <Typography variant="body1">
            <b>Availability:</b> {data.maxPassengers}
          </Typography>
        </CardContent>
        <CardActions style={{ display: "flex", justifyContent: "flex-end" }}>
          <CustomButton label="Book now" />
        </CardActions>
      </StyledCard>
    </Box>
  );
}