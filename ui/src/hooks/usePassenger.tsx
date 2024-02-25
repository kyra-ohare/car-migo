import { UseQueryResult, useQuery } from '@tanstack/react-query';
import { axiosInstance } from '../integration/instance';
import { IPassengerEntity } from '../interfaces';

const endpoint = '/v1/passengers';

export const usePassengerProfile = (): UseQueryResult<IPassengerEntity> =>
  useQuery({
    queryKey: ['useGetPassengerProfile'],
    queryFn: async () => (await axiosInstance.get(endpoint + '/profile')).data,
  });

export const usePassengerCreation = async () => {
  const response = await axiosInstance.post(endpoint + '/create');
  return response.data;
};

export const usePassengerDeletion = async () => {
  const response = await axiosInstance.delete(endpoint);
  return response.data;
};

export const useAdminPassengerProfileById = async (id: number) => {
  const response = await axiosInstance.get(endpoint + '/profile/' + id);
  return response.data;
};

export const useAdminPassengerCreationById = async (id: number) => {
  const response = await axiosInstance.post(endpoint + '/create/' + id);
  return response.data;
};

export const useAdminPassengerDeleteById = async (id: number) => {
  const response = await axiosInstance.delete(endpoint + '/profile/' + id);
  return response.data;
};
