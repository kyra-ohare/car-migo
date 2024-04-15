/* eslint-disable @typescript-eslint/ban-ts-comment */
import { http, HttpResponse } from 'msw';
import { appConstants, locations } from '../constants';
import { testConstants } from './test_constants';

const baseUrl =
  appConstants.serverDomain + ':' + appConstants.serverPort + '/v1';

export const handlers = [
  http.post(baseUrl + '/users/confirm-email', ({ request }) => {
    const url = new URL(request.url);
    const email = url.searchParams.get('email');

    if (email == testConstants.validEmail) {
      return new HttpResponse(null, { status: 200 });
    }

    if (email == testConstants.notFoundEmail) {
      return new HttpResponse(null, { status: 404 });
    }

    if (email == testConstants.conflictEmail) {
      return new HttpResponse(null, { status: 409 });
    }

    if (email == testConstants.serverDown) {
      return new HttpResponse(null, { status: 503 });
    }

    return new HttpResponse(null, { status: 500 });
  }),

  http.post(baseUrl + '/users/create', async ({ request }) => {
    const info = await request.json();
    //@ts-ignore
    const { firstName, lastName, dob, phoneNumber, email, password, confirmPassword } = info!;

    if (
      firstName == testConstants.firstName &&
      lastName == testConstants.lastName &&
      dob == testConstants.dob &&
      phoneNumber == testConstants.phoneNumber &&
      email == testConstants.validEmail &&
      password == testConstants.validPassword &&
      confirmPassword == testConstants.validPassword
    ) {
      return new HttpResponse(null, { status: 200 });
    }

    if (
      firstName == testConstants.firstName &&
      lastName == testConstants.lastName &&
      dob == testConstants.dob &&
      phoneNumber == testConstants.phoneNumber &&
      email == testConstants.conflictEmail &&
      password == testConstants.validPassword &&
      confirmPassword == testConstants.validPassword
    ) {
      return new HttpResponse(null, { status: 409 });
    }

    return new HttpResponse(null, { status: 500 });
  }),

  http.post(baseUrl + '/login', async ({ request }) => {
    const info = await request.json();
    //@ts-ignore
    const { email, password } = info!;

    if (
      email == testConstants.validEmail &&
      password == testConstants.validPassword
    ) {
      return HttpResponse.json(
        {
          accessToken: testConstants.jwtToken,
          refreshToken: testConstants.jwtToken,
        },
        { status: 201 }
      );
    }

    if (email == testConstants.notFoundEmail) {
      return HttpResponse.json(
        {
          status: 403,
          error: 'Forbidden',
          message: 'Incorrect email (foo@example.com) and/or password.',
          path: 'Not specified',
          timestamp: '2024-04-02T20:24:53.006643887',
        },
        { status: 403 }
      );
    }

    return new HttpResponse(null, { status: 500 });
  }),

  http.get(baseUrl + '/users/profile', () => {
    return HttpResponse.json(
      {
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
      { status: 200 }
    );
  }),

  http.delete(baseUrl + '/users', () => {
    return new HttpResponse(null, { status: 204 });
  }),

  http.post(baseUrl + '/drivers/create', () => {
    return new HttpResponse(null, { status: 201 });
  }),

  http.delete(baseUrl + '/drivers', () => {
    return new HttpResponse(null, { status: 204 });
  }),

  http.post(baseUrl + '/passengers/create', () => {
    return new HttpResponse(null, { status: 201 });
  }),

  http.delete(baseUrl + '/passengers', () => {
    return new HttpResponse(null, { status: 204 });
  }),

  http.get(baseUrl + '/journeys/search', async ({ request }) => {
    const info = await request.json();
    //@ts-ignore
    const { locationIdFrom, locationIdTo, dateTimeFrom, dateTimeTo } = info!;
    console.log(info);

    if (
      locationIdFrom == locations[1].value &&
      locationIdTo == locations[3].value// &&
      // dateTimeFrom == '2023-11-30T09:00:00Z' &&
      // dateTimeTo == '2023-12-01T09:00:00Z'
    ) {
      return HttpResponse.json(
        {
          status: 404,
          error: 'Not Found',
          message:
            'No journeys found for this route. SearchJourneysRequest(locationIdFrom=1, locationIdTo=2, dateTimeFrom=2023-11-30T09:00:00Z, dateTimeTo=2023-12-01T09:00:00Z)',
          path: 'Not specified',
          timestamp: '2024-04-12T21:56:29.131168793',
        },
        { status: 404 }
      );
    }

    return new HttpResponse(null, { status: 500 });
  }),
];
