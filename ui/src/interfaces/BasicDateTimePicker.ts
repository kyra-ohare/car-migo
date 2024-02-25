export interface IBasicDateTimePickerProps {
  value: string;
  label: string;
  name: string;
  onChange(field: string, value: any, shouldValidate?: boolean | undefined): Promise<any>;
  formikTouched: boolean | undefined;
  formikErrors: string | undefined;
}
