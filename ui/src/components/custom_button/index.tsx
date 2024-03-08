import { ICustomButtonProps } from '../../interfaces';
import { StyledButton } from './styled';

export default function CustomButton(props: ICustomButtonProps) {
  return (
    <StyledButton variant='contained' data-testid={props.dataTestId} {...props}>
      {props.label}
    </StyledButton>
  );
}
