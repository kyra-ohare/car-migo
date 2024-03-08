import '@testing-library/jest-dom';
import { screen } from '@testing-library/react';
import { useAuthStore } from './hooks/useAuthStore';
import TestUtils from './test_utils';
import App from './App';

// jest.mock('react-router-dom', () => ({
//   ...jest.requireActual('react-router-dom'),
//   useNavigate: () => jest.fn(),
// }));

const initialStoreState = useAuthStore.getState();

describe('App Unit Test', () => {
  test('renders App.tsx routes', () => {
    initialStoreState.setIsAuthorized(true);
    TestUtils.render(<App />);

    expect(screen.getByTestId('is-authorized-routes')).toBeInTheDocument();
    expect(screen.getByTestId('is-unauthorized-routes')).toBeInTheDocument();
  });
});
