import { http, HttpResponse } from 'msw';
import { locations } from '../constants';
import { testConstants } from './test_constants';

export const journeysHandlers = [
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
      return HttpResponse.json([
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
      ]);
    }

    return new HttpResponse(null, { status: 500 });
  }),
];
