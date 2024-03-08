import {
  DateValidationError,
  PickerChangeHandlerContext,
} from '@mui/x-date-pickers';

export interface IBasicDatePickerProps {
  label: string;
  name: string;
  disableFuture?: boolean;
  disablePast?: boolean;
  value: string | null;
  onChange(
    value: unknown,
    context: PickerChangeHandlerContext<DateValidationError>
  ): void;
  error: boolean | undefined;
  helperText: string | boolean | undefined;
  dataTestId: string;
}



