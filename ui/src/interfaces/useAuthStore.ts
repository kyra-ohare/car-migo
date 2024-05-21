export interface IAuthStore {
  isAuthorized: boolean | null;
  setIsAuthorized: (authorized: boolean) => void;
}
