import { http, HttpResponse } from 'msw';
import { testConstants } from './test_constants';

export const driversHandlers = [
  http.get(testConstants.baseUrl + '/drivers/profile', () => {
    return HttpResponse.json(
      {
        id: 2,
        licenseNumber: '16548329',
        platformUser: {
          id: 2,
          createdDate: '2022-01-04T00:00:00Z',
          firstName: 'Mary',
          lastName: 'Green',
          dob: '1990-06-30T00:00:00Z',
          email: 'mary.green@example.com',
          phoneNumber: '0286579635',
          userAccessStatus: { id: 2, status: 'ACTIVE' },
          passenger: true,
          driver: true,
        },
      },
      { status: 200 }
    );
  }),

  http.post(testConstants.baseUrl + '/drivers/create', () => {
    return new HttpResponse(null, { status: 201 });
  }),

  http.delete(testConstants.baseUrl + '/drivers', () => {
    return new HttpResponse(null, { status: 204 });
  }),
];
