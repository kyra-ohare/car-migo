import * as React from "react";
import { useMutation } from "@tanstack/react-query";
import {
  Avatar,
  Box,
  Button,
  CssBaseline,
  Container,
  Grid,
  Link,
  TextField,
  Typography,
} from "@mui/material";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import {
  BasicDatePicker,
  Footer,
} from "../../components";
import { createUser } from "../../hooks/useCreateUser";
import { useState } from "react";

// TODO remove, this demo shouldn't need to reset the theme.
const defaultTheme = createTheme();

const SignUp = () => {
  const mutateUser = useMutation({
    mutationFn: createUser,
    onSuccess: (data) => {
      console.log("Success!", data);
    },
    onError: (error) => {
      console.log(error.response?.data);
    },
  });

  const dateToday = new Date();

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [dob, setDob] = useState<Date>(dateToday);
  const [phoneNumber, setPhoneNumber] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log("firstName", firstName);
    console.log("lastName", lastName);
    console.log("dob", dob.toISOString());
    console.log("phoneNumber", phoneNumber);
    console.log("email", email);
    console.log("password", password);
    console.log("confirmPassword", confirmPassword);

    mutateUser.mutate({
      // firstName: "My",
      // lastName: "Test",
      // dob: "1960-02-26T00:00:00Z",
      // email: "my.test@example2.com",
      // password: "Pass1234!",
      // phoneNumber: "028657345912",
      firstName: firstName,
      lastName: lastName,
      dob: dob,
      phoneNumber: phoneNumber,
      email: email,
      password: password,
    });
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
        >
          <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
            <LockOutlinedIcon />
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
                <TextField
                  fullWidth
                  id="firstName"
                  name="firstName"
                  label="First Name"
                  autoFocus
                  required
                  autoComplete="given-name"
                  value={firstName}
                  onChange={(event) => {
                    setFirstName(event.target.value);
                  }}
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  required
                  fullWidth
                  id="lastName"
                  label="Last Name"
                  name="lastName"
                  autoComplete="family-name"
                  value={lastName}
                  onChange={(event) => {
                    setLastName(event.target.value);
                  }}
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
                <TextField
                  required
                  fullWidth
                  id="phoneNumber"
                  label="Phone Number"
                  name="phoneNumber"
                  autoComplete="phoneNumber"
                  value={phoneNumber}
                  onChange={(event) => {
                    setPhoneNumber(event.target.value);
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                  value={email}
                  onChange={(event) => {
                    setEmail(event.target.value);
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  autoComplete="new-password"
                  value={password}
                  onChange={(event) => {
                    setPassword(event.target.value);
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="confirmPassword"
                  label="Confirm Password"
                  type="password"
                  id="confirmPassword"
                  value={confirmPassword}
                  onChange={(event) => {
                    setConfirmPassword(event.target.value);
                  }}
                />
              </Grid>
              {/* <Grid item xs={12}>
                <YesNoRadioButtonsGroup
                  label="Do you want to be a passenger?"
                  tooltip="As a passenger, you can book journeys."
                  value={isPassenger}
                  onChange={(event: {
                    target: { value: React.SetStateAction<string> };
                  }) => {
                    setIsPassenger(event.target.value);
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <YesNoRadioButtonsGroup
                  label="Do you want to be a driver?"
                  tooltip="As a driver, you can create journeys."
                  value={isDriver}
                  onChange={(event: {
                    target: { value: React.SetStateAction<string> };
                  }) => {
                    setIsDriver(event.target.value);
                  }}
                />
              </Grid> */}
            </Grid>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign Up
            </Button>
            <Grid container justifyContent="flex-end">
              <Grid item>
                <Link href="/sign-in" variant="body2">
                  Already have an account? Sign in
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
        <Footer />
      </Container>
    </ThemeProvider>
  );
};

export default SignUp;
