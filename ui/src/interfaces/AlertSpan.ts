import { AlertColor } from '@mui/material';
import { OverridableStringUnion } from '@mui/types';

export interface IAlertSpanProps {
  severity: AlertColor;
  variant: OverridableStringUnion<'filled' | 'outlined' | 'standard'>;
  title: string;
  text: string;
  state: () => void;
}
