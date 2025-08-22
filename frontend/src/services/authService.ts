import { apiClient } from './api';
import { LoginRequest, LoginResponse, SignUpRequest, User } from '@/types';

export const authService = {
  // Login
  login: async (credentials: LoginRequest): Promise<LoginResponse> => {
    return apiClient.post<LoginResponse>('/auth/login', credentials);
  },

  // Sign up
  signUp: async (userData: SignUpRequest): Promise<{ user: User; message: string }> => {
    return apiClient.post<{ user: User; message: string }>('/auth/signup', userData);
  },

  // Refresh token
  refreshToken: async (refreshToken: string): Promise<LoginResponse> => {
    return apiClient.post<LoginResponse>('/auth/refresh', { refreshToken });
  },

  // Logout
  logout: async (): Promise<void> => {
    return apiClient.post<void>('/auth/logout');
  },

  // Get current user
  getCurrentUser: async (): Promise<User> => {
    return apiClient.get<User>('/auth/me');
  },

  // Update profile
  updateProfile: async (userData: Partial<User>): Promise<User> => {
    return apiClient.put<User>('/auth/profile', userData);
  },

  // Change password
  changePassword: async (data: {
    currentPassword: string;
    newPassword: string;
  }): Promise<void> => {
    return apiClient.post<void>('/auth/change-password', data);
  },

  // Forgot password
  forgotPassword: async (email: string): Promise<void> => {
    return apiClient.post<void>('/auth/forgot-password', { email });
  },

  // Reset password
  resetPassword: async (data: {
    token: string;
    newPassword: string;
  }): Promise<void> => {
    return apiClient.post<void>('/auth/reset-password', data);
  },

  // Verify email
  verifyEmail: async (token: string): Promise<void> => {
    return apiClient.post<void>('/auth/verify-email', { token });
  },

  // Resend verification email
  resendVerification: async (email: string): Promise<void> => {
    return apiClient.post<void>('/auth/resend-verification', { email });
  },
};