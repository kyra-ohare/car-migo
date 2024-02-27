import dayjs, { Dayjs } from 'dayjs';
import { StyledDateTimePicker } from './styled';
import { IBasicDateTimePickerProps } from '../../interfaces';
import { appConstants } from '../../constants';

export default function BasicDateTimePicker(props: IBasicDateTimePickerProps) {
  return (
    <StyledDateTimePicker
      {...props}
      value={props.value ? dayjs(props.value) : null}
      onChange={(value: string | number | Date | Dayjs | null | undefined) =>
        props.onChange(props.name, dayjs(value).toISOString() || '', true)
      }
      label={props.label}
      format={appConstants.dateTimeFormat}
      slotProps={{
        textField: {
          error: props.formikTouched && Boolean(props.formikErrors),
          helperText: props.formikTouched && props.formikErrors,
        },
      }}
    />
  );
}
