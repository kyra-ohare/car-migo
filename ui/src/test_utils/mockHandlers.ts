import { http, HttpResponse } from 'msw';
import { appConstants } from '../constants';
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

    return new HttpResponse(null, { status: 500 });
  }),

  http.post(baseUrl + '/login', async ({ request }) => {
    console.log("from MockHandlers");
    console.log(request);
    const info = await request.formData();
    console.log("\ninfo", info);
    const url = new URL(request.url);
    const email = url.searchParams.get('email');
    const password = url.searchParams.get('password');
    // const email = request.body;
    

    if (
      email == testConstants.validEmail &&
      password == testConstants.validPassword
    ) {
      return new HttpResponse(null, { status: 200 });
      // return HttpResponse.json(
      //   {
      //     accessToken: 'access-token',
      //     refreshToken: 'refresh-token',
      //   },
      //   { status: 201 }
      // );
    }

    if (
      email == testConstants.notFoundEmail ||
      password == testConstants.invalidPassword
    ) {
      return new HttpResponse(null, { status: 403 });
      // return HttpResponse.json(
      //   {
      //     status: 403,
      //     error: 'Forbidden',
      //     message: 'Incorrect email (foo@example.com) and/or password.',
      //     path: 'Not specified',
      //     timestamp: '2024-04-02T20:24:53.006643887',
      //   },
      //   { status: 403 }
      // );
    }

    return new HttpResponse(null, { status: 200 });
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
];
