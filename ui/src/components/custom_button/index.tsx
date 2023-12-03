import { styled } from "@mui/material/styles";
import { Button } from "@mui/material";

const BootstrapButton = styled(Button)({
  boxShadow: "5px 6px 5px rgba(0, 0, 0, 0.2)",
  textTransform: "none",
  fontSize: 16,
  padding: "6px 12px",
  border: "1px solid",
  lineHeight: 1.5,
  backgroundColor: "#00000",
  borderColor: "#808080",
  fontFamily: [
    "-apple-system",
    "BlinkMacSystemFont",
    '"Segoe UI"',
    "Roboto",
    '"Helvetica Neue"',
    "Arial",
    "sans-serif",
    '"Apple Color Emoji"',
    '"Segoe UI Emoji"',
    '"Segoe UI Symbol"',
  ].join(","),
  "&:hover": {
    // backgroundColor: "#0069d9",
    // borderColor: "#0062cc",
    boxShadow: "none",
  },
  "&:active": {
    boxShadow: "none",
    // backgroundColor: "#0062cc",
    // borderColor: "#005cbf",
  },
  // "&:focus": {
  //   boxShadow: "0 0 0 0.2rem rgba(0,123,255,.5)",
  // },
});

export default function CustomButton(props: any) {
  return (
    <BootstrapButton variant="contained" {...props}>
      {props.label}
    </BootstrapButton>
  );
}
