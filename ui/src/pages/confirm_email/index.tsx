import { SetStateAction, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Box, InputAdornment } from "@mui/material";
import { CatchyMessage, WelcomeMessage } from "../home/styled";
import { EmailRounded } from "@mui/icons-material";
import { confirmUserEmail } from "../../hooks/usePlatformUser";
import { useMutation } from "@tanstack/react-query";
import { DialogBox, CustomButton, CustomTextField } from "../../components";
import navigation from "../../constants/navigation";

export default function ConfirmEmail() {
  const [email, setEmail] = useState("");
  const [isError, setIsError] = useState<boolean>(false);
  const [helperText, setHelperText] = useState("");
  const navigate = useNavigate();
  const [openDialog, setOpenDialog] = useState<boolean>(false);

  const mutateConfirmEmail = useMutation({
    mutationFn: confirmUserEmail,
    onSuccess: (data) => {
      console.log("Email confirmed!", data);
      setOpenDialog(true);
    },
    onError: (error) => {
      const errorMsg = error.response?.data.message;
      setIsError(true);
      if (errorMsg === "User is already active") {
        setHelperText("Yayyy! You have already confirmed your email.");
      } else {
        setHelperText(errorMsg);
      }
    },
  });

  const handleSubmit = () => {
    mutateConfirmEmail.mutate(email);
    console.log(email);
  };

  const dialogState = (data: React.SetStateAction<boolean>) => {
    setOpenDialog(data);
  };

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

  return (
    <Box
      component="form"
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
          onChange={(event: { target: { value: SetStateAction<string> } }) => {
            setEmail(event.target.value);
          }}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <EmailRounded />
              </InputAdornment>
            ),
          }}
          error={isError}
          helperText={isError ? helperText : ""}
        />
      </div>
      <div>
        <CustomButton
          label="Confirm Email"
          onClick={handleSubmit}
          sx={{ mt: 3 }}
        />
      </div>
    </Box>
  );
}
