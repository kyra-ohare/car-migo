import { Box, Typography, Grid, CardContent, CardActions } from '@mui/material';
import { DeleteRounded } from '@mui/icons-material';
import { IJourneyEntity } from '../../interfaces';
import { useMutation } from '@tanstack/react-query';
import { useDeletePassenger } from '../../hooks/useJourney';
import DriverCard from './card_driver';
import PassengerCard from './card_passenger';
import RouteHeadline from './route_headline';
import SearchCard from './card_search';
import { StyledButton, StyledGrid, StyledJourneyCard } from './styled';

interface IJourneyV2 {
  label: string;
  journeys: IJourneyEntity[];
  origin?: string | undefined;
  destination?: string;
}

export default function Journey(props: IJourneyV2) {
  const mutateDeletePassenger = useMutation({
    mutationFn: useDeletePassenger,
    onSuccess: (data) => {
      console.log('Success deleting passenger', data);
    },
    onError: (error) => {
      console.log('Success deleting passenger', error);
    },
  });
  const handleCancelJourney = (journeyId: number) => {
    console.log('Journey journeyId', journeyId);
    mutateDeletePassenger.mutate(journeyId);
    // setPassengerJourneys(
    //   props.journeys.filter((journey) => journey.id !== id)
    // );
  };

  return (
    <Box sx={{ flexGrow: 1, padding: 2 }} data-testid='journey-component'>
      <Typography variant='h4' component='h2' sx={{ mb: 3 }}>
        {props.label === 'search' ? (
          <RouteHeadline
            origin={props.origin}
            destination={props.destination}
          />
        ) : (
          <>{props.label}</>
        )}
      </Typography>
      <StyledGrid container spacing={2}>
        {props.journeys.map((journey: IJourneyEntity) => (
          <Grid item key={journey.id}>
            <StyledJourneyCard raised>
              <CardContent>
                {props.label === 'search' ? (
                  <SearchCard journey={journey} />
                ) : (
                  <>
                    <RouteHeadline
                      origin={journey.locationFrom.description}
                      destination={journey.locationTo.description}
                    />
                    {journey.passengers ? (
                      <PassengerCard journey={journey} />
                    ) : (
                      <DriverCard journey={journey} />
                    )}
                  </>
                )}
              </CardContent>
              <CardActions sx={{ justifyContent: 'center' }}>
                <StyledButton
                  size='small'
                  type='submit'
                  onClick={() => handleCancelJourney(journey.id)}
                  endIcon={<DeleteRounded />}
                  data-testid='close-journey-button'
                >
                  Cancel Journey
                </StyledButton>
              </CardActions>
            </StyledJourneyCard>
          </Grid>
        ))}
      </StyledGrid>
    </Box>
  );
}
