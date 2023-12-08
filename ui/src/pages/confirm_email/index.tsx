import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Box, InputAdornment } from "@mui/material";
import { CatchyMessage, WelcomeMessage } from "../home/styled";
import { EmailRounded } from "@mui/icons-material";
import { confirmUserEmail } from "../../hooks/usePlatformUser";
import { useMutation } from "@tanstack/react-query";
import { DialogBox, CustomButton, CustomTextField, CustomAlert } from "../../components";
import navigation from "../../constants/navigation";
import { useFormik } from "formik";
import * as Yup from "yup";

const validationSchema = Yup.object().shape({
  email: Yup.string().required("Email must not be empty."),
});

const initialValues = {
  email: "",
};

export default function ConfirmEmail() {
  const [openDialog, setOpenDialog] = useState<boolean>(false);
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  const formik = useFormik({
    initialValues: initialValues,
    validationSchema: validationSchema,
    onSubmit: (values) => {
      handleFormSubmit(values);
    },
    enableReinitialize: true,
  });

  const mutateConfirmEmail = useMutation({
    mutationFn: confirmUserEmail,
    onSuccess: (data) => {
      setOpenDialog(true);
    },
    onError: (error) => {
      const errorMsg = error.response?.data.message;
      if (errorMsg === "User is already active") {
        setSnackbarMessage("Yayyy! You have already confirmed your email.");
      } else {
        setSnackbarMessage(errorMsg);
      }
      setOpenSnackbar(true);
    },
  });

  const handleFormSubmit = (values: any) => {
    mutateConfirmEmail.mutate(values.email);
  };

  const dialogState = (data: React.SetStateAction<boolean>) => {
    setOpenDialog(data);
  };

  const navigate = useNavigate();

  const dialogRedirect = () => {
    navigate(navigation.SIGN_IN_PAGE);
  };

  if (openDialog) {
    return (
      <DialogBox
        open={openDialog}
        state={dialogState}
        title="Email confirmed"
        text="Let's sign in!"
        redirect={dialogRedirect}
      />
    );
  }

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  return (
    <Box
      component="form"
      onSubmit={formik.handleSubmit}
      sx={{
        "& .MuiTextField-root": { m: 1, width: "30vw" },
      }}
    >
      <WelcomeMessage>Confirm your email</WelcomeMessage>
      <CatchyMessage>
        You can enjoy all Car-Migo advantages after this confirmation.
      </CatchyMessage>
      <div>
        <CustomTextField
          id="confirm-email-address"
          label="Email Address"
          name="email"
          autoComplete="email"
          value={formik.values.email}
          onChange={formik.handleChange}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <EmailRounded />
              </InputAdornment>
            ),
          }}
          error={Boolean(formik.errors.email)}
          helperText={formik.touched.email && formik.errors.email}
        />
      </div>
      <div>
        <CustomButton label="Confirm Email" type="submit" sx={{ mt: 3 }} />
      </div>
      <CustomAlert
          open={openSnackbar}
          onClose={handleCloseSnackbar}
          severity="success"
          message={snackbarMessage}
        />
    </Box>
  );
}
