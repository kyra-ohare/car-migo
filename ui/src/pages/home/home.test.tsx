import '@testing-library/jest-dom';
import Homepage from '.';
import { screen } from '@testing-library/react';
import TestUtils from '../../test_utils';

describe('Home Unit Tests', () => {
  test('renders the homepage', () => {
    TestUtils.render(<Homepage />);
    expect(screen.getByText('Welcome to Car-Migo')).toBeInTheDocument();
  });
});
