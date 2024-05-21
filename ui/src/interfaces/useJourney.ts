export interface IJourneyRequest {
  locationIdFrom: string;
  locationIdTo: string;
  dateTimeFrom: string;
  dateTimeTo: string;
}

export interface IJourneyResponse {
  id: number;
  departure: number;
  destination: string;
  maxPassengers: number;
  availability: number;
  date: string;
  time: string;
}
