import { IPlatformUserEntity } from './PlatformUser';

export interface IPassengerEntity {
  id: string;
  platformUser: IPlatformUserEntity;
}
