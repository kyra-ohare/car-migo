import { appConstants } from './app_constants';

export const navigation = {
  CONFIRM_EMAIL_PAGE: '/confirm-email',
  FORGOT_PASSWORD_PAGE: '/forgot-password',
  HOME_PAGE: '/home',
  PROFILE_PAGE: '/profile',
  SIGN_IN_PAGE: '/sign-in',
  SIGN_UP_PAGE: '/sign-up',
  CREATE_JOURNEYS: '/create-journeys',
  YOUR_JOURNEYS: '/your-journeys',
  PLAYGROUND: '/playground',
};

export const role = {
  authorized: 'authorized',
  unauthorized: 'unauthorized',
};

export const pageMapper = [
  { label: 'Home', path: navigation.HOME_PAGE, role: role.unauthorized },
  { label: 'Home', path: navigation.HOME_PAGE, role: role.authorized },
  { label: 'Profile', path: navigation.PROFILE_PAGE, role: role.authorized },
  {
    label: 'Confirm email',
    path: navigation.CONFIRM_EMAIL_PAGE,
    role: role.unauthorized,
  },
  {
    label: 'Forgot Password',
    path: navigation.FORGOT_PASSWORD_PAGE,
    role: role.unauthorized,
  },
  {
    label: appConstants.createJourneys,
    path: navigation.CREATE_JOURNEYS,
    role: role.authorized,
  },
  {
    label: appConstants.yourJourneys,
    path: navigation.YOUR_JOURNEYS,
    role: role.authorized,
  },
  { label: 'Sign In', path: navigation.SIGN_IN_PAGE, role: role.unauthorized },
  { label: 'Sign Up', path: navigation.SIGN_UP_PAGE, role: role.unauthorized },
];
