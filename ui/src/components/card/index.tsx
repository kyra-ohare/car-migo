import { ReactNode } from "react";
import { Grid, Typography } from "@mui/material";

interface ICard {
  id: string;
  label: string;
  icon: ReactNode;
  imageAlt: string;
  title: string;
  text: ReactNode;
}

export default function ActionAreaCard(props: ICard) {
  return (
    <Grid container sx={{ marginTop: "20px", marginRight: "50px" }}>
      <Grid
        item
        xs={3}
        sx={{ display: "flex", justifyContent: "center", alignItems: "center" }}
      >
        {props.icon}
      </Grid>
      <Grid item xs={9}>
        <Typography gutterBottom variant="h5" component="div">
          {props.title}
        </Typography>
        <Typography variant="body1" color="text.primary">
          {props.text}
        </Typography>
      </Grid>
    </Grid>
  );
}

// Grid: think of columns from a spreadsheet. Its default number of columns is 12 so 3 columns from the first Grid + 9 from the second = 12.
