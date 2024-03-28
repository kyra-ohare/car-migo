import '@testing-library/jest-dom';
import { screen, waitFor } from '@testing-library/react';
import TestUtils from '../../test_utils';
import ConfirmEmail from '.';
import { vi } from 'vitest';
import userEvent from '@testing-library/user-event';

vi.mock('react-router-dom', async (importOriginal) => {
  const actual: Record<string, unknown> = await importOriginal();
  return {
    ...actual,
    useNavigate: () => vi.fn(),
  };
});

describe('Confirm Email Unit Tests', () => {
  test('renders confirm_email page', () => {
    TestUtils.render(<ConfirmEmail />);

    expect(screen.getByTestId('box')).toBeInTheDocument();
    expect(screen.getByTestId('welcome-message')).toBeInTheDocument();
    expect(screen.getByTestId('catchy-message')).toBeInTheDocument();
    expect(screen.getByTestId('confirm-email-address')).toBeInTheDocument();
    expect(screen.getByTestId('submit-button')).toBeInTheDocument();
  });

  test('renders AlertPopUp when user clicks on the button', async () => {
    const user = userEvent.setup();
    TestUtils.render(<ConfirmEmail />);
    expect(screen.getByTestId('submit-button')).toBeInTheDocument();
    user.click(screen.getByTestId('submit-button'));
    screen.debug(undefined, Infinity);
    await waitFor(() => {
      expect(screen.getByText('Email must not be empty.')).toBeInTheDocument();
    });
  });
});
