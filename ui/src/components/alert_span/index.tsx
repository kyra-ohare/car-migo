import { Alert, AlertTitle, Box } from '@mui/material';
import { CustomButton } from '..';
import { IAlertSpanProps } from '../../interfaces';

export default function AlertSpan(props: IAlertSpanProps) {
  return (
    <Box
      sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}
      data-testid='box-alert-span'
    >
      <Alert severity={props.severity} variant={props.variant}>
        <AlertTitle>
          <b>{props.title}</b>
        </AlertTitle>
        <b>{props.text}</b>
        <CustomButton
          label='Close'
          onClick={props.state}
          datatestid='close-button-box-alert-span'
        />
      </Alert>
    </Box>
  );
}
