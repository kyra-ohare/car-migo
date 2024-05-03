import '@testing-library/jest-dom';
import { screen } from '@testing-library/react';
import TestUtils from '../../test_utils';
import CreateJourneys from '.';

describe('Home Unit Tests', () => {
  test('renders the homepage where authorization is set to false', async () => {
    TestUtils.render(<CreateJourneys />);

    expect(screen.getByTestId('create-journeys')).toBeInTheDocument();
  });
});
