import { UseQueryResult, useQuery } from "@tanstack/react-query";
import { AxiosResponse } from "axios";
import { axiosInstance, axiosInstanceNoAuth } from "../integration/instance";

const endpoint = "/v1/journeys";

export interface IJourney {
  // id: number;
  departure: number;
  // destination: string;
  // maxPassengers: number;
  // availability: number;
  // date: string;
  // time: string;
}

export const useJourneySearchQuery = (): UseQueryResult<IJourney> =>
  useQuery({
    queryKey: ["getJourneySearch"],
    queryFn: async () =>
      (
        await axiosInstanceNoAuth.get(endpoint + "/search", {
          params: {
            locationIdFrom: 5,
            locationIdTo: 1,
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