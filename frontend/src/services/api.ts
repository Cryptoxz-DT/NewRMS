import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import { useAuthStore } from '@/store/authStore';
import { useUIStore } from '@/store/uiStore';

// Create axios instance
const api: AxiosInstance = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8085/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    const { accessToken } = useAuthStore.getState();
    
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor
api.interceptors.response.use(
  (response: AxiosResponse) => {
    return response;
  },
  async (error) => {
    const { addNotification } = useUIStore.getState();
    const { logout, refreshToken } = useAuthStore.getState();
    
    if (error.response?.status === 401) {
      // Try to refresh token
      if (refreshToken) {
        try {
          const response = await api.post('/auth/refresh', {
            refreshToken,
          });
          
          const { accessToken: newAccessToken } = response.data;
          useAuthStore.getState().login({
            ...response.data,
            accessToken: newAccessToken,
          });
          
          // Retry original request
          error.config.headers.Authorization = `Bearer ${newAccessToken}`;
          return api.request(error.config);
        } catch (refreshError) {
          logout();
          addNotification({
            type: 'error',
            title: 'Session Expired',
            message: 'Please log in again.',
          });
        }
      } else {
        logout();
        addNotification({
          type: 'error',
          title: 'Authentication Required',
          message: 'Please log in to continue.',
        });
      }
    } else if (error.response?.status >= 500) {
      addNotification({
        type: 'error',
        title: 'Server Error',
        message: 'Something went wrong. Please try again later.',
      });
    } else if (error.code === 'NETWORK_ERROR') {
      addNotification({
        type: 'error',
        title: 'Network Error',
        message: 'Please check your internet connection.',
      });
    }
    
    return Promise.reject(error);
  }
);

// Generic API methods
export const apiClient = {
  get: <T>(url: string, config?: AxiosRequestConfig): Promise<T> =>
    api.get(url, config).then((response) => response.data),
    
  post: <T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> =>
    api.post(url, data, config).then((response) => response.data),
    
  put: <T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> =>
    api.put(url, data, config).then((response) => response.data),
    
  patch: <T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> =>
    api.patch(url, data, config).then((response) => response.data),
    
  delete: <T>(url: string, config?: AxiosRequestConfig): Promise<T> =>
    api.delete(url, config).then((response) => response.data),
};

export default api;