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
  LocationOn,
  GroupsRounded,
  WeekendRounded,
  DirectionsCarRounded,
  PersonOutlineRounded,
} from '@mui/icons-material';
import { IJourneyEntity } from '../../interfaces';

interface IJourneyV2 {
  label: string;
  journeys: IJourneyEntity[];
}

export default function JourneyV2(props: IJourneyV2) {
  // label, journeys

  const handleCancel = (id: number) => {
    // setPassengerJourneys(
    //   props.journeys.filter((journey) => journey.id !== id)
    // );
  };

  return (
    <Box sx={{ flexGrow: 1, padding: 2 }}>
      <Typography variant='h4' component='h2' sx={{ mb: 3 }}>
        {/* As a passenger, here are your upcoming journeys */}
        {props.label}
      </Typography>
      <Grid container spacing={2}>
        {props.journeys.map((journey: IJourneyEntity) => (
          <Grid item key={journey.id}>
            <StyledJourneyCard raised>
              <CardContent>
                <Typography variant='h6' component='div' gutterBottom>
                  <LocationOn color='primary' />{' '}
                  {journey.locationFrom.description} →{' '}
                  {journey.locationTo.description}
                </Typography>
                <Typography variant='body1' sx={{ mb: 1 }}>
                  <CalendarMonthRounded
                    sx={{ verticalAlign: 'bottom', mr: 1 }}
                  />
                  {formatDate(journey.dateTime)}
                </Typography>
                <Typography variant='body2' sx={{ mb: 1 }}>
                  <AccessAlarmRounded sx={{ verticalAlign: 'bottom', mr: 1 }} />
                  {formatTime(journey.dateTime)}
                </Typography>
                <Typography variant='body2'>
                  <GroupsRounded sx={{ verticalAlign: 'bottom', mr: 1 }} />
                  Max passengers: {journey.maxPassengers}
                </Typography>
                {/* <Typography variant='body2'>
                  <WeekendRounded sx={{ verticalAlign: 'bottom', mr: 1 }} />
                  Availability: {journey.availability}
                </Typography> */}
                {}
                <Typography variant='body2'>
                  <DirectionsCarRounded
                    sx={{ verticalAlign: 'bottom', mr: 1 }}
                  />
                  Driver: {journey.driver.platformUser.firstName}{' '}
                  {journey.driver.platformUser.lastName}
                </Typography>
                {journey.passengers &&
              //   <Typography variant='body2'>
              //   <WeekendRounded sx={{ verticalAlign: 'bottom', mr: 1 }} />
              //   Availability: {journey.availability}
              // </Typography>
                  (journey.passengers.map((passenger) => (
                    <Typography variant='body2'>
                      <PersonOutlineRounded
                        sx={{ verticalAlign: 'bottom', mr: 1 }}
                      />
                      Passenger: {passenger.platformUser.firstName}{' '}
                      {passenger.platformUser.lastName}
                    </Typography>
                  )))}
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
                  onClick={() => handleCancel(journey.id)}
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

function formatDate(dateTimeStr: string): string {
  const dateObj = new Date(dateTimeStr);
  return [
    dateObj.getDate().toString().padStart(2, '0'),
    (dateObj.getMonth() + 1).toString().padStart(2, '0'),
    dateObj.getFullYear(),
  ].join('-');
}

function formatTime(dateTimeStr: string): string {
  const dateObj = new Date(dateTimeStr);
  return [
    dateObj.getHours().toString().padStart(2, '0'),
    dateObj.getMinutes().toString().padStart(2, '0'),
  ].join(':');
}

export const StyledJourneyCard = styled(Card)`
  transition: transform 0.2s ease-in-out;
  margin-bottom: 15px;

  &:hover {
    transform: translateY(-5px);
  }
`;
