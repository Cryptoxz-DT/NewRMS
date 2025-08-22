import React from 'react';
import { motion } from 'framer-motion';
import { Loader2 } from 'lucide-react';
import { cn } from '../../utils';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'outline' | 'ghost' | 'danger' | 'success';
  size?: 'sm' | 'md' | 'lg' | 'xl';
  loading?: boolean;
  icon?: React.ReactNode;
  iconPosition?: 'left' | 'right';
  fullWidth?: boolean;
  children?: React.ReactNode;
}

const Button: React.FC<ButtonProps> = ({
  variant = 'primary',
  size = 'md',
  loading = false,
  icon,
  iconPosition = 'left',
  fullWidth = false,
  className,
  disabled,
  children,
  ...props
}) => {
  const baseClasses = [
    'inline-flex items-center justify-center font-medium rounded-lg',
    'transition-all duration-200 ease-in-out',
    'focus:outline-none focus:ring-2 focus:ring-offset-2',
    'disabled:opacity-50 disabled:cursor-not-allowed',
    'active:scale-95',
  ];

  const variants = {
    primary: [
      'bg-primary-600 hover:bg-primary-700 text-white',
      'focus:ring-primary-500 shadow-sm hover:shadow-md',
    ],
    secondary: [
      'bg-secondary-100 hover:bg-secondary-200 text-secondary-900',
      'focus:ring-secondary-500 shadow-sm hover:shadow-md',
      'dark:bg-secondary-800 dark:hover:bg-secondary-700 dark:text-secondary-100',
    ],
    outline: [
      'border-2 border-primary-600 text-primary-600 hover:bg-primary-50',
      'focus:ring-primary-500 hover:border-primary-700 hover:text-primary-700',
      'dark:border-primary-400 dark:text-primary-400 dark:hover:bg-primary-900/20',
    ],
    ghost: [
      'text-secondary-700 hover:bg-secondary-100 hover:text-secondary-900',
      'focus:ring-secondary-500',
      'dark:text-secondary-300 dark:hover:bg-secondary-800 dark:hover:text-secondary-100',
    ],
    danger: [
      'bg-error-600 hover:bg-error-700 text-white',
      'focus:ring-error-500 shadow-sm hover:shadow-md',
    ],
    success: [
      'bg-success-600 hover:bg-success-700 text-white',
      'focus:ring-success-500 shadow-sm hover:shadow-md',
    ],
  };

  const sizes = {
    sm: 'px-3 py-1.5 text-sm gap-1.5',
    md: 'px-4 py-2 text-sm gap-2',
    lg: 'px-6 py-3 text-base gap-2',
    xl: 'px-8 py-4 text-lg gap-3',
  };

  const classes = cn(
    baseClasses,
    variants[variant],
    sizes[size],
    fullWidth && 'w-full',
    className
  );

  const iconElement = loading ? (
    <Loader2 className="animate-spin" size={size === 'sm' ? 14 : size === 'lg' ? 20 : size === 'xl' ? 24 : 16} />
  ) : icon ? (
    <span className="flex-shrink-0">{icon}</span>
  ) : null;

  return (
    <motion.button
      className={classes}
      disabled={disabled || loading}
      whileHover={{ scale: disabled || loading ? 1 : 1.02 }}
      whileTap={{ scale: disabled || loading ? 1 : 0.98 }}
      {...props}
    >
      {iconElement && iconPosition === 'left' && iconElement}
      {children && <span className={loading ? 'opacity-0' : ''}>{children}</span>}
      {iconElement && iconPosition === 'right' && iconElement}
      {loading && (
        <div className="absolute inset-0 flex items-center justify-center">
          <Loader2 className="animate-spin" size={size === 'sm' ? 14 : size === 'lg' ? 20 : size === 'xl' ? 24 : 16} />
        </div>
      )}
    </motion.button>
  );
};

export default Button;