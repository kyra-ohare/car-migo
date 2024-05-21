import '@testing-library/jest-dom';
import { screen } from '@testing-library/react';
import TestUtils from '../../test_utils';
import ForgotPassword from '.';

describe('Home Unit Tests', () => {
  test('renders the homepage where authorization is set to false', async () => {
    TestUtils.render(<ForgotPassword />);

    expect(screen.getByTestId('forgot-password')).toBeInTheDocument();
  });
});
