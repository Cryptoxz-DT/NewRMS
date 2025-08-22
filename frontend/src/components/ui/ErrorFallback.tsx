import React from 'react';
import { motion } from 'framer-motion';
import { AlertTriangle, RefreshCw, Home } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import Button from './Button';
import Card from './Card';

interface ErrorFallbackProps {
  error: Error;
  resetErrorBoundary: () => void;
}

const ErrorFallback: React.FC<ErrorFallbackProps> = ({ error, resetErrorBoundary }) => {
  const navigate = useNavigate();

  const handleGoHome = () => {
    navigate('/dashboard');
    resetErrorBoundary();
  };

  return (
    <div className="min-h-screen bg-secondary-50 dark:bg-secondary-950 flex items-center justify-center p-4">
      <motion.div
        className="w-full max-w-md"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
      >
        <Card variant="elevated" padding="lg" className="text-center">
          <motion.div
            className="inline-flex items-center justify-center w-16 h-16 bg-error-100 rounded-full mb-6"
            animate={{ rotate: [0, 10, -10, 0] }}
            transition={{ duration: 2, repeat: Infinity, repeatDelay: 3 }}
          >
            <AlertTriangle className="w-8 h-8 text-error-600" />
          </motion.div>

          <h1 className="text-2xl font-bold text-secondary-900 dark:text-white mb-2">
            Oops! Something went wrong
          </h1>
          
          <p className="text-secondary-600 dark:text-secondary-400 mb-6">
            We encountered an unexpected error. Don't worry, our team has been notified.
          </p>

          {process.env.NODE_ENV === 'development' && (
            <details className="mb-6 text-left">
              <summary className="cursor-pointer text-sm font-medium text-secondary-700 dark:text-secondary-300 mb-2">
                Error Details (Development)
              </summary>
              <pre className="text-xs bg-secondary-100 dark:bg-secondary-800 p-3 rounded-lg overflow-auto text-error-600 dark:text-error-400">
                {error.message}
                {error.stack && '\n\n' + error.stack}
              </pre>
            </details>
          )}

          <div className="flex flex-col sm:flex-row gap-3">
            <Button
              variant="primary"
              onClick={resetErrorBoundary}
              icon={<RefreshCw size={18} />}
              fullWidth
            >
              Try Again
            </Button>
            <Button
              variant="outline"
              onClick={handleGoHome}
              icon={<Home size={18} />}
              fullWidth
            >
              Go Home
            </Button>
          </div>
        </Card>
      </motion.div>
    </div>
  );
};

export default ErrorFallback;