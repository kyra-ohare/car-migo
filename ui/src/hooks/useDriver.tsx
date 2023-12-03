import { axiosInstance } from "../integration/instance";

const endpoint = "/v1/drivers";

interface ICreateDriver {
  licenseNumber: string;
}

export const getDriver = async () => {
  const response = await axiosInstance.get(endpoint + "/profile");
  return response.data;
};

export const createDriver = async (driver: ICreateDriver) => {
  const response = await axiosInstance.post(endpoint + "/create", driver);
  return response.data;
};

export const deleteDriver = async () => {
  const response = await axiosInstance.delete(endpoint);
  return response.data;
};

// These are Admin APIs
interface IDriverId {
  id: number;
}

export const getDriverById = async (id: IDriverId) => {
  const response = await axiosInstance.get(endpoint + "/profile/" + id);
  return response.data;
};

export const createDriverById = async (id: IDriverId) => {
  const response = await axiosInstance.post(endpoint + "/create", id);
  return response.data;
};

export const deleteDriverById = async (id: IDriverId) => {
  const response = await axiosInstance.delete(endpoint + "/profile/" + id);
  return response.data;
};
