import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import { axiosInstance, setBearerToken } from '../integration/instance';
import { appConstants, navigation } from '../constants';
import { IAccessToken, IToken, IUseTokens } from '../interfaces';
import { useAuthStore } from './useAuthStore';

export const useTokens = (): IUseTokens => {
  const { setIsAuthorized } = useAuthStore();
  const navigate = useNavigate();

  const checkIfValidToken = async (token: IToken) => {
    const decodedAccess = jwtDecode<IAccessToken>(token.accessToken);
    const decodedRefresh = jwtDecode<IAccessToken>(token.refreshToken);

    const accessTokenDate = new Date(decodedAccess.exp * 1000);
    const refreshTokenDate = new Date(decodedRefresh.exp * 1000);
    const nowDate = new Date();

    if (accessTokenDate > nowDate) {
      localStorage.setItem(appConstants.accessToken, token.accessToken);
      localStorage.setItem(appConstants.refreshToken, token.refreshToken);

      setBearerToken(token.accessToken);
      setIsAuthorized(true);
    }
    if (accessTokenDate < nowDate && refreshTokenDate > nowDate) {
      const config = {
        headers: { Authorization: `Bearer ${token.refreshToken}` },
      };

      const resp = await axiosInstance.get('/api/authenticate/refresh', config);

      localStorage.setItem(appConstants.accessToken, resp.data.accessToken);
      localStorage.setItem(appConstants.refreshToken, resp.data.refreshToken);
      setIsAuthorized(true);

      navigate(0);
    }
    if (accessTokenDate < nowDate && refreshTokenDate < nowDate) {
      setIsAuthorized(false);
    }
  };

  const checkLocalStorageTokens = () => {
    const localStorageAccess =
      localStorage.getItem(appConstants.accessToken) || '';
    const localStorageRefresh =
      localStorage.getItem(appConstants.refreshToken) || '';
    if (localStorageAccess.length !== 0 || localStorageRefresh.length !== 0) {
      checkIfValidToken({
        accessToken: localStorageAccess,
        refreshToken: localStorageRefresh,
      });
    } else {
      setIsAuthorized(false);
    }
  };

  const clearLocalStorageTokens = () => {
    localStorage.removeItem(appConstants.accessToken);
    localStorage.removeItem(appConstants.refreshToken);
    setIsAuthorized(false);
    navigate(navigation.SIGN_IN_PAGE);
  };

  return {
    checkIfValidToken,
    checkLocalStorageTokens,
    clearLocalStorageTokens,
  };
};
