import { UseQueryResult, useQuery } from "@tanstack/react-query";
import { AxiosResponse } from "axios";
import { axiosInstance } from "../integration/instance";

const endpoint = "/v1/passengers";

export const getPassengerProfile = (): UseQueryResult<AxiosResponse<any>> =>
  useQuery({
    queryKey: ["useGetPassengerProfile"],
    queryFn: async () => (await axiosInstance.get(endpoint + "/profile")).data,
  });

// export const getPassengerProfile = async () => {
//   const response = await axiosInstance.get(endpoint + "/profile");
//   return response.data;
// };

export const createPassenger = async () => {
  const response = await axiosInstance.post(endpoint + "/create");
  return response.data;
};

export const deletePassenger = async () => {
  const response = await axiosInstance.delete(endpoint);
  return response.data;
};

// These are Admin APIs
interface IPassengerId {
  id: number;
}

export const getPassengerProfileById = async (id: IPassengerId) => {
  const response = await axiosInstance.get(endpoint + "/profile/" + id);
  return response.data;
};

export const createPassengerById = async (id: IPassengerId) => {
  const response = await axiosInstance.post(endpoint + "/create/" + id);
  return response.data;
};

export const deletePassengerById = async (id: IPassengerId) => {
  const response = await axiosInstance.delete(endpoint + "/profile/" + id);
  return response.data;
};
