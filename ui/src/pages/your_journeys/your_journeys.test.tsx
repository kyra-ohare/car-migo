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
      expect(screen.getByTestId('journey-card-4')).toBeInTheDocument();
    });
  });

  test('delete a passenger\'s journey', async () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<YourJourneys />);

    await waitFor(async () => {
      const cancelJourney1 = screen.getByTestId('cancel-journey-button-1');
      expect(cancelJourney1).toBeInTheDocument();
      await userEvent.click(cancelJourney1);
      await waitFor(() => {
        expect(screen.getByTestId('journey-alert-pop-up')).toBeInTheDocument();
        expect(
          screen.getByText('You are no longer a passenger to this journey.')
        ).toBeInTheDocument();
      });
    });

    await waitFor(async () => {
      const cancelJourney2 = screen.getByTestId('cancel-journey-button-2');
      expect(cancelJourney2).toBeInTheDocument();
      await userEvent.click(cancelJourney2);
      await waitFor(() => {
        expect(screen.getByTestId('journey-alert-pop-up')).toBeInTheDocument();
        expect(
          screen.getByText('It was not possible to remove you from this journey.')
        ).toBeInTheDocument();
      });
    });
  });

  test('delete a journey', async () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<YourJourneys />);

    await waitFor(async () => {
      const cancelJourney3 = screen.getByTestId('cancel-journey-button-3');
      expect(cancelJourney3).toBeInTheDocument();
      await userEvent.click(cancelJourney3);
      await waitFor(() => {
        expect(screen.getByTestId('journey-alert-pop-up')).toBeInTheDocument();
        expect(
          screen.getByText('Journey deleted successfully.')
        ).toBeInTheDocument();
      });
    });

    await waitFor(async () => {
      const cancelJourney4 = screen.getByTestId('cancel-journey-button-4');
      expect(cancelJourney4).toBeInTheDocument();
      await userEvent.click(cancelJourney4);
      await waitFor(() => {
        expect(screen.getByTestId('journey-alert-pop-up')).toBeInTheDocument();
        expect(
          screen.getByText('It was not possible to delete this journey.')
        ).toBeInTheDocument();
      });
    });
  });
});
