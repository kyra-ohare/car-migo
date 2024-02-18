import { axiosInstanceNoAuth } from '../integration/instance';

interface IAuthenticationRequest {
  email: string;
  password: string;
}

export const authenticate = async (request: IAuthenticationRequest) => {
  const response = await axiosInstanceNoAuth.post('/v1/login', request);
  return response.data;
};
