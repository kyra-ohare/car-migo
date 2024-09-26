import { ICustomNumberFieldProps } from '../../interfaces';
import { StyledNumberField } from './styled';

export default function CustomNumberField(props: ICustomNumberFieldProps) {
  return (
    <StyledNumberField
      fullWidth
      sx={{ mt: 2 }}
      data-testid={props.datatestid}
      slotProps={{
        htmlInput: {
          min: props.min,
          max: props.max,
          'data-testid': `${props.datatestid}-input`,
        },
      }}
      {...props}
    />
  );
}
