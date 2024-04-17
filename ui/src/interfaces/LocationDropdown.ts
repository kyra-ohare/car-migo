export interface ILocationDropdown {
  id: string;
  label: string;
  name: string;
  value: any;
  onChange(
    field: string,
    value: any,
    shouldValidate?: boolean | undefined
  ): Promise<any>;
  formikTouched: boolean | undefined;
  formikErrors: string | undefined;
  datatestid: string;
}