import React from 'react';
import { motion } from 'framer-motion';
import { cn } from '@/utils';

interface CardProps {
  children: React.ReactNode;
  className?: string;
  variant?: 'default' | 'elevated' | 'outlined' | 'glass';
  padding?: 'none' | 'sm' | 'md' | 'lg' | 'xl';
  hover?: boolean;
  clickable?: boolean;
  onClick?: () => void;
}

const Card: React.FC<CardProps> = ({
  children,
  className,
  variant = 'default',
  padding = 'md',
  hover = false,
  clickable = false,
  onClick,
}) => {
  const baseClasses = [
    'rounded-xl transition-all duration-200 ease-in-out',
  ];

  const variants = {
    default: [
      'bg-white border border-secondary-200',
      'dark:bg-secondary-800 dark:border-secondary-700',
    ],
    elevated: [
      'bg-white shadow-soft border border-secondary-100',
      'dark:bg-secondary-800 dark:border-secondary-700 dark:shadow-none',
    ],
    outlined: [
      'bg-transparent border-2 border-secondary-300',
      'dark:border-secondary-600',
    ],
    glass: [
      'bg-white/80 backdrop-blur-sm border border-white/20',
      'dark:bg-secondary-800/80 dark:border-secondary-700/50',
    ],
  };

  const paddings = {
    none: '',
    sm: 'p-3',
    md: 'p-4',
    lg: 'p-6',
    xl: 'p-8',
  };

  const hoverEffects = hover && [
    'hover:shadow-medium hover:-translate-y-1',
    'dark:hover:shadow-none dark:hover:bg-secondary-700',
  ];

  const clickableEffects = clickable && [
    'cursor-pointer active:scale-98',
    hover && 'hover:shadow-medium hover:-translate-y-1',
  ];

  const classes = cn(
    baseClasses,
    variants[variant],
    paddings[padding],
    hoverEffects,
    clickableEffects,
    className
  );

  const MotionCard = motion.div;

  return (
    <MotionCard
      className={classes}
      onClick={onClick}
      whileHover={hover || clickable ? { y: -2, scale: 1.01 } : undefined}
      whileTap={clickable ? { scale: 0.98 } : undefined}
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.3 }}
    >
      {children}
    </MotionCard>
  );
};

interface CardHeaderProps {
  children: React.ReactNode;
  className?: string;
}

const CardHeader: React.FC<CardHeaderProps> = ({ children, className }) => {
  return (
    <div className={cn('mb-4', className)}>
      {children}
    </div>
  );
};

interface CardTitleProps {
  children: React.ReactNode;
  className?: string;
  as?: 'h1' | 'h2' | 'h3' | 'h4' | 'h5' | 'h6';
}

const CardTitle: React.FC<CardTitleProps> = ({ 
  children, 
  className, 
  as: Component = 'h3' 
}) => {
  return (
    <Component className={cn(
      'text-lg font-semibold text-secondary-900 dark:text-secondary-100',
      className
    )}>
      {children}
    </Component>
  );
};

interface CardDescriptionProps {
  children: React.ReactNode;
  className?: string;
}

const CardDescription: React.FC<CardDescriptionProps> = ({ children, className }) => {
  return (
    <p className={cn(
      'text-sm text-secondary-600 dark:text-secondary-400 mt-1',
      className
    )}>
      {children}
    </p>
  );
};

interface CardContentProps {
  children: React.ReactNode;
  className?: string;
}

const CardContent: React.FC<CardContentProps> = ({ children, className }) => {
  return (
    <div className={cn('', className)}>
      {children}
    </div>
  );
};

interface CardFooterProps {
  children: React.ReactNode;
  className?: string;
}

const CardFooter: React.FC<CardFooterProps> = ({ children, className }) => {
  return (
    <div className={cn('mt-4 pt-4 border-t border-secondary-200 dark:border-secondary-700', className)}>
      {children}
    </div>
  );
};

// Export compound component
Card.Header = CardHeader;
Card.Title = CardTitle;
Card.Description = CardDescription;
Card.Content = CardContent;
Card.Footer = CardFooter;

export default Card;