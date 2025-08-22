import React from 'react';
import { motion } from 'framer-motion';
import { Outlet } from 'react-router-dom';
import { useUIStore } from '@/store/uiStore';
import { cn } from '@/utils';
import Sidebar from './Sidebar';
import Header from './Header';
import NotificationToast from '@/components/ui/NotificationToast';

const Layout: React.FC = () => {
  const { sidebarOpen, theme } = useUIStore();

  React.useEffect(() => {
    // Apply theme to document
    if (theme.mode === 'dark') {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }, [theme.mode]);

  return (
    <div className={cn(
      'min-h-screen bg-secondary-50 dark:bg-secondary-950',
      'transition-colors duration-200'
    )}>
      <div className="flex h-screen overflow-hidden">
        {/* Sidebar */}
        <Sidebar />

        {/* Main content */}
        <div className="flex-1 flex flex-col overflow-hidden">
          {/* Header */}
          <Header />

          {/* Page content */}
          <main className="flex-1 overflow-auto">
            <motion.div
              className="p-4 lg:p-6"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.3 }}
            >
              <Outlet />
            </motion.div>
          </main>
        </div>
      </div>

      {/* Notifications */}
      <NotificationToast />
    </div>
  );
};

export default Layout;