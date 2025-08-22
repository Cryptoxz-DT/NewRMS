import React from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { X, CheckCircle, AlertCircle, AlertTriangle, Info } from 'lucide-react';
import { useUIStore } from '../../store/uiStore';
import { cn } from '../../utils';
import Button from './Button';

const NotificationToast: React.FC = () => {
  const { notifications, removeNotification } = useUIStore();

  const getIcon = (type: string) => {
    switch (type) {
      case 'success':
        return <CheckCircle className="w-5 h-5" />;
      case 'error':
        return <AlertCircle className="w-5 h-5" />;
      case 'warning':
        return <AlertTriangle className="w-5 h-5" />;
      case 'info':
        return <Info className="w-5 h-5" />;
      default:
        return <Info className="w-5 h-5" />;
    }
  };

  const getStyles = (type: string) => {
    switch (type) {
      case 'success':
        return 'bg-success-50 border-success-200 text-success-800 dark:bg-success-900/20 dark:border-success-800 dark:text-success-200';
      case 'error':
        return 'bg-error-50 border-error-200 text-error-800 dark:bg-error-900/20 dark:border-error-800 dark:text-error-200';
      case 'warning':
        return 'bg-warning-50 border-warning-200 text-warning-800 dark:bg-warning-900/20 dark:border-warning-800 dark:text-warning-200';
      case 'info':
        return 'bg-blue-50 border-blue-200 text-blue-800 dark:bg-blue-900/20 dark:border-blue-800 dark:text-blue-200';
      default:
        return 'bg-secondary-50 border-secondary-200 text-secondary-800 dark:bg-secondary-900/20 dark:border-secondary-800 dark:text-secondary-200';
    }
  };

  const getIconStyles = (type: string) => {
    switch (type) {
      case 'success':
        return 'text-success-600 dark:text-success-400';
      case 'error':
        return 'text-error-600 dark:text-error-400';
      case 'warning':
        return 'text-warning-600 dark:text-warning-400';
      case 'info':
        return 'text-blue-600 dark:text-blue-400';
      default:
        return 'text-secondary-600 dark:text-secondary-400';
    }
  };

  return (
    <div className="fixed top-4 right-4 z-50 space-y-2 max-w-sm w-full">
      <AnimatePresence>
        {notifications.map((notification) => (
          <motion.div
            key={notification.id}
            className={cn(
              'p-4 rounded-lg border shadow-medium backdrop-blur-sm',
              getStyles(notification.type)
            )}
            initial={{ opacity: 0, x: 300, scale: 0.8 }}
            animate={{ opacity: 1, x: 0, scale: 1 }}
            exit={{ opacity: 0, x: 300, scale: 0.8 }}
            transition={{ type: 'spring', stiffness: 300, damping: 30 }}
            layout
          >
            <div className="flex items-start gap-3">
              <div className={cn('flex-shrink-0', getIconStyles(notification.type))}>
                {getIcon(notification.type)}
              </div>
              <div className="flex-1 min-w-0">
                <h4 className="text-sm font-medium mb-1">
                  {notification.title}
                </h4>
                <p className="text-sm opacity-90">
                  {notification.message}
                </p>
              </div>
              <Button
                variant="ghost"
                size="sm"
                className="p-1 -mr-1 -mt-1 opacity-70 hover:opacity-100"
                onClick={() => removeNotification(notification.id)}
                icon={<X size={16} />}
              />
            </div>
          </motion.div>
        ))}
      </AnimatePresence>
    </div>
  );
};

export default NotificationToast;