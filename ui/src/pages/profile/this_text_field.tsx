import { CustomTextField } from '../../components';
import { ICustomTextFieldProps } from '../../interfaces';

export function ThisTextField(props: ICustomTextFieldProps) {
  return (
    <>
      <CustomTextField
        {...props}
        sx={{ mt: 2 }}
        InputProps={{
          readOnly: true,
        }}
      />
    </>
  );
}
