import { useQuery, UseQueryResult } from '@tanstack/react-query';
import { axiosInstance, axiosInstanceNoAuth } from '../integration/instance';
import {
  IPlatformUserCreation,
  IPlatformUserEmail,
  IPlatformUserEntity,
} from '../interfaces';

const endpoint = '/v1/users';

export const useUserProfile = (): UseQueryResult<IPlatformUserEntity> =>
  useQuery({
    queryKey: ['useGetProfile'],
    queryFn: async () => (await axiosInstance.get(endpoint + '/profile')).data,
  });

export const useUserCreation = async (user: IPlatformUserCreation) => {
  user.dob = '1990-06-30T00:00:00Z'; // TODO
  const response = await axiosInstanceNoAuth.post(endpoint + '/create', user);
  return response.data;
};

export const useEmailConfirmation = async (params: IPlatformUserEmail) => {
  const response = await axiosInstanceNoAuth.post(
    endpoint + '/confirm-email?email=' + params.email
  );
  return response.data;
};

export const useUserDeletion = async () => {
  const response = await axiosInstance.delete(endpoint);
  return response.data;
};
