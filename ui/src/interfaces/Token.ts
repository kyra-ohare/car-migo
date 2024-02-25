export interface IAccessToken {
  iat: number;
  exp: number;
  sub: number;
}

export interface IStoreTokens {
  bearer: string;
  setBearer: (bearer: string) => void;
}

export interface IUseTokens {
  checkIfValidToken: (tokens: IToken) => Promise<void>;
  checkLocalStorageTokens: () => void;
  clearLocalStorageTokens: () => void;
}

export interface IToken {
  accessToken: string;
  refreshToken: string;
}
