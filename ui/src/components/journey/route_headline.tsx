import { Typography } from '@mui/material';
import { LocationOn } from '@mui/icons-material';
import { IRouteHeadline } from '../../interfaces';

export default function RouteHeadline(props: IRouteHeadline) {
  const { origin, destination } = props;
  return (
    <Typography variant='h6' component='div' gutterBottom>
      <LocationOn color='primary' />
      {origin} → {destination}
    </Typography>
  );
}
