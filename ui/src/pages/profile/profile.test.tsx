import '@testing-library/jest-dom';
import { screen } from '@testing-library/react';
import { vi } from 'vitest';
import TestUtils from '../../test_utils';
import Profile from '.';
import { useAuthStore } from '../../hooks/useAuthStore';

vi.mock('react-router-dom', async (importOriginal) => {
  const actual: Record<string, unknown> = await importOriginal();
  return {
    ...actual,
    useNavigate: () => vi.fn(),
  };
});

const initialStoreState = useAuthStore.getState();

describe('Profile Unit Tests', () => {
  test('boo', () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<Profile />);

    screen.debug(undefined, Infinity);
    expect(
      screen.getByTestId('read-only-first-name-input')
    ).toBeInTheDocument();
  });
});
