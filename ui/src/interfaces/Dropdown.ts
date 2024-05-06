import { Theme } from "@emotion/react";
import { SxProps } from "@mui/material";

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
  datatestid: string;
  // sx?:  SxProps<Theme> | undefined;
}


//(property) AutocompleteProps<Value, Multiple extends boolean | undefined, DisableClearable extends boolean | undefined, FreeSolo extends boolean | undefined, ChipComponent extends React.ElementType<any, keyof React.JSX.IntrinsicElements> = "div">.sx?: SxProps<Theme> | undefined