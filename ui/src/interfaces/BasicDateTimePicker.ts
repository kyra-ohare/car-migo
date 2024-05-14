import { FormikErrors } from "formik";
import { IJourneyCreation } from ".";

export interface IBasicDateTimePickerProps {
  label: string;
  name: string;
  disableFuture?: boolean;
  disablePast?: boolean;
  value: string;
  onChange(
    field: string,
    value: IJourneyCreation,
    shouldValidate?: boolean | undefined
  ): Promise<void> | Promise<FormikErrors<IJourneyCreation>>;
  formikTouched: boolean | undefined;
  formikErrors: string | undefined;
  datatestid: string;
}
