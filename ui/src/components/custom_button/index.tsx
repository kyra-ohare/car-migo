import { BootstrapButton } from './styled';

export default function CustomButton(props: any) {
  return (
    <BootstrapButton variant='contained' {...props}>
      {props.label}
    </BootstrapButton>
  );
}
