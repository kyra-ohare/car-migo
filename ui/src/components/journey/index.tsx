import { useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import {
  Box,
  Typography,
  Grid2,
  CardContent,
  CardActions,
  AlertColor,
} from '@mui/material';
import { DeleteRounded } from '@mui/icons-material';
import { StyledButton, StyledGrid, StyledJourneyCard } from './styled';
import { AlertPopUp, CustomButton } from '..';
import ViewPassengerCard from './view_passender_card';
import ViewDriverCard from './view_driver_card';
import RouteHeadline from './route_headline';
import ViewSearchCard from './view_search_card';
import { IJourneyEntity, IJourneyProps } from '../../interfaces';
import {
  useAddPassenger,
  useDeleteJourney,
  useDeletePassenger,
} from '../../hooks/useJourney';
import { useUserProfile } from '../../hooks/usePlatformUser';
import { httpStatus } from '../../constants';

export default function Journey(props: IJourneyProps) {
  const [journeys, setJourneys] = useState<IJourneyEntity[]>(props.journeys);
  const [snackbarSeverity, setSnackbarSeverity] = useState<AlertColor>('info');
  const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);
  const [snackbarMessage, setSnackbarMessage] = useState<string>('');
  const { isSuccess: isUserProfileSuccess, data: isUserDataSuccess } =
    useUserProfile();

  const processSnackbar = (severity: AlertColor, message: string) => {
    setSnackbarSeverity(severity);
    setSnackbarMessage(message);
    setOpenSnackbar(true);
  };

  const mutateAddPassenger = useMutation({
    mutationFn: useAddPassenger,
    onSuccess: (resp) => {
      if (resp.status.toString() === httpStatus.OK) {
        const journeyIdFromURL = findJourneyIdFromURL(resp.config.url);
        if (journeyIdFromURL !== 0) {
          setJourneys(
            journeys &&
              journeys.filter((journey) => journey.id !== journeyIdFromURL)
          );
          processSnackbar('info', 'Your journey was booked.');
        }
      }
    },
    onError: (error: Error) => {
      if (error.message.endsWith(httpStatus.FORBIDDEN)) {
        processSnackbar('warning', 'Please, log in to book this journey.');
      } else if (error.message.endsWith(httpStatus.NOT_FOUND)) {
        processSnackbar('error', 'Go to Profile to register as a passenger.');
      } else if (error.message.endsWith(httpStatus.NOT_ACCEPTABLE)) {
        processSnackbar('error', 'Sorry but this journey is full.');
      } else if (error.message.endsWith(httpStatus.UNPROCESSABLE_ENTITY)) {
        processSnackbar('error', 'You are the driver of this journey.');
      } else if (error.message.endsWith(httpStatus.CONFLICT)) {
        processSnackbar('info', 'You are already a passenger to this journey.');
      }
    },
  });

  const bookJourney = (journeyId: number) => {
    mutateAddPassenger.mutate(journeyId);
  };
  const mutateDeletePassenger = useMutation({
    mutationFn: useDeletePassenger,
    onSuccess: async (resp) => {
      if (resp.status.toString() === httpStatus.NO_CONTENT) {
        const journeyIdFromURL = findJourneyIdFromURL(resp.config.url);
        if (journeyIdFromURL !== 0) {
          setJourneys(
            journeys &&
              journeys.filter((journey) => journey.id !== journeyIdFromURL)
          );
          processSnackbar(
            'info',
            'You are no longer a passenger to this journey.'
          );
        }
      }
    },
    onError: () => {
      processSnackbar(
        'error',
        'It was not possible to remove you from this journey.'
      );
    },
  });

  const mutateDeleteJourney = useMutation({
    mutationFn: useDeleteJourney,
    onSuccess: (resp) => {
      if (resp.status.toString() === httpStatus.NO_CONTENT) {
        const journeyIdFromURL = findJourneyIdFromURL(resp.config.url);
        if (journeyIdFromURL !== 0) {
          setJourneys(
            journeys &&
              journeys.filter((journey) => journey.id !== journeyIdFromURL)
          );
          processSnackbar('info', 'Journey deleted successfully.');
        }
      }
    },
    onError: () => {
      processSnackbar('error', 'It was not possible to delete this journey.');
    },
  });

  const handleCancelJourney = async (journey: IJourneyEntity) => {
    // eslint-disable-next-line
    isUserProfileSuccess &&
    journey.driver.platformUser.id === isUserDataSuccess.id
      ? mutateDeleteJourney.mutate(journey.id)
      : mutateDeletePassenger.mutate(journey.id);
  };

  const SearchRouteHeading = () => (
    <>
      {journeys && journeys.length ? (
        <RouteHeadline origin={props.origin} destination={props.destination} />
      /* c8 ignore next */
      ) : (
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
            <Grid2 key={journey.id} data-testid={'journey-card-' + journey.id}>
              {props.label === 'search' ? (
                <StyledJourneyCard
                  raised
                  sx={{
                    backgroundColor:
                      journey.availability <= 0 ? 'grey.300' : 'white',
                    opacity: journey.availability <= 0 ? 0.7 : 1,
                  }}
                >
                  <CardContent>
                    <ViewSearchCard journey={journey} />
                  </CardContent>
                  <CardActions sx={{ justifyContent: 'center' }}>
                    <CustomButton
                      label='Book'
                      onClick={() => bookJourney(journey.id)}
                      datatestid={'book-journey-button-' + journey.id}
                    />
                  </CardActions>
                </StyledJourneyCard>
              ) : (
                <StyledJourneyCard raised>
                  <CardContent>
                    <RouteHeadline
                      origin={journey.locationFrom.description}
                      destination={journey.locationTo.description}
                    />
                    {isUserProfileSuccess &&
                    journey.driver.platformUser.id === isUserDataSuccess.id ? (
                      <ViewDriverCard journey={journey} />
                    ) : (
                      <ViewPassengerCard journey={journey} />
                    )}
                  </CardContent>
                  <CardActions sx={{ justifyContent: 'center' }}>
                    <StyledButton
                      size='small'
                      type='submit'
                      onClick={() => handleCancelJourney(journey)}
                      endIcon={<DeleteRounded />}
                      data-testid={'cancel-journey-button-' + journey.id}
                    >
                      Cancel Journey
                    </StyledButton>
                  </CardActions>
                </StyledJourneyCard>
              )}
            </Grid2>
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
