import axios, { InternalAxiosRequestConfig } from "axios";

export const axiosInstanceNoAuth = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL || "http://localhost:8086",
});

export const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL || "http://localhost:8086",
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
