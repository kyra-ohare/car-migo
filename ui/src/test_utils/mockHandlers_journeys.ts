import { http, HttpResponse } from 'msw';
import { locations } from '../constants';
import { testConstants } from './test_constants';

export const journeysHandlers = [
  http.post(testConstants.baseUrl + '/journeys', async ({ request }) => {
    const info = await request.json();

    // @ts-expect-error Ignoring this error once I ensure it is possible to deconstruct `info`
    const { locationIdFrom, locationIdTo, dateTime, maxPassengers } = info;

    if (
      locationIdFrom === 3 &&
      locationIdTo === 4 &&
      dateTime === '2016-04-15T09:00:00.000Z' &&
      maxPassengers === 2
    ) {
      return new HttpResponse(null, { status: 201 });
    }

    if (
      locationIdFrom === 1 &&
      locationIdTo === 2 &&
      dateTime === '2024-05-20T10:00:00.000Z' &&
      maxPassengers === 4
    ) {
      return new HttpResponse(null, { status: 403 });
    }

    if (
      locationIdFrom === 2 &&
      locationIdTo === 4 &&
      dateTime === '2024-09-11T18:00:00.000Z' &&
      maxPassengers === 5
    ) {
      return new HttpResponse(null, { status: 404 });
    }

    return new HttpResponse(null, { status: 500 });
  }),

  http.get(testConstants.baseUrl + '/journeys/search', async ({ request }) => {
    const url = new URL(request.url);
    const locationIdFrom = url.searchParams.get('locationIdFrom');
    const locationIdTo = url.searchParams.get('locationIdTo');

    if (
      locationIdFrom == locations[0].value.toString() &&
      locationIdTo == locations[2].value.toString()
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

    if (
      locationIdFrom == locations[4].value.toString() &&
      locationIdTo == locations[0].value.toString()
    ) {
      return HttpResponse.json(
        [
          {
            id: 5,
            createdDate: '2022-01-05T00:00:00Z',
            locationFrom: {
              id: 5,
              description: 'Newry',
            },
            locationTo: {
              id: 1,
              description: 'Rostrevor',
            },
            maxPassengers: 3,
            availability: 1,
            dateTime: '2022-12-02T08:15:00Z',
          },
          {
            id: 6,
            createdDate: '2022-01-05T00:00:00Z',
            locationFrom: {
              id: 5,
              description: 'Newry',
            },
            locationTo: {
              id: 1,
              description: 'Rostrevor',
            },
            maxPassengers: 3,
            availability: 3,
            dateTime: '2022-12-03T08:00:00Z',
          },
          {
            id: 7,
            createdDate: '2022-31-10T00:00:00Z',
            locationFrom: {
              id: 5,
              description: 'Newry',
            },
            locationTo: {
              id: 1,
              description: 'Rostrevor',
            },
            maxPassengers: 3,
            availability: 1,
            dateTime: '2023-04-01T12:00:00Z',
          },
          {
            id: 8,
            createdDate: '2024-31-10T15:00:00Z',
            locationFrom: {
              id: 5,
              description: 'Newry',
            },
            locationTo: {
              id: 1,
              description: 'Rostrevor',
            },
            maxPassengers: 2,
            availability: 2,
            dateTime: '2024-04-01T12:00:00Z',
          },
        ],
        { status: 200 }
      );
    }
    return new HttpResponse(null, { status: 500 });
  }),

  http.get(testConstants.baseUrl + '/journeys/passengers/my-journeys', () => {
    return HttpResponse.json(
      [
        {
          id: 1,
          createdDate: '2021-11-30T00:00:00Z',
          locationFrom: { id: 1, description: 'Rostrevor' },
          locationTo: { id: 2, description: 'Belfast' },
          maxPassengers: 2,
          availability: 0,
          dateTime: '2021-12-01T09:00:00Z',
          driver: {
            id: 1,
            licenseNumber: '35789654',
            platformUser: {
              id: 1,
              createdDate: '2021-12-24T00:00:00Z',
              firstName: 'John',
              lastName: 'Smith',
              dob: '1970-02-23T00:00:00Z',
              email: 'john.smith@example.com',
              phoneNumber: '0287513626',
              userAccessStatus: { id: 2, status: 'ACTIVE' },
              passenger: false,
              driver: false,
            },
          },
        },
        {
          id: 2,
          createdDate: '2021-11-30T00:00:00Z',
          locationFrom: { id: 3, description: 'Downpatrick' },
          locationTo: { id: 4, description: 'Armagh' },
          maxPassengers: 3,
          availability: 0,
          dateTime: '2021-12-02T09:00:00Z',
          driver: {
            id: 1,
            licenseNumber: '35789654',
            platformUser: {
              id: 1,
              createdDate: '2021-12-24T00:00:00Z',
              firstName: 'John',
              lastName: 'Smith',
              dob: '1970-02-23T00:00:00Z',
              email: 'john.smith@example.com',
              phoneNumber: '0287513626',
              userAccessStatus: { id: 2, status: 'ACTIVE' },
              passenger: false,
              driver: false,
            },
          },
        },
      ],
      { status: 200 }
    );
  }),

  http.get(testConstants.baseUrl + '/journeys/drivers/my-journeys', () => {
    return HttpResponse.json(
      [
        {
          id: 3,
          createdDate: '2022-01-04T00:00:00Z',
          locationFrom: { id: 4, description: 'Armagh' },
          locationTo: { id: 3, description: 'Downpatrick' },
          maxPassengers: 4,
          availability: 1,
          dateTime: '2022-01-04T15:30:00Z',
          driver: {
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
              passenger: false,
              driver: false,
            },
          },
          passengers: [
            {
              id: 1,
              platformUser: {
                id: 1,
                createdDate: '2021-12-24T00:00:00Z',
                firstName: 'John',
                lastName: 'Smith',
                dob: '1970-02-23T00:00:00Z',
                email: 'john.smith@example.com',
                phoneNumber: '0287513626',
                userAccessStatus: { id: 2, status: 'ACTIVE' },
                passenger: false,
                driver: false,
              },
            },
            {
              id: 3,
              platformUser: {
                id: 3,
                createdDate: '2022-01-05T00:00:00Z',
                firstName: 'Paul',
                lastName: 'Gibson',
                dob: '1995-07-08T00:00:00Z',
                email: 'paul.gibson@example.com',
                phoneNumber: '0286547891',
                userAccessStatus: { id: 2, status: 'ACTIVE' },
                passenger: false,
                driver: false,
              },
            },
            {
              id: 4,
              platformUser: {
                id: 4,
                createdDate: '2022-01-28T00:00:00Z',
                firstName: 'Jess',
                lastName: 'Brown',
                dob: '1986-01-04T00:00:00Z',
                email: 'jess.brown@example.com',
                phoneNumber: '0283215978',
                userAccessStatus: { id: 3, status: 'SUSPENDED' },
                passenger: false,
                driver: false,
              },
            },
          ],
        },
        {
          id: 4,
          createdDate: "2021-11-30T00:00:00Z",
          locationFrom: {
            id: 1,
            description: "Rostrevor"
          },
          locationTo: {
            id: 2,
            description: "Belfast"
          },
          maxPassengers: 2,
          availability: 0,
          dateTime: "2021-12-01T09:00:00Z",
          driver: {
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
              passenger: false,
              driver: false,
            },
          },
          passengers: [
            {
              id: 1,
              platformUser: {
                id: 1,
                createdDate: '2021-12-24T00:00:00Z',
                firstName: 'John',
                lastName: 'Smith',
                dob: '1970-02-23T00:00:00Z',
                email: 'john.smith@example.com',
                phoneNumber: '0287513626',
                userAccessStatus: { id: 2, status: 'ACTIVE' },
                passenger: false,
                driver: false,
              },
            },
            {
              id: 3,
              platformUser: {
                id: 3,
                createdDate: '2022-01-05T00:00:00Z',
                firstName: 'Paul',
                lastName: 'Gibson',
                dob: '1995-07-08T00:00:00Z',
                email: 'paul.gibson@example.com',
                phoneNumber: '0286547891',
                userAccessStatus: { id: 2, status: 'ACTIVE' },
                passenger: false,
                driver: false,
              },
            },
          ],
        },
      ],
      { status: 200 }
    );
  }),

  http.post(
    testConstants.baseUrl + '/journeys/:passengerId/add-passenger',
    ({ params }) => {
      const { passengerId } = params;

      if (passengerId === '5') return new HttpResponse(null, { status: 200 });

      if (passengerId === '6') return new HttpResponse(null, { status: 409 });

      if (passengerId === '7') return new HttpResponse(null, { status: 403 });

      if (passengerId === '8') return new HttpResponse(null, { status: 404 });

      return new HttpResponse(null, { status: 500 });
    }
  ),

  http.delete(
    testConstants.baseUrl + '/journeys/:journeyId/remove-passenger',
    ({ params }) => {
      const { journeyId } = params;
      if (journeyId === '1') {
        return new HttpResponse(null, { status: 204 });
      }
      return new HttpResponse(null, { status: 500 });
    }
  ),

  http.delete(testConstants.baseUrl + '/journeys/:journeyId', ({ params }) => {
    const { journeyId } = params;
    if (journeyId === '3') {
      return new HttpResponse(null, { status: 204 });
    }
    return new HttpResponse(null, { status: 500 });
  }),
];
