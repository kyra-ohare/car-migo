import '@testing-library/jest-dom';
import { screen, waitFor } from '@testing-library/react';
import TestUtils from '../../test_utils';
import YourJourneys from '.';
import { useAuthStore } from '../../hooks/useAuthStore';
import { vi } from 'vitest';

const mockNavigate = vi.fn();

vi.mock('react-router-dom', async (importOriginal) => {
  const actual: Record<string, unknown> = await importOriginal();
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

const initialStoreState = useAuthStore.getState();

describe('YourJourneys Unit Tests', () => {
  test('renders YourJourneys component', async () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<YourJourneys />);

    await waitFor(async () => {
      expect(screen.getByTestId('passenger-journeys')).toBeInTheDocument();
      expect(screen.getByTestId('driver-journeys')).toBeInTheDocument();
    });
  });
});
