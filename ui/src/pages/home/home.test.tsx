import '@testing-library/jest-dom';
import { screen } from '@testing-library/react';
import { useAuthStore } from '../../hooks/useAuthStore';
import TestUtils from '../../test_utils';
import Homepage from '.';
import { vi } from 'vitest';
vi.mock('react-router-dom', async (importOriginal) => {
  const actual: Record<string, unknown> = await importOriginal();
  return {
    ...actual,
    useNavigate: () => vi.fn(),
  };
});

const initialStoreState = useAuthStore.getState();

describe('Home Unit Tests', () => {
  test('renders the homepage where authorization is set to true', () => {
    initialStoreState.setIsAuthorized(false);
    TestUtils.render(<Homepage />);

    expect(screen.getByTestId('welcome-message')).toBeInTheDocument();
    expect(screen.getByTestId('catchy-message')).toHaveTextContent(
      'The best way to get to places fast, at low cost and, most importantly, eco-friendly.'
    );
    expect(screen.getByTestId('sign-up-render')).toBeInTheDocument();
    expect(screen.getByTestId('sign-in-render')).toBeInTheDocument();
    expect(screen.getByTestId('what-is-it-card')).toBeInTheDocument();
    expect(screen.getByTestId('why-card')).toBeInTheDocument();
  });

  test('does not render sign-in and sign-up buttons due to authorization is set to true', () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<Homepage />);

    expect(screen.getByTestId('welcome-message')).toBeInTheDocument();
    expect(screen.queryByTestId('sign-up-render')).not.toBeInTheDocument();
  });
});
