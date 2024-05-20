import '@testing-library/jest-dom';
import { fireEvent, screen, waitFor } from '@testing-library/react';
import TestUtils from '../../test_utils';
import CreateJourneys from '.';
import { useAuthStore } from '../../hooks/useAuthStore';
import userEvent from '@testing-library/user-event';

const initialStoreState = useAuthStore.getState();

describe('CreateJourneys Unit Tests', () => {
  test('renders CreateJourneys component', async () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<CreateJourneys />);

    expect(
      screen.getByTestId('create-journey-origin-input')
    ).toBeInTheDocument();
    expect(
      screen.getByTestId('create-journey-destination-input')
    ).toBeInTheDocument();
    expect(
      screen.getByTestId('create-journeys-date-time-picker')
    ).toBeInTheDocument();
    expect(
      screen.getByTestId('create-journeys-max-passengers')
    ).toBeInTheDocument();
    expect(screen.getByTestId('submit-button')).toBeInTheDocument();
  });

  test('create a journey returns 201', async () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<CreateJourneys />);

    const originField = screen.getByLabelText('Origin');
    const destinationField = screen.getByLabelText('Destination');
    const dateTimeField = screen.getByLabelText('Date/Time');
    const maxPassengersField = screen.getByLabelText('Max passengers');
    const submitButton = screen.getByTestId('submit-button');

    expect(originField).toBeInTheDocument();
    expect(destinationField).toBeInTheDocument();
    expect(dateTimeField).toBeInTheDocument();
    expect(maxPassengersField).toBeInTheDocument();
    expect(submitButton).toBeInTheDocument();

    fireEvent.change(originField, { target: { value: 'Downpatrick' } });
    fireEvent.keyDown(originField, { key: 'ArrowDown' });
    fireEvent.keyDown(originField, { key: 'ArrowDown' });
    fireEvent.keyDown(originField, { key: 'ArrowDown' });
    fireEvent.keyDown(originField, { key: 'Enter' });

    fireEvent.change(destinationField, { target: { value: 'Armagh' } });
    fireEvent.keyDown(destinationField, { key: 'ArrowDown' });
    fireEvent.keyDown(destinationField, { key: 'ArrowDown' });
    fireEvent.keyDown(destinationField, { key: 'ArrowDown' });
    fireEvent.keyDown(destinationField, { key: 'ArrowDown' });
    fireEvent.keyDown(destinationField, { key: 'Enter' });

    fireEvent.change(dateTimeField, {
      target: { value: '15/04/2016 10:00 am' },
    });

    fireEvent.change(maxPassengersField, {
      target: { value: 2 },
    });

    await userEvent.click(submitButton);
    await waitFor(() => {
      expect(screen.getByTestId('alert-pop-up')).toBeInTheDocument();
      expect(
        screen.getByText('Your journey was created successfully.')
      ).toBeInTheDocument();
    });
  });

  test('create a journey returns 403', async () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<CreateJourneys />);

    const originField = screen.getByLabelText('Origin');
    const destinationField = screen.getByLabelText('Destination');
    const dateTimeField = screen.getByLabelText('Date/Time');
    const maxPassengersField = screen.getByLabelText('Max passengers');
    const submitButton = screen.getByTestId('submit-button');

    fireEvent.change(originField, { target: { value: 'Rostrevor' } });
    fireEvent.keyDown(originField, { key: 'ArrowDown' });
    fireEvent.keyDown(originField, { key: 'Enter' });

    fireEvent.change(destinationField, { target: { value: 'Belfast' } });
    fireEvent.keyDown(destinationField, { key: 'ArrowDown' });
    fireEvent.keyDown(destinationField, { key: 'ArrowDown' });
    fireEvent.keyDown(destinationField, { key: 'Enter' });

    fireEvent.change(dateTimeField, {
      target: { value: '20/05/2024 11:00 am' },
    });

    fireEvent.change(maxPassengersField, {
      target: { value: 4 },
    });

    await userEvent.click(submitButton);
    await waitFor(() => {
      expect(screen.getByTestId('alert-pop-up')).toBeInTheDocument();
      expect(
        screen.getByText(
          'You cannot create a Journey. Are you a driver? Check if you are one in Profile.'
        )
      ).toBeInTheDocument();
    });
  });

  test('create a journey returns 500', async () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<CreateJourneys />);

    const originField = screen.getByLabelText('Origin');
    const destinationField = screen.getByLabelText('Destination');
    const dateTimeField = screen.getByLabelText('Date/Time');
    const maxPassengersField = screen.getByLabelText('Max passengers');
    const submitButton = screen.getByTestId('submit-button');

    fireEvent.change(originField, { target: { value: 'Belfast' } });
    fireEvent.keyDown(originField, { key: 'ArrowDown' });
    fireEvent.keyDown(originField, { key: 'ArrowDown' });
    fireEvent.keyDown(originField, { key: 'Enter' });

    fireEvent.change(destinationField, { target: { value: 'Newry' } });
    fireEvent.keyDown(destinationField, { key: 'ArrowDown' });
    fireEvent.keyDown(destinationField, { key: 'ArrowDown' });
    fireEvent.keyDown(destinationField, { key: 'ArrowDown' });
    fireEvent.keyDown(destinationField, { key: 'ArrowDown' });
    fireEvent.keyDown(destinationField, { key: 'ArrowDown' });
    fireEvent.keyDown(destinationField, { key: 'Enter' });

    fireEvent.change(dateTimeField, {
      target: { value: '15/04/2016 10:00 am' },
    });

    fireEvent.change(maxPassengersField, {
      target: { value: 3 },
    });

    await userEvent.click(submitButton);
    await waitFor(() => {
      expect(screen.getByTestId('alert-pop-up')).toBeInTheDocument();
      expect(
        screen.getByText(
          "Something didn't go according to the plan and your journey was not created."
        )
      ).toBeInTheDocument();
    });

    const closeAlertPopUp = screen.getByTitle('Close');
    expect(closeAlertPopUp).toBeInTheDocument();
    await userEvent.click(closeAlertPopUp);
    await waitFor(() => {
      expect(screen.queryByTestId('alert-pop-up')).not.toBeInTheDocument();
    });
  });
});
