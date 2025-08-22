import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { authService } from '../services/authService';
import { useAuthStore } from '../store/authStore';
import { useUIStore } from '../store/uiStore';
import { LoginRequest, SignUpRequest } from '../types';

export const useAuth = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const { login: setAuthData, logout: clearAuthData, setLoading, setError } = useAuthStore();
  const { addNotification } = useUIStore();

  // Login mutation
  const loginMutation = useMutation({
    mutationFn: authService.login,
    onMutate: () => {
      setLoading(true);
      setError(null);
    },
    onSuccess: (data) => {
      setAuthData(data);
      addNotification({
        type: 'success',
        title: 'Welcome back!',
        message: `Hello ${data.user.firstName || data.user.username}`,
      });
      navigate('/dashboard');
    },
    onError: (error: any) => {
      const message = error.response?.data?.message || 'Login failed';
      setError(message);
      addNotification({
        type: 'error',
        title: 'Login Failed',
        message,
      });
    },
    onSettled: () => {
      setLoading(false);
    },
  });

  // Sign up mutation
  const signUpMutation = useMutation({
    mutationFn: authService.signUp,
    onMutate: () => {
      setLoading(true);
      setError(null);
    },
    onSuccess: (data) => {
      addNotification({
        type: 'success',
        title: 'Account Created',
        message: data.message || 'Please check your email to verify your account',
      });
      navigate('/login');
    },
    onError: (error: any) => {
      const message = error.response?.data?.message || 'Sign up failed';
      setError(message);
      addNotification({
        type: 'error',
        title: 'Sign Up Failed',
        message,
      });
    },
    onSettled: () => {
      setLoading(false);
    },
  });

  // Logout mutation
  const logoutMutation = useMutation({
    mutationFn: authService.logout,
    onSuccess: () => {
      clearAuthData();
      queryClient.clear();
      addNotification({
        type: 'info',
        title: 'Logged Out',
        message: 'You have been successfully logged out',
      });
      navigate('/login');
    },
    onError: () => {
      // Even if logout fails on server, clear local data
      clearAuthData();
      queryClient.clear();
      navigate('/login');
    },
  });

  // Get current user query
  const { data: currentUser, isLoading: isLoadingUser } = useQuery({
    queryKey: ['auth', 'currentUser'],
    queryFn: authService.getCurrentUser,
    enabled: !!useAuthStore.getState().accessToken,
    retry: false,
    staleTime: 5 * 60 * 1000, // 5 minutes
  });

  // Update profile mutation
  const updateProfileMutation = useMutation({
    mutationFn: authService.updateProfile,
    onSuccess: (data) => {
      queryClient.setQueryData(['auth', 'currentUser'], data);
      useAuthStore.getState().updateUser(data);
      addNotification({
        type: 'success',
        title: 'Profile Updated',
        message: 'Your profile has been updated successfully',
      });
    },
    onError: (error: any) => {
      const message = error.response?.data?.message || 'Failed to update profile';
      addNotification({
        type: 'error',
        title: 'Update Failed',
        message,
      });
    },
  });

  // Change password mutation
  const changePasswordMutation = useMutation({
    mutationFn: authService.changePassword,
    onSuccess: () => {
      addNotification({
        type: 'success',
        title: 'Password Changed',
        message: 'Your password has been changed successfully',
      });
    },
    onError: (error: any) => {
      const message = error.response?.data?.message || 'Failed to change password';
      addNotification({
        type: 'error',
        title: 'Password Change Failed',
        message,
      });
    },
  });

  // Forgot password mutation
  const forgotPasswordMutation = useMutation({
    mutationFn: authService.forgotPassword,
    onSuccess: () => {
      addNotification({
        type: 'success',
        title: 'Reset Email Sent',
        message: 'Please check your email for password reset instructions',
      });
    },
    onError: (error: any) => {
      const message = error.response?.data?.message || 'Failed to send reset email';
      addNotification({
        type: 'error',
        title: 'Reset Failed',
        message,
      });
    },
  });

  return {
    // Mutations
    login: (credentials: LoginRequest) => loginMutation.mutate(credentials),
    signUp: (userData: SignUpRequest) => signUpMutation.mutate(userData),
    logout: () => logoutMutation.mutate(),
    updateProfile: (userData: any) => updateProfileMutation.mutate(userData),
    changePassword: (data: any) => changePasswordMutation.mutate(data),
    forgotPassword: (email: string) => forgotPasswordMutation.mutate(email),

    // Loading states
    isLoggingIn: loginMutation.isPending,
    isSigningUp: signUpMutation.isPending,
    isLoggingOut: logoutMutation.isPending,
    isUpdatingProfile: updateProfileMutation.isPending,
    isChangingPassword: changePasswordMutation.isPending,
    isSendingResetEmail: forgotPasswordMutation.isPending,
    isLoadingUser,

    // Data
    currentUser,

    // Auth state
    ...useAuthStore(),
  };
};