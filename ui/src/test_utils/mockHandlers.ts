import { http, HttpResponse } from 'msw';
import { appConstants } from '../constants';

const baseUrl = appConstants.serverDomain + ':' + appConstants.serverPort;

export const handlers = [
  http.get(baseUrl + '/response', () => {
    return HttpResponse.json({});
  }),
  http.get(baseUrl + '/confirm-email', () => {
    return new HttpResponse(null, { status: 404 });
  }),
  // http.get(baseUrl + '/confirm-email', ({ request }) => {
  //   // no body when 200 OK

  //   // 404 body:
  //   //   {
  //   //     "status": 404,
  //   //     "error": "Not Found",
  //   //     "message": "User not found!",
  //   //     "path": "Not specified",
  //   //     "timestamp": "2024-03-27T12:08:47.98217684"
  //   // }

  //   // 409 CONFLICT
  //   //   {
  //   //     "status": 409,
  //   //     "error": "Conflict",
  //   //     "message": "User is already active",
  //   //     "path": "Not specified",
  //   //     "timestamp": "2024-03-27T12:09:19.72731059"
  //   // }
  //   const url = new URL(request.url);
  //   const email = url.searchParams.get('email');
  //   if (!email) {
  //     return new HttpResponse(null, { status: 404 });
  //   }
  //   // return HttpResponse.json({ worked: true });
  // }),
];
