import '@testing-library/jest-dom';
import { fireEvent, screen, waitFor } from '@testing-library/react';
import { userEvent } from '@testing-library/user-event';
import { vi } from 'vitest';
import TestUtils from '../../test_utils';
import ConfirmEmail from '.';
import { testConstants } from '../../test_utils/test_constants';

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
    expect(
      screen.getByTestId('confirm-email-address-input')
    ).toBeInTheDocument();
    expect(screen.getByTestId('submit-button')).toBeInTheDocument();
    expect(screen.queryByTestId('alert-pop-up')).not.toBeInTheDocument();
  });
});

describe('renders AlertPopUp component', () => {
  test('button clicked with valid data returns 200 OK', async () => {
    TestUtils.render(<ConfirmEmail />);
    const emailDataField = screen.getByTestId('confirm-email-address-input');
    const submitButton = screen.getByTestId('submit-button');

    expect(emailDataField).toBeInTheDocument();
    expect(submitButton).toBeInTheDocument();

    fireEvent.change(emailDataField, {
      target: { value: testConstants.validEmail },
    });
    await userEvent.click(submitButton);
    await waitFor(() => {
      expect(screen.getByTestId('dialog-box')).toBeInTheDocument();
    });
  });

  test('button clicked with no input', async () => {
    TestUtils.render(<ConfirmEmail />);
    const submitButton = screen.getByTestId('submit-button');

    expect(submitButton).toBeInTheDocument();
    await userEvent.click(submitButton);
    expect(screen.getByText('Email must not be empty.')).toBeInTheDocument();
  });

  test('button clicked with invalid data returns 404 NOT FOUND', async () => {
    TestUtils.render(<ConfirmEmail />);
    const emailDataField = screen.getByTestId('confirm-email-address-input');
    const submitButton = screen.getByTestId('submit-button');

    expect(submitButton).toBeInTheDocument();

    fireEvent.change(emailDataField, {
      target: { value: testConstants.notFoundEmail },
    });
    await userEvent.click(submitButton);
    await waitFor(() => {
      expect(screen.getByTestId('alert-pop-up')).toBeInTheDocument();
      expect(
        screen.getByText(
          "Mmmm! We can't find an account for " + testConstants.notFoundEmail
        )
      ).toBeInTheDocument();
    });
  });

  test('button clicked with invalid data returns 409 CONFLICT', async () => {
    TestUtils.render(<ConfirmEmail />);
    const emailDataField = screen.getByTestId('confirm-email-address-input');
    const submitButton = screen.getByTestId('submit-button');

    expect(submitButton).toBeInTheDocument();

    fireEvent.change(emailDataField, {
      target: { value: testConstants.conflictEmail },
    });
    await userEvent.click(submitButton);
    await waitFor(() => {
      expect(screen.getByTestId('alert-pop-up')).toBeInTheDocument();
      expect(
        screen.getByText('Yayyy! You have already confirmed your email.')
      ).toBeInTheDocument();
    });
  });
});
