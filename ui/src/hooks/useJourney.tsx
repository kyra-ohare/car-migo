import { UseQueryResult, useQuery } from "@tanstack/react-query";
import { axiosInstance, axiosInstanceNoAuth } from "../integration/instance";

const endpoint = "/v1/journeys";

export interface IJourneyRequest {
  locationIdFrom: string;
  locationIdTo: string;
  dateTimeFrom: string;
  dateTimeTo: string;
}

export interface IJourneyResponse {
  id: number;
  departure: number;
  destination: string;
  maxPassengers: number;
  availability: number;
  date: string;
  time: string;
}

export const useJourneySearchQuery = async (params: IJourneyRequest) => {
  const response = await axiosInstanceNoAuth.get(endpoint + "/search", {
    params: {
      locationIdFrom: params.locationIdFrom,
      locationIdTo: params.locationIdTo,
      dateTimeFrom: "2021-11-30T09:00:00Z",
      dateTimeTo: "2023-12-01T09:00:00Z",
    },
  });
  return response.data;
};

export const useJourneySearchQuery2 = (
  params: IJourneyRequest
): UseQueryResult<IJourneyResponse> =>
  useQuery({
    queryKey: ["getJourneySearch"],
    queryFn: async () =>
      (
        await axiosInstanceNoAuth.get(endpoint + "/search", {
          params: {
            locationIdFrom: params.locationIdFrom,
            locationIdTo: params.locationIdTo,
            dateTimeFrom: "2021-11-30T09:00:00Z",
            dateTimeTo: "2023-12-01T09:00:00Z",
          },
        })
      ).data,
    retry: 1,
    enabled: false,
  });
