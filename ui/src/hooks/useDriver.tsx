import { useQuery, UseQueryResult } from '@tanstack/react-query';
import { axiosInstance } from '../integration/instance';
import { IDriverLicenseNumberProperty, IDriverEntity } from '../interfaces';

const endpoint = '/v1/drivers';

export const useDriverProfile = (): UseQueryResult<IDriverEntity> =>
  useQuery({
    queryKey: ['useGetDriverProfile'],
    queryFn: async () => (await axiosInstance.get(endpoint + '/profile')).data,
  });

export const useDriverCreation = async (driver: IDriverLicenseNumberProperty) => {
  const response = await axiosInstance.post(endpoint + '/create', driver);
  return response.data;
};

export const useDriverDeletion = async () => {
  const response = await axiosInstance.delete(endpoint);
  return response.data;
};
