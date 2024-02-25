import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import {
  AlertColor,
  Box,
  Container,
  createTheme,
  CssBaseline,
  Grid,
  InputAdornment,
  ThemeProvider,
  Typography,
} from '@mui/material';
import { InfoOutlined, Delete } from '@mui/icons-material';
import {
  AlertPopUp,
  CustomButton,
  CustomTooltip,
  Footer,
  Loader,
} from '../../components';
import { CatchyMessage } from '../home/styled';
import { useUserProfile } from '../../hooks/usePlatformUser';
import { useDriverCreation, useDriverDeletion } from '../../hooks/useDriver';
import {
  usePassengerCreation,
  usePassengerDeletion,
} from '../../hooks/usePassenger';
import { navigation } from '../../constants';
import { ThisTextField } from './this_text_field';

export default function Profile() {
  const [firstName, setFirstName] = useState<string>('');
  const [lastName, setLastName] = useState<string>('');
  const [dob, setDob] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [phoneNumber, setPhoneNumber] = useState<string>('');
  const [isPassenger, setIsPassenger] = useState<boolean>(false);
  const [isDriver, setIsDriver] = useState<boolean>(false);
  const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);
  const [snackbarMessage, setSnackbarMessage] = useState<string>('');
  const [snackbarSeverity, setSnackbarSeverity] =
    useState<AlertColor>('success');
  const { isSuccess, data } = useUserProfile();
  const navigate = useNavigate();
  const defaultTheme = createTheme();

  const parseDob = (dob: string) => {
    const findT = dob.indexOf('T');
    const arrayDate = dob.substring(0, findT).split('-');
    return arrayDate[2] + '/' + arrayDate[1] + '/' + arrayDate[0];
  };

  const signOut = () => {
    navigate(navigation.HOME_PAGE);
  };

  const deleteAccount = () => {
    navigate(navigation.HOME_PAGE);
  };

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  const mustateDeletePassenger = useMutation({
    mutationFn: usePassengerDeletion,
    onSuccess: () => {},
    onError: () => {},
  });

  const mutateCreatePassenger = useMutation({
    mutationFn: usePassengerCreation,
    onSuccess: () => {},
    onError: () => {},
  });

  const handlePassenger = () => {
    if (isPassenger) {
      mustateDeletePassenger.mutate();
      setIsPassenger(false);
      setSnackbarMessage("Oh no! You're not a passenger anymore.");
      setSnackbarSeverity('error');
      setOpenSnackbar(true);
    } else if (!isPassenger) {
      mutateCreatePassenger.mutate();
      setIsPassenger(true);
      setSnackbarMessage("Yabba dabba doo! You've just become a passenger.");
      setSnackbarSeverity('success');
      setOpenSnackbar(true);
    }
  };

  const mustateDeleteDriver = useMutation({
    mutationFn: useDriverDeletion,
    onSuccess: () => {},
    onError: () => {},
  });

  const mutateCreateDriver = useMutation({
    mutationFn: useDriverCreation,
    onSuccess: () => {},
    onError: () => {},
  });

  const handleDriver = () => {
    if (isDriver) {
      mustateDeleteDriver.mutate();
      setIsDriver(false);
      setSnackbarMessage("Oh no! You're not a driver anymore.");
      setSnackbarSeverity('error');
      setOpenSnackbar(true);
    } else if (!isDriver) {
      mutateCreateDriver.mutate({
        licenseNumber: '11111',
      });
      setIsDriver(true);
      setSnackbarMessage("Yippee! You've just become a driver.");
      setSnackbarSeverity('success');
      setOpenSnackbar(true);
    }
  };

  const PassengerGrid = () => {
    return isPassenger === true ? (
      <>
        <Grid item xs>
          <Grid container justifyContent='center'>
            <Typography variant='body1'>You are a passenger</Typography>
            <InputAdornment position='start' sx={{ mt: 1.5 }}>
              <CustomTooltip
                icon={<InfoOutlined />}
                text='As a passenger, you can book journeys.'
                link="Click here if you don't want to be a passenger anymore."
                behaviour={handlePassenger}
              />
            </InputAdornment>
          </Grid>
        </Grid>
      </>
    ) : (
      <>
        <Grid item xs>
          <CustomButton
            label='Become a Passenger'
            sx={{ mt: 3 }}
            onClick={handlePassenger}
          />
        </Grid>
      </>
    );
  };

  const DriverGrid = () => {
    return isDriver === true ? (
      <>
        <Grid item xs>
          <Grid container justifyContent='center'>
            <Typography variant='body1'>You are a driver</Typography>
            <InputAdornment position='start' sx={{ mt: 1.5 }}>
              <CustomTooltip
                icon={<InfoOutlined />}
                text='As a driver, you can create journeys.'
                link="Click here if you don't want to be a driver anymore."
                behaviour={handleDriver}
              />
            </InputAdornment>
          </Grid>
        </Grid>
      </>
    ) : (
      <>
        <Grid item xs>
          <CustomButton label='Become a Driver' onClick={handleDriver} />
        </Grid>
      </>
    );
  };

  useEffect(() => {
    if (isSuccess && data) {
      setFirstName(data.firstName);
      setLastName(data.lastName);
      setDob(parseDob(data.dob));
      setEmail(data.email);
      setPhoneNumber(data.phoneNumber);
      setIsDriver(data.driver);
      setIsPassenger(data.passenger);
    }
  }, [isSuccess, data]);

  return !isSuccess ? (
    <Loader />
  ) : (
    <ThemeProvider theme={defaultTheme}>
      <Container component='main' maxWidth='xs'>
        <CssBaseline />
        <Box component='form' noValidate sx={{ mt: 3 }}>
          <CatchyMessage>About you</CatchyMessage>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <ThisTextField
                id='read-only-first-name'
                label='First Name'
                value={firstName}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <ThisTextField
                id='read-only-last-name'
                label='Last Name'
                value={lastName}
              />
            </Grid>
            <Grid item xs={12}>
              <ThisTextField
                id='read-only-dob'
                label='Date of Birth'
                value={dob}
              />
            </Grid>
            <Grid item xs={12}>
              <ThisTextField id='read-only-email' label='Email' value={email} />
            </Grid>
            <Grid item xs={12}>
              <ThisTextField
                id='read-only-phone-number'
                label='Phone Number'
                value={phoneNumber}
              />
            </Grid>
            <Grid item xs={12}>
              <PassengerGrid />
            </Grid>
            <Grid item xs={12}>
              <DriverGrid />
            </Grid>
            <Grid item xs={12}>
              <CustomButton
                type='submit'
                label='Sign Out'
                sx={{ mt: 6, mb: 3 }}
                onClick={signOut}
              />
            </Grid>
            <Grid item xs={12}>
              <CustomButton
                type='submit'
                label='Delete my account'
                color='error'
                endIcon={<Delete />}
                onClick={deleteAccount}
              />
            </Grid>
          </Grid>
        </Box>
        <AlertPopUp
          open={openSnackbar}
          onClose={handleCloseSnackbar}
          severity={snackbarSeverity}
          message={snackbarMessage}
        />
        <Footer />
      </Container>
    </ThemeProvider>
  );
}
