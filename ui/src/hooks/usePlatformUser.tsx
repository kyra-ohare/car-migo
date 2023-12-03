import { AxiosResponse } from "axios";
import { axiosInstance, axiosInstanceNoAuth } from "../integration/instance";
import { useQuery, UseQueryResult } from "@tanstack/react-query";

const endpoint = "/v1/users";

export const useGetProfile = (): UseQueryResult<AxiosResponse<any>> =>
  useQuery({
    queryKey: ["useGetProfile"],
    queryFn: async () => (await axiosInstance.get(endpoint + "/profile")).data,
  });

interface ICreateUser {
  firstName: string;
  lastName: string;
  dob: Date;
  phoneNumber: string;
  email: string;
  password: string | undefined;
}

export const createUser = async (user: ICreateUser) => {
  const response = await axiosInstanceNoAuth.post(endpoint + "/create", user);
  return response.data;
};

export const confirmUserEmail = async (email: string) => {
  const response = await axiosInstanceNoAuth.post(
    endpoint + "/confirm-email?email=" + email
  );
  return response.data;
};

// tricky one
export const patchUser = async (user: ICreateUser) => {
  const response = await axiosInstance.patch(endpoint, user);
  return response.data;
};

export const deleteUser = async () => {
  const response = await axiosInstance.delete(endpoint);
  return response.data;
};

// These are Admin APIs
export const getUserProfileById = async (id: number) => {
  const response = await axiosInstance.get(endpoint + "/" + id);
  return response.data;
};

export const patchUserById = async (id: number) => {
  const response = await axiosInstance.post(endpoint + "/" + id);
  return response.data;
};

export const deleteUserById = async (id: number) => {
  const response = await axiosInstance.delete(endpoint + "/" + id);
  return response.data;
};
