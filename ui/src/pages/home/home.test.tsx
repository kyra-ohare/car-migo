import '@testing-library/jest-dom';
import { screen } from '@testing-library/react';
import { useAuthStore } from '../../hooks/useAuthStore';
import TestUtils from '../../test_utils';
import Homepage from '.';
import { vi } from 'vitest';
import userEvent from '@testing-library/user-event';

const mockNavigate = vi.fn();

vi.mock('react-router-dom', async (importOriginal) => {
  const actual: Record<string, unknown> = await importOriginal();
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

const initialStoreState = useAuthStore.getState();

describe('Home Unit Tests', () => {
  test('renders the homepage where authorization is set to false', async () => {
    initialStoreState.setIsAuthorized(false);
    TestUtils.render(<Homepage />);

    expect(screen.getByTestId('welcome-message-container')).toBeInTheDocument();
    expect(screen.getByTestId('welcome-message')).toBeInTheDocument();
    expect(screen.getByTestId('catchy-message')).toHaveTextContent(
      'The best way to get to places fast, at low cost and, most importantly, eco-friendly.'
    );
    expect(screen.getByTestId('sign-up-render')).toBeInTheDocument();
    expect(screen.getByTestId('sign-in-render')).toBeInTheDocument();
    expect(screen.getByTestId('what-is-it-card')).toBeInTheDocument();
    expect(screen.getByTestId('why-card')).toBeInTheDocument();

    await userEvent.click(screen.getByTestId('sign-up-render'));
    expect(mockNavigate).toHaveBeenCalled();
    await userEvent.click(screen.getByTestId('sign-in-render'));
    expect(mockNavigate).toHaveBeenCalled();
  });

  test('does not render sign-in and sign-up buttons because authorization is set to true', () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<Homepage />);

    expect(screen.getByTestId('welcome-message-container')).toBeInTheDocument();
    expect(screen.getByTestId('welcome-message')).toBeInTheDocument();
    expect(screen.queryByTestId('sign-up-render')).not.toBeInTheDocument();
    expect(screen.queryByTestId('sign-in-render')).not.toBeInTheDocument();
  });
});
