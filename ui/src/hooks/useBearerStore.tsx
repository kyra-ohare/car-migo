import { create } from 'zustand';
import { IStoreTokens } from '../interfaces';

export const useBearerStore = create<IStoreTokens>((set) => ({
  bearer: '',
  setBearer: (bearerToken: string) =>
    set(() => ({
      bearer: bearerToken,
    })),
}));
