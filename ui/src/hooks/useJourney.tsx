import { UseQueryResult, useQuery } from '@tanstack/react-query';
import { axiosInstance, axiosInstanceNoAuth } from '../integration/instance';
import { IJourneyEntity, IJourneyRequest } from '../interfaces';

const endpoint = '/v1/journeys';

export const useJourneySearchQuery = async (params: IJourneyRequest) => {
  const response = await axiosInstanceNoAuth.get(endpoint + '/search', {
    params: {
      locationIdFrom: params.locationIdFrom,
      locationIdTo: params.locationIdTo,
      dateTimeFrom: params.dateTimeFrom,
      dateTimeTo: params.dateTimeTo,
    },
  });
  return response.data;
};

export const useGetDriverJourneys = (): UseQueryResult<IJourneyEntity[]> =>
  useQuery({
    queryKey: ['useGetDriverJourneys'],
    queryFn: async () =>
      (await axiosInstance.get(endpoint + '/drivers/my-journeys')).data,
  });

export const useGetPassengerJourneys = (): UseQueryResult<IJourneyEntity[]> =>
  useQuery({
    queryKey: ['useGetPassengerJourneys'],
    queryFn: async () =>
      (await axiosInstance.get(endpoint + '/passengers/my-journeys')).data,
  });
