import { useQuery, UseQueryResult } from '@tanstack/react-query';
import { AxiosResponse } from 'axios';
import { axiosInstanceNoAuth } from '../integration/instance';

export const useGetHeartbeat = (): UseQueryResult<AxiosResponse<any>> =>
  useQuery({
    queryKey: ['useGetHeartbeat'],
    queryFn: async () => (await axiosInstanceNoAuth.get(`/v1/heartbeat`)).data,
  });
