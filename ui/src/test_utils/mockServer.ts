import { setupServer } from 'msw/node';
import { platformUserHandlers } from './mockHandlers_platformUsers';
import { driversHandlers } from './mockHandlers_drivers';
import { passengersHandlers } from './mockHandlers_passengers';
import { journeysHandlers } from './mockHandlers_journeys';

export const server = setupServer(
  ...platformUserHandlers,
  ...driversHandlers,
  ...passengersHandlers,
  ...journeysHandlers
);
