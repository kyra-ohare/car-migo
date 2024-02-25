import { Routes, Route, Navigate } from 'react-router-dom';
import { navigation } from '../../constants';
import {
  ConfirmEmail,
  ForgotPassword,
  Homepage,
  Playground,
  Profile,
  SignIn,
  SignUp,
} from '../../pages';

export const AuthenticatedRoutes = (
  <Routes>
    <Route path='*' element={<Navigate to={navigation.HOME_PAGE} replace />} />
    <Route path='/' element={<Homepage />} />
    <Route path={navigation.HOME_PAGE} element={<Homepage />} />
    <Route path={navigation.PROFILE_PAGE} element={<Profile />} />
    <Route path={navigation.PLAYGROUND} element={<Playground />} />
  </Routes>
);

export const UnauthenticatedRoutes = (
  <Routes>
    <Route path='*' element={<Navigate to={navigation.HOME_PAGE} replace />} />
    <Route path='/' element={<Homepage />} />
    <Route path={navigation.HOME_PAGE} element={<Homepage />} />
    <Route path={navigation.SIGN_UP_PAGE} element={<SignUp />} />
    <Route path={navigation.SIGN_IN_PAGE} element={<SignIn />} />
    <Route path={navigation.CONFIRM_EMAIL_PAGE} element={<ConfirmEmail />} />
    <Route path={navigation.PLAYGROUND} element={<Playground />} />
    <Route
      path={navigation.FORGOT_PASSWORD_PAGE}
      element={<ForgotPassword />}
    />
  </Routes>
);
