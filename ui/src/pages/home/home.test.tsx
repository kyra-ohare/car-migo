import '@testing-library/jest-dom';
import { fireEvent, screen, waitFor } from '@testing-library/react';
import { useAuthStore } from '../../hooks/useAuthStore';
import TestUtils from '../../test_utils';
import Homepage from '.';
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

  test('finding and booking journeys', async () => {
    TestUtils.render(<Homepage />);
    const leavingFromField = screen.getByLabelText('Leaving From');
    const goingToField = screen.getByLabelText('Going to');
    const earliestDateTimeField = screen.getByLabelText('Earliest Date/Time');
    const latestDateTimeField = screen.getByLabelText('Latest Date/Time');
    const searchSubmitButton = screen.getByTestId('search-submit-button');

    expect(leavingFromField).toBeInTheDocument();
    expect(goingToField).toBeInTheDocument();
    expect(earliestDateTimeField).toBeInTheDocument();
    expect(latestDateTimeField).toBeInTheDocument();
    expect(searchSubmitButton).toBeInTheDocument();

    fireEvent.change(leavingFromField, { target: { value: 'Newry' } });
    fireEvent.keyDown(leavingFromField, { key: 'ArrowDown' });
    fireEvent.keyDown(leavingFromField, { key: 'ArrowDown' });
    fireEvent.keyDown(leavingFromField, { key: 'ArrowDown' });
    fireEvent.keyDown(leavingFromField, { key: 'ArrowDown' });
    fireEvent.keyDown(leavingFromField, { key: 'ArrowDown' });
    fireEvent.keyDown(leavingFromField, { key: 'Enter' });

    fireEvent.change(goingToField, { target: { value: 'Rostrevor' } });
    fireEvent.keyDown(goingToField, { key: 'ArrowDown' });
    fireEvent.keyDown(goingToField, { key: 'Enter' });

    fireEvent.change(earliestDateTimeField, {
      target: { value: '15/04/2016 10:00 am' },
    });
    fireEvent.keyDown(earliestDateTimeField, { key: 'ArrowDown' });
    fireEvent.keyDown(earliestDateTimeField, { key: 'Enter' });

    fireEvent.change(latestDateTimeField, {
      target: { value: '15/04/2024 02:15 pm' },
    });
    fireEvent.keyDown(latestDateTimeField, { key: 'ArrowDown' });
    fireEvent.keyDown(latestDateTimeField, { key: 'Enter' });

    userEvent.click(searchSubmitButton);
    await waitFor(() => {
      const journeyComponent = screen.getByTestId('journey-component');
      expect(journeyComponent).toBeInTheDocument();
    });

    await waitFor(async () => {
      const journeyCardId5 = screen.getByTestId('journey-card-5');
      expect(journeyCardId5).toBeInTheDocument();
      const bookjourneyCardId5 = screen.getByTestId('book-journey-button-5');
      expect(bookjourneyCardId5).toBeInTheDocument();
      await userEvent.click(bookjourneyCardId5);
      await waitFor(() => {
        expect(journeyCardId5).not.toBeInTheDocument();
      });
    });

    await waitFor(async () => {
      const journeyCardId6 = screen.getByTestId('journey-card-6');
      expect(journeyCardId6).toBeInTheDocument();
      const bookjourneyCardId6 = screen.getByTestId('book-journey-button-6');
      expect(bookjourneyCardId6).toBeInTheDocument();

      await userEvent.click(bookjourneyCardId6);
      await waitFor(() => {
        expect(screen.getByTestId('journey-alert-pop-up')).toBeInTheDocument();
        expect(
          screen.getByText('You are already a passenger to this journey.')
        ).toBeInTheDocument();
      });
    });

    await waitFor(async () => {
      const journeyCardId7 = screen.getByTestId('journey-card-7');
      expect(journeyCardId7).toBeInTheDocument();
      const bookjourneyCardId7 = screen.getByTestId('book-journey-button-7');
      expect(bookjourneyCardId7).toBeInTheDocument();

      await userEvent.click(bookjourneyCardId7);
      await waitFor(() => {
        expect(screen.getByTestId('journey-alert-pop-up')).toBeInTheDocument();
        expect(
          screen.getByText('Please, log in to book this journey.')
        ).toBeInTheDocument();
      });
    });

    await waitFor(async () => {
      const journeyCardId8 = screen.getByTestId('journey-card-8');
      expect(journeyCardId8).toBeInTheDocument();
      const bookjourneyCardId8 = screen.getByTestId('book-journey-button-8');
      expect(bookjourneyCardId8).toBeInTheDocument();

      await userEvent.click(bookjourneyCardId8);
      await waitFor(() => {
        expect(screen.getByTestId('journey-alert-pop-up')).toBeInTheDocument();
        expect(
          screen.getByText('Go to Profile to register as a passenger.')
        ).toBeInTheDocument();
      });
    });

    await waitFor(async () => {
      const journeyCardId9 = screen.getByTestId('journey-card-9');
      expect(journeyCardId9).toBeInTheDocument();
      const bookjourneyCardId9 = screen.getByTestId('book-journey-button-9');
      expect(bookjourneyCardId9).toBeInTheDocument();
      
      await userEvent.click(bookjourneyCardId9);
      await waitFor(() => {
        expect(screen.getByTestId('journey-alert-pop-up')).toBeInTheDocument();
        expect(
          screen.getByText('Sorry but this journey is full.')
        ).toBeInTheDocument();
      });
    });

    await waitFor(async () => {
      const journeyCardId10 = screen.getByTestId('journey-card-10');
      expect(journeyCardId10).toBeInTheDocument();
      const bookjourneyCardId10 = screen.getByTestId('book-journey-button-10');
      expect(bookjourneyCardId10).toBeInTheDocument();

      await userEvent.click(bookjourneyCardId10);
      await waitFor(() => {
        expect(screen.getByTestId('journey-alert-pop-up')).toBeInTheDocument();
        expect(
          screen.getByText('You are the driver of this journey.')
        ).toBeInTheDocument();
      });
    });
  });

  test('finding no journeys', async () => {
    TestUtils.render(<Homepage />);
    const leavingFromField = screen.getByLabelText('Leaving From');
    const goingToField = screen.getByLabelText('Going to');
    const earliestDateTimeField = screen.getByLabelText('Earliest Date/Time');
    const latestDateTimeField = screen.getByLabelText('Latest Date/Time');
    const searchSubmitButton = screen.getByTestId('search-submit-button');

    expect(leavingFromField).toBeInTheDocument();
    expect(goingToField).toBeInTheDocument();
    expect(earliestDateTimeField).toBeInTheDocument();
    expect(latestDateTimeField).toBeInTheDocument();
    expect(searchSubmitButton).toBeInTheDocument();

    fireEvent.change(leavingFromField, { target: { value: 'Rostrevor' } });
    fireEvent.keyDown(leavingFromField, { key: 'ArrowDown' });
    fireEvent.keyDown(leavingFromField, { key: 'Enter' });

    fireEvent.change(goingToField, { target: { value: 'Downpatrick' } });
    fireEvent.keyDown(goingToField, { key: 'ArrowDown' });
    fireEvent.keyDown(goingToField, { key: 'ArrowDown' });
    fireEvent.keyDown(goingToField, { key: 'ArrowDown' });
    fireEvent.keyDown(goingToField, { key: 'Enter' });

    fireEvent.change(earliestDateTimeField, {
      target: { value: '15/04/2016 10:00 am' },
    });
    fireEvent.keyDown(earliestDateTimeField, { key: 'ArrowDown' });
    fireEvent.keyDown(earliestDateTimeField, { key: 'Enter' });

    fireEvent.change(latestDateTimeField, {
      target: { value: '15/04/2024 02:15 pm' },
    });
    fireEvent.keyDown(latestDateTimeField, { key: 'ArrowDown' });
    fireEvent.keyDown(latestDateTimeField, { key: 'Enter' });

    userEvent.click(searchSubmitButton);
    await waitFor(async () => {
      expect(screen.getByTestId('box-alert-span')).toBeInTheDocument();
      const boxAlertSpan = screen.getByTestId('close-button-box-alert-span');
      expect(boxAlertSpan).toBeInTheDocument();
      await userEvent.click(boxAlertSpan);
      await waitFor(() => {
        expect(screen.queryByTestId('box-alert-span')).not.toBeInTheDocument();
      });
    });
  });
});
