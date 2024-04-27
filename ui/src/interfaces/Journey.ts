import { IDriverEntity, IPassengerEntity } from '.';

export interface IJourneyEntity {
  id: number;
  createdDate: string;
  locationFrom: IJourneyLocationProperty;
  locationTo: IJourneyLocationProperty;
  maxPassengers: number;
  availability: number;
  dateTime: string;
  driver: IDriverEntity;
  passengers: IPassengerEntity[];
}

export interface IJourneyProps {
  results: IJourneyEntity[];
  departure: string;
  destination: string;
  state: (state: boolean) => void;
}

export interface IJourneyLocationProperty {
  id: number;
  description: string;
}
