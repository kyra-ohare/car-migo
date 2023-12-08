import { create } from "zustand";

interface ITokensStore {
  bearer: string;
  setBearer: (bearer: string) => void;
}

const tokenStore = create<ITokensStore>((set) => ({
  bearer: "",
  setBearer: (bearerToken: string) =>
    set(() => ({
      bearer: bearerToken,
    })),
}));

export default tokenStore;
