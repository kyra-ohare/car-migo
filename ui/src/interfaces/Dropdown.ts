export interface IDropdown {
  id: string;
  label: string;
  name: string;
  value: any;
  options: any;
  selectedOption: string;
  setSelectedOption: React.Dispatch<React.SetStateAction<string>>;
  onChange(
    field: string,
    value: any,
    shouldValidate?: boolean | undefined
  ): Promise<any>;
  formikTouched: boolean | undefined;
  formikErrors: string | undefined;
  widthStyle: string | number;
  mrStyle: string | number;
  datatestid: string;
}
