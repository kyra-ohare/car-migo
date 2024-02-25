import { ChangeEvent } from 'react';
import {
  FilledInputProps,
  OutlinedInputProps,
  InputProps,
} from '@mui/material';

export interface ICustomTextFieldProps {
  id?: string;
  label?: string;
  name?: string;
  text?: string;
  autoComplete?: string;
  sx?: { mt?: number };
  type?: string;
  required?: boolean;
  value?: string;
  onChange?: {
    (e: ChangeEvent<Element>): void;
    <T_1 = string | ChangeEvent<Element>>(
      field: T_1
    ): T_1 extends ChangeEvent ? void : (e: string | ChangeEvent) => void;
  };
  error?: boolean | undefined;
  helperText?: string | boolean | undefined;
  InputProps?:
    | Partial<FilledInputProps>
    | Partial<OutlinedInputProps>
    | Partial<InputProps>;
}
