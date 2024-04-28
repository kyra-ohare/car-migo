import { UseQueryResult, useQuery } from '@tanstack/react-query';
import { axiosInstance, axiosInstanceNoAuth } from '../integration/instance';
import { IJourneyEntity, IJourneyRequest } from '../interfaces';

const endpoint = '/v1/journeys';

export const useJourneySearch = async (params: IJourneyRequest) => {
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

export const useAddPassenger = async (journeyId: number) => {
  const response = await axiosInstance.post(
    endpoint + '/' + journeyId + '/add-passenger'
  );
  return response.data;
};

export const useDeletePassenger = async (journeyId: number) => {
  const response = await axiosInstance.delete(
    endpoint + '/' + journeyId + '/remove-passenger'
  );
  return response.data;
};