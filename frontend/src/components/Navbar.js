import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { 
  LayoutDashboard, 
  ShoppingCart, 
  Users, 
  UtensilsCrossed, 
  UserCheck, 
  Table,
  LogOut 
} from 'lucide-react';
import './Navbar.css';

const Navbar = () => {
  const { logout, user } = useAuth();
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
    <nav className="navbar">
      <div className="navbar-header">
        <h2 className="navbar-title">NewRMS</h2>
        <p className="navbar-subtitle">Restaurant Management</p>
      </div>

      <ul className="navbar-menu">
        {menuItems.map((item) => {
          const Icon = item.icon;
          const isActive = location.pathname === item.path;
          
          return (
            <li key={item.path}>
              <Link 
                to={item.path} 
                className={`navbar-link ${isActive ? 'active' : ''}`}
              >
                <Icon size={20} />
                <span>{item.label}</span>
              </Link>
            </li>
          );
        })}
      </ul>

      <div className="navbar-footer">
        {user && (
          <div className="user-info">
            <div className="user-avatar">
              {user.username?.charAt(0).toUpperCase() || 'U'}
            </div>
            <div className="user-details">
              <p className="user-name">{user.username}</p>
              <p className="user-role">Administrator</p>
            </div>
          </div>
        )}
        
        <button onClick={handleLogout} className="logout-btn">
          <LogOut size={18} />
          <span>Logout</span>
        </button>
      </div>
    </nav>
  );
};

export default Navbar;