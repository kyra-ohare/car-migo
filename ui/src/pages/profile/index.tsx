import { useEffect, useState } from "react";
import { Box, TextField } from "@mui/material";
import useGetProfile from "../../hooks/useGetPlatformUser";
import { Loader } from "../../components";

const Profile = () => {
  const { status, data } = useGetProfile();
  console.log("data", data);
  console.log("status", status);

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [dob, setDob] = useState<Date>();
  const [email, setEmail] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");

  useEffect(() => {
    // useEffect keeps an eye on data changes.
    if (status === "success") {
      const {firstName} = data;
      setFirstName(firstName);
      setFirstName(data.firstName);
      setLastName(data.lastName);
      setDob(data.dob);
      setEmail(data.email);
      setPhoneNumber(data.phoneNumber);
    }
  }, [status, data]); // passing an empty array because I want it to render only once.

  return (
    status !== 'success' ? (
    <Loader />
  ) : (
    <Box
      component="form"
      sx={{
        "& .MuiTextField-root": { m: 1, width: "25vw" },
      }}
      noValidate
      autoComplete="off"
    >
      <div>
        <TextField
          id="outlined-read-only-input"
          label="First Name"
          value={firstName}
          InputProps={{
            readOnly: true,
          }}
        />
      </div>
      <div>
        <TextField
          id="outlined-read-only-input"
          label="Last Name"
          value={lastName}
          InputProps={{
            readOnly: true,
          }}
        />
        <TextField
          id="outlined-read-only-input"
          label="Date of Birth"
          value={dob}
          InputProps={{
            readOnly: true,
          }}
        />
        <TextField
          id="outlined-read-only-input"
          label="Email"
          value={email}
          InputProps={{
            readOnly: true,
          }}
        />
        <TextField
          id="outlined-read-only-input"
          label="Phone number"
          value={phoneNumber}
          InputProps={{
            readOnly: true,
          }}
        />
      </div>
    </Box>
  )
  );
};

export default Profile;
