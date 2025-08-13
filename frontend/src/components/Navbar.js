import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useTheme } from '../context/ThemeContext';
import { motion } from 'framer-motion';
import { 
  LayoutDashboard, 
  ShoppingCart, 
  Users, 
  UtensilsCrossed, 
  UserCheck, 
  Table,
  LogOut,
  Sun,
  Moon
} from 'lucide-react';
import './Navbar.css';

const Navbar = () => {
  const { logout, user } = useAuth();
  const { isDarkMode, toggleTheme } = useTheme();
  const location = useLocation();

  const menuItems = [
    { path: '/dashboard', icon: LayoutDashboard, label: 'Dashboard' },
    { path: '/orders', icon: ShoppingCart, label: 'Orders' },
    { path: '/customers', icon: Users, label: 'Customers' },
    { path: '/menu', icon: UtensilsCrossed, label: 'Menu' },
    { path: '/staff', icon: UserCheck, label: 'Staff' },
    { path: '/tables', icon: Table, label: 'Tables' },
  ];

  const handleLogout = () => {
    logout();
  };

  return (
    <motion.nav 
      className="navbar"
      initial={{ x: -250 }}
      animate={{ x: 0 }}
      transition={{ duration: 0.3, ease: "easeOut" }}
    >
      <div className="navbar-header">
        <motion.h2 
          className="navbar-title"
          whileHover={{ scale: 1.05 }}
          transition={{ duration: 0.2 }}
        >
          NewRMS
        </motion.h2>
        <p className="navbar-subtitle">Restaurant Management</p>
      </div>

      <ul className="navbar-menu">
        {menuItems.map((item, index) => {
          const Icon = item.icon;
          const isActive = location.pathname === item.path;
          
          return (
            <motion.li 
              key={item.path}
              initial={{ opacity: 0, x: -20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: index * 0.1, duration: 0.3 }}
            >
              <Link 
                to={item.path} 
                className={`navbar-link ${isActive ? 'active' : ''}`}
              >
                <motion.div
                  whileHover={{ scale: 1.1, rotate: 5 }}
                  whileTap={{ scale: 0.95 }}
                  transition={{ duration: 0.2 }}
                >
                  <Icon size={20} />
                </motion.div>
                <span>{item.label}</span>
              </Link>
            </motion.li>
          );
        })}
      </ul>

      <div className="navbar-footer">
        <motion.button
          onClick={toggleTheme}
          className="theme-toggle"
          whileHover={{ scale: 1.1 }}
          whileTap={{ scale: 0.9 }}
          transition={{ duration: 0.2 }}
        >
          <motion.div
            animate={{ rotate: isDarkMode ? 180 : 0 }}
            transition={{ duration: 0.3 }}
          >
            {isDarkMode ? <Sun size={18} /> : <Moon size={18} />}
          </motion.div>
          <span>{isDarkMode ? 'Light' : 'Dark'}</span>
        </motion.button>

        {user && (
          <motion.div 
            className="user-info"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.5 }}
          >
            <motion.div 
              className="user-avatar"
              whileHover={{ scale: 1.1 }}
              transition={{ duration: 0.2 }}
            >
              {user.username?.charAt(0).toUpperCase() || 'U'}
            </motion.div>
            <div className="user-details">
              <p className="user-name">{user.username}</p>
              <p className="user-role">Administrator</p>
            </div>
          </motion.div>
        )}
        
        <motion.button 
          onClick={handleLogout} 
          className="logout-btn"
          whileHover={{ scale: 1.05, backgroundColor: '#fee2e2' }}
          whileTap={{ scale: 0.95 }}
          transition={{ duration: 0.2 }}
        >
          <motion.div
            whileHover={{ rotate: 15 }}
            transition={{ duration: 0.2 }}
          >
            <LogOut size={18} />
          </motion.div>
          <span>Logout</span>
        </motion.button>
      </div>
    </motion.nav>
  );
};

export default Navbar;