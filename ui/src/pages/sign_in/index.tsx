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
  DialogBox,
  CustomAlert,
} from "../../components";
import { SetStateAction, useState } from "react";
import authenticate from "../../hooks/useAuthentication";
import navigation from "../../constants/navigation";
import { processUserErrorMsgs } from "../../utils/processUserErrorMsgs";
import http_status from "../../constants/http_status";

// TODO: I can sign in but i can't send JWT to axios. I tried useEffect below but it didn't work.
export default function SignIn() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isEmailError, setIsEmailError] = useState<boolean>(false);
  const [helperEmailText, setHelperEmailText] = useState("");
  const [isPasswordError, setIsPasswordError] = useState<boolean>(false);
  const [helperPasswordText, setHelperPasswordText] = useState("");
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("success");

  // let jwt = "";

  const navigate = useNavigate();
  const mutateAuthenticateUser = useMutation({
    mutationFn: authenticate,
    onSuccess: (data) => {
      console.log("Success!", data.jwt);
      // jwt = data.jwt;
      navigate(navigation.HOME_PAGE);
    },
    onError: (error) => {
      // console.log(error);
      const errorMsg = error.response?.data.message;
      const { email, password } = processUserErrorMsgs(errorMsg);

      if (email) {
        setIsEmailError(true);
        setHelperEmailText(email);
      }
      if (password) {
        setIsPasswordError(true);
        setHelperPasswordText(password);
      }

      const errorStatus = error.response?.data.status;
      if (errorStatus === http_status.FORBIDDEN) {
        setSnackbarMessage("Oh no! " + errorMsg);
        setSnackbarSeverity("error");
        setOpenSnackbar(true);
      }
    },
  });

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // setHelperEmailText("");
    // setHelperPasswordText("");
    console.log("Email", email);
    mutateAuthenticateUser.mutate({
      email: email,
      password: password,
    });
  };

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  // useEffect(() => {
  //   // useEffect keeps an eye on data changes.
  //   setBearerToken(jwt);
  //   console.log("setBearerToken", jwt);
  // }, [jwt]); // passing an empty array because I want it to render only once.

  // TODO remove, this demo shouldn't need to reset the theme.
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
            onSubmit={handleSubmit}
            sx={{ mt: 1 }}
          >
            <CustomTextField
              id="sign-in-with-email"
              label="Email Address"
              name="email"
              autoComplete="email"
              required
              onChange={(event: {
                target: { value: SetStateAction<string> };
              }) => {
                setEmail(event.target.value);
              }}
              error={isEmailError}
              helperText={isEmailError ? helperEmailText : ""}
            />
            <CustomTextField
              id="sign-in-with-password"
              label="Password"
              name="password"
              autoComplete="current-password"
              type="password"
              required
              onChange={(event: {
                target: { value: SetStateAction<string> };
              }) => {
                setPassword(event.target.value);
              }}
              error={isPasswordError}
              helperText={isPasswordError ? helperPasswordText : ""}
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
          severity={snackbarSeverity}
          message={snackbarMessage}
        />
        <Footer />
      </Container>
    </ThemeProvider>
  );
}
