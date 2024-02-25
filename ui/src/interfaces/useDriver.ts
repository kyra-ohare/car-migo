import { IPlatformUserEntity } from '.';

export interface IDriverCreation {
  licenseNumber: string;
}

export interface IDriverEntity {
  id: number;
  licenseNumber: string;
  platformUser: IPlatformUserEntity;
}
