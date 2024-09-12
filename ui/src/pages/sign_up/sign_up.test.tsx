import '@testing-library/jest-dom';
import { fireEvent, screen, waitFor } from '@testing-library/react';
import TestUtils from '../../test_utils';
import SignUp from '.';
import { vi } from 'vitest';
import userEvent from '@testing-library/user-event';
import { testConstants } from '../../test_utils/test_constants';

const mockNavigate = vi.fn();

vi.mock('react-router-dom', async (importOriginal) => {
  const actual: Record<string, unknown> = await importOriginal();
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

describe('Sign Up Unit Tests', () => {
  test('renders sign_up page', () => {
    TestUtils.render(<SignUp />);

    expect(screen.getByTestId('outer-box')).toBeInTheDocument();
    expect(screen.getByTestId('inner-box')).toBeInTheDocument();
    expect(screen.getByTestId('sign-up-first-name')).toBeInTheDocument();
    expect(screen.getByTestId('sign-up-last-name')).toBeInTheDocument();
    expect(screen.getByTestId('CalendarIcon')).toBeInTheDocument();
    expect(screen.getByTestId('sign-up-phone-number')).toBeInTheDocument();
    expect(screen.getByTestId('sign-up-email-address')).toBeInTheDocument();
    expect(screen.getByTestId('sign-up-password')).toBeInTheDocument();
    expect(screen.getByTestId('sign-up-confirm-password')).toBeInTheDocument();
    expect(screen.getByTestId('submit-button')).toBeInTheDocument();
    expect(screen.getByTestId('links')).toBeInTheDocument();
  });

  test('button clicked with valid data returns 200 OK', async () => {
    TestUtils.render(<SignUp />);
    const firstNameField = screen.getByLabelText('First Name');
    const lastNameField = screen.getByLabelText('Last Name');
    const dobField = screen.getByTestId('date-picker-input');
    const phoneNumberField = screen.getByLabelText('Phone Number');
    const emailAddressField = screen.getByLabelText('Email Address');
    const passwordField = screen.getByLabelText('Password');
    const confirmPasswordField = screen.getByLabelText('Confirm Password');
    const submitButton = screen.getByTestId('submit-button');

    expect(firstNameField).toBeInTheDocument();
    expect(lastNameField).toBeInTheDocument();
    expect(dobField).toBeInTheDocument();
    expect(phoneNumberField).toBeInTheDocument();
    expect(emailAddressField).toBeInTheDocument();
    expect(passwordField).toBeInTheDocument();
    expect(confirmPasswordField).toBeInTheDocument();
    expect(submitButton).toBeInTheDocument();

    fireEvent.change(firstNameField, {
      target: { value: testConstants.firstName },
    });
    fireEvent.change(lastNameField, {
      target: { value: testConstants.lastName },
    });
    fireEvent.change(dobField, { target: { value: '03/05/1997' } });
    fireEvent.change(phoneNumberField, {
      target: { value: testConstants.phoneNumber },
    });
    fireEvent.change(emailAddressField, {
      target: { value: testConstants.validEmail },
    });
    fireEvent.change(passwordField, {
      target: { value: testConstants.validPassword },
    });
    fireEvent.change(confirmPasswordField, {
      target: { value: testConstants.validPassword },
    });

    await userEvent.click(submitButton);
    await waitFor(async () => {
      const okButton = screen.getByTestId('ok-button');
      expect(okButton).toBeInTheDocument();
      expect(screen.getByText('Account created')).toBeInTheDocument();

      await userEvent.click(okButton);
      await waitFor(() => {
        expect(mockNavigate).toHaveBeenCalled();
      });
    });
  });

  test('button clicked with some missing data fails validation', async () => {
    TestUtils.render(<SignUp />);
    const submitButton = screen.getByTestId('submit-button');
    expect(submitButton).toBeInTheDocument();
    await userEvent.click(submitButton);
    await waitFor(() => {
      expect(
        screen.getByText('First name must not be empty.')
      ).toBeInTheDocument();
      expect(
        screen.getByText('Last name must not be empty.')
      ).toBeInTheDocument();
      // expect(screen.getByText('Minimum age is ' + minAge)).toBeInTheDocument(); // TODO
      expect(
        screen.getByText('Phone number must not be empty.')
      ).toBeInTheDocument();
      expect(screen.getByText('Email must not be empty.')).toBeInTheDocument();
      expect(
        screen.getByText('Password must not be empty.')
      ).toBeInTheDocument();
      expect(screen.getByText('Confirm your password.')).toBeInTheDocument();
    });
  });

  test('already registered user registering again returns 409 CONFLICT', async () => {
    TestUtils.render(<SignUp />);
    const firstNameField = screen.getByLabelText('First Name');
    const lastNameField = screen.getByLabelText('Last Name');
    const dobField = screen.getByTestId('date-picker-input');
    const phoneNumberField = screen.getByLabelText('Phone Number');
    const emailAddressField = screen.getByLabelText('Email Address');
    const passwordField = screen.getByLabelText('Password');
    const confirmPasswordField = screen.getByLabelText('Confirm Password');
    const submitButton = screen.getByTestId('submit-button');

    expect(firstNameField).toBeInTheDocument();
    expect(lastNameField).toBeInTheDocument();
    expect(dobField).toBeInTheDocument();
    expect(phoneNumberField).toBeInTheDocument();
    expect(emailAddressField).toBeInTheDocument();
    expect(passwordField).toBeInTheDocument();
    expect(confirmPasswordField).toBeInTheDocument();
    expect(submitButton).toBeInTheDocument();

    fireEvent.change(firstNameField, {
      target: { value: testConstants.firstName },
    });
    fireEvent.change(lastNameField, {
      target: { value: testConstants.lastName },
    });
    fireEvent.change(dobField, { target: { value: '03/05/1997' } });
    fireEvent.change(phoneNumberField, {
      target: { value: testConstants.phoneNumber },
    });
    fireEvent.change(emailAddressField, {
      target: { value: testConstants.conflictEmail },
    });
    fireEvent.change(passwordField, {
      target: { value: testConstants.validPassword },
    });
    fireEvent.change(confirmPasswordField, {
      target: { value: testConstants.validPassword },
    });

    await userEvent.click(submitButton);
    await waitFor(async () => {
      expect(screen.getByTestId('alert-pop-up')).toBeInTheDocument();
      expect(
        screen.getByText(
          'Oh! We already have an account for ' + testConstants.conflictEmail
        )
      ).toBeInTheDocument();
    });
    const closeAlertPopUp = screen.getByTestId('CloseIcon');
    expect(closeAlertPopUp).toBeInTheDocument();
    await userEvent.click(closeAlertPopUp);
    await waitFor(() => {
      expect(screen.queryByTestId('alert-pop-up')).not.toBeInTheDocument();
    });
  });
});
