import { FormikErrors } from "formik";
import { IJourneyCreation, ILocations } from ".";

export interface IDropdown {
  id: string;
  label: string;
  name: string;
  value: any;
  options: ILocations[];
  selectedOption: string;
  setSelectedOption: React.Dispatch<React.SetStateAction<string>>;
  onChange(
    field: string,
    value: IJourneyCreation,
    shouldValidate?: boolean | undefined
  ): Promise<void> | Promise<FormikErrors<IJourneyCreation>>;
  formikTouched: boolean | undefined;
  formikErrors: string | undefined;
  widthStyle?: string | number;
  mrStyle?: string | number;
  datatestid: string;
}
