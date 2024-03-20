import { HttpResponse, http } from "msw";
import { appConstants } from "../constants";

const baseUrl = appConstants.serverDomain + ":" + appConstants.serverPort;

export const requestHandlers = [
  http.get(baseUrl + "/resource", () => {
    return HttpResponse.json({ id: "abc-123" });
  }),
];
