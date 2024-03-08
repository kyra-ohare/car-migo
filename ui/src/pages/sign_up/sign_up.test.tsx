import '@testing-library/jest-dom';
import { screen } from '@testing-library/react';
import TestUtils from '../../test_utils';
import SignUp from '.';

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => jest.fn(),
}));

describe('Sign Up Unit Tests', () => {
  test('renders sign_up page', () => {
    TestUtils.render(<SignUp />);

    expect(screen.getByTestId('outer-box')).toBeInTheDocument();
    expect(screen.getByTestId('inner-box')).toBeInTheDocument();
    expect(screen.getByTestId('sign-up-first-name')).toBeInTheDocument();
    expect(screen.getByTestId('sign-up-last-name')).toBeInTheDocument();
    // TODO:
    // screen.debug(undefined, Infinity);
    // expect(screen.getByTestId('date-of-birth')).toBeInTheDocument();
    expect(screen.getByTestId('sign-up-phone-number')).toBeInTheDocument();
    expect(screen.getByTestId('sign-up-email-address')).toBeInTheDocument();
    expect(screen.getByTestId('sign-up-password')).toBeInTheDocument();
    expect(screen.getByTestId('sign-up-confirm-password')).toBeInTheDocument();
    expect(screen.getByTestId('submit-button')).toBeInTheDocument();
    expect(screen.getByTestId('links')).toBeInTheDocument();
    // TODO: see attempt in confirm_email.test.tsx
    // expect(screen.getByTestId('alert-pop-up')).toBeInTheDocument();
  });
});
