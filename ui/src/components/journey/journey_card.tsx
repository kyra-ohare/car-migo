import { Box, CardActions, CardContent, Typography } from "@mui/material";
import { CustomButton } from "..";
import { StyledJourneyCard } from "./styled";
import { IJourneyCardProps } from "../../interfaces";

export default function JourneyCard(props: IJourneyCardProps) {
  const data = props.data;

  return (
    <Box display="flex" justifyContent="center" alignItems="center">
      <StyledJourneyCard raised={true}>
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
          <CustomButton label="Book now" datatestid="book-button" />
        </CardActions>
      </StyledJourneyCard>
    </Box>
  );
}
