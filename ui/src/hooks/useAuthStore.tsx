import { create } from 'zustand';
import { IAuthStore } from '../interfaces';

export const useAuthStore = create<IAuthStore>((set) => ({
  isAuthorized: null,
  setIsAuthorized: (authorized: boolean) => {
    set(() => ({
      isAuthorized: authorized,
    }));
  },
}));
