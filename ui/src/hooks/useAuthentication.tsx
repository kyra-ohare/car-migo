import { axiosInstanceNoAuth } from '../integration/instance';
import { IAuthenticationRequest } from '../interfaces';

export const useAuthentication = async (request: IAuthenticationRequest) => {
  const response = await axiosInstanceNoAuth.post('/v1/login', request);
  return response.data;
};
