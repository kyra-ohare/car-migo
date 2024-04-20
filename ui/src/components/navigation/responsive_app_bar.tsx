import { Box, Button, Typography } from '@mui/material';
import Car from '../../assets/car.png';
import { pageMapper } from '../../constants';
import { role } from '../../constants/navigation';
import { useAuthStore } from './../../hooks/useAuthStore';

export default function ResponsiveAppBar(props: any) {
  const { isAuthorized } = useAuthStore();
  return (
    <>
      <Box
        component='img'
        sx={{
          height: 60,
          width: 100,
          maxHeight: { xs: 60, md: 60 },
          maxWidth: { xs: 100, md: 100 },
          marginRight: '10px',
        }}
        alt='car-migo'
        src={Car}
        data-testid='car-image-img'
      />
      <Typography
        variant='h5'
        noWrap
        component='a'
        href='/'
        sx={{
          mr: 2,
          display: { xs: 'flex', md: 'none' },
          flexGrow: 1,
          fontFamily: 'monospace',
          fontWeight: 700,
          letterSpacing: '.3rem',
          color: 'inherit',
          textDecoration: 'none',
        }}
      >
        Car-Migo2
      </Typography>
      <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
        {pageMapper
          .filter(
            (page) =>
              (isAuthorized === true && page.role == role.authorized) ||
              (isAuthorized === false && page.role == role.unauthorized)
          )
          .map((page) => (
            <Button
              key={page.label}
              onClick={() => props.handleNav(page.path!)}
              sx={{ my: 2, color: '#ffffff', display: 'block' }}
              data-testid={'row-menu-' + page.label}
            >
              {page.label}
            </Button>
          ))}
      </Box>
    </>
  );
}
