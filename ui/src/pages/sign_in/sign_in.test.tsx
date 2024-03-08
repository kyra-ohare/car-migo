import '@testing-library/jest-dom';
import { screen } from '@testing-library/react';
import TestUtils from '../../test_utils';
import SignIn from '.';

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => jest.fn(),
}));

describe('Sign In Unit Tests', () => {
  test('renders sign_in page', () => {
    TestUtils.render(<SignIn />);

    expect(screen.getByTestId('outer-box')).toBeInTheDocument();
    expect(screen.getByTestId('inner-box')).toBeInTheDocument();
    expect(screen.getByTestId('sign-in-with-email')).toBeInTheDocument();
    expect(screen.getByTestId('sign-in-with-password')).toBeInTheDocument();
    expect(screen.getByTestId('form-control-label')).toBeInTheDocument();
    expect(screen.getByTestId('submit-button')).toBeInTheDocument();
    expect(screen.getByTestId('links')).toBeInTheDocument();
    // TODO: see attempt in confirm_email.test.tsx
    // expect(screen.getByTestId('alert-pop-up')).toBeInTheDocument();
  });
});
