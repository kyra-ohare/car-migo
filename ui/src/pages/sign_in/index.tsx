import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useMutation } from "@tanstack/react-query";
import { useFormik } from "formik";
import {
  Avatar,
  Box,
  CssBaseline,
  Container,
  Grid,
  Link,
  Typography,
  Checkbox,
  FormControlLabel,
} from "@mui/material";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import { LockOutlined } from "@mui/icons-material";
import {
  Footer,
  CustomButton,
  CustomTextField,
  AlertPopUp,
} from "../../components";
import { useAuthentication } from "../../hooks/useAuthentication";
import { useBearerStore } from "../../hooks/useBearerStore";
import { useTokens } from "../../hooks/useTokens";
import { IAuthenticationRequest, IToken } from "../../interfaces";
import { initialSignInValues } from "./initial_values";
import { signInValidationSchema } from "./validation_schema";
import { httpStatus, navigation, validation } from "../../constants";

export default function SignIn() {
  const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);
  const [snackbarMessage, setSnackbarMessage] = useState<string>("");
  const { setBearer } = useBearerStore();
  const { checkIfValidToken } = useTokens();
  const navigate = useNavigate();
  const defaultTheme = createTheme();

  const mutateAuthenticateUser = useMutation({
    mutationFn: useAuthentication,
    onSuccess: (data: IToken) => {
      setBearer(data.accessToken);
      checkIfValidToken({
        accessToken: data.accessToken,
        refreshToken: data.refreshToken,
      });
      navigate(navigation.HOME_PAGE);
    },
    onError: (error: Error, variables: IAuthenticationRequest) => {
      if (error.message.endsWith(httpStatus.FORBIDDEN)) {
        setSnackbarMessage("Oh no! Bad credentials for " + variables.email);
      } else {
        setSnackbarMessage(validation.GENERIC_ERROR_MSG);
      }
      setOpenSnackbar(true);
    },
  });

  const handleFormSubmit = (values: IAuthenticationRequest) => {
    mutateAuthenticateUser.mutate({
      email: values.email,
      password: values.password,
    });
  };

  const formik = useFormik({
    initialValues: initialSignInValues,
    validationSchema: signInValidationSchema,
    onSubmit: (values) => {
      handleFormSubmit(values);
    },
    enableReinitialize: true,
  });

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  return (
    <ThemeProvider theme={defaultTheme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
          data-testid="outer-box"
        >
          <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
            <LockOutlined />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign In
          </Typography>
          <Box
            component="form"
            noValidate
            onSubmit={formik.handleSubmit}
            sx={{ mt: 1 }}
            data-testid="inner-box"
          >
            <CustomTextField
              id="sign-in-with-email"
              label="Email Address"
              name="email"
              autoComplete="email"
              required
              value={formik.values.email}
              onChange={formik.handleChange}
              error={formik.touched.email && Boolean(formik.errors.email)}
              helperText={formik.touched.email && formik.errors.email}
              datatestid="sign-in-with-email"
            />
            <CustomTextField
              id="sign-in-with-password"
              label="Password"
              name="password"
              autoComplete="current-password"
              type="password"
              required
              value={formik.values.password}
              onChange={formik.handleChange}
              error={formik.touched.password && Boolean(formik.errors.password)}
              helperText={formik.touched.password && formik.errors.password}
              datatestid="sign-in-with-password"
            />
            <FormControlLabel
              sx={{ mt: 2 }}
              control={<Checkbox value="remember" color="primary" />}
              label="Remember me"
              data-testid="form-control-label"
            />
            <CustomButton
              fullWidth
              type="submit"
              label="Sign In"
              sx={{ mt: 3, mb: 2 }}
              datatestid="submit-button"
            />
            <Grid container data-testid="links">
              <Grid item xs sx={{ ml: -9 }}>
                <Link href={navigation.FORGOT_PASSWORD_PAGE} variant="body2">
                  Forgot password?
                </Link>
              </Grid>
              <Grid item>
                <Link href={navigation.SIGN_UP_PAGE} variant="body2">
                  Don't have an account? Sign Up
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
        <AlertPopUp
          open={openSnackbar}
          onClose={handleCloseSnackbar}
          severity="error"
          message={snackbarMessage}
          datatestid="alert-pop-up"
        />
        <Footer />
      </Container>
    </ThemeProvider>
  );
}
