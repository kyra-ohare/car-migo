import { ICustomTextFieldProps } from '../../interfaces';
import { StyledTextField } from './styled';

export default function CustomTextField(props: ICustomTextFieldProps) {
  return <StyledTextField fullWidth sx={{ mt: '20px' }} {...props} />;
}
