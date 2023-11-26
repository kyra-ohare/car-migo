import { AxiosResponse } from "axios";
import { axiosInstance } from "../integration/instance";
import { useQuery, UseQueryResult } from "@tanstack/react-query";

export const useGetProfile = (): UseQueryResult<AxiosResponse<any>> =>
  useQuery({
    queryKey: ["useGetProfile"],
    queryFn: async () => (await axiosInstance.get(`/v1/users/profile`)).data,
  });

export default useGetProfile;
