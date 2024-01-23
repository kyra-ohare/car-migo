import { Fragment } from "react";
import {
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
} from "@mui/material";
import { CustomButton } from "..";

export default function DialogBox(props: any) {
  const handleClose = () => {
    props.state(false);
    props.redirect();
  };

  return (
    <Fragment>
      <Dialog
        open={props.open}
        onClose={handleClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">{props.title}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description" color="black">
            {props.text}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <CustomButton label="OK" onClick={handleClose} />
        </DialogActions>
      </Dialog>
    </Fragment>
  );
}