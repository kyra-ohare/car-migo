import { SetStateAction, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import { useFormik } from 'formik';
import { AlertColor, Box, InputAdornment } from '@mui/material';
import { EmailRounded } from '@mui/icons-material';
import {
  AlertPopUp,
  DialogBox,
  CustomButton,
  CustomTextField,
} from '../../components';
import { CatchyMessage, WelcomeMessage } from '../home/styled';
import { useEmailConfirmation } from '../../hooks/usePlatformUser';
import { initialEmailValue } from './initial_values';
import { confirmEmailValidationSchema } from './validation_schema';
import { httpStatus, navigation, validation } from '../../constants';
import { IPlatformUserEmail } from '../../interfaces';

export default function ConfirmEmail() {
  const [openDialog, setOpenDialog] = useState<boolean>(false);
  const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);
  const [snackbarSeverity, setSnackbarSeverity] =
    useState<AlertColor>('success');
  const [snackbarMessage, setSnackbarMessage] = useState<string>('');
  const navigate = useNavigate();

  const dialogRedirect = () => {
    navigate(navigation.SIGN_IN_PAGE);
  };

  const dialogState = (data: SetStateAction<boolean>) => {
    setOpenDialog(data);
  };

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  const mutateConfirmEmail = useMutation({
    mutationFn: useEmailConfirmation,
    onSuccess: () => {
      setOpenDialog(true);
    },
    onError: (error: Error, variables: IPlatformUserEmail) => {
      if (error.message.endsWith(httpStatus.CONFLICT)) {
        setSnackbarMessage('Yayyy! You have already confirmed your email.');
      } else if (error.message.endsWith(httpStatus.NOT_FOUND)) {
        setSnackbarSeverity('error');
        setSnackbarMessage("Mmmm! We can't find an account for " + variables);
      } else {
        setSnackbarSeverity('error');
        setSnackbarMessage(validation.GENERIC_ERROR_MSG);
      }
      setOpenSnackbar(true);
    },
  });

  const handleFormSubmit = (values: IPlatformUserEmail) => {
    mutateConfirmEmail.mutate({ email: values.email });
  };

  const formik = useFormik({
    initialValues: initialEmailValue,
    validationSchema: confirmEmailValidationSchema,
    onSubmit: (values) => {
      handleFormSubmit(values);
    },
    enableReinitialize: true,
  });

  if (openDialog) {
    return (
      <DialogBox
        open={openDialog}
        state={dialogState}
        title='Email confirmed'
        text="Let's sign in!"
        redirect={dialogRedirect}
      />
    );
  }

  return (
    <Box
      component='form'
      onSubmit={formik.handleSubmit}
      sx={{
        '& .MuiTextField-root': { m: 1, width: '30vw' },
      }}
    >
      <WelcomeMessage>Confirm your email</WelcomeMessage>
      <CatchyMessage>
        You can enjoy all Car-Migo advantages after this confirmation.
      </CatchyMessage>
      <div>
        <CustomTextField
          id='confirm-email-address'
          label='Email Address'
          name='email'
          autoComplete='email'
          value={formik.values.email}
          onChange={formik.handleChange}
          InputProps={{
            startAdornment: (
              <InputAdornment position='start'>
                <EmailRounded />
              </InputAdornment>
            ),
          }}
          error={Boolean(formik.errors.email)}
          helperText={formik.touched.email && formik.errors.email}
        />
      </div>
      <div>
        <CustomButton label='Confirm Email' type='submit' sx={{ mt: 3 }} />
      </div>
      <AlertPopUp
        open={openSnackbar}
        onClose={handleCloseSnackbar}
        severity={snackbarSeverity}
        message={snackbarMessage}
      />
    </Box>
  );
}
