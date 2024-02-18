import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
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
import { LogoDev } from '@mui/icons-material';
import MenuIcon from '@mui/icons-material/Menu';
import navigation from '../../constants/navigation';
import useTokens from '../../utils/tokenStore';
import { useAuthStore } from '../../utils/authStore';
import constants from '../../constants/app_constants';
import Car from '../../assets/car.png';

const pages = [
  { label: 'Home', path: navigation.HOME_PAGE },
  { label: 'Profile', path: navigation.PROFILE_PAGE },
  { label: 'Confirm email', path: navigation.CONFIRM_EMAIL_PAGE },
  { label: 'Playground', path: navigation.PLAYGROUND },
];

const settings = [constants.profile, constants.your_journeys, constants.logout];

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

  const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElNav(event.currentTarget);
  };
  const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
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
    if (page === constants.profile) {
      navigateTo(navigation.PROFILE_PAGE);
    }
    if (page === constants.logout) {
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
            >
              Car-Migo
            </Typography>

            <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
              <IconButton
                size='large'
                aria-label='account of current user'
                aria-controls='menu-appbar'
                aria-haspopup='true'
                onClick={handleOpenNavMenu}
                color='inherit'
              >
                <MenuIcon />
              </IconButton>
              <Menu
                id='menu-appbar'
                anchorEl={anchorElNav}
                anchorOrigin={{
                  vertical: 'bottom',
                  horizontal: 'left',
                }}
                keepMounted
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'left',
                }}
                open={Boolean(anchorElNav)}
                onClose={handleCloseNavMenu}
                sx={{
                  display: { xs: 'block', md: 'none' },
                }}
              >
                {pages.map((page) => (
                  <MenuItem key={page.label} onClick={handleCloseNavMenu}>
                    <Link to={page.path!}>{page.label}</Link>
                  </MenuItem>
                ))}
              </Menu>
            </Box>
            <LogoDev sx={{ display: { xs: 'flex', md: 'none' }, mr: 1 }} />
            <Typography
              variant='h5'
              noWrap
              component='a'
              href=''
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
              LOGO
            </Typography>
            <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
              {pages.map((page) => (
                <Button
                  key={page.label}
                  onClick={() => handleNav(page.path!)}
                  sx={{ my: 2, color: 'white', display: 'block' }}
                >
                  {page.label}
                </Button>
              ))}
            </Box>

            {isAuthorized === true && (
              <>
                <Box sx={{ flexGrow: 0 }}>
                  <Tooltip title='Open settings'>
                    <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
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
