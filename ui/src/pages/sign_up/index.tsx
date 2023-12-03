import { SetStateAction, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useMutation } from "@tanstack/react-query";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import { createUser } from "../../hooks/usePlatformUser";
import navigation from "../../constants/navigation";
import { processUserErrorMsgs } from "../../utils/processUserErrorMsgs";
import {
  Avatar,
  Box,
  CssBaseline,
  Container,
  Grid,
  Link,
  Typography,
} from "@mui/material";
import { LockOutlined } from "@mui/icons-material";
import {
  BasicDatePicker,
  Footer,
  CustomButton,
  CustomTextField,
  CustomAlert,
} from "../../components";
import http_status from "../../constants/http_status";

export default function SignUp() {
  const dateToday = new Date();

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [dob, setDob] = useState<Date>(dateToday);
  const [phoneNumber, setPhoneNumber] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [isFirstNameError, setIsFirstNameError] = useState<boolean>(false);
  const [helperFirstNameText, setHelperFirstNameText] = useState("");
  const [isLastNameError, setIsLastNameError] = useState<boolean>(false);
  const [helperLastNameText, setHelperLastNameText] = useState("");
  const [isPhoneNumberError, setIsPhoneNumberError] = useState<boolean>(false);
  const [helperPhoneNumberText, setHelperPhoneNumberText] = useState("");
  const [isEmailError, setIsEmailError] = useState<boolean>(false);
  const [helperEmailText, setHelperEmailText] = useState("");
  const [isPasswordError, setIsPasswordError] = useState<boolean>(false);
  const [helperPasswordText, setHelperPasswordText] = useState("");
  // const [isConfirmedPasswordError, setIsConfirmedPasswordError] = useState<boolean>(false);
  // const [helperConfirmedPasswordText, setHelperConfirmedPasswordText] = useState("");
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("success");

  const navigate = useNavigate();

  const mutateUser = useMutation({
    mutationFn: createUser,
    onSuccess: (data) => {
      console.log("Success!", data);
      navigate(navigation.HOME_PAGE);
    },
    onError: (error) => {
      const errorMsg = error.response?.data.message;
      const { firstName, lastName, phoneNumber, email, password } =
        processUserErrorMsgs(errorMsg);

      if (firstName) {
        setIsFirstNameError(true);
        setHelperFirstNameText(firstName);
      }
      if (lastName) {
        setIsLastNameError(true);
        setHelperLastNameText(lastName);
      }
      if (phoneNumber) {
        setIsPhoneNumberError(true);
        setHelperPhoneNumberText(phoneNumber);
      }
      if (email) {
        setIsEmailError(true);
        setHelperEmailText(email);
      }
      if (password) {
        setIsPasswordError(true);
        setHelperPasswordText(password);
      }
      const errorStatus = error.response?.data.status;
      if (errorStatus === http_status.CONFLICT) {
        setSnackbarMessage("Oh no! " + errorMsg);
        setSnackbarSeverity("error");
        setOpenSnackbar(true);
      }
    },
  });

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // if( password !== confirmPassword) {
    //   setIsConfirmedPasswordError(true);
    //   setHelperConfirmedPasswordText("Oh dear! Passwords don't match");
    // }
    // console.log("firstName", firstName);
    // console.log("lastName", lastName);
    // console.log("dob", dob.toISOString());
    // console.log("phoneNumber", phoneNumber);
    // console.log("email", email);
    // console.log("password", password);
    // console.log("confirmPassword", confirmPassword);

    mutateUser.mutate({
    //   firstName: "My",
    //   lastName: "Test",
    //   dob: "1960-02-26T00:00:00Z",
    //   phoneNumber: "028657345912",
    //   email: "mary.green@example.com",
    //   password: "Pass1234!",

      firstName: firstName,
      lastName: lastName,
      dob: dob,
      phoneNumber: phoneNumber,
      email: email,
      password: password,
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
            <LockOutlined />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign up
          </Typography>
          <Box
            component="form"
            noValidate
            onSubmit={handleSubmit}
            sx={{ mt: 3 }}
          >
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <ThisTextField
                  id="sign-up-first-name"
                  label="First Name"
                  name="first-name"
                  required
                  autoComplete="first-name"
                  onChange={(event: {
                    target: { value: SetStateAction<string> };
                  }) => {
                    setFirstName(event.target.value);
                  }}
                  error={isFirstNameError}
                  helperText={isFirstNameError ? helperFirstNameText : ""}
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <ThisTextField
                  id="sign-up-last-name"
                  label="Last Name"
                  name="last-name"
                  required
                  autoComplete="last-name"
                  onChange={(event: {
                    target: { value: SetStateAction<string> };
                  }) => {
                    setLastName(event.target.value);
                  }}
                  error={isLastNameError}
                  helperText={isLastNameError ? helperLastNameText : ""}
                />
              </Grid>
              <Grid item xs={12}>
                <BasicDatePicker
                  label="Date of Birth *"
                  // defaultValue={dayjs("2022-04-17")}
                  // value="boo"
                  views={["day", "month", "year"]}
                  // value={dob}
                  onChange={(newValue: React.SetStateAction<Date>) =>
                    setDob(newValue)
                  }
                />
              </Grid>
              <Grid item xs={12}>
                <ThisTextField
                  id="sign-up-phone-number"
                  label="Phone Number"
                  name="phone-number"
                  required
                  autoComplete="phone-number"
                  onChange={(event: {
                    target: { value: SetStateAction<string> };
                  }) => {
                    setPhoneNumber(event.target.value);
                  }}
                  error={isPhoneNumberError}
                  helperText={isPhoneNumberError ? helperPhoneNumberText : ""}
                />
              </Grid>
              <Grid item xs={12}>
                <ThisTextField
                  id="sign-up-email-address"
                  label="Email Address"
                  name="email-address"
                  required
                  autoComplete="email"
                  onChange={(event: {
                    target: { value: SetStateAction<string> };
                  }) => {
                    setEmail(event.target.value);
                  }}
                  error={isEmailError}
                  helperText={isEmailError ? helperEmailText : ""}
                />
              </Grid>
              <Grid item xs={12}>
                <ThisTextField
                  id="sign-up-password"
                  label="Password"
                  name="password"
                  type="password"
                  autoComplete="password"
                  required
                  onChange={(event: {
                    target: { value: SetStateAction<string> };
                  }) => {
                    setPassword(event.target.value);
                  }}
                  error={isPasswordError}
                  helperText={isPasswordError ? helperPasswordText : ""}
                />
              </Grid>
              <Grid item xs={12}>
                <ThisTextField
                  id="sign-up-confirm-password"
                  label="Confirm Password"
                  name="confirm-password"
                  type="password"
                  autoComplete="password"
                  required
                  onChange={(event: {
                    target: { value: SetStateAction<string> };
                  }) => {
                    setConfirmPassword(event.target.value);
                  }}
                />
              </Grid>
            </Grid>
            <CustomButton
              fullWidth
              type="submit"
              label="Sign Up"
              sx={{ mt: 5, mb: 2 }}
            />
            <Grid container justifyContent="flex-end">
              <Grid item>
                <Link href={navigation.SIGN_IN_PAGE} variant="body2">
                  Already have an account? Sign in
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

function ThisTextField(props: any) {
  return (
    <>
      <CustomTextField sx={{ mt: 1 }} {...props} />
    </>
  );
}
