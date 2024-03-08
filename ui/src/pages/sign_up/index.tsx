import { SetStateAction, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import { useFormik } from 'formik';
import {
  Avatar,
  Box,
  CssBaseline,
  Container,
  Grid,
  Link,
  Typography,
} from '@mui/material';
import { LockOutlined } from '@mui/icons-material';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import {
  BasicDatePicker,
  Footer,
  CustomButton,
  DialogBox,
  AlertPopUp,
} from '../../components';
import { ThisTextField } from './this_text_field';
import { useUserCreation } from '../../hooks/usePlatformUser';
import { httpStatus, navigation } from '../../constants';
import { initialSignUpValues } from './initial_values';
import { signUpValidationSchema } from './validation_schema';
import { IPlatformUserCreation } from '../../interfaces';

export default function SignUp() {
  const [openDialog, setOpenDialog] = useState<boolean>(false);
  const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);
  const [snackbarMessage, setSnackbarMessage] = useState<string>('');
  const navigate = useNavigate();
  const defaultTheme = createTheme();

  const dialogState = (data: SetStateAction<boolean>) => {
    setOpenDialog(data);
  };

  const dialogRedirect = () => {
    navigate(navigation.CONFIRM_EMAIL_PAGE);
  };

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  const formik = useFormik({
    initialValues: initialSignUpValues,
    validationSchema: signUpValidationSchema,
    onSubmit: (values) => {
      handleFormSubmit(values);
    },
    enableReinitialize: true,
  });

  const mutateUser = useMutation({
    mutationFn: useUserCreation,
    onSuccess: () => {
      setOpenDialog(true);
    },
    onError: (error: Error, variables: IPlatformUserCreation) => {
      if (error.message.endsWith(httpStatus.CONFLICT)) {
        setSnackbarMessage(
          'Oh! We already have an account for ' + variables.email
        );
        setOpenSnackbar(true);
      }
    },
  });

  const handleFormSubmit = (values: IPlatformUserCreation) => {
    mutateUser.mutate({
      ...values,
      firstName: values.firstName,
      lastName: values.lastName,
      dob: values.dob,
      email: values.email,
      password: values.password,
      confirmPassword: values.confirmPassword,
      phoneNumber: values.phoneNumber,
    });
  };

  if (openDialog) {
    return (
      <DialogBox
        open={openDialog}
        state={dialogState}
        title='Account created'
        text='You should receive an email to confirm it but, in the meantime, just confirm it here 😊'
        redirect={dialogRedirect}
      />
    );
  }

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
            <LockOutlined />
          </Avatar>
          <Typography component='h1' variant='h5'>
            Sign up
          </Typography>
          <Box
            component='form'
            noValidate
            onSubmit={formik.handleSubmit}
            sx={{ mt: 3 }}
            data-testid='inner-box'
          >
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <ThisTextField
                  id='sign-up-first-name'
                  label='First Name'
                  name='firstName'
                  autoComplete='first-name'
                  value={formik.values.firstName}
                  onChange={formik.handleChange}
                  error={
                    formik.touched.firstName && Boolean(formik.errors.firstName)
                  }
                  helperText={
                    formik.touched.firstName && formik.errors.firstName
                  }
                  dataTestId='sign-up-first-name'
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <ThisTextField
                  id='sign-up-last-name'
                  label='Last Name'
                  name='lastName'
                  autoComplete='last-name'
                  value={formik.values.lastName}
                  onChange={formik.handleChange}
                  error={
                    formik.touched.lastName && Boolean(formik.errors.lastName)
                  }
                  helperText={formik.touched.lastName && formik.errors.lastName}
                  dataTestId='sign-up-last-name'
                />
              </Grid>
              <Grid item xs={12}>
                <BasicDatePicker
                  label='Date of Birth'
                  name='dob'
                  disableFuture
                  value={formik.values.dob || null}
                  onChange={(value: unknown) =>
                    formik.setFieldValue('dob', value, true)
                  }
                  error={formik.touched.dob && Boolean(formik.errors.dob)}
                  helperText={formik.touched.dob && formik.errors.dob}
                  dataTestId='date-of-birth'
                />
              </Grid>
              <Grid item xs={12}>
                <ThisTextField
                  id='sign-up-phone-number'
                  label='Phone Number'
                  name='phoneNumber'
                  autoComplete='phone-number'
                  value={formik.values.phoneNumber}
                  onChange={formik.handleChange}
                  error={
                    formik.touched.phoneNumber &&
                    Boolean(formik.errors.phoneNumber)
                  }
                  helperText={
                    formik.touched.phoneNumber && formik.errors.phoneNumber
                  }
                  dataTestId='sign-up-phone-number'
                />
              </Grid>
              <Grid item xs={12}>
                <ThisTextField
                  id='sign-up-email-address'
                  label='Email Address'
                  name='email'
                  autoComplete='email'
                  value={formik.values.email}
                  onChange={formik.handleChange}
                  error={formik.touched.email && Boolean(formik.errors.email)}
                  helperText={formik.touched.email && formik.errors.email}
                  dataTestId='sign-up-email-address'
                />
              </Grid>
              <Grid item xs={12}>
                <ThisTextField
                  id='sign-up-password'
                  label='Password'
                  name='password'
                  type='password'
                  autoComplete='password'
                  value={formik.values.password}
                  onChange={formik.handleChange}
                  error={
                    formik.touched.password && Boolean(formik.errors.password)
                  }
                  helperText={formik.touched.password && formik.errors.password}
                  dataTestId='sign-up-password'
                />
              </Grid>
              <Grid item xs={12}>
                <ThisTextField
                  id='sign-up-confirm-password'
                  label='Confirm Password'
                  name='confirmPassword'
                  type='password'
                  autoComplete='password'
                  value={formik.values.confirmPassword}
                  onChange={formik.handleChange}
                  error={
                    formik.touched.confirmPassword &&
                    Boolean(formik.errors.confirmPassword)
                  }
                  helperText={
                    formik.touched.confirmPassword &&
                    formik.errors.confirmPassword
                  }
                  dataTestId='sign-up-confirm-password'
                />
              </Grid>
            </Grid>
            <CustomButton
              fullWidth
              type='submit'
              label='Sign Up'
              sx={{ mt: 5, mb: 2 }}
              dataTestId='submit-button'
            />
            <Grid container justifyContent='flex-end' data-testid='links'>
              <Grid item>
                <Link href={navigation.SIGN_IN_PAGE} variant='body2'>
                  Already have an account? Sign in
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
        <AlertPopUp
          open={openSnackbar}
          onClose={handleCloseSnackbar}
          severity='info'
          message={snackbarMessage}
          dataTestId='alert-pop-up'
        />
        <Footer />
      </Container>
    </ThemeProvider>
  );
}
