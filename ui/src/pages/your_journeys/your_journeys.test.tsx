import '@testing-library/jest-dom';
import { screen, waitFor } from '@testing-library/react';
import TestUtils from '../../test_utils';
import YourJourneys from '.';
import { useAuthStore } from '../../hooks/useAuthStore';
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

describe('YourJourneys Unit Tests', () => {
  test('renders YourJourneys component', async () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<YourJourneys />);

    await waitFor(async () => {
      expect(screen.getByTestId('passenger-journeys')).toBeInTheDocument();
      expect(screen.getByTestId('driver-journeys')).toBeInTheDocument();
      expect(screen.getByTestId('journey-card-1')).toBeInTheDocument();
      expect(screen.getByTestId('journey-card-2')).toBeInTheDocument();
      expect(screen.getByTestId('journey-card-3')).toBeInTheDocument();
    });
  });

  test('cancel a journey', async () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<YourJourneys />);

    await waitFor(async () => {
      const cancelJourney1 = screen.getByTestId('cancel-journey-button-1');
      expect(cancelJourney1).toBeInTheDocument();
      await userEvent.click(cancelJourney1);
    });
  });
});
