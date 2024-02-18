import { Fragment } from 'react';
import { IconButton, Link } from '@mui/material';
import { BootstrapTooltip } from './styled';

export default function CustomTooltip(props: any) {
  const handleClick = () => {
    props.behaviour();
  };

  return (
    <BootstrapTooltip
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
    </BootstrapTooltip>
  );
}
