import { ButtonPropsColorOverrides } from '@mui/material';
import { OverridableStringUnion } from '@mui/types';
import { ReactNode } from 'react';

export interface ICustomButtonProps {
  label: string;
  type?: 'reset' | 'button' | 'submit';
  onClick?: () => void;
  sx?: { mt?: number; mb?: number };
  fullWidth?: boolean;
  color?: OverridableStringUnion<
    | 'error'
    | 'success'
    | 'inherit'
    | 'info'
    | 'warning'
    | 'primary'
    | 'secondary',
    ButtonPropsColorOverrides
  >;
  endIcon?: ReactNode;
}
