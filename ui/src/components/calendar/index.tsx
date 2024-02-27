import { StyledDatePicker } from './styled';
import { IBasicDatePickerProps } from '../../interfaces';
import { appConstants } from '../../constants';

export default function BasicDatePicker(props: IBasicDatePickerProps) {
  return <StyledDatePicker {...props} format={appConstants.dateFormat} />;
}
