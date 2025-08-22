import { create } from 'zustand';
import { Order, Table, MenuItem, Customer, DashboardStats } from '../types';

interface RestaurantState {
  // Orders
  orders: Order[];
  activeOrders: Order[];
  selectedOrder: Order | null;
  
  // Tables
  tables: Table[];
  selectedTable: Table | null;
  
  // Menu
  menuItems: MenuItem[];
  categories: string[];
  
  // Customers
  customers: Customer[];
  
  // Dashboard
  dashboardStats: DashboardStats | null;
  
  // Loading states
  ordersLoading: boolean;
  tablesLoading: boolean;
  menuLoading: boolean;
  customersLoading: boolean;
  statsLoading: boolean;
}

interface RestaurantActions {
  // Orders
  setOrders: (orders: Order[]) => void;
  addOrder: (order: Order) => void;
  updateOrder: (orderId: string, updates: Partial<Order>) => void;
  removeOrder: (orderId: string) => void;
  setSelectedOrder: (order: Order | null) => void;
  setOrdersLoading: (loading: boolean) => void;
  
  // Tables
  setTables: (tables: Table[]) => void;
  updateTable: (tableId: string, updates: Partial<Table>) => void;
  setSelectedTable: (table: Table | null) => void;
  setTablesLoading: (loading: boolean) => void;
  
  // Menu
  setMenuItems: (items: MenuItem[]) => void;
  addMenuItem: (item: MenuItem) => void;
  updateMenuItem: (itemId: string, updates: Partial<MenuItem>) => void;
  removeMenuItem: (itemId: string) => void;
  setMenuLoading: (loading: boolean) => void;
  
  // Customers
  setCustomers: (customers: Customer[]) => void;
  addCustomer: (customer: Customer) => void;
  updateCustomer: (customerId: string, updates: Partial<Customer>) => void;
  setCustomersLoading: (loading: boolean) => void;
  
  // Dashboard
  setDashboardStats: (stats: DashboardStats) => void;
  setStatsLoading: (loading: boolean) => void;
  
  // Utility
  getOrdersByStatus: (status: string) => Order[];
  getAvailableTables: () => Table[];
  getMenuItemsByCategory: (category: string) => MenuItem[];
}

export const useRestaurantStore = create<RestaurantState & RestaurantActions>((set, get) => ({
  // State
  orders: [],
  activeOrders: [],
  selectedOrder: null,
  tables: [],
  selectedTable: null,
  menuItems: [],
  categories: [],
  customers: [],
  dashboardStats: null,
  ordersLoading: false,
  tablesLoading: false,
  menuLoading: false,
  customersLoading: false,
  statsLoading: false,

  // Actions
  setOrders: (orders: Order[]) => {
    const activeOrders = orders.filter(order => 
      ['PENDING', 'CONFIRMED', 'PREPARING', 'READY'].includes(order.status)
    );
    set({ orders, activeOrders });
  },

  addOrder: (order: Order) => {
    set((state) => {
      const newOrders = [...state.orders, order];
      const activeOrders = newOrders.filter(o => 
        ['PENDING', 'CONFIRMED', 'PREPARING', 'READY'].includes(o.status)
      );
      return { orders: newOrders, activeOrders };
    });
  },

  updateOrder: (orderId: string, updates: Partial<Order>) => {
    set((state) => {
      const updatedOrders = state.orders.map(order =>
        order.id === orderId ? { ...order, ...updates } : order
      );
      const activeOrders = updatedOrders.filter(order => 
        ['PENDING', 'CONFIRMED', 'PREPARING', 'READY'].includes(order.status)
      );
      return { orders: updatedOrders, activeOrders };
    });
  },

  removeOrder: (orderId: string) => {
    set((state) => ({
      orders: state.orders.filter(order => order.id !== orderId),
      activeOrders: state.activeOrders.filter(order => order.id !== orderId),
      selectedOrder: state.selectedOrder?.id === orderId ? null : state.selectedOrder,
    }));
  },

  setSelectedOrder: (order: Order | null) => {
    set({ selectedOrder: order });
  },

  setOrdersLoading: (loading: boolean) => {
    set({ ordersLoading: loading });
  },

  setTables: (tables: Table[]) => {
    set({ tables });
  },

  updateTable: (tableId: string, updates: Partial<Table>) => {
    set((state) => ({
      tables: state.tables.map(table =>
        table.id === tableId ? { ...table, ...updates } : table
      ),
    }));
  },

  setSelectedTable: (table: Table | null) => {
    set({ selectedTable: table });
  },

  setTablesLoading: (loading: boolean) => {
    set({ tablesLoading: loading });
  },

  setMenuItems: (items: MenuItem[]) => {
    const categories = [...new Set(items.map(item => item.category))];
    set({ menuItems: items, categories });
  },

  addMenuItem: (item: MenuItem) => {
    set((state) => {
      const newItems = [...state.menuItems, item];
      const categories = [...new Set(newItems.map(i => i.category))];
      return { menuItems: newItems, categories };
    });
  },

  updateMenuItem: (itemId: string, updates: Partial<MenuItem>) => {
    set((state) => {
      const updatedItems = state.menuItems.map(item =>
        item.id === itemId ? { ...item, ...updates } : item
      );
      const categories = [...new Set(updatedItems.map(item => item.category))];
      return { menuItems: updatedItems, categories };
    });
  },

  removeMenuItem: (itemId: string) => {
    set((state) => {
      const filteredItems = state.menuItems.filter(item => item.id !== itemId);
      const categories = [...new Set(filteredItems.map(item => item.category))];
      return { menuItems: filteredItems, categories };
    });
  },

  setMenuLoading: (loading: boolean) => {
    set({ menuLoading: loading });
  },

  setCustomers: (customers: Customer[]) => {
    set({ customers });
  },

  addCustomer: (customer: Customer) => {
    set((state) => ({
      customers: [...state.customers, customer],
    }));
  },

  updateCustomer: (customerId: string, updates: Partial<Customer>) => {
    set((state) => ({
      customers: state.customers.map(customer =>
        customer.id === customerId ? { ...customer, ...updates } : customer
      ),
    }));
  },

  setCustomersLoading: (loading: boolean) => {
    set({ customersLoading: loading });
  },

  setDashboardStats: (stats: DashboardStats) => {
    set({ dashboardStats: stats });
  },

  setStatsLoading: (loading: boolean) => {
    set({ statsLoading: loading });
  },

  // Utility functions
  getOrdersByStatus: (status: string) => {
    return get().orders.filter(order => order.status === status);
  },

  getAvailableTables: () => {
    return get().tables.filter(table => table.status === 'AVAILABLE');
  },

  getMenuItemsByCategory: (category: string) => {
    return get().menuItems.filter(item => item.category === category);
  },
}));