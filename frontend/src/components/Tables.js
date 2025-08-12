import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Plus, Edit, Trash2, Search } from 'lucide-react';

const Tables = () => {
  const [tables, setTables] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchTables();
  }, []);

  const fetchTables = async () => {
    try {
      setLoading(true);
      const response = await axios.get('/api/tables');
      setTables(response.data);
    } catch (error) {
      console.error('Error fetching tables:', error);
      // Mock data for demo
      setTables([
        { id: 1, tableNumber: 1, capacity: 4, status: 'Available' },
        { id: 2, tableNumber: 2, capacity: 2, status: 'Occupied' },
        { id: 3, tableNumber: 3, capacity: 6, status: 'Available' },
        { id: 4, tableNumber: 4, capacity: 4, status: 'Reserved' },
        { id: 5, tableNumber: 5, capacity: 8, status: 'Available' },
      ]);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this table?')) {
      try {
        await axios.delete(`/api/tables/${id}`);
        setTables(tables.filter(table => table.id !== id));
      } catch (error) {
        console.error('Error deleting table:', error);
        alert('Error deleting table');
      }
    }
  };

  const getStatusBadge = (status) => {
    const statusClasses = {
      'Available': 'badge-success',
      'Occupied': 'badge-danger',
      'Reserved': 'badge-warning'
    };
    
    return (
      <span className={`badge ${statusClasses[status] || 'badge-secondary'}`}>
        {status}
      </span>
    );
  };

  const filteredTables = tables.filter(table =>
    table.tableNumber.toString().includes(searchTerm) ||
    table.status.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) {
    return <div className="loading">Loading tables...</div>;
  }

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Table Management</h1>
        <button className="btn btn-primary">
          <Plus size={18} />
          Add Table
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
              placeholder="Search tables..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="form-input"
              style={{ paddingLeft: '2.5rem' }}
            />
          </div>
        </div>

        {filteredTables.length > 0 ? (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))', gap: '1rem' }}>
            {filteredTables.map((table) => (
              <div key={table.id} className="card" style={{ margin: 0, padding: '1.5rem' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '1rem' }}>
                  <div>
                    <h3 style={{ margin: '0 0 0.5rem 0', fontSize: '1.25rem' }}>
                      Table {table.tableNumber}
                    </h3>
                    <p style={{ margin: '0 0 0.5rem 0', color: '#64748b' }}>
                      Capacity: {table.capacity} people
                    </p>
                    {getStatusBadge(table.status)}
                  </div>
                  <div className="actions">
                    <button className="btn btn-secondary" style={{ padding: '0.5rem' }}>
                      <Edit size={16} />
                    </button>
                    <button 
                      className="btn btn-danger" 
                      style={{ padding: '0.5rem' }}
                      onClick={() => handleDelete(table.id)}
                    >
                      <Trash2 size={16} />
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className="empty-state">
            <div className="empty-state-icon">🪑</div>
            <p>No tables found</p>
          </div>
        )}
      </div>

      <div className="card">
        <h2 style={{ margin: '0 0 1rem 0', fontSize: '1.25rem' }}>Table Status Overview</h2>
        <div style={{ display: 'flex', gap: '2rem', flexWrap: 'wrap' }}>
          <div style={{ textAlign: 'center' }}>
            <div style={{ fontSize: '2rem', fontWeight: '700', color: '#16a34a' }}>
              {tables.filter(t => t.status === 'Available').length}
            </div>
            <div style={{ fontSize: '0.875rem', color: '#64748b' }}>Available</div>
          </div>
          <div style={{ textAlign: 'center' }}>
            <div style={{ fontSize: '2rem', fontWeight: '700', color: '#dc2626' }}>
              {tables.filter(t => t.status === 'Occupied').length}
            </div>
            <div style={{ fontSize: '0.875rem', color: '#64748b' }}>Occupied</div>
          </div>
          <div style={{ textAlign: 'center' }}>
            <div style={{ fontSize: '2rem', fontWeight: '700', color: '#d97706' }}>
              {tables.filter(t => t.status === 'Reserved').length}
            </div>
            <div style={{ fontSize: '0.875rem', color: '#64748b' }}>Reserved</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Tables;