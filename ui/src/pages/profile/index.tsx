import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import {
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
  CustomTextField,
  CustomTooltip,
  Footer,
  Loader,
} from '../../components';
import navigation from '../../constants/navigation';
import { CatchyMessage } from '../home/styled';
import { useGetProfile } from '../../hooks/usePlatformUser';
import { createDriver, deleteDriver } from '../../hooks/useDriver';
import { createPassenger, deletePassenger } from '../../hooks/usePassenger';

export default function Profile() {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [dob, setDob] = useState('');
  const [email, setEmail] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [isPassenger, setIsPassenger] = useState<boolean>(false);
  const [isDriver, setIsDriver] = useState<boolean>(false);
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState('');
  const [snackbarSeverity, setSnackbarSeverity] = useState('success');
  const { isSuccess, data } = useGetProfile();

  const parseDob = (dob: string) => {
    const findT = dob.indexOf('T');
    const arrayDate = dob.substring(0, findT).split('-');
    return arrayDate[2] + '/' + arrayDate[1] + '/' + arrayDate[0];
  };

  const navigate = useNavigate();
  const signOut = () => {
    navigate(navigation.HOME_PAGE);
  };

  const deleteAccount = () => {
    navigate(navigation.HOME_PAGE);
  };

  const mustateDeletePassenger = useMutation({
    mutationFn: deletePassenger,
    onSuccess: () => {},
    onError: () => {},
  });

  const mutateCreatePassenger = useMutation({
    mutationFn: createPassenger,
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
    mutationFn: deleteDriver,
    onSuccess: () => {},
    onError: () => {},
  });

  const mutateCreateDriver = useMutation({
    mutationFn: createDriver,
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

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
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

  function ThisTextField(props: any) {
    return (
      <>
        <CustomTextField
          {...props}
          sx={{ mt: 2 }}
          InputProps={{
            readOnly: true,
          }}
        />
      </>
    );
  }

  const defaultTheme = createTheme();

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
