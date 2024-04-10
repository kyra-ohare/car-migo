import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import {
  Box,
  Container,
  createTheme,
  CssBaseline,
  Grid,
  ThemeProvider,
} from '@mui/material';
import { Delete } from '@mui/icons-material';
import { CustomButton, Footer, Loader } from '../../components';
import { CatchyMessage } from '../home/styled';
import { useUserDeletion, useUserProfile } from '../../hooks/usePlatformUser';
import { useDriverCreation, useDriverDeletion } from '../../hooks/useDriver';
import {
  usePassengerCreation,
  usePassengerDeletion,
} from '../../hooks/usePassenger';
import { navigation } from '../../constants';
import { ThisTextField } from './this_text_field';
import { useTokens } from '../../hooks/useTokens';
import SwitchWithTooltip from '../../components/swith_with_tooltip';

export default function Profile() {
  const [firstName, setFirstName] = useState<string>('');
  const [lastName, setLastName] = useState<string>('');
  const [dob, setDob] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [phoneNumber, setPhoneNumber] = useState<string>('');
  const [isPassenger, setIsPassenger] = useState<boolean>(false);
  const [passengerLabel, setPassengerLabel] = useState<string>('');
  const [passengerTooltip, setPassengerTooltip] = useState<string>('');
  const [isDriver, setIsDriver] = useState<boolean>(false);
  const [driverLabel, setDriverLabel] = useState<string>('');
  const [driverTooltip, setDriverTooltip] = useState<string>('');
  const { isSuccess, data } = useUserProfile();
  const { clearLocalStorageTokens } = useTokens();
  const navigate = useNavigate();
  const defaultTheme = createTheme();

  const parseDob = (dob: string) => {
    const findT = dob.indexOf('T');
    const arrayDate = dob.substring(0, findT).split('-');
    return arrayDate[2] + '/' + arrayDate[1] + '/' + arrayDate[0];
  };

  const signOut = () => {
    clearLocalStorageTokens();
  };

  const mutateDeleteUser = useMutation({
    mutationFn: useUserDeletion,
    onSuccess: () => {},
    onError: () => {},
  });

  const deleteAccount = () => {
    mutateDeleteUser.mutate();
    clearLocalStorageTokens();
    navigate(navigation.HOME_PAGE);
  };

  const mutateDeletePassenger = useMutation({
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
      mutateDeletePassenger.mutate();
      setIsPassenger(false);
    } else {
      mutateCreatePassenger.mutate();
      setIsPassenger(true);
    }
    setPassengerLabelAndTooltip(!isPassenger);
  };

  function setPassengerLabelAndTooltip(state: boolean) {
    if (state) {
      setPassengerLabel('You are a passenger');
      setPassengerTooltip('which means you can book journeys.');
    } else {
      setPassengerLabel('Become a passenger');
      setPassengerTooltip('since you cannot book journeys at the moment.');
    }
  }

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
    } else {
      mutateCreateDriver.mutate({
        licenseNumber: '11111',
      });
      setIsDriver(true);
    }
    setDriverLabelAndTooltip(!isDriver);
  };

  function setDriverLabelAndTooltip(state: boolean) {
    if (state) {
      setDriverLabel('You are a driver');
      setDriverTooltip('so you can create journeys.');
    } else {
      setDriverLabel('Become a driver');
      setDriverTooltip('since you cannot create journeys.');
    }
  }
  useEffect(() => {
    if (isSuccess && data) {
      setFirstName(data.firstName);
      setLastName(data.lastName);
      setDob(parseDob(data.dob));
      setEmail(data.email);
      setPhoneNumber(data.phoneNumber);

      setIsDriver(data.driver);
      setDriverLabelAndTooltip(data.driver);

      setIsPassenger(data.passenger);
      setPassengerLabelAndTooltip(data.passenger);
    }
  }, [isSuccess, data]);

  return !isSuccess ? (
    <Loader data-testid='loader' />
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
                datatestid='read-only-first-name'
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <ThisTextField
                id='read-only-last-name'
                label='Last Name'
                value={lastName}
                datatestid='read-only-last-name'
              />
            </Grid>
            <Grid item xs={12}>
              <ThisTextField
                id='read-only-dob'
                label='Date of Birth'
                value={dob}
                datatestid='read-only-dob'
              />
            </Grid>
            <Grid item xs={12}>
              <ThisTextField
                id='read-only-email'
                label='Email'
                value={email}
                datatestid='read-only-email'
              />
            </Grid>
            <Grid item xs={12}>
              <ThisTextField
                id='read-only-phone-number'
                label='Phone Number'
                value={phoneNumber}
                datatestid='read-only-phone-number'
              />
            </Grid>
            <Grid item xs={12}>
              <SwitchWithTooltip
                tooltipText={passengerTooltip}
                label={passengerLabel}
                isChecked={isPassenger}
                handleSwithWithTooltip={handlePassenger}
                datatestid='passenger-switch-with-tooltip'
              />
            </Grid>
            <Grid item xs={12}>
              <SwitchWithTooltip
                tooltipText={driverTooltip}
                label={driverLabel}
                isChecked={isDriver}
                handleSwithWithTooltip={handleDriver}
                datatestid='driver-switch-with-tooltip'
              />
            </Grid>
            <Grid item xs={12}>
              <CustomButton
                type='submit'
                label='Sign Out'
                sx={{ mt: 6, mb: 3 }}
                onClick={signOut}
                datatestid='sign-out-button'
              />
            </Grid>
            <Grid item xs={12}>
              <CustomButton
                type='submit'
                label='Delete my account'
                color='error'
                endIcon={<Delete />}
                onClick={deleteAccount}
                datatestid='delete-account'
              />
            </Grid>
          </Grid>
        </Box>
        <Footer />
      </Container>
    </ThemeProvider>
  );
}
