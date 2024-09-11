import { Alert, Snackbar } from '@mui/material';
import { IAlertPopUpProps } from '../../interfaces';

export default function AlertPopUp(props: IAlertPopUpProps) {
  return (
    <Snackbar
      open={props.open}
      onClose={props.onClose}
      autoHideDuration={6000}
      data-testid={props.datatestid}
    >
      <Alert
        onClose={props.onClose}
        severity={props.severity}
        sx={{ width: '100' }}
      >
        {props.message}
      </Alert>
    </Snackbar>
  );
}
