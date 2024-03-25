import { http, HttpResponse } from "msw";

const baseUrl = "http://localhost:8086";

export const handlers = [
  http.get(baseUrl + "/response", () => {
    return HttpResponse.json({});
  }),
  http.get(baseUrl + "/confirm-email", () => {
    return HttpResponse.json({});
  }),
];
