import { useState } from 'react';
import {
  Box,
  Card,
  CardContent,
  CardActions,
  Button,
  Typography,
  Grid,
} from '@mui/material';
import { IJourneyEntity } from '../../interfaces';
import {
  AccessAlarmRounded,
  DeleteRounded,
  CalendarMonthRounded,
  LocationOn,
} from '@mui/icons-material';

export default function ComboBox() {
  const [passengerJourneys, setPassengerJourneys] =
    useState<IJourneyEntity[]>(dummy_journeys);

  const handleCancel = (id: number) => {
    setPassengerJourneys(
      passengerJourneys.filter((journey) => journey.id !== id)
    );
  };

  return (
    <Box sx={{ flexGrow: 1, padding: 2 }}>
      <Typography variant='h4' component='h2' sx={{ mb: 3 }}>
        Your Upcoming Journeys
      </Typography>
      <Grid container spacing={2}>
        {passengerJourneys.map((journey) => (
          <Grid item key={journey.id}>
            <Card raised>
              <CardContent>
                <Typography variant='h6' component='div' gutterBottom>
                  <LocationOn color='primary' />{' '}
                  {journey.locationFrom.description} →{' '}
                  {journey.locationTo.description}
                </Typography>
                <Typography variant='body1' sx={{ mb: 1.5 }}>
                  <CalendarMonthRounded
                    sx={{ verticalAlign: 'bottom', mr: 1 }}
                  />
                  {formatDate(journey.dateTime)}
                </Typography>
                <Typography variant='body2'>
                  <AccessAlarmRounded sx={{ verticalAlign: 'bottom', mr: 1 }} />
                  {formatTime(journey.dateTime)}
                </Typography>
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
                  startIcon={<DeleteRounded />}
                >
                  Cancel Journey
                </Button>
              </CardActions>
            </Card>
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

const dummy_journeys: IJourneyEntity[] = [
  {
    id: 1,
    locationFrom: {
      id: 1,
      description: 'New York',
    },
    locationTo: {
      id: 2,
      description: 'Los Angeles',
    },
    maxPassengers: 3,
    dateTime: '2020-11-30T09:00:00Z',
    createdDate: '2020-11-30T09:00:00Z',
    availability: 0,
  },
  {
    id: 2,
    locationFrom: {
      id: 3,
      description: 'Chicago',
    },
    locationTo: {
      id: 4,
      description: 'Houston',
    },
    maxPassengers: 4,
    dateTime: '2020-11-30T09:00:00Z',
    createdDate: '2020-11-30T09:00:00Z',
    availability: 0,
  },
  {
    id: 3,
    locationFrom: {
      id: 5,
      description: 'Miami',
    },
    locationTo: {
      id: 6,
      description: 'Seattle',
    },
    maxPassengers: 5,
    dateTime: '2020-11-30T09:00:00Z',
    createdDate: '2020-11-30T09:00:00Z',
    availability: 0,
  },
  {
    id: 4,
    locationFrom: {
      id: 5,
      description: 'Orlando',
    },
    locationTo: {
      id: 1,
      description: 'Philadelphia City',
    },
    maxPassengers: 5,
    dateTime: '2020-11-30T09:00:00Z',
    createdDate: '2020-11-30T09:00:00Z',
    availability: 0,
  },
];
