import { ICustomTextFieldProps } from '../../interfaces';

export default function Text(props: ICustomTextFieldProps) {
  return (
    <div>
      <p>{props.text}</p>
    </div>
  );
}
