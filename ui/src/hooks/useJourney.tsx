import { UseQueryResult, useQuery } from "@tanstack/react-query";
import { AxiosResponse } from "axios";
import { axiosInstance, axiosInstanceNoAuth } from "../integration/instance";

const endpoint = "/v1/journeys";

export interface IJourneyResponse {
  id: number;
  departure: number;
  destination: string;
  maxPassengers: number;
  availability: number;
  date: string;
  time: string;
}

export interface IJourneyParameters {
  locationIdFrom: number;
  locationIdTo: number;
  dateTimeFrom: string;
  dateTimeTo: string;
}

export const useJourneySearchQuery = (
  params: IJourneyParameters
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
    // refetchOnWindowFocus: false,
    retry: 1,
    enabled: false,
  });

// export const useGetProfile = (): UseQueryResult<ICreateUser> =>
// useQuery({
//   queryKey: ["useGetProfile"],
//   queryFn: async () => (await axiosInstance.get(endpoint + "/profile")).data,
// });
