export const navigation = {
  CONFIRM_EMAIL_PAGE: '/confirm-email',
  FORGOT_PASSWORD_PAGE: '/forgot-password',
  HOME_PAGE: '/home',
  PROFILE_PAGE: '/profile',
  SIGN_IN_PAGE: '/sign-in',
  SIGN_UP_PAGE: '/sign-up',
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
  { label: 'Playground', path: navigation.PLAYGROUND, role: role.authorized },
  { label: 'Playground', path: navigation.PLAYGROUND, role: role.unauthorized },
  {
    label: 'Forgot Password',
    path: navigation.FORGOT_PASSWORD_PAGE,
    role: role.unauthorized,
  },
  {
    label: 'Your Journeys',
    path: navigation.YOUR_JOURNEYS,
    role: role.authorized,
  },
  { label: 'Sign In', path: navigation.SIGN_IN_PAGE, role: role.unauthorized },
  { label: 'Sign Up', path: navigation.SIGN_UP_PAGE, role: role.unauthorized },
];
