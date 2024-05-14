import { IPlatformUserEntity } from '.';

export interface IDriverLicenseNumberProperty {
  licenseNumber: string;
}

export interface IDriverEntity {
  id: number;
  licenseNumber: string;
  platformUser: IPlatformUserEntity;
}
