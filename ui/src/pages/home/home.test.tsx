import '@testing-library/jest-dom';
import { screen } from '@testing-library/react';
import TestUtils from '../../test_utils';
import { default as Homepage } from '.';

jest.mock('../../hooks/useAuthStore', () => ({
  useAuthStore: () => ({
    isAuthorized: true,
  }),
}));
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => jest.fn(),
}));

describe('Home Unit Tests', () => {
  test('renders the homepage', () => {
    TestUtils.render(<Homepage />);
    expect(screen.getByText('Welcome to Car-Migo')).toBeInTheDocument();
  });
});
