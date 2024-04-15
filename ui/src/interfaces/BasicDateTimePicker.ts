export interface IBasicDateTimePickerProps {
  label: string;
  name: string;
  disableFuture?: boolean;
  disablePast?: boolean;
  value: string;
  onChange(
    field: string,
    value: any,
    shouldValidate?: boolean | undefined
  ): Promise<any>;
  formikTouched: boolean | undefined;
  formikErrors: string | undefined;
  datatestid: string;
}
