import { AxiosResponse } from "axios";
import { axiosInstanceNoAuth } from "../integration/instance";
import { useQuery, UseQueryResult } from "@tanstack/react-query";

export const useGetHeartbeat = (): UseQueryResult<AxiosResponse<any>> =>
  useQuery({
    queryKey: ["useGetHeartbeat"],
    queryFn: async () => (await axiosInstanceNoAuth.get(`/v1/heartbeat`)).data,
  });
