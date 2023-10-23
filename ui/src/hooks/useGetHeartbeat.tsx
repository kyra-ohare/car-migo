import { AxiosResponse } from "axios";
import { axiosInstance } from "../integration/instance";
import { useQuery, UseQueryResult } from "@tanstack/react-query";

export const useGetHeartbeat = (): UseQueryResult<AxiosResponse<any>> =>
  useQuery(
    "useGetHeartbeat,
    async () => (await axiosInstance.get(`/v1/heartbeat`)).data
  );
