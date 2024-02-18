import { axiosInstance, axiosInstanceNoAuth } from '../integration/instance';
import { useQuery, UseQueryResult } from '@tanstack/react-query';

const endpoint = '/v1/users';

interface ICreateUser {
  firstName: string;
  lastName: string;
  dob: string;
  phoneNumber: string;
  email: string;
  password: string | undefined;
  passenger: boolean;
  driver: boolean;
}

export const useGetProfile = (): UseQueryResult<ICreateUser> =>
  useQuery({
    queryKey: ['useGetProfile'],
    queryFn: async () => (await axiosInstance.get(endpoint + '/profile')).data,
  });

export const createUser = async (user: ICreateUser) => {
  const response = await axiosInstanceNoAuth.post(endpoint + '/create', user);
  return response.data;
};

export const confirmUserEmail = async (email: string) => {
  const response = await axiosInstanceNoAuth.post(
    endpoint + '/confirm-email?email=' + email
  );
  return response.data;
};

export const patchUser = async (user: ICreateUser) => {
  const response = await axiosInstance.patch(endpoint, user);
  return response.data;
};

export const deleteUser = async () => {
  const response = await axiosInstance.delete(endpoint);
  return response.data;
};

export const getAdminUserProfileById = async (id: number) => {
  const response = await axiosInstance.get(endpoint + '/' + id);
  return response.data;
};

export const patchAdminUserById = async (id: number) => {
  const response = await axiosInstance.post(endpoint + '/' + id);
  return response.data;
};

export const deleteAdminUserById = async (id: number) => {
  const response = await axiosInstance.delete(endpoint + '/' + id);
  return response.data;
};
