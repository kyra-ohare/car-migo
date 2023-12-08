import { useEffect, useState } from "react";
import {
  Box,
  Container,
  CssBaseline,
  Grid,
  createTheme,
  ThemeProvider,
  Typography,
  InputAdornment,
} from "@mui/material";
import InfoOutlined from "@mui/icons-material/InfoOutlined";
import DeleteIcon from "@mui/icons-material/Delete";
import {
  Footer,
  Loader,
  CustomButton,
  CustomTextField,
  CustomTooltip,
  CustomAlert,
} from "../../components";
import { CatchyMessage } from "../home/styled";
import { useNavigate } from "react-router-dom";
import navigation from "../../constants/navigation";
import { useGetProfile } from "../../hooks/usePlatformUser";
import { useMutation } from "@tanstack/react-query";
import { createPassenger, deletePassenger } from "../../hooks/usePassenger";
import { createDriver, deleteDriver } from "../../hooks/useDriver";

export default function Profile() {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [dob, setDob] = useState<Date>();
  const [email, setEmail] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [isPassenger, setIsPassenger] = useState<boolean>(false);
  const [isDriver, setIsDriver] = useState<boolean>(false);
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarSeverity, setSnackbarSeverity] = useState("success");

  const { status, data } = useGetProfile();

  useEffect(() => {
    // useEffect keeps an eye on data changes.
    console.log(data);
    if (status === "success") {
      const { firstName } = data;
      setFirstName(firstName);
      setFirstName(data.firstName);
      setLastName(data.lastName);
      setDob(parseDob(data.dob));
      setEmail(data.email);
      setPhoneNumber(data.phoneNumber);
      setIsDriver(data.driver);
      setIsPassenger(data.passenger);
    }
  }, [status, data]); // passing an empty array because I want it to render only once.

  const parseDob = (dob) => {
    const findT = dob.indexOf("T");
    const arrayDate = dob.substring(0, findT).split("-");
    return arrayDate[2] + "/" + arrayDate[1] + "/" + arrayDate[0];
  };

  const navigate = useNavigate();
  const signOut = () => {
    console.log("remove JWT from context");
    navigate(navigation.HOME_PAGE);
  };

  const deleteAccount = () => {
    console.log("remove account");
    navigate(navigation.HOME_PAGE);
  };

  const mustateDeletePassenger = useMutation({
    mutationFn: deletePassenger,
    onSuccess: (data) => {
      console.log("Success!", data);
    },
    onError: (error) => {
      console.error("Error deleting a passenger in Profile!", error);
    },
  });

  const mutateCreatePassenger = useMutation({
    mutationFn: createPassenger,
    onSuccess: (data) => {
      console.log("Success!", data);
    },
    onError: (error) => {
      console.error("Error creating a passenger in Profile!", error);
    },
  });

  const handlePassenger = () => {
    if (isPassenger) {
      console.error("boo");
      mustateDeletePassenger.mutate();
      setIsPassenger(false);
      setSnackbarMessage("Oh no! You're not a passenger anymore.");
      setSnackbarSeverity("error");
      setOpenSnackbar(true);
    } else if (!isPassenger) {
      mutateCreatePassenger.mutate();
      setIsPassenger(true);
      setSnackbarMessage("Yabba dabba doo! You've just become a passenger.");
      setSnackbarSeverity("success");
      setOpenSnackbar(true);
    } else {
      console.error("Error in Profile handlePassenger()");
    }
  };

  const mustateDeleteDriver = useMutation({
    mutationFn: deleteDriver,
    onSuccess: (data) => {
      console.log("Success!", data);
    },
    onError: (error) => {
      console.error("Error deleting a driver in Profile!", error);
    },
  });

  const mutateCreateDriver = useMutation({
    mutationFn: createDriver,
    onSuccess: (data) => {
      console.log("Success!", data);
    },
    onError: (error) => {
      console.error("Error creating a driver in Profile!", error);
    },
  });

  const handleDriver = () => {
    if (isDriver) {
      mustateDeleteDriver.mutate();
      setIsDriver(false);
      setSnackbarMessage("Oh no! You're not a driver anymore.");
      setSnackbarSeverity("error");
      setOpenSnackbar(true);
    } else if (!isDriver) {
      mutateCreateDriver.mutate({
        licenseNumber: "11111",
      });
      setIsDriver(true);
      setSnackbarMessage("Yippee! You've just become a driver.");
      setSnackbarSeverity("success");
      setOpenSnackbar(true);
    } else {
      console.error("Error in Profile handleDriver()");
    }
  };

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  const PassengerGrid = () => {
    return isPassenger === true ? (
      <>
        <Grid item xs>
          <Grid container justifyContent="center">
            <Typography variant="body1">You are a passenger</Typography>
            <InputAdornment position="start" sx={{ mt: 1.5 }}>
              <CustomTooltip
                icon={<InfoOutlined />}
                text="As a passenger, you can book journeys."
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
            label="Become a Passenger"
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
          <Grid container justifyContent="center">
            <Typography variant="body1">You are a driver</Typography>
            <InputAdornment position="start" sx={{ mt: 1.5 }}>
              <CustomTooltip
                icon={<InfoOutlined />}
                text="As a driver, you can create journeys."
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
          <CustomButton label="Become a Driver" onClick={handleDriver} />
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
  return status !== "success" ? (
    <Loader />
  ) : (
    <ThemeProvider theme={defaultTheme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box component="form" noValidate sx={{ mt: 3 }}>
          <CatchyMessage>About you</CatchyMessage>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <ThisTextField
                id="read-only-first-name"
                label="First Name"
                value={firstName}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <ThisTextField
                id="read-only-last-name"
                label="Last Name"
                value={lastName}
              />
            </Grid>
            <Grid item xs={12}>
              <ThisTextField
                id="read-only-dob"
                label="Date of Birth"
                value={dob}
              />
            </Grid>
            <Grid item xs={12}>
              <ThisTextField id="read-only-email" label="Email" value={email} />
            </Grid>
            <Grid item xs={12}>
              <ThisTextField
                id="read-only-phone-number"
                label="Phone Number"
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
                type="submit"
                label="Sign Out"
                sx={{ mt: 6, mb: 3 }}
                onClick={signOut}
              />
            </Grid>
            <Grid item xs={12}>
              <CustomButton
                type="submit"
                label="Delete my account"
                color="error"
                endIcon={<DeleteIcon />}
                onClick={deleteAccount}
              />
            </Grid>
          </Grid>
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
