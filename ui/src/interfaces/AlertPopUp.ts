import { AlertColor } from '@mui/material';

export interface IAlertPopUpProps {
  open: boolean;
  onClose: () => void;
  severity: AlertColor;
  message: string;
}
