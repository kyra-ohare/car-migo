import { LocationOn } from '@mui/icons-material';
import { Typography } from '@mui/material';

export interface IRouteHeadline {
  origin: string | undefined;
  destination: string | undefined;
}

export default function RouteHeadline(props: IRouteHeadline) {
  const { origin, destination } = props;
  return (
    <Typography variant='h6' component='div' gutterBottom>
      <LocationOn color='primary' />
      {origin} → {destination}
    </Typography>
  );
}
