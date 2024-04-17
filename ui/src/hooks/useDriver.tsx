import { useQuery, UseQueryResult } from '@tanstack/react-query';
import { axiosInstance } from '../integration/instance';
import { IDriverCreation, IDriverEntity } from '../interfaces';

const endpoint = '/v1/drivers';

export const useDriverProfile = (): UseQueryResult<IDriverEntity> =>
  useQuery({
    queryKey: ['useGetDriverProfile'],
    queryFn: async () => (await axiosInstance.get(endpoint + '/profile')).data,
  });

export const useDriverCreation = async (driver: IDriverCreation) => {
  const response = await axiosInstance.post(endpoint + '/create', driver);
  return response.data;
};

export const useDriverDeletion = async () => {
  const response = await axiosInstance.delete(endpoint);
  return response.data;
};

// TODO: admin UI
// export const useAdminDriverProfileById = async (id: number) => {
//   const response = await axiosInstance.get(endpoint + '/profile/' + id);
//   return response.data;
// };

// TODO: admin UI
// export const useAdminDriverCreationById = async (id: number) => {
//   const response = await axiosInstance.post(endpoint + '/create', id);
//   return response.data;
// };

// TODO: admin UI
// export const useAdminDeletionById = async (id: number) => {
//   const response = await axiosInstance.delete(endpoint + '/profile/' + id);
//   return response.data;
// };
