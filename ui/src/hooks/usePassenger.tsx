import { axiosInstance } from '../integration/instance';

const endpoint = '/v1/passengers';

export const usePassengerCreation = async () => {
  const response = await axiosInstance.post(endpoint + '/create');
  return response.data;
};

export const usePassengerDeletion = async () => {
  const response = await axiosInstance.delete(endpoint);
  return response.data;
};
