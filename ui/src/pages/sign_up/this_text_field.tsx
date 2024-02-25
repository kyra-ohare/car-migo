import { CustomTextField } from '../../components';
import { ICustomTextFieldProps } from '../../interfaces';

export function ThisTextField(props: ICustomTextFieldProps) {
  return (
    <>
      <CustomTextField sx={{ mt: 1 }} {...props} />
    </>
  );
}
