import { Link, Typography } from "@mui/material";

const Footer = (props: any) => {
  return (
    <Typography
      variant="body2"
      color="text.secondary"
      align="center"
      marginTop="5vh"
      {...props}
    >
      {"Copyright © "}
      <Link color="inherit" href="https://car-migo.com/">
        https://car-migo.com
      </Link>{" "}
      {new Date().getFullYear()}
      {"."}
    </Typography>
  );
};

export default Footer;
