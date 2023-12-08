import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useMutation } from "@tanstack/react-query";
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
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import {
  Footer,
  CustomButton,
  CustomTextField,
  CustomAlert,
} from "../../components";
import authenticate from "../../hooks/useAuthentication";
import navigation from "../../constants/navigation";
import tokenStore from "../../utils/tokensStore";
import { useFormik } from "formik";
import * as Yup from "yup";
import http_status from "../../constants/http_status";

const validationSchema = Yup.object().shape({
  email: Yup.string().required("Email must not be empty."),
  password: Yup.string().required("Password must not be empty."),
});

const initialValues = {
  email: "",
  password: "",
};

export default function SignIn() {
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const { setBearer } = tokenStore();

  const formik = useFormik({
    initialValues: initialValues,
    validationSchema: validationSchema,
    onSubmit: (values) => {
      handleFormSubmit(values);
    },
    enableReinitialize: true,
  });

  const navigate = useNavigate();
  const mutateAuthenticateUser = useMutation({
    mutationFn: authenticate,
    onSuccess: (data) => {
      setBearer(data.jwt);
      navigate(navigation.HOME_PAGE);
    },
    onError: (error) => {
      if (error.response?.data.status === http_status.FORBIDDEN) {
        setSnackbarMessage("Oh no! " + error.response?.data.message);
        setOpenSnackbar(true);
      }
    },
  });
  const handleFormSubmit = (values: any) => {
    mutateAuthenticateUser.mutate({
      email: values.email,
      password: values.password,
    });
  };

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  const defaultTheme = createTheme();
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
        >
          <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign In
          </Typography>
          <Box
            component="form"
            noValidate
            onSubmit={formik.handleSubmit}
            sx={{ mt: 1 }}
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
            />
            <FormControlLabel
              sx={{ mt: 2 }}
              control={<Checkbox value="remember" color="primary" />}
              label="Remember me"
            />
            <CustomButton
              fullWidth
              type="submit"
              label="Sign In"
              sx={{ mt: 3, mb: 2 }}
            />
            <Grid container>
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
        <CustomAlert
          open={openSnackbar}
          onClose={handleCloseSnackbar}
          severity="error"
          message={snackbarMessage}
        />
        <Footer />
      </Container>
    </ThemeProvider>
  );
}
