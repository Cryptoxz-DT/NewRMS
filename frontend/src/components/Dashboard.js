import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { motion } from 'framer-motion';
import { 
  ShoppingCart, 
  Users, 
  UtensilsCrossed, 
  DollarSign,
  TrendingUp,
  Clock
} from 'lucide-react';

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalOrders: 0,
    totalCustomers: 0,
    totalDishes: 0,
    totalRevenue: 0
  });
  const [recentOrders, setRecentOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      
      // Fetch basic stats
      const [ordersRes, customersRes] = await Promise.all([
        axios.get('/api/orders'),
        axios.get('/api/customers')
      ]);

      setStats({
        totalOrders: ordersRes.data.length,
        totalCustomers: customersRes.data.length,
        totalDishes: 25, // Placeholder
        totalRevenue: 15420 // Placeholder
      });

      // Get recent orders (last 5)
      setRecentOrders(ordersRes.data.slice(-5).reverse());
      
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Loading dashboard...</div>;
  }

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1
      }
    }
  };

  const itemVariants = {
    hidden: { y: 20, opacity: 0 },
    visible: {
      y: 0,
      opacity: 1,
      transition: {
        duration: 0.5,
        ease: "easeOut"
      }
    }
  };

  const statsData = [
    { icon: ShoppingCart, value: stats.totalOrders, label: 'Total Orders', color: 'blue' },
    { icon: Users, value: stats.totalCustomers, label: 'Total Customers', color: 'green' },
    { icon: UtensilsCrossed, value: stats.totalDishes, label: 'Menu Items', color: 'yellow' },
    { icon: DollarSign, value: `$${stats.totalRevenue.toLocaleString()}`, label: 'Total Revenue', color: 'purple' }
  ];

  return (
    <motion.div
      variants={containerVariants}
      initial="hidden"
      animate="visible"
    >
      <motion.div className="page-header" variants={itemVariants}>
        <h1 className="page-title">Dashboard</h1>
      </motion.div>

      <motion.div className="stats-grid" variants={containerVariants}>
        {statsData.map((stat, index) => {
          const Icon = stat.icon;
          return (
            <motion.div
              key={index}
              className="stat-card"
              variants={itemVariants}
              whileHover={{ 
                scale: 1.02,
                transition: { duration: 0.2 }
              }}
              whileTap={{ scale: 0.98 }}
            >
              <motion.div 
                className={`stat-icon ${stat.color}`}
                whileHover={{ 
                  rotate: [0, -10, 10, -10, 0],
                  transition: { duration: 0.5 }
                }}
              >
                <Icon />
              </motion.div>
              <div className="stat-content">
                <motion.h3
                  initial={{ scale: 0 }}
                  animate={{ scale: 1 }}
                  transition={{ delay: 0.5 + index * 0.1, type: "spring", stiffness: 200 }}
                >
                  {stat.value}
                </motion.h3>
                <p>{stat.label}</p>
              </div>
            </motion.div>
          );
        })}
      </motion.div>

      <motion.div 
        className="card"
        variants={itemVariants}
        whileHover={{ y: -4 }}
      >
        <div className="page-header">
          <h2 style={{ margin: 0, fontSize: '1.25rem' }}>Recent Orders</h2>
        </div>
        
        {recentOrders.length > 0 ? (
          <motion.table 
            className="table"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.3 }}
          >
            <thead>
              <tr>
                <th>Order ID</th>
                <th>Customer</th>
                <th>Date</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {recentOrders.map((order, index) => (
                <motion.tr 
                  key={order.id}
                  initial={{ opacity: 0, x: -20 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ delay: 0.4 + index * 0.1 }}
                  whileHover={{ 
                    backgroundColor: 'rgba(59, 130, 246, 0.05)',
                    transition: { duration: 0.2 }
                  }}
                >
                  <td>#{order.id}</td>
                  <td>{order.customer?.name || 'Walk-in'}</td>
                  <td>{new Date(order.orderTime).toLocaleDateString()}</td>
                  <td>
                    <motion.span 
                      className="badge badge-success"
                      whileHover={{ scale: 1.05 }}
                      whileTap={{ scale: 0.95 }}
                    >
                      {order.status?.name || 'Pending'}
                    </motion.span>
                  </td>
                </motion.tr>
              ))}
            </tbody>
          </motion.table>
        ) : (
          <motion.div 
            className="empty-state"
            initial={{ opacity: 0, scale: 0.8 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ delay: 0.3 }}
          >
            <motion.div 
              className="empty-state-icon"
              animate={{ 
                rotate: [0, 10, -10, 0],
                transition: { duration: 2, repeat: Infinity, repeatDelay: 3 }
              }}
            >
              <Clock />
            </motion.div>
            <p>No recent orders found</p>
          </motion.div>
        )}
      </motion.div>

      <motion.div 
        className="card"
        variants={itemVariants}
        whileHover={{ y: -4 }}
      >
        <h2 style={{ margin: '0 0 1rem 0', fontSize: '1.25rem' }}>Quick Actions</h2>
        <motion.div 
          style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}
          variants={containerVariants}
        >
          {[
            { icon: ShoppingCart, label: 'New Order', primary: true },
            { icon: Users, label: 'Add Customer' },
            { icon: UtensilsCrossed, label: 'Manage Menu' },
            { icon: TrendingUp, label: 'View Reports' }
          ].map((action, index) => {
            const Icon = action.icon;
            return (
              <motion.button 
                key={index}
                className={`btn ${action.primary ? 'btn-primary' : 'btn-secondary'}`}
                variants={itemVariants}
                whileHover={{ 
                  scale: 1.05,
                  transition: { duration: 0.2 }
                }}
                whileTap={{ scale: 0.95 }}
              >
                <motion.div
                  whileHover={{ rotate: 15 }}
                  transition={{ duration: 0.2 }}
                >
                  <Icon size={18} />
                </motion.div>
                {action.label}
              </motion.button>
            );
          })}
        </motion.div>
      </motion.div>
    </motion.div>
  );
};

export default Dashboard;