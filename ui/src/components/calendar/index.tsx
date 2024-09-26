import { StyledDatePicker } from './styled';
import { IBasicDatePickerProps } from '../../interfaces';
import { appConstants } from '../../constants';
import dayjs, { Dayjs } from 'dayjs';

export default function BasicDatePicker(props: IBasicDatePickerProps) {
  return (
    <StyledDatePicker
      {...props}
      value={props.value ? dayjs(props.value) : null}
      onChange={(value: string | number | Date | Dayjs | null | undefined) =>
        props.onChange(props.name, dayjs(value).toISOString() || '', true)
      }
      format={appConstants.dateFormat}
      slotProps={{
        textField: {
          inputProps: {
            'data-testid': props.datatestid,
          },
          error: props.formikTouched && Boolean(props.formikErrors),
          helperText: props.formikTouched && props.formikErrors,
        },
      }}
    />
  );
}
