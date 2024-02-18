import { Box, CardActions, CardContent, Typography } from '@mui/material';
import { CustomButton } from '..';
import { StyledCard } from './styled';

export default function JourneyCard(props: any) {
  const data = props.data;

  return (
    <Box display='flex' justifyContent='center' alignItems='center'>
      <StyledCard raised={true}>
        <CardContent>
          <Typography variant='body1'>
            <b>When?</b> {data.createdDate}
          </Typography>
          <Typography variant='body1'>
            <b>What time?</b> {data.createdDate}
          </Typography>
          <Typography variant='body1'>
            <b>Availability:</b> {data.maxPassengers}
          </Typography>
        </CardContent>
        <CardActions style={{ display: 'flex', justifyContent: 'flex-end' }}>
          <CustomButton label='Book now' />
        </CardActions>
      </StyledCard>
    </Box>
  );
}
