import { ICustomTextFieldProps } from "../../interfaces";
import { StyledTextField } from "./styled";

export default function CustomTextField(props: ICustomTextFieldProps) {
  return (
    <StyledTextField
      fullWidth
      sx={{ mt: "20px" }}
      data-testid={props.datatestid}
      {...props}
    />
  );
}

// TODO:
// declared 'data-testid' => passes but there is a Warning when running. E.g.: confirm_email.test.tsx:
//         React does not recognize the `datatestid` prop on a DOM element.

// not declared => fails

// problem with the spreading props ({...props})
