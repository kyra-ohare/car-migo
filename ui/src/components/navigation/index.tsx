import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  AppBar,
  Avatar,
  Box,
  Container,
  IconButton,
  Menu,
  MenuItem,
  Toolbar,
  Tooltip,
  Typography,
} from '@mui/material';
import { Menu as MenuIcon } from '@mui/icons-material';
import { appConstants, navigation, pageMapper } from '../../constants';
import { useTokens } from '../../hooks/useTokens';
import { useAuthStore } from '../../hooks/useAuthStore';
import ResponsiveAppBar from './responsive_app_bar';
import { role } from '../../constants/navigation';

const settings = [
  appConstants.profile,
  appConstants.createJourneys,
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
    if (page === appConstants.profile) {
      navigateTo(navigation.PROFILE_PAGE);
    }
    if (page === appConstants.createJourneys) {
      navigateTo(navigation.CREATE_JOURNEYS);
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
              sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}
              data-testid='row-menu'
            >
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
                data-testid='menu-appbar-items'
              >
                {pageMapper
                  .filter(
                    (page) =>
                      (isAuthorized === true && page.role == role.authorized) ||
                      (isAuthorized === false && page.role == role.unauthorized)
                  )
                  .map((page) => (
                    <MenuItem
                      key={page.label}
                      onClick={handleCloseNavMenu}
                      data-testid={'menu-item-' + page.label}
                    >
                      <Link to={page.path!}>{page.label} </Link>
                    </MenuItem>
                  ))}
              </Menu>
            </Box>

            <ResponsiveAppBar handleNav={handleNav} />

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
