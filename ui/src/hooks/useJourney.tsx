import { axiosInstanceNoAuth } from '../integration/instance';
import { IJourneyRequest } from '../interfaces';

const endpoint = '/v1/journeys';

export const useJourneySearchQuery = async (params: IJourneyRequest) => {
  const response = await axiosInstanceNoAuth.get(endpoint + '/search', {
    params: {
      locationIdFrom: params.locationIdFrom,
      locationIdTo: params.locationIdTo,
      dateTimeFrom: params.dateTimeFrom,
      dateTimeTo: params.dateTimeTo,
    },
  });
  return response.data;
};
