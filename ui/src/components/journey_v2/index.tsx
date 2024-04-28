import {
  Box,
  Typography,
  Grid,
  Card,
  CardContent,
  CardActions,
  Button,
  styled,
} from '@mui/material';
import {
  CalendarMonthRounded,
  AccessAlarmRounded,
  DeleteRounded,
} from '@mui/icons-material';
import { IJourneyEntity } from '../../interfaces';
import { useMutation } from '@tanstack/react-query';
import { useDeletePassenger } from '../../hooks/useJourney';
import DriverCard from './card_driver';
import PassengerCard from './card_passenger';
import RouteHeadline from './route_headline';
import DateTime from './card_date_time';
import SearchCard from './card_search';

interface IJourneyV2 {
  label: string;
  journeys: IJourneyEntity[];
  origin?: string | undefined;
  destination?: string;
}

export default function JourneyV2(props: IJourneyV2) {
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
    <Box sx={{ flexGrow: 1, padding: 2 }}>
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
      <Grid container spacing={2}>
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
                <Button
                  size='small'
                  type='submit'
                  sx={{
                    color: '#ff4d4d',
                    '&:hover': {
                      backgroundColor: '#f2f2f2',
                    },
                  }}
                  onClick={() => handleCancelJourney(journey.id)}
                  endIcon={<DeleteRounded />}
                >
                  Cancel Journey
                </Button>
              </CardActions>
            </StyledJourneyCard>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}

export const StyledJourneyCard = styled(Card)`
  transition: transform 0.2s ease-in-out;
  margin-bottom: 15px;

  &:hover {
    transform: translateY(-5px);
  }
`;
