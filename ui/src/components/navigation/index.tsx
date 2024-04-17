import React from 'react';
import { useNavigate } from 'react-router-dom';
import {
  AppBar,
  Avatar,
  Box,
  Button,
  Container,
  IconButton,
  Menu,
  MenuItem,
  Toolbar,
  Tooltip,
  Typography,
} from '@mui/material';
import Car from '../../assets/car.png';
import { appConstants, navigation, pageMapper } from '../../constants';
import { useTokens } from '../../hooks/useTokens';
import { useAuthStore } from '../../hooks/useAuthStore';

const settings = [
  appConstants.profile,
  appConstants.yourJourneys,
  appConstants.logout,
];

export default function NavBar({ children }: { children: React.ReactNode }) {
  const [anchorElNav, setAnchorElNav] = React.useState<null | HTMLElement>(
    null
  );
  const [anchorElUser, setAnchorElUser] = React.useState<null | HTMLElement>(
    null
  );
  const { clearLocalStorageTokens } = useTokens();
  const navigate = useNavigate();
  const { isAuthorized } = useAuthStore();

  const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleNav = (path: string) => {
    navigate(path);
    setAnchorElNav(null);
  };

  const navigateTo = (path: string) => {
    navigate(path);
  };

  const handleCloseUserMenu = (page: string) => {
    setAnchorElUser(null);
    if (page === appConstants.profile) {
      navigateTo(navigation.PROFILE_PAGE);
    }
    if (page === appConstants.yourJourneys) {
      navigateTo(navigation.YOUR_JOURNEYS);
    }
    if (page === appConstants.logout) {
      clearLocalStorageTokens();
    }
  };

  return (
    <>
      <AppBar>
        <Container maxWidth='xl'>
          <Toolbar disableGutters>
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
              variant='h6'
              noWrap
              component='a'
              href='/'
              sx={{
                mr: 2,
                display: { xs: 'none', md: 'flex' },
                fontFamily: 'monospace',
                fontWeight: 700,
                letterSpacing: '.3rem',
                color: 'inherit',
                textDecoration: 'none',
              }}
              data-testid='car-migo-title'
            >
              Car-Migo
            </Typography>
            <Box
              sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}
              data-testid='row-menu'
            >
              {pageMapper.map((page) => (
                <Button
                  key={page.label}
                  onClick={() => handleNav(page.path!)}
                  sx={{ my: 2, color: '#ffffff', display: 'block' }}
                  data-testid={'row-menu-' + page.label}
                >
                  {page.label}
                </Button>
              ))}
            </Box>

            {isAuthorized === true && (
              <>
                <Box sx={{ flexGrow: 0 }} data-testid='authorized-settings'>
                  <Tooltip title='Open settings'>
                    <IconButton
                      onClick={handleOpenUserMenu}
                      sx={{ p: 0 }}
                      data-testid='menu-settings-button'
                    >
                      <Avatar alt='Remy Sharp' />
                    </IconButton>
                  </Tooltip>
                  <Menu
                    sx={{ mt: '45px' }}
                    id='menu-appbar'
                    anchorEl={anchorElUser}
                    anchorOrigin={{
                      vertical: 'top',
                      horizontal: 'right',
                    }}
                    keepMounted
                    transformOrigin={{
                      vertical: 'top',
                      horizontal: 'right',
                    }}
                    open={Boolean(anchorElUser)}
                    onClose={handleCloseUserMenu}
                  >
                    {settings.map((setting) => (
                      <MenuItem
                        key={setting}
                        onClick={() => handleCloseUserMenu(setting)}
                        data-testid={'settings-menu-' + setting}
                      >
                        <Typography textAlign='center'>{setting}</Typography>
                      </MenuItem>
                    ))}
                  </Menu>
                </Box>
              </>
            )}
          </Toolbar>
        </Container>
      </AppBar>
      <Toolbar />
      {children}
    </>
  );
}
