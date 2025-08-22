import React, { forwardRef } from 'react';
import { motion } from 'framer-motion';
import { Eye, EyeOff, AlertCircle } from 'lucide-react';
import { cn } from '@/utils';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
  helperText?: string;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
  variant?: 'default' | 'filled' | 'outline';
  inputSize?: 'sm' | 'md' | 'lg';
}

const Input = forwardRef<HTMLInputElement, InputProps>(
  (
    {
      label,
      error,
      helperText,
      leftIcon,
      rightIcon,
      variant = 'default',
      inputSize = 'md',
      type = 'text',
      className,
      disabled,
      ...props
    },
    ref
  ) => {
    const [showPassword, setShowPassword] = React.useState(false);
    const [isFocused, setIsFocused] = React.useState(false);

    const inputType = type === 'password' && showPassword ? 'text' : type;

    const baseClasses = [
      'w-full transition-all duration-200 ease-in-out',
      'focus:outline-none focus:ring-2 focus:ring-offset-1',
      'disabled:opacity-50 disabled:cursor-not-allowed',
    ];

    const variants = {
      default: [
        'border border-secondary-300 rounded-lg bg-white',
        'focus:border-primary-500 focus:ring-primary-500/20',
        'dark:border-secondary-600 dark:bg-secondary-800',
        'dark:focus:border-primary-400 dark:focus:ring-primary-400/20',
        error && 'border-error-500 focus:border-error-500 focus:ring-error-500/20',
      ],
      filled: [
        'border-0 rounded-lg bg-secondary-100',
        'focus:bg-white focus:ring-primary-500/20',
        'dark:bg-secondary-700 dark:focus:bg-secondary-600',
        error && 'bg-error-50 focus:bg-error-50 focus:ring-error-500/20',
      ],
      outline: [
        'border-2 border-secondary-300 rounded-lg bg-transparent',
        'focus:border-primary-500 focus:ring-primary-500/20',
        'dark:border-secondary-600',
        'dark:focus:border-primary-400 dark:focus:ring-primary-400/20',
        error && 'border-error-500 focus:border-error-500 focus:ring-error-500/20',
      ],
    };

    const sizes = {
      sm: 'px-3 py-2 text-sm',
      md: 'px-4 py-3 text-base',
      lg: 'px-5 py-4 text-lg',
    };

    const inputClasses = cn(
      baseClasses,
      variants[variant],
      sizes[inputSize],
      leftIcon && 'pl-10',
      (rightIcon || type === 'password') && 'pr-10',
      className
    );

    const containerClasses = cn(
      'relative',
      disabled && 'opacity-50 cursor-not-allowed'
    );

    return (
      <div className="space-y-1">
        {label && (
          <motion.label
            className={cn(
              'block text-sm font-medium transition-colors duration-200',
              error ? 'text-error-700' : 'text-secondary-700 dark:text-secondary-300',
              isFocused && !error && 'text-primary-600 dark:text-primary-400'
            )}
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.2 }}
          >
            {label}
          </motion.label>
        )}

        <div className={containerClasses}>
          {leftIcon && (
            <div className="absolute left-3 top-1/2 transform -translate-y-1/2 text-secondary-400">
              {leftIcon}
            </div>
          )}

          <motion.input
            ref={ref}
            type={inputType}
            className={inputClasses}
            disabled={disabled}
            onFocus={() => setIsFocused(true)}
            onBlur={() => setIsFocused(false)}
            whileFocus={{ scale: 1.01 }}
            transition={{ duration: 0.1 }}
            {...props}
          />

          {type === 'password' && (
            <button
              type="button"
              className="absolute right-3 top-1/2 transform -translate-y-1/2 text-secondary-400 hover:text-secondary-600 transition-colors duration-200"
              onClick={() => setShowPassword(!showPassword)}
              tabIndex={-1}
            >
              {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
            </button>
          )}

          {rightIcon && type !== 'password' && (
            <div className="absolute right-3 top-1/2 transform -translate-y-1/2 text-secondary-400">
              {rightIcon}
            </div>
          )}

          {error && (
            <div className="absolute right-3 top-1/2 transform -translate-y-1/2 text-error-500">
              <AlertCircle size={18} />
            </div>
          )}
        </div>

        {(error || helperText) && (
          <motion.div
            className={cn(
              'text-sm',
              error ? 'text-error-600' : 'text-secondary-500'
            )}
            initial={{ opacity: 0, height: 0 }}
            animate={{ opacity: 1, height: 'auto' }}
            exit={{ opacity: 0, height: 0 }}
            transition={{ duration: 0.2 }}
          >
            {error || helperText}
          </motion.div>
        )}
      </div>
    );
  }
);

Input.displayName = 'Input';

export default Input;