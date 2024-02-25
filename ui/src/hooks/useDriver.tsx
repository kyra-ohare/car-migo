import { axiosInstance } from '../integration/instance';
import { IDriverCreation } from '../interfaces';

const endpoint = '/v1/drivers';

export const useDriverProfile = async () => {
  const response = await axiosInstance.get(endpoint + '/profile');
  return response.data;
};

export const useDriverCreation = async (driver: IDriverCreation) => {
  const response = await axiosInstance.post(endpoint + '/create', driver);
  return response.data;
};

export const useDriverDeletion = async () => {
  const response = await axiosInstance.delete(endpoint);
  return response.data;
};

export const useAdminDriverProfileById = async (id: number) => {
  const response = await axiosInstance.get(endpoint + '/profile/' + id);
  return response.data;
};

export const useAdminDriverCreationById = async (id: number) => {
  const response = await axiosInstance.post(endpoint + '/create', id);
  return response.data;
};

export const useAdminDeletionById = async (id: number) => {
  const response = await axiosInstance.delete(endpoint + '/profile/' + id);
  return response.data;
};
