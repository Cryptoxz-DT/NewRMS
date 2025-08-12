import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Plus, Eye, Trash2, Search } from 'lucide-react';

const Orders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      setLoading(true);
      const response = await axios.get('/api/orders');
      setOrders(response.data);
    } catch (error) {
      console.error('Error fetching orders:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this order?')) {
      try {
        await axios.delete(`/api/orders/${id}`);
        setOrders(orders.filter(order => order.id !== id));
      } catch (error) {
        console.error('Error deleting order:', error);
        alert('Error deleting order');
      }
    }
  };

  const filteredOrders = orders.filter(order =>
    order.id.toString().includes(searchTerm) ||
    order.customer?.name?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) {
    return <div className="loading">Loading orders...</div>;
  }

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Orders</h1>
        <button className="btn btn-primary">
          <Plus size={18} />
          New Order
        </button>
      </div>

      <div className="card">
        <div style={{ marginBottom: '1.5rem' }}>
          <div style={{ position: 'relative', maxWidth: '300px' }}>
            <Search size={18} style={{ 
              position: 'absolute', 
              left: '0.75rem', 
              top: '50%', 
              transform: 'translateY(-50%)',
              color: '#64748b'
            }} />
            <input
              type="text"
              placeholder="Search orders..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="form-input"
              style={{ paddingLeft: '2.5rem' }}
            />
          </div>
        </div>

        {filteredOrders.length > 0 ? (
          <table className="table">
            <thead>
              <tr>
                <th>Order ID</th>
                <th>Customer</th>
                <th>Table</th>
                <th>Staff</th>
                <th>Date</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredOrders.map((order) => (
                <tr key={order.id}>
                  <td>#{order.id}</td>
                  <td>{order.customer?.name || 'Walk-in'}</td>
                  <td>Table {order.tableInfo?.tableNumber || 'N/A'}</td>
                  <td>{order.staff?.name || 'N/A'}</td>
                  <td>{new Date(order.orderTime).toLocaleDateString()}</td>
                  <td>
                    <span className="badge badge-success">
                      {order.status?.name || 'Pending'}
                    </span>
                  </td>
                  <td>
                    <div className="actions">
                      <button className="btn btn-secondary" style={{ padding: '0.5rem' }}>
                        <Eye size={16} />
                      </button>
                      <button 
                        className="btn btn-danger" 
                        style={{ padding: '0.5rem' }}
                        onClick={() => handleDelete(order.id)}
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <div className="empty-state">
            <div className="empty-state-icon">📋</div>
            <p>No orders found</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Orders;