import { Fragment } from 'react';
import { IconButton, Link } from '@mui/material';
import { StyledTooltip } from './styled';
import { ICustomTooltipProps } from '../../interfaces';

export default function CustomTooltip(props: ICustomTooltipProps) {
  const handleClick = () => {
    props.behaviour();
  };

  return (
    <StyledTooltip
      title={
        <Fragment>
          <p>{props.text}</p>
          <Link component='button' variant='caption' onClick={handleClick}>
            {props.link}
          </Link>
        </Fragment>
      }
    >
      <IconButton>{props.icon}</IconButton>
    </StyledTooltip>
  );
}
