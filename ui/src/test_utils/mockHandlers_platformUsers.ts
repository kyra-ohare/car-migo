import { http, HttpResponse } from 'msw';
import { testConstants } from './test_constants';

export const platformUserHandlers = [
  http.post(testConstants.baseUrl + '/users/confirm-email', ({ request }) => {
    const url = new URL(request.url);
    const email = url.searchParams.get('email');

    if (email == testConstants.validEmail) {
      return new HttpResponse(null, { status: 200 });
    }

    if (email == testConstants.notFoundEmail) {
      return new HttpResponse(null, { status: 404 });
    }

    if (email == testConstants.conflictEmail) {
      return new HttpResponse(null, { status: 422 });
    }

    if (email == testConstants.serverDown) {
      return new HttpResponse(null, { status: 503 });
    }

    return new HttpResponse(null, { status: 500 });
  }),

  http.post(testConstants.baseUrl + '/users/create', async ({ request }) => {
    const info = await request.json();
    // @ts-expect-error Ignoring this error once I ensure it is possible to deconstruct `info`
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

  http.post(testConstants.baseUrl + '/login', async ({ request }) => {
    const info = await request.json();
    // @ts-expect-error Ignoring this error once I ensure it is possible to deconstruct `info`
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

  http.get(testConstants.baseUrl + '/users/profile', () => {
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

  http.delete(testConstants.baseUrl + '/users', () => {
    return new HttpResponse(null, { status: 204 });
  }),
];
