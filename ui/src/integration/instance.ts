import axios, { InternalAxiosRequestConfig } from 'axios';
import { appConstants } from '../constants';

const serverURL = appConstants.serverDomain + ':' + appConstants.serverPort;

export const axiosInstanceNoAuth = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL || serverURL,
});

export const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL || serverURL,
});

export const setBearerToken = (bearer: string) => {
  axiosInstance.interceptors.request.use(
    (config): InternalAxiosRequestConfig => {
      if (config.headers) {
        config.headers.Authorization = `Bearer ${bearer}`;
      }
      return config;
    }
  );
};
