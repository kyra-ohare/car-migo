import '@testing-library/jest-dom';
import { fireEvent, screen, waitFor } from '@testing-library/react';
import { useAuthStore } from '../../hooks/useAuthStore';
import TestUtils from '../../test_utils';
import { vi } from 'vitest';
import NavBar from '.';

const mockNavigate = vi.fn();

vi.mock('react-router-dom', async (importOriginal) => {
  const actual: Record<string, unknown> = await importOriginal();
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

const initialStoreState = useAuthStore.getState();

describe('Navigation Unit Tests', () => {
  test('renders nav bar for unauthorized users', () => {
    initialStoreState.setIsAuthorized(false);
    TestUtils.render(
      <NavBar>
        <div />
      </NavBar>
    );

    expect(screen.getAllByTestId('car-image-img')).toBeDefined();
    expect(screen.getAllByTestId('car-migo-title')).toBeDefined();
    expect(screen.getAllByTestId('row-menu')).toBeDefined();

    const buttonsArray: Array<HTMLElement[]> = [
      screen.getAllByTestId('row-menu-Home'),
      screen.getAllByTestId('row-menu-Confirm email'),
      screen.getAllByTestId('row-menu-Profile'),
      screen.getAllByTestId('row-menu-Playground'),
    ];

    buttonsArray.forEach((buttons) => {
      buttons.forEach(async (button) => {
        expect(button).toBeInTheDocument();
        fireEvent.click(button);
        await waitFor(() => {
          expect(mockNavigate).toHaveBeenCalled();
        });
      });
    });
  });

  test('renders nav bar for authorized users', async () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(
      <NavBar>
        <div />
      </NavBar>
    );

    expect(screen.getAllByTestId('authorized-settings')).toBeDefined();

    const menuSettingsButton = screen.getAllByTestId('menu-settings-button');
    menuSettingsButton.forEach(async (button) => {
      expect(button).toBeInTheDocument();
      fireEvent.click(button);
      await waitFor(() => {
        expect(mockNavigate).toHaveBeenCalled();
      });
    });

    const settingsArray: Array<HTMLElement[]> = [
      screen.getAllByTestId('settings-menu-Profile'),
      screen.getAllByTestId('settings-menu-Your Journeys'),
      screen.getAllByTestId('settings-menu-Logout'),
    ];
    settingsArray.forEach((settings) => {
      settings.forEach(async (option) => {
        fireEvent.click(option);
        await waitFor(() => {
          expect(mockNavigate).toHaveBeenCalled();
        });
      });
    });
  });
});
