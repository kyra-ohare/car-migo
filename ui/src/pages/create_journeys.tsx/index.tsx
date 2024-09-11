import { useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { useFormik } from 'formik';
import {
  AlertColor,
  Avatar,
  Box,
  CssBaseline,
  Container,
  Grid2,
  Typography,
} from '@mui/material';
import { EmojiTransportationRounded } from '@mui/icons-material';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import {
  AlertPopUp,
  BasicDateTimePicker,
  CustomButton,
  CustomNumberField,
  Dropdown,
  Footer,
} from '../../components';
import { httpStatus, locations } from '../../constants';
import { IJourneyCreation } from '../../interfaces';
import { initialCreateJourneysValues } from '../your_journeys/initial_values';
import { createJourneyValidationSchema } from './validation_schema';
import { useCreateJourney } from '../../hooks/useJourney';

export default function CreateYourneys() {
  const [selectedLeaving, setSelectedLeaving] = useState<string>('');
  const [selectedGoing, setSelectedGoing] = useState<string>('');
  const [snackbarSeverity, setSnackbarSeverity] = useState<AlertColor>('info');
  const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);
  const [snackbarMessage, setSnackbarMessage] = useState<string>('');
  const defaultTheme = createTheme();

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  const formik = useFormik({
    initialValues: initialCreateJourneysValues,
    validationSchema: createJourneyValidationSchema,
    onSubmit: (values: IJourneyCreation) => {
      mutateCreateJourney.mutate({
        locationIdFrom: values.locationIdFrom,
        locationIdTo: values.locationIdTo,
        dateTime: values.dateTime,
        maxPassengers: values.maxPassengers,
      });
    },
    enableReinitialize: true,
  });

  const mutateCreateJourney = useMutation({
    mutationFn: useCreateJourney,
    onSuccess: () => {
      setSnackbarMessage('Your journey was created successfully.');
      setSnackbarSeverity('info');
      setOpenSnackbar(true);
    },
    onError: (error: Error) => {
      if (error.message.endsWith(httpStatus.NOT_FOUND)) {
        setSnackbarMessage(
          'You cannot create a Journey. Are you a driver? Check if you are one in Profile.'
        );
      } else {
        setSnackbarMessage(
          "Something didn't go according to the plan and your journey was not created."
        );
      }
      setSnackbarSeverity('error');
      setOpenSnackbar(true);
    },
  });

  return (
    <ThemeProvider theme={defaultTheme}>
      <Container component='main' maxWidth='xs'>
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
          data-testid='outer-box'
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <EmojiTransportationRounded />
          </Avatar>
          <Typography component='h1' variant='h5'>
            Journeys creation
          </Typography>
          <Box
            component='form'
            noValidate
            onSubmit={formik.handleSubmit}
            sx={{ mt: 3 }}
            data-testid='inner-box'
          >
            <Grid2 container spacing={2}>
              <Grid2 size={{ xs: 12 }}>
                <Dropdown
                  id='create-journey-origin'
                  label='Origin'
                  name='locationIdFrom'
                  options={locations}
                  selectedOption={selectedLeaving}
                  setSelectedOption={setSelectedLeaving}
                  onChange={formik.setFieldValue}
                  value={formik.values.locationIdFrom}
                  formikErrors={formik.errors.locationIdFrom}
                  formikTouched={formik.touched.locationIdFrom}
                  widthStyle='auto'
                  mrStyle='auto'
                  datatestid='create-journey-origin'
                />
              </Grid2>
              <Grid2 size={{ xs: 12 }}>
                <Dropdown
                  id='create-journey-destination'
                  label='Destination'
                  name='locationIdTo'
                  options={locations}
                  selectedOption={selectedGoing}
                  setSelectedOption={setSelectedGoing}
                  value={formik.values.locationIdTo}
                  onChange={formik.setFieldValue}
                  formikErrors={formik.errors.locationIdTo}
                  formikTouched={formik.touched.locationIdTo}
                  widthStyle='auto'
                  mrStyle='auto'
                  datatestid='create-journey-destination'
                />
              </Grid2>
              <Grid2 size={{ xs: 12 }} sx={{ mt: 1 }}>
                <BasicDateTimePicker
                  label='Date/Time'
                  name='dateTime'
                  disablePast
                  value={formik.values.dateTime}
                  onChange={formik.setFieldValue}
                  formikErrors={formik.errors.dateTime}
                  formikTouched={formik.touched.dateTime}
                  datatestid='create-journeys-date-time-picker'
                />
              </Grid2>
              <Grid2 size={{ xs: 12 }}>
                <CustomNumberField
                  id='create-journeys-max-passengers'
                  label='Max passengers'
                  name='maxPassengers'
                  type='number'
                  min={1}
                  max={10}
                  value={formik.values.maxPassengers}
                  onChange={formik.handleChange}
                  error={
                    formik.touched.maxPassengers &&
                    Boolean(formik.errors.maxPassengers)
                  }
                  helperText={
                    formik.touched.maxPassengers && formik.errors.maxPassengers
                  }
                  datatestid='create-journeys-max-passengers'
                />
              </Grid2>
            </Grid2>
            <CustomButton
              fullWidth
              type='submit'
              label='Create'
              sx={{ mt: 5, mb: 2 }}
              datatestid='submit-button'
            />
          </Box>
        </Box>
        <AlertPopUp
          open={openSnackbar}
          onClose={handleCloseSnackbar}
          severity={snackbarSeverity}
          message={snackbarMessage}
          datatestid='alert-pop-up'
        />
        <Footer />
      </Container>
    </ThemeProvider>
  );
}
