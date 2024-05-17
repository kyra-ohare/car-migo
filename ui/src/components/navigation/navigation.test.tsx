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

    const menuRowOptions: Array<HTMLElement[]> = [
      screen.getAllByTestId('row-menu-Home'),
      screen.getAllByTestId('row-menu-Confirm email'),
      screen.getAllByTestId('row-menu-Forgot Password'),
      screen.getAllByTestId('row-menu-Sign In'),
      screen.getAllByTestId('row-menu-Sign Up'),
    ];
    iterateArrayOfHtml(menuRowOptions);

    const responsiveMenuItems: Array<HTMLElement[]> = [
      screen.getAllByTestId('menu-item-Home'),
      screen.getAllByTestId('menu-item-Confirm email'),
      screen.getAllByTestId('menu-item-Forgot Password'),
      screen.getAllByTestId('menu-item-Sign In'),
      screen.getAllByTestId('menu-item-Sign Up'),
    ];
    iterateArrayOfHtml(responsiveMenuItems);
  });

  test('renders nav bar for authorized users', async () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(
      <NavBar>
        <div />
      </NavBar>
    );

    expect(screen.getAllByTestId('authorized-settings')).toBeDefined();

    const menuRowOptions: Array<HTMLElement[]> = [
      screen.getAllByTestId('row-menu-Home'),
      screen.getAllByTestId('row-menu-Profile'),
      screen.getAllByTestId('row-menu-Create Journeys'),
      screen.getAllByTestId('row-menu-Your Journeys'),
    ];
    iterateArrayOfHtml(menuRowOptions);

    const responsiveMenuItems: Array<HTMLElement[]> = [
      screen.getAllByTestId('menu-item-Home'),
      screen.getAllByTestId('menu-item-Profile'),
      screen.getAllByTestId('menu-item-Create Journeys'),
      screen.getAllByTestId('menu-item-Your Journeys'),
    ];
    iterateArrayOfHtml(responsiveMenuItems);

    const menuSettingsButton = screen.getAllByTestId('menu-settings-button');
    iterateHtmlArray(menuSettingsButton);

    const menuSettingsButtons: Array<HTMLElement[]> = [
      screen.getAllByTestId('settings-menu-Profile'),
      screen.getAllByTestId('settings-menu-Create Journeys'),
      screen.getAllByTestId('settings-menu-Your Journeys'),
    ];
    iterateArrayOfHtml(menuSettingsButtons);

    const logout = screen.getAllByTestId('settings-menu-Logout');
    iterateHtmlArray(logout);
  });

  test('renders the MenuIcon', () => {
    TestUtils.render(
      <NavBar>
        <div />
      </NavBar>
    );

    const menuIcon = screen.getAllByTestId('MenuIcon');
    expect(menuIcon).toBeDefined();
    iterateHtmlArray(menuIcon);
  });
});

function iterateArrayOfHtml(mapper: Array<HTMLElement[]>) {
  mapper.forEach((buttons) => {
    buttons.forEach(async (button) => {
      expect(button).toBeInTheDocument();
      fireEvent.click(button);
      await waitFor(() => {
        expect(mockNavigate).toHaveBeenCalled();
      });
    });
  });
}

function iterateHtmlArray(element: HTMLElement[]) {
  element.forEach(async (icon) => {
    fireEvent.click(icon);
    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalled();
    });
  });
}
