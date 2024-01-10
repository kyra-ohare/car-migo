import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useMutation } from "@tanstack/react-query";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import { createUser } from "../../hooks/usePlatformUser";
import navigation from "../../constants/navigation";
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
  DialogBox,
  CustomAlert,
} from "../../components";
import { useFormik } from "formik";
import * as Yup from "yup"; // Yup is a schema builder for runtime value parsing and validation
import dayjs from "dayjs";
import validation from "../../constants/validation";
import http_status from "../../constants/http_status";

const validationSchema = Yup.object().shape({
  firstName: Yup.string().required("First name must not be empty."),
  lastName: Yup.string().required("Last name must not be empty."),
  // dob: Yup.date().required("Date of birth must not be empty."), // todo
  phoneNumber: Yup.string().required("Phone number must not be empty."),
  email: Yup.string()
    .email("Invalid email format.")
    .min(validation.EMAIL_MIN_SIZE)
    .max(validation.EMAIL_MAX_SIZE)
    .required("Email must not be empty."),
  password: Yup.string()
    .matches(validation.PASSWORD_RULE, validation.VALID_PASSWORD_MESSAGE)
    .required("Password must not be empty."),
  confirmPassword: Yup.string()
    .oneOf([Yup.ref("password")], "Passwords must match.")
    .required("Confirm your password."),
});

const initialValues = {
  firstName: "",
  lastName: "",
  dob: null,
  email: "",
  phoneNumber: "",
  password: "",
  confirmPassword: "",
};

export default function SignUp() {
  const dateToday = dayjs();

  const [openDialog, setOpenDialog] = useState<boolean>(false);
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  // useFormik is a hook for building forms
  const formik = useFormik({
    initialValues: initialValues,
    validationSchema: validationSchema,
    onSubmit: (values) => {
      handleFormSubmit(values);
    },
    enableReinitialize: true,
  });

  const mutateUser = useMutation({
    mutationFn: createUser,
    onSuccess: (data) => {
      setOpenDialog(true);
    },
    onError: (error) => {
      const data = error.response?.data;
      if (data.status === http_status.CONFLICT) {
        setSnackbarMessage("Oh no! " + data.message);
        setOpenSnackbar(true);
      }
    },
  });

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const handleFormSubmit = (values: any) => {
    mutateUser.mutate({
      // firstName: "My",
      // lastName: "Test",
      dob: "1960-02-26T00:00:00Z",
      // phoneNumber: "028657345912",
      // email: "my.test@example.com",
      // password: "Pass1234!",

      firstName: values.firstName,
      lastName: values.lastName,
      // dob: values.dob,
      phoneNumber: values.phoneNumber,
      email: values.email,
      password: values.password,
      passenger: false,
      driver: false,
    });
  };

  const dialogState = (data: React.SetStateAction<boolean>) => {
    setOpenDialog(data);
  };

  const navigate = useNavigate();

  const dialogRedirect = () => {
    navigate(navigation.CONFIRM_EMAIL_PAGE);
  };

  if (openDialog) {
    return (
      <DialogBox
        open={openDialog}
        state={dialogState}
        title="Account created"
        text="You should receive an email to confirm it but, in the meantime, just confirm it here 😊"
        redirect={dialogRedirect}
      />
    );
  }

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
            onSubmit={formik.handleSubmit}
            sx={{ mt: 3 }}
          >
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <ThisTextField
                  id="sign-up-first-name"
                  label="First Name"
                  name="firstName"
                  required
                  autoComplete="first-name"
                  value={formik.values.firstName}
                  onChange={formik.handleChange}
                  error={
                    formik.touched.firstName && Boolean(formik.errors.firstName)
                  }
                  helperText={
                    formik.touched.firstName && formik.errors.firstName
                  }
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <ThisTextField
                  id="sign-up-last-name"
                  label="Last Name"
                  name="lastName"
                  required
                  autoComplete="last-name"
                  value={formik.values.lastName}
                  onChange={formik.handleChange}
                  error={
                    formik.touched.lastName && Boolean(formik.errors.lastName)
                  }
                  helperText={formik.touched.lastName && formik.errors.lastName}
                />
              </Grid>
              <Grid item xs={12}>
                <BasicDatePicker
                  label="Date of Birth *"
                  name="dob"
                  views={["day", "month", "year"]}
                  value={formik.values.dob || null}
                  onChange={(value: unknown) =>
                    formik.setFieldValue("dob", value, true)
                  }
                  error={formik.touched.dob && Boolean(formik.errors.dob)}
                  helperText={formik.touched.dob && formik.errors.dob}
                />
              </Grid>
              <Grid item xs={12}>
                <ThisTextField
                  id="sign-up-phone-number"
                  label="Phone Number"
                  name="phoneNumber"
                  required
                  autoComplete="phone-number"
                  value={formik.values.phoneNumber}
                  onChange={formik.handleChange}
                  error={
                    formik.touched.phoneNumber &&
                    Boolean(formik.errors.phoneNumber)
                  }
                  helperText={
                    formik.touched.phoneNumber && formik.errors.phoneNumber
                  }
                />
              </Grid>
              <Grid item xs={12}>
                <ThisTextField
                  id="sign-up-email-address"
                  label="Email Address"
                  name="email"
                  required
                  autoComplete="email"
                  value={formik.values.email}
                  onChange={formik.handleChange}
                  error={formik.touched.email && Boolean(formik.errors.email)}
                  helperText={formik.touched.email && formik.errors.email}
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
                  value={formik.values.password}
                  onChange={formik.handleChange}
                  error={
                    formik.touched.password && Boolean(formik.errors.password)
                  }
                  helperText={formik.touched.password && formik.errors.password}
                />
              </Grid>
              <Grid item xs={12}>
                <ThisTextField
                  id="sign-up-confirm-password"
                  label="Confirm Password"
                  name="confirmPassword"
                  type="password"
                  autoComplete="password"
                  required
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
          severity="info"
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
