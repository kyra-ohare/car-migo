import { Typography } from '@mui/material';
import { DirectionsCarRounded } from '@mui/icons-material';
import DateTime from './date_time';
import { IJourneyResponseProps } from '../../interfaces';

export default function ViewPassengerCard(props: IJourneyResponseProps) {
  const { driver } = props.journey;

  return (
    <>
      <DateTime journey={props.journey} />
      <Typography variant='body2'>
        <DirectionsCarRounded sx={{ verticalAlign: 'bottom', mr: 1 }} />
        Driver: {driver.platformUser.firstName} {driver.platformUser.lastName}
      </Typography>
    </>
  );
}
