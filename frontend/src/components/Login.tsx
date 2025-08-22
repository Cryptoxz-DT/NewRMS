import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { Link } from 'react-router-dom';
import { User, Lock, ChefHat } from 'lucide-react';
import { useAuth } from '../hooks/useAuth';
import Button from './ui/Button';
import Input from './ui/Input';
import Card from './ui/Card';

const Login: React.FC = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
  });
  const { login, isLoggingIn, error } = useAuth();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    login(formData);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData(prev => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-50 via-white to-secondary-50 dark:from-secondary-950 dark:via-secondary-900 dark:to-secondary-950 flex items-center justify-center p-4">
      <motion.div
        className="w-full max-w-md"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
      >
        {/* Logo and Title */}
        <div className="text-center mb-8">
          <motion.div
            className="inline-flex items-center justify-center w-16 h-16 bg-primary-600 rounded-2xl mb-4"
            whileHover={{ scale: 1.05, rotate: 5 }}
            transition={{ type: 'spring', stiffness: 300, damping: 20 }}
          >
            <ChefHat className="w-8 h-8 text-white" />
          </motion.div>
          <h1 className="text-3xl font-bold text-secondary-900 dark:text-white mb-2">
            Welcome Back
          </h1>
          <p className="text-secondary-600 dark:text-secondary-400">
            Sign in to your NewRMS account
          </p>
        </div>

        {/* Login Form */}
        <Card variant="elevated" padding="lg">
          <form onSubmit={handleSubmit} className="space-y-6">
            {error && (
              <motion.div
                className="p-4 bg-error-50 border border-error-200 rounded-lg text-error-700 text-sm dark:bg-error-900/20 dark:border-error-800 dark:text-error-200"
                initial={{ opacity: 0, height: 0 }}
                animate={{ opacity: 1, height: 'auto' }}
                transition={{ duration: 0.3 }}
              >
                {error}
              </motion.div>
            )}

            <div className="space-y-4">
              <Input
                label="Username"
                name="username"
                type="text"
                value={formData.username}
                onChange={handleChange}
                leftIcon={<User size={18} />}
                placeholder="Enter your username"
                required
                disabled={isLoggingIn}
              />

              <Input
                label="Password"
                name="password"
                type="password"
                value={formData.password}
                onChange={handleChange}
                leftIcon={<Lock size={18} />}
                placeholder="Enter your password"
                required
                disabled={isLoggingIn}
              />
            </div>

            <div className="flex items-center justify-between">
              <label className="flex items-center">
                <input
                  type="checkbox"
                  className="w-4 h-4 text-primary-600 border-secondary-300 rounded focus:ring-primary-500 focus:ring-2"
                />
                <span className="ml-2 text-sm text-secondary-600 dark:text-secondary-400">
                  Remember me
                </span>
              </label>
              <Link
                to="/forgot-password"
                className="text-sm text-primary-600 hover:text-primary-700 dark:text-primary-400 dark:hover:text-primary-300 transition-colors duration-200"
              >
                Forgot password?
              </Link>
            </div>

            <Button
              type="submit"
              variant="primary"
              size="lg"
              fullWidth
              loading={isLoggingIn}
              disabled={!formData.username || !formData.password}
            >
              Sign In
            </Button>
          </form>

          <div className="mt-6 text-center">
            <p className="text-sm text-secondary-600 dark:text-secondary-400">
              Don't have an account?{' '}
              <Link
                to="/signup"
                className="font-medium text-primary-600 hover:text-primary-700 dark:text-primary-400 dark:hover:text-primary-300 transition-colors duration-200"
              >
                Sign up
              </Link>
            </p>
          </div>
        </Card>

        {/* Demo Credentials */}
        <motion.div
          className="mt-6 p-4 bg-secondary-100 dark:bg-secondary-800 rounded-lg"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.5, duration: 0.3 }}
        >
          <h3 className="text-sm font-medium text-secondary-900 dark:text-secondary-100 mb-2">
            Demo Credentials
          </h3>
          <div className="text-xs text-secondary-600 dark:text-secondary-400 space-y-1">
            <p><strong>Admin:</strong> admin / admin123</p>
            <p><strong>Manager:</strong> manager / manager123</p>
            <p><strong>Waiter:</strong> waiter / waiter123</p>
          </div>
        </motion.div>
      </motion.div>
    </div>
  );
};

export default Login;