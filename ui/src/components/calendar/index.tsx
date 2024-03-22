import { StyledDatePicker } from "./styled";
import { IBasicDatePickerProps } from "../../interfaces";
import { appConstants } from "../../constants";

export default function BasicDatePicker(props: IBasicDatePickerProps) {
  return (
    <StyledDatePicker
      format={appConstants.dateFormat}
      data-testid={props.datatestid}
      {...props}
    />
  );
}
