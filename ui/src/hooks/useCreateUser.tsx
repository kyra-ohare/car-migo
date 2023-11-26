import { axiosInstance, axiosInstanceNoAuth } from "../integration/instance";

interface ICreateUser {
  firstName: string;
  lastName: string;
  dob: Date;
  phoneNumber: string;
  email: string;
  password: string | undefined;
}

export const createUser = async (user: ICreateUser) => {
  const response = await axiosInstanceNoAuth.post("/v1/users/create", user);
  return response.data;
};

interface ICreatePassenger {
  id: string;
}

export const createPassenger = async (passenger: ICreatePassenger) => {
  const response = await axiosInstance.post("/v1/passengers/create", passenger);
  return response.data;
};

interface ICreateDriver {
  id: string;
  licenseNumber: string;
}

export const createDriver = async (driver: ICreateDriver) => {
  const response = await axiosInstance.post("/v1/drivers/create", driver);
  return response.data;
};
