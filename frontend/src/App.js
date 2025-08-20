import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Login from './components/Login';
import Signup from './components/Signup';
import Dashboard from './components/Dashboard';
import Orders from './components/Orders';
import Customers from './components/Customers';
import Menu from './components/Menu';
import Staff from './components/Staff';
import Tables from './components/Tables';
import { AuthProvider, useAuth } from './context/AuthContext';
import { ThemeProvider } from './context/ThemeContext';
import './App.css';

function AppContent() {
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return (
      <div className="loading">
        <div>Loading...</div>
      </div>
    );
  }

  return (
    <Router>
      <div className="App">
        {isAuthenticated && <Navbar />}
        <main className={isAuthenticated ? 'main-content' : ''}>
          <Routes>
            <Route 
              path="/login" 
              element={!isAuthenticated ? <Login /> : <Navigate to="/dashboard" />} 
            />
            <Route 
              path="/signup" 
              element={!isAuthenticated ? <Signup /> : <Navigate to="/dashboard" />} 
            />
            <Route 
              path="/dashboard" 
              element={isAuthenticated ? <Dashboard /> : <Navigate to="/login" />} 
            />
            <Route 
              path="/orders" 
              element={isAuthenticated ? <Orders /> : <Navigate to="/login" />} 
            />
            <Route 
              path="/customers" 
              element={isAuthenticated ? <Customers /> : <Navigate to="/login" />} 
            />
            <Route 
              path="/menu" 
              element={isAuthenticated ? <Menu /> : <Navigate to="/login" />} 
            />
            <Route 
              path="/staff" 
              element={isAuthenticated ? <Staff /> : <Navigate to="/login" />} 
            />
            <Route 
              path="/tables" 
              element={isAuthenticated ? <Tables /> : <Navigate to="/login" />} 
            />
            <Route 
              path="/" 
              element={<Navigate to={isAuthenticated ? "/dashboard" : "/login"} />} 
            />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <AppContent />
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;