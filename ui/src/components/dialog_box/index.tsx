import { Fragment } from 'react';
import {
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
} from '@mui/material';
import { CustomButton } from '..';
import { IDialogBoxProps } from '../../interfaces';

export default function DialogBox(props: IDialogBoxProps) {
  const handleClose = () => {
    props.state(false);
    props.redirect();
  };

  return (
    <Fragment>
      <Dialog
        open={props.open}
        onClose={handleClose}
        aria-labelledby='alert-dialog-title'
        aria-describedby='alert-dialog-description'
        data-testid={props.datatestid}
      >
        <DialogTitle id='alert-dialog-title'>{props.title}</DialogTitle>
        <DialogContent>
          <DialogContentText id='alert-dialog-description' color='#000000'>
            {props.text}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <CustomButton
            label='OK'
            onClick={handleClose}
            datatestid='ok-button'
          />
        </DialogActions>
      </Dialog>
    </Fragment>
  );
}
