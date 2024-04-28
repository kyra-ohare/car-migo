import '@testing-library/jest-dom';
import { screen } from '@testing-library/react';
import TestUtils from '../../test_utils';
import YourJourneys from '.';

describe('Home Unit Tests', () => {
  test('renders the homepage where authorization is set to false', async () => {
    TestUtils.render(<YourJourneys />);

    expect(screen.getByTestId('create-journey-container')).toBeInTheDocument();
  });
});
