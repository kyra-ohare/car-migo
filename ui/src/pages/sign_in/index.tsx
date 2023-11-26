import React, { useEffect } from "react";
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
  Checkbox,
  FormControlLabel,
} from "@mui/material";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import { DialogBox, Footer } from "../../components";
import { useState } from "react";
import authenticationRequest from "../../hooks/useSignIn";

// TODO: I can sign in but i can't send JWT to axios

const SignIn = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [openDialog, setOpenDialog] = useState<Boolean>(false);
  const [dialogText, setDialogText] = useState("");
  const dialogState = (data: React.SetStateAction<Boolean>) => {
    setOpenDialog(data);
  };

  const mutateUser = useMutation({
    mutationFn: authenticationRequest,
    onSuccess: (data) => {
      console.log("Success!", data.jwt);
    },
    onError: (error) => {
      const errorMsg = error.response?.data.message;
      setOpenDialog(true);
      setDialogText(errorMsg);
    },
  });

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    mutateUser.mutate({
      email: email,
      password: password,
    });
  };

  // TODO remove, this demo shouldn't need to reset the theme.
  const defaultTheme = createTheme();
  if (openDialog) {
    return (
      <DialogBox
        open={openDialog}
        dialogState={dialogState}
        dialogTitle="Whoops!"
        dialogText={dialogText}
      />
    );
  }
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
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              autoFocus
              value={email}
              onChange={(event) => {
                setEmail(event.target.value);
              }}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
              value={password}
              onChange={(event) => {
                setPassword(event.target.value);
              }}
            />
            <FormControlLabel
              control={<Checkbox value="remember" color="primary" />}
              label="Remember me"
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign In
            </Button>
            <Grid container>
              <Grid item xs>
                <Link href="/" variant="body2">
                  Forgot password?
                </Link>
              </Grid>
              <Grid item>
                <Link href="/sign-up" variant="body2">
                  Don't have an account? Sign Up
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

export default SignIn;
