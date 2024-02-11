import { Alert, AlertTitle, Box } from "@mui/material";
import { CustomButton } from "..";

export default function AlertSpan(props: any) {
  return (
    <Box
      sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
      }}
    >
      <Alert severity={props.severity} variant={props.variant}>
        <AlertTitle>
          <b>{props.title}</b>
        </AlertTitle>
        <b>{props.text}</b>
        <CustomButton label="Close" onClick={props.state} />
      </Alert>
    </Box>
  );
}
