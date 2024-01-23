import { jwtDecode } from "jwt-decode";
import { axiosInstance, setBearerToken } from "../integration/instance";
import { useAuthStore } from "./authStore";
import { useNavigate } from "react-router-dom";
import constants from "../constants/app_constants";
import navigation from "../constants/navigation";

interface IAccessToken {
  iat: number;
  exp: number;
  sub: number;
}

interface IUseTokens {
  checkIfValidToken: (tokens: any) => Promise<void>;
  checkLocalStorageTokens: () => void;
  clearLocalStorageTokens: () => void;
}

const useTokens = (): IUseTokens => {
  const { setIsAuthorized } = useAuthStore();
  const navigate = useNavigate();

  const checkIfValidToken = async (tokens: any) => {
    const decodedAccess = jwtDecode<IAccessToken>(tokens.accessToken);
    const decodedRefresh = jwtDecode<IAccessToken>(tokens.refreshToken);

    const accessTokenDate = new Date(decodedAccess.exp * 1000);
    const refreshTokenDate = new Date(decodedRefresh.exp * 1000);
    const nowDate = new Date();

    if (accessTokenDate > nowDate) {
      // localStorage is a React built-in function
      localStorage.setItem(constants.accessToken, tokens.accessToken);
      localStorage.setItem(constants.refreshToken, tokens.refreshToken);

      setBearerToken(tokens.accessToken);
      setIsAuthorized(true);
    }
    // car-migo doesn't have refresh token yet
    if (accessTokenDate < nowDate && refreshTokenDate > nowDate) {
      const config = {
        headers: { Authorization: `Bearer ${tokens.refreshToken}` },
      };

      const resp = await axiosInstance.get("/api/authenticate/refresh", config);

      localStorage.setItem(constants.accessToken, resp.data.accessToken);
      localStorage.setItem(constants.refreshToken, resp.data.refreshToken);
      setIsAuthorized(true);

      navigate(0);
    }
    if (accessTokenDate < nowDate && refreshTokenDate < nowDate) {
      setIsAuthorized(false);
    }
  };

  const checkLocalStorageTokens = () => {
    const localStorageAccess = localStorage.getItem(constants.accessToken);
    const localStorageRefresh = localStorage.getItem(constants.refreshToken);
    if (localStorageAccess || localStorageRefresh) {
      checkIfValidToken({
        accessToken: localStorageAccess,
        refreshToken: localStorageRefresh,
      });
    } else {
      setIsAuthorized(false);
    }
  };

  const clearLocalStorageTokens = () => {
    localStorage.removeItem(constants.accessToken);
    localStorage.removeItem(constants.refreshToken);
    setIsAuthorized(false);
    navigate(navigation.SIGN_IN_PAGE);
  };

  return {
    checkIfValidToken,
    checkLocalStorageTokens,
    clearLocalStorageTokens,
  };
};

export default useTokens;
