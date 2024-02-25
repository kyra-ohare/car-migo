import { useQuery, UseQueryResult } from '@tanstack/react-query';
import { axiosInstanceNoAuth } from '../integration/instance';
import { IHeartbeat } from '../interfaces';

export const useGetHeartbeat = (): UseQueryResult<IHeartbeat> =>
  useQuery({
    queryKey: ['useGetHeartbeat'],
    queryFn: async () => (await axiosInstanceNoAuth.get(`/v1/heartbeat`)).data,
  });
