import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { UtensilsCrossed, Eye, EyeOff } from 'lucide-react';
import './Login.css';

const Login = () => {
  const [credentials, setCredentials] = useState({
    username: '',
    password: ''
  });
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  
  const { login } = useAuth();

  const handleChange = (e) => {
    setCredentials({
      ...credentials,
      [e.target.name]: e.target.value
    });
    // Clear error when user starts typing
    if (error) setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await fetch('http://localhost:8085/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          usernameOrEmail: credentials.username,
          password: credentials.password
        }),
      });

      const data = await response.json();

      if (response.ok) {
        // Store JWT tokens
        localStorage.setItem('accessToken', data.accessToken);
        localStorage.setItem('refreshToken', data.refreshToken);
        localStorage.setItem('user', JSON.stringify({
          id: data.id,
          firstName: data.firstName,
          lastName: data.lastName,
          email: data.email,
          username: data.username,
          roles: data.roles
        }));
        
        // Call the login function from context
        const result = await login(credentials.username, credentials.password);
        
        if (!result.success) {
          setError(result.error);
        }
      } else {
        setError(data.message || 'Login failed');
      }
    } catch (error) {
      setError('Network error. Please try again.');
    }
    
    setLoading(false);
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <div className="login-header">
          <div className="login-icon">
            <UtensilsCrossed size={32} />
          </div>
          <h1 className="login-title">NewRMS</h1>
          <p className="login-subtitle">Restaurant Management System</p>
        </div>

        <form onSubmit={handleSubmit} className="login-form">
          {error && (
            <div className="error">
              {error}
            </div>
          )}

          <div className="form-group">
            <label htmlFor="username" className="form-label">
              Username
            </label>
            <input
              type="text"
              id="username"
              name="username"
              value={credentials.username}
              onChange={handleChange}
              className="form-input"
              placeholder="Enter your username"
              required
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="password" className="form-label">
              Password
            </label>
            <div className="password-input-container">
              <input
                type={showPassword ? 'text' : 'password'}
                id="password"
                name="password"
                value={credentials.password}
                onChange={handleChange}
                className="form-input"
                placeholder="Enter your password"
                required
                disabled={loading}
              />
              <button
                type="button"
                className="password-toggle"
                onClick={() => setShowPassword(!showPassword)}
                disabled={loading}
              >
                {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </div>
          </div>

          <button
            type="submit"
            className="login-btn"
            disabled={loading || !credentials.username || !credentials.password}
          >
            {loading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>

        <div className="login-footer">
          <p>
            Don't have an account?{' '}
            <Link to="/signup" className="signup-link">
              Sign Up
            </Link>
          </p>
          <hr style={{ margin: '20px 0', border: 'none', borderTop: '1px solid #eee' }} />
          <p>Demo Credentials:</p>
          <p><strong>Username:</strong> admin</p>
          <p><strong>Password:</strong> password</p>
        </div>
      </div>
    </div>
  );
};

export default Login;