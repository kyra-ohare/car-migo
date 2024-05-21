import { Link, Typography } from '@mui/material';
import { appConstants } from '../../constants';

export default function Footer() {
  return (
    <Typography
      variant='body2'
      color='text.secondary'
      align='center'
      marginTop='5vh'
    >
      {'Copyright © '}
      <Link color='inherit' href={appConstants.appDomain}>
        {appConstants.appDomain}
      </Link>{' '}
      {new Date().getFullYear()}
    </Typography>
  );
}
