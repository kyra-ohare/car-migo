import { Grid2, Typography } from '@mui/material';
import {
  GroupsRounded,
  WeekendRounded,
  PersonOutlineRounded,
} from '@mui/icons-material';
import DateTime from './date_time';
import { IJourneyResponseProps, IPassengerEntity } from '../../interfaces';

export default function ViewDriverCard(props: IJourneyResponseProps) {
  const { maxPassengers, availability, passengers } = props.journey;
  return (
    <>
      <DateTime journey={props.journey} />
      <Typography variant='body2'>
        <GroupsRounded sx={{ verticalAlign: 'bottom', mr: 1 }} />
        Max passengers: {maxPassengers}
      </Typography>
      <Typography variant='body2'>
        <WeekendRounded sx={{ verticalAlign: 'bottom', mr: 1 }} />
        Availability: {availability}
      </Typography>
      {passengers !== undefined && (
        <>
          <Typography variant='body2' sx={{ mt: 1.5 }}>
            Your passengers are:
          </Typography>
          {passengers.map(
            (
              passenger: IPassengerEntity
            ) => (
              <Grid2 key={passenger.id}>
                <Typography variant='body2' sx={{ mt: 0.25 }}>
                  <PersonOutlineRounded
                    fontSize='small'
                    sx={{ verticalAlign: 'bottom', mr: 1 }}
                  />
                  {passenger.platformUser.firstName}{' '}
                  {passenger.platformUser.lastName}
                </Typography>
              </Grid2>
            )
          )}
        </>
      )}
    </>
  );
}
