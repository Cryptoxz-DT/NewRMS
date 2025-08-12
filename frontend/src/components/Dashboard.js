import React, { useState, useEffect } from 'react';
import axios from 'axios';
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

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Dashboard</h1>
      </div>

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon blue">
            <ShoppingCart />
          </div>
          <div className="stat-content">
            <h3>{stats.totalOrders}</h3>
            <p>Total Orders</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon green">
            <Users />
          </div>
          <div className="stat-content">
            <h3>{stats.totalCustomers}</h3>
            <p>Total Customers</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon yellow">
            <UtensilsCrossed />
          </div>
          <div className="stat-content">
            <h3>{stats.totalDishes}</h3>
            <p>Menu Items</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon purple">
            <DollarSign />
          </div>
          <div className="stat-content">
            <h3>${stats.totalRevenue.toLocaleString()}</h3>
            <p>Total Revenue</p>
          </div>
        </div>
      </div>

      <div className="card">
        <div className="page-header">
          <h2 style={{ margin: 0, fontSize: '1.25rem' }}>Recent Orders</h2>
        </div>
        
        {recentOrders.length > 0 ? (
          <table className="table">
            <thead>
              <tr>
                <th>Order ID</th>
                <th>Customer</th>
                <th>Date</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {recentOrders.map((order) => (
                <tr key={order.id}>
                  <td>#{order.id}</td>
                  <td>{order.customer?.name || 'Walk-in'}</td>
                  <td>{new Date(order.orderTime).toLocaleDateString()}</td>
                  <td>
                    <span className="badge badge-success">
                      {order.status?.name || 'Pending'}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <div className="empty-state">
            <div className="empty-state-icon">
              <Clock />
            </div>
            <p>No recent orders found</p>
          </div>
        )}
      </div>

      <div className="card">
        <h2 style={{ margin: '0 0 1rem 0', fontSize: '1.25rem' }}>Quick Actions</h2>
        <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
          <button className="btn btn-primary">
            <ShoppingCart size={18} />
            New Order
          </button>
          <button className="btn btn-secondary">
            <Users size={18} />
            Add Customer
          </button>
          <button className="btn btn-secondary">
            <UtensilsCrossed size={18} />
            Manage Menu
          </button>
          <button className="btn btn-secondary">
            <TrendingUp size={18} />
            View Reports
          </button>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;