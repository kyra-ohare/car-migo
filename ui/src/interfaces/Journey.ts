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
  journey: {
    availability: number;
    dateTime: string;
    maxPassengers: number;
    driver: {
      platformUser: { firstName: any; lastName: any };
    };
    passengers: IPassengerEntity[];
  };
}

export interface IJourneyLocationProperty {
  id: number;
  description: string;
}
