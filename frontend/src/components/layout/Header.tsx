import React from 'react';
import { motion } from 'framer-motion';
import { Bell, Search, Sun, Moon, Menu } from 'lucide-react';
import { useUIStore } from '@/store/uiStore';
import { useAuth } from '@/hooks/useAuth';
import { cn } from '@/utils';
import Button from '@/components/ui/Button';
import Input from '@/components/ui/Input';

const Header: React.FC = () => {
  const { theme, toggleTheme, setSidebarOpen, notifications } = useUIStore();
  const { user } = useAuth();
  const [searchQuery, setSearchQuery] = React.useState('');

  const unreadNotifications = notifications.filter(n => !n.read).length;

  return (
    <motion.header
      className={cn(
        'sticky top-0 z-30 bg-white/80 backdrop-blur-lg border-b border-secondary-200',
        'dark:bg-secondary-900/80 dark:border-secondary-700'
      )}
      initial={{ y: -100 }}
      animate={{ y: 0 }}
      transition={{ type: 'spring', stiffness: 300, damping: 30 }}
    >
      <div className="flex items-center justify-between px-4 py-3 lg:px-6">
        {/* Left section */}
        <div className="flex items-center gap-4">
          {/* Mobile menu button */}
          <Button
            variant="ghost"
            size="sm"
            className="lg:hidden"
            onClick={() => setSidebarOpen(true)}
            icon={<Menu size={18} />}
          />

          {/* Search */}
          <div className="hidden sm:block w-80">
            <Input
              placeholder="Search orders, customers, menu items..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              leftIcon={<Search size={18} />}
              variant="filled"
              inputSize="sm"
            />
          </div>
        </div>

        {/* Right section */}
        <div className="flex items-center gap-2">
          {/* Search button for mobile */}
          <Button
            variant="ghost"
            size="sm"
            className="sm:hidden"
            icon={<Search size={18} />}
          />

          {/* Theme toggle */}
          <Button
            variant="ghost"
            size="sm"
            onClick={toggleTheme}
            icon={theme.mode === 'light' ? <Moon size={18} /> : <Sun size={18} />}
          />

          {/* Notifications */}
          <div className="relative">
            <Button
              variant="ghost"
              size="sm"
              icon={<Bell size={18} />}
            />
            {unreadNotifications > 0 && (
              <motion.div
                className="absolute -top-1 -right-1 w-5 h-5 bg-error-500 text-white text-xs rounded-full flex items-center justify-center"
                initial={{ scale: 0 }}
                animate={{ scale: 1 }}
                transition={{ type: 'spring', stiffness: 300, damping: 20 }}
              >
                {unreadNotifications > 9 ? '9+' : unreadNotifications}
              </motion.div>
            )}
          </div>

          {/* User menu */}
          <div className="flex items-center gap-3 ml-2">
            <div className="hidden sm:block text-right">
              <p className="text-sm font-medium text-secondary-900 dark:text-white">
                {user?.firstName || user?.username}
              </p>
              <p className="text-xs text-secondary-500 dark:text-secondary-400">
                {user?.role}
              </p>
            </div>
            <motion.div
              className="w-8 h-8 bg-primary-100 rounded-full flex items-center justify-center cursor-pointer"
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
            >
              <span className="text-sm font-medium text-primary-700">
                {user?.firstName?.charAt(0) || user?.username?.charAt(0) || 'U'}
              </span>
            </motion.div>
          </div>
        </div>
      </div>
    </motion.header>
  );
};

export default Header;