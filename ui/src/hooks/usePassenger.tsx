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

// TODO: admin UI
// export const useAdminPassengerProfileById = async (id: number) => {
//   const response = await axiosInstance.get(endpoint + '/profile/' + id);
//   return response.data;
// };

// TODO: admin UI
// export const useAdminPassengerCreationById = async (id: number) => {
//   const response = await axiosInstance.post(endpoint + '/create/' + id);
//   return response.data;
// };

// TODO: admin UI
// export const useAdminPassengerDeleteById = async (id: number) => {
//   const response = await axiosInstance.delete(endpoint + '/profile/' + id);
//   return response.data;
// };
