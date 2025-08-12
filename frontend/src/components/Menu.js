import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Plus, Edit, Trash2, Search } from 'lucide-react';

const Menu = () => {
  const [dishes, setDishes] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchMenuData();
  }, []);

  const fetchMenuData = async () => {
    try {
      setLoading(true);
      const [dishesRes, categoriesRes] = await Promise.all([
        axios.get('/api/dishes'),
        axios.get('/api/categories')
      ]);
      setDishes(dishesRes.data);
      setCategories(categoriesRes.data);
    } catch (error) {
      console.error('Error fetching menu data:', error);
      // Mock data for demo
      setDishes([
        { id: 1, name: 'Margherita Pizza', price: 12.99, category: { name: 'Pizza' } },
        { id: 2, name: 'Caesar Salad', price: 8.99, category: { name: 'Salads' } },
        { id: 3, name: 'Grilled Chicken', price: 15.99, category: { name: 'Main Course' } },
        { id: 4, name: 'Chocolate Cake', price: 6.99, category: { name: 'Desserts' } },
      ]);
      setCategories([
        { id: 1, name: 'Pizza' },
        { id: 2, name: 'Salads' },
        { id: 3, name: 'Main Course' },
        { id: 4, name: 'Desserts' },
      ]);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this dish?')) {
      try {
        await axios.delete(`/api/dishes/${id}`);
        setDishes(dishes.filter(dish => dish.id !== id));
      } catch (error) {
        console.error('Error deleting dish:', error);
        alert('Error deleting dish');
      }
    }
  };

  const filteredDishes = dishes.filter(dish =>
    dish.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    dish.category?.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) {
    return <div className="loading">Loading menu...</div>;
  }

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Menu Management</h1>
        <button className="btn btn-primary">
          <Plus size={18} />
          Add Dish
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
              placeholder="Search dishes..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="form-input"
              style={{ paddingLeft: '2.5rem' }}
            />
          </div>
        </div>

        {filteredDishes.length > 0 ? (
          <table className="table">
            <thead>
              <tr>
                <th>Dish Name</th>
                <th>Category</th>
                <th>Price</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredDishes.map((dish) => (
                <tr key={dish.id}>
                  <td>{dish.name}</td>
                  <td>{dish.category?.name || 'Uncategorized'}</td>
                  <td>${dish.price.toFixed(2)}</td>
                  <td>
                    <div className="actions">
                      <button className="btn btn-secondary" style={{ padding: '0.5rem' }}>
                        <Edit size={16} />
                      </button>
                      <button 
                        className="btn btn-danger" 
                        style={{ padding: '0.5rem' }}
                        onClick={() => handleDelete(dish.id)}
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
            <div className="empty-state-icon">🍽️</div>
            <p>No dishes found</p>
          </div>
        )}
      </div>

      <div className="card">
        <h2 style={{ margin: '0 0 1rem 0', fontSize: '1.25rem' }}>Categories</h2>
        <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
          {categories.map((category) => (
            <span 
              key={category.id}
              style={{
                padding: '0.5rem 1rem',
                backgroundColor: '#f1f5f9',
                borderRadius: '1rem',
                fontSize: '0.875rem',
                color: '#475569'
              }}
            >
              {category.name}
            </span>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Menu;