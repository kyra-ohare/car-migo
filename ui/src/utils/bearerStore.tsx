import { create } from 'zustand';

interface ITokensStore {
  bearer: string;
  setBearer: (bearer: string) => void;
}

export const bearerStore = create<ITokensStore>((set) => ({
  bearer: '',
  setBearer: (bearerToken: string) =>
    set(() => ({
      bearer: bearerToken,
    })),
}));

