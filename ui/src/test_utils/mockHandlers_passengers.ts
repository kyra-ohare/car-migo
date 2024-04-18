import { http, HttpResponse } from 'msw';
import { testConstants } from './test_constants';

export const passengersHandlers = [
  http.post(testConstants.baseUrl + '/passengers/create', () => {
    return new HttpResponse(null, { status: 201 });
  }),

  http.delete(testConstants.baseUrl + '/passengers', () => {
    return new HttpResponse(null, { status: 204 });
  }),
];
