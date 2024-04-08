import '@testing-library/jest-dom';
import { screen, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import TestUtils from '../../test_utils';
import Profile from '.';
import { useAuthStore } from '../../hooks/useAuthStore';
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

describe('Profile Unit Tests', () => {
  test('renders homepage', async () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<Profile />);

    await waitFor(async () => {
      expect(
        screen.getByTestId('read-only-first-name-input')
      ).toBeInTheDocument();
      expect(
        screen.getByTestId('read-only-last-name-input')
      ).toBeInTheDocument();
      expect(screen.getByTestId('read-only-dob-input')).toBeInTheDocument();
      expect(screen.getByTestId('read-only-email-input')).toBeInTheDocument();
      expect(
        screen.getByTestId('read-only-phone-number-input')
      ).toBeInTheDocument();

      const signOutButton = screen.getByTestId('sign-out-button');
      expect(signOutButton).toBeInTheDocument();
      await userEvent.click(signOutButton);
      expect(mockNavigate).toHaveBeenCalled();

      const deleteButton = screen.getByTestId('delete-account');
      expect(deleteButton).toBeInTheDocument();
      await userEvent.click(deleteButton);
      expect(mockNavigate).toHaveBeenCalled();
    });
  });

  test('test toggle for the passenger', async () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<Profile />);

    await waitFor(async () => {
      const passengerToggle = screen.getByTestId(
        'passenger-switch-with-tooltip-switch'
      );
      expect(passengerToggle).toBeInTheDocument();
      expect(screen.getByLabelText('You are a passenger')).toBeInTheDocument();

      await userEvent.hover(passengerToggle);
      await waitFor(() => {
        expect(
          screen.getByLabelText('which means you can book journeys.')
        ).toBeInTheDocument();
      });

      await userEvent.click(passengerToggle);
      await waitFor(() => {
        expect(passengerToggle).toBeInTheDocument();
        expect(screen.getByLabelText('Become a passenger')).toBeInTheDocument();
      });

      await userEvent.hover(passengerToggle);
      await waitFor(() => {
        expect(
          screen.getByLabelText('since you cannot book journeys at the moment.')
        ).toBeInTheDocument();
      });

      await userEvent.click(passengerToggle);
      await waitFor(() => {
        expect(passengerToggle).toBeInTheDocument();
        expect(
          screen.getByLabelText('You are a passenger')
        ).toBeInTheDocument();
      });
    });
  });

  test('test toggle for the driver', async () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<Profile />);

    await waitFor(async () => {
      const driverToggle = screen.getByTestId(
        'driver-switch-with-tooltip-switch'
      );
      expect(driverToggle).toBeInTheDocument();
      expect(screen.getByLabelText('You are a driver')).toBeInTheDocument();

      await userEvent.hover(driverToggle);
      await waitFor(() => {
        expect(
          screen.getByLabelText('so you can create journeys.')
        ).toBeInTheDocument();
      });
      await userEvent.click(driverToggle);
      await waitFor(() => {
        expect(
          screen.getByTestId('driver-switch-with-tooltip')
        ).toBeInTheDocument();
        expect(screen.getByLabelText('Become a driver')).toBeInTheDocument();
      });

      await userEvent.hover(driverToggle);
      await waitFor(() => {
        expect(
          screen.getByLabelText('since you cannot create journeys.')
        ).toBeInTheDocument();
      });

      await userEvent.click(driverToggle);
      await waitFor(() => {
        expect(screen.getByLabelText('You are a driver')).toBeInTheDocument();
      });
    });
  });
});
