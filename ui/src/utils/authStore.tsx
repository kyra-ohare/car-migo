import { create } from "zustand"; // it'll be availabe across the board.

interface IAuthStore {
  isAuthorized: boolean | null;
  setIsAuthorized: (authorized: boolean) => void;
}

export const useAuthStore = create<IAuthStore>((set) => ({
  isAuthorized: null,

  setIsAuthorized: (authorized: boolean) => {
    set(() => ({
      isAuthorized: authorized,
    }));
  },
}));
