import React, { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Set up axios defaults
  useEffect(() => {
    const token = localStorage.getItem('authToken');
    if (token) {
      axios.defaults.headers.common['Authorization'] = `Basic ${token}`;
      setIsAuthenticated(true);
      // You might want to validate the token here
    }
    setLoading(false);
  }, []);

  const login = async (username, password) => {
    try {
      // Create base64 encoded credentials
      const credentials = btoa(`${username}:${password}`);
      
      // Set the authorization header
      axios.defaults.headers.common['Authorization'] = `Basic ${credentials}`;
      
      // Test the credentials by making a request to a protected endpoint
      const response = await axios.get('/api/auth/user');
      
      // If successful, store the credentials and update state
      localStorage.setItem('authToken', credentials);
      setIsAuthenticated(true);
      setUser(response.data);
      
      return { success: true };
    } catch (error) {
      // Clear any stored credentials on failure
      delete axios.defaults.headers.common['Authorization'];
      localStorage.removeItem('authToken');
      
      return { 
        success: false, 
        error: error.response?.data?.message || 'Invalid credentials' 
      };
    }
  };

  const logout = () => {
    localStorage.removeItem('authToken');
    delete axios.defaults.headers.common['Authorization'];
    setIsAuthenticated(false);
    setUser(null);
  };

  const value = {
    isAuthenticated,
    user,
    loading,
    login,
    logout
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};