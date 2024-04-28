import { Box, Typography } from '@mui/material';
import { ArrowForwardOutlined } from '@mui/icons-material';
import { CustomButton, JourneyCard } from '..';
import { IJourneyEntity } from '../../interfaces';

export default function Journey(props: any) {
  const handleCloseResults = () => {
    props.state(false);
  };

  return (
    <Box
      sx={{ backgroundColor: '#f0f0f0', padding: 5 }}
      data-testid='journey-component'
    >
      <Typography variant='h6' sx={{ mb: '15px', display: 'inline-flex' }}>
        <b>
          {props.departure}
          <ArrowForwardOutlined />
          {props.destination}
        </b>
      </Typography>
      {props.results.map((data: IJourneyEntity) => (
        <JourneyCard key={data.id} data={data} />
      ))}
      <Box display='flex' justifyContent='flex-end'>
        <CustomButton
          label='Close results'
          onClick={handleCloseResults}
          datatestid='close-journey-button'
        />
      </Box>
    </Box>
  );
}
