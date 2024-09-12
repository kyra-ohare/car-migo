import { FormikErrors } from "formik";

export interface IBasicDateTimePickerProps {
  label: string;
  name: string;
  disableFuture?: boolean;
  disablePast?: boolean;
  value: string;
  onChange(
    field: string,
    value: string,
    shouldValidate?: boolean | undefined
  ): Promise<void> | Promise<FormikErrors<any>>;
  formikTouched: boolean | undefined;
  formikErrors: string | undefined;
  datatestid: string;
}
