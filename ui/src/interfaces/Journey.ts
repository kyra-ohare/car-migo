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

export interface IJourneyLocationProperty {
  id: number;
  description: string;
}

export interface IJourneyProps {
  label: string;
  journeys: IJourneyEntity[];
  origin?: string | undefined;
  destination?: string;
  datatestid: string;
}

export interface IJourneyResponseProps {
  journey: {
    availability: number;
    dateTime: string;
    maxPassengers: number;
    driver: {
      platformUser: { firstName: string; lastName: string };
    };
    passengers: IPassengerEntity[];
  };
}
