import {
  Box,
  Typography,
  Grid,
  CardContent,
  CardActions,
  AlertColor,
} from '@mui/material';
import { DeleteRounded } from '@mui/icons-material';
import { IJourneyEntity, IJourneyProps } from '../../interfaces';
import { useMutation } from '@tanstack/react-query';
import { useAddPassenger, useDeletePassenger } from '../../hooks/useJourney';
import ViewPassengerCard from './view_passender_card';
import ViewDriverCard from './view_driver_card';
import RouteHeadline from './route_headline';
import ViewSearchCard from './view_search_card';
import { StyledButton, StyledGrid, StyledJourneyCard } from './styled';
import CustomButton from '../custom_button';
import { useEffect, useState } from 'react';
import { httpStatus } from '../../constants';
import { AlertPopUp } from '..';

export default function Journey(props: IJourneyProps) {
  const [journeys, setJourneys] = useState<IJourneyEntity[]>(props.journeys);
  const [snackbarSeverity, setSnackbarSeverity] = useState<AlertColor>('info');
  const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);
  const [snackbarMessage, setSnackbarMessage] = useState<string>('');

  const mutateDeletePassenger = useMutation({
    mutationFn: useDeletePassenger,
    onSuccess: async (resp) => {
      if (resp.status.toString() === httpStatus.NO_CONTENT) {
        const journeyIdFromURL = findJourneyIdFromURL(resp.config.url);
        if (journeyIdFromURL !== 0) {
          setJourneys(
            journeys.filter((journey) => journey.id !== journeyIdFromURL)
          );
        }
      }
    },
    onError: () => {},
  });

  const handleCancelJourney = async (journeyId: number) => {
    mutateDeletePassenger.mutate(journeyId);
  };

  const mutateAddPassenger = useMutation({
    mutationFn: useAddPassenger,
    onSuccess: (resp) => {
      if (resp.status.toString() === httpStatus.OK) {
        const journeyIdFromURL = findJourneyIdFromURL(resp.config.url);
        if (journeyIdFromURL !== 0) {
          setJourneys(
            journeys.filter((journey) => journey.id !== journeyIdFromURL)
          );
        }
      }
    },
    onError: (error: Error) => {
      if (error.message.endsWith(httpStatus.CONFLICT)) {
        setSnackbarSeverity('info');
        setSnackbarMessage('You are already a passenger to this journey.');
        setOpenSnackbar(true);
      }
      if (error.message.endsWith(httpStatus.FORBIDDEN)) {
        setSnackbarSeverity('warning');
        setSnackbarMessage('Please, log in to book this journey.');
        setOpenSnackbar(true);
      }
    },
  });

  const bookJourney = (journeyId: number) => {
    mutateAddPassenger.mutate(journeyId);
  };

  useEffect(() => {
    journeys.length;
  }, [journeys]);

  const SearchRouteHeading = () => (
    <>
      {journeys.length ? (
        <RouteHeadline origin={props.origin} destination={props.destination} />
      ) : (
        /* c8 ignore next */
        /* c8 ignore next */
        <></>
      )}
    </>
  );

  return (
    <Box sx={{ flexGrow: 1, padding: 2 }} data-testid={props.datatestid}>
      <Typography variant='h4' component='h2' sx={{ mb: 3 }}>
        {props.label === 'search' ? <SearchRouteHeading /> : <>{props.label}</>}
      </Typography>
      <StyledGrid container spacing={2}>
        {journeys &&
          journeys.map((journey: IJourneyEntity) => (
            <Grid
              item
              key={journey.id}
              data-testid={'journey-card-' + journey.id}
            >
              <StyledJourneyCard raised>
                <CardContent>
                  {props.label === 'search' ? (
                    <ViewSearchCard journey={journey} />
                  ) : (
                    <>
                      <RouteHeadline
                        origin={journey.locationFrom.description}
                        destination={journey.locationTo.description}
                      />
                      {journey.passengers ? (
                        <ViewDriverCard journey={journey} />
                      ) : (
                        <ViewPassengerCard journey={journey} />
                      )}
                    </>
                  )}
                </CardContent>
                <CardActions sx={{ justifyContent: 'center' }}>
                  {props.label === 'search' ? (
                    <CustomButton
                      label='Book'
                      onClick={() => bookJourney(journey.id)}
                      datatestid={'book-journey-button-' + journey.id}
                    />
                  ) : (
                    <StyledButton
                      size='small'
                      type='submit'
                      onClick={() => handleCancelJourney(journey.id)}
                      endIcon={<DeleteRounded />}
                      data-testid={'cancel-journey-button-' + journey.id}
                    >
                      Cancel Journey
                    </StyledButton>
                  )}
                </CardActions>
              </StyledJourneyCard>
            </Grid>
          ))}
      </StyledGrid>
      <AlertPopUp
        open={openSnackbar}
        onClose={() => setOpenSnackbar(false)}
        severity={snackbarSeverity}
        message={snackbarMessage}
        datatestid='journey-alert-pop-up'
      />
    </Box>
  );
}

function findJourneyIdFromURL(url: string | undefined): number {
  if (url === undefined) {
    /* c8 ignore next */
    return 0;
    /* c8 ignore next */
  }
  const regex = /\/journeys\/(\d+)/;
  const match = url.match(regex);

  if (match && match[1]) {
    return parseInt(match[1], 10);
  }
  /* c8 ignore next */
  return 0;
  /* c8 ignore next */
}
