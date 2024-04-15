import '@testing-library/jest-dom';
import { fireEvent, screen, waitFor } from '@testing-library/react';
import { useAuthStore } from '../../hooks/useAuthStore';
import TestUtils from '../../test_utils';
import Homepage from '.';
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

const initialStoreState = useAuthStore.getState();

describe('Home Unit Tests', () => {
  test('renders the homepage where authorization is set to false', async () => {
    initialStoreState.setIsAuthorized(false);
    TestUtils.render(<Homepage />);

    expect(screen.getByTestId('welcome-message-container')).toBeInTheDocument();
    expect(screen.getByTestId('welcome-message')).toBeInTheDocument();
    expect(screen.getByTestId('catchy-message')).toHaveTextContent(
      'The best way to get to places fast, at low cost and, most importantly, eco-friendly.'
    );
    expect(screen.getByTestId('car-migo-pic')).toBeInTheDocument();
    expect(screen.getByTestId('sign-up-render')).toBeInTheDocument();
    expect(screen.getByTestId('sign-in-render')).toBeInTheDocument();
    expect(screen.getByTestId('what-is-it-card')).toBeInTheDocument();
    expect(screen.getByTestId('why-card')).toBeInTheDocument();

    await userEvent.click(screen.getByTestId('sign-up-render'));
    expect(mockNavigate).toHaveBeenCalled();
    await userEvent.click(screen.getByTestId('sign-in-render'));
    expect(mockNavigate).toHaveBeenCalled();
  });

  test('does not render sign-in and sign-up buttons because authorization is set to true', () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<Homepage />);

    expect(screen.getByTestId('welcome-message-container')).toBeInTheDocument();
    expect(screen.getByTestId('welcome-message')).toBeInTheDocument();
    expect(screen.queryByTestId('sign-up-render')).not.toBeInTheDocument();
    expect(screen.queryByTestId('sign-in-render')).not.toBeInTheDocument();
  });

  test('renders Search component', () => {
    TestUtils.render(<Homepage />);

    expect(screen.getByLabelText('Leaving From')).toBeInTheDocument();
    expect(screen.getByLabelText('Going to')).toBeInTheDocument();
    expect(screen.getByTestId('earliest-date-time-picker')).toBeInTheDocument();
    expect(screen.getByTestId('latest-date-time-picker')).toBeInTheDocument();
    expect(screen.getByTestId('search-submit-button')).toBeInTheDocument();
    expect(screen.getByTestId('search-submit-button')).toBeInTheDocument();
  });

  test('search button clicked with missing data fails validation', async () => {
    TestUtils.render(<Homepage />);
    const searchSubmitButton = screen.getByTestId('search-submit-button');
    expect(searchSubmitButton).toBeInTheDocument();

    userEvent.click(searchSubmitButton);
    await waitFor(() => {
      expect(screen.getByText('coming from...')).toBeInTheDocument();
      expect(screen.getByText('heading to...')).toBeInTheDocument();
      expect(screen.getByText('date from...')).toBeInTheDocument();
      expect(screen.getByText('date to...')).toBeInTheDocument();
    });
  });

  test('finding no journeys', async () => {
    TestUtils.render(<Homepage />);
    // const leavingFromField = screen.getByLabelText('Leaving From');
    const leavingFromField = screen.getByTestId('leaving-from-dropdown-input');
    const goingToField = screen.getByLabelText('Going to');
    // const goingToField = screen.getByTestId('going-to-dropdown-input');
    const earliestDateTimeField = screen.getByTestId(
      'earliest-date-time-picker'
    );
    const latestDateTimeField = screen.getByTestId('latest-date-time-picker');
    const searchSubmitButton = screen.getByTestId('search-submit-button');

    expect(leavingFromField).toBeInTheDocument();
    expect(goingToField).toBeInTheDocument();
    expect(earliestDateTimeField).toBeInTheDocument();
    expect(latestDateTimeField).toBeInTheDocument();
    expect(searchSubmitButton).toBeInTheDocument();

    // userEvent.click(leavingFromField);
    // userEvent.type(leavingFromField, 'Rostrevor');
    // userEvent.click(searchSubmitButton);

    // fireEvent.click(leavingFromField);
    fireEvent.change(leavingFromField, { target: { value: 'Rostrevor' } });
    // fireEvent.keyDown(leavingFromField, { key: 'Enter', code: 'Enter' });
    fireEvent.keyDown(leavingFromField, { key: "ArrowDown" });
    fireEvent.keyDown(leavingFromField, { key: "ArrowDown" });
    fireEvent.keyDown(leavingFromField, { key: "Enter" }); 

    await waitFor(() => {
      const rostrevor = screen.getByText('Rostrevor');
      expect(rostrevor).toBeInTheDocument();
    });

    userEvent.click(searchSubmitButton);
    await waitFor(async () => {
      expect(screen.getByText('coming from...')).toBeInTheDocument();
      expect(screen.getByText('heading to...')).toBeInTheDocument();
      expect(screen.getByText('date from...')).toBeInTheDocument();
      expect(screen.getByText('date to...')).toBeInTheDocument();
    });

    // userEvent.click(goingToField);
    // await waitFor(() => {
    //   const downpatrick = screen.getByText('Downpatrick');
    //   expect(downpatrick).toBeInTheDocument();
    //   userEvent.click(downpatrick);
    // });

    // userEvent.click(earliestDateTimeField);
    // await waitFor(() => {
    //   userEvent.keyboard('2021-12-01T09:00:00Z');
    // });

    // userEvent.click(goingToField);
    // await waitFor(() => {
    //   userEvent.keyboard('2023-12-01T09:00:00Z');
    // });

    // userEvent.click(searchSubmitButton);
    // await waitFor(async () => {
    //   const boxAlertSpan = screen.getByTestId('box-alert-span');
    //   const closeButtonAlertSpan = screen.getByTestId('close-button-alert-span');

    //   expect(boxAlertSpan).toBeInTheDocument();
    //   expect(closeButtonAlertSpan).toBeInTheDocument();

    // userEvent.click(goingToField);
    // await waitFor(() => {
    //   expect(boxAlertSpan).not.toBeInTheDocument();
    //   expect(closeButtonAlertSpan).not.toBeInTheDocument();
    // });
    // });
  });
});
