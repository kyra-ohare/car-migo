import '@testing-library/jest-dom';
import { screen } from '@testing-library/react';
import TestUtils from '../../test_utils';
import CreateJourneys from '.';
import { useAuthStore } from '../../hooks/useAuthStore';

const initialStoreState = useAuthStore.getState();

describe('CreateJourneys Unit Tests', () => {
  test('renders CreateJourneys component', async () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<CreateJourneys />);

    expect(screen.getByLabelText('Origin')).toBeInTheDocument();
    expect(screen.getByLabelText('Destination')).toBeInTheDocument();
    expect(screen.getByTestId('create-journeys-date-time-picker')).toBeInTheDocument();
    expect(screen.getByTestId('create-journeys-max-passengers')).toBeInTheDocument();
    expect(screen.getByTestId('submit-button')).toBeInTheDocument();
  });
});
