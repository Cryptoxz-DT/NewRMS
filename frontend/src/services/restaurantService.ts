import { apiClient } from './api';
import {
  Order,
  Table,
  MenuItem,
  Customer,
  Reservation,
  DashboardStats,
  PaginatedResponse,
  ApiResponse,
} from '../types';

export const restaurantService = {
  // Orders
  orders: {
    getAll: async (params?: {
      page?: number;
      limit?: number;
      status?: string;
      dateFrom?: string;
      dateTo?: string;
    }): Promise<PaginatedResponse<Order>> => {
      return apiClient.get<PaginatedResponse<Order>>('/orders', { params });
    },

    getById: async (id: string): Promise<Order> => {
      return apiClient.get<Order>(`/orders/${id}`);
    },

    create: async (orderData: Partial<Order>): Promise<Order> => {
      return apiClient.post<Order>('/orders', orderData);
    },

    update: async (id: string, updates: Partial<Order>): Promise<Order> => {
      return apiClient.put<Order>(`/orders/${id}`, updates);
    },

    updateStatus: async (id: string, status: string): Promise<Order> => {
      return apiClient.patch<Order>(`/orders/${id}/status`, { status });
    },

    delete: async (id: string): Promise<void> => {
      return apiClient.delete<void>(`/orders/${id}`);
    },

    getActive: async (): Promise<Order[]> => {
      return apiClient.get<Order[]>('/orders/active');
    },

    getByTable: async (tableId: string): Promise<Order[]> => {
      return apiClient.get<Order[]>(`/orders/table/${tableId}`);
    },
  },

  // Tables
  tables: {
    getAll: async (): Promise<Table[]> => {
      return apiClient.get<Table[]>('/tables');
    },

    getById: async (id: string): Promise<Table> => {
      return apiClient.get<Table>(`/tables/${id}`);
    },

    create: async (tableData: Partial<Table>): Promise<Table> => {
      return apiClient.post<Table>('/tables', tableData);
    },

    update: async (id: string, updates: Partial<Table>): Promise<Table> => {
      return apiClient.put<Table>(`/tables/${id}`, updates);
    },

    updateStatus: async (id: string, status: string): Promise<Table> => {
      return apiClient.patch<Table>(`/tables/${id}/status`, { status });
    },

    delete: async (id: string): Promise<void> => {
      return apiClient.delete<void>(`/tables/${id}`);
    },

    getAvailable: async (): Promise<Table[]> => {
      return apiClient.get<Table[]>('/tables/available');
    },
  },

  // Menu Items
  menu: {
    getAll: async (params?: {
      category?: string;
      available?: boolean;
      search?: string;
    }): Promise<MenuItem[]> => {
      return apiClient.get<MenuItem[]>('/menu', { params });
    },

    getById: async (id: string): Promise<MenuItem> => {
      return apiClient.get<MenuItem>(`/menu/${id}`);
    },

    create: async (itemData: Partial<MenuItem>): Promise<MenuItem> => {
      return apiClient.post<MenuItem>('/menu', itemData);
    },

    update: async (id: string, updates: Partial<MenuItem>): Promise<MenuItem> => {
      return apiClient.put<MenuItem>(`/menu/${id}`, updates);
    },

    delete: async (id: string): Promise<void> => {
      return apiClient.delete<void>(`/menu/${id}`);
    },

    getCategories: async (): Promise<string[]> => {
      return apiClient.get<string[]>('/menu/categories');
    },

    updateAvailability: async (id: string, available: boolean): Promise<MenuItem> => {
      return apiClient.patch<MenuItem>(`/menu/${id}/availability`, { available });
    },
  },

  // Customers
  customers: {
    getAll: async (params?: {
      page?: number;
      limit?: number;
      search?: string;
    }): Promise<PaginatedResponse<Customer>> => {
      return apiClient.get<PaginatedResponse<Customer>>('/customers', { params });
    },

    getById: async (id: string): Promise<Customer> => {
      return apiClient.get<Customer>(`/customers/${id}`);
    },

    create: async (customerData: Partial<Customer>): Promise<Customer> => {
      return apiClient.post<Customer>('/customers', customerData);
    },

    update: async (id: string, updates: Partial<Customer>): Promise<Customer> => {
      return apiClient.put<Customer>(`/customers/${id}`, updates);
    },

    delete: async (id: string): Promise<void> => {
      return apiClient.delete<void>(`/customers/${id}`);
    },

    search: async (query: string): Promise<Customer[]> => {
      return apiClient.get<Customer[]>(`/customers/search`, { params: { q: query } });
    },
  },

  // Reservations
  reservations: {
    getAll: async (params?: {
      page?: number;
      limit?: number;
      date?: string;
      status?: string;
    }): Promise<PaginatedResponse<Reservation>> => {
      return apiClient.get<PaginatedResponse<Reservation>>('/reservations', { params });
    },

    getById: async (id: string): Promise<Reservation> => {
      return apiClient.get<Reservation>(`/reservations/${id}`);
    },

    create: async (reservationData: Partial<Reservation>): Promise<Reservation> => {
      return apiClient.post<Reservation>('/reservations', reservationData);
    },

    update: async (id: string, updates: Partial<Reservation>): Promise<Reservation> => {
      return apiClient.put<Reservation>(`/reservations/${id}`, updates);
    },

    updateStatus: async (id: string, status: string): Promise<Reservation> => {
      return apiClient.patch<Reservation>(`/reservations/${id}/status`, { status });
    },

    delete: async (id: string): Promise<void> => {
      return apiClient.delete<void>(`/reservations/${id}`);
    },

    getByDate: async (date: string): Promise<Reservation[]> => {
      return apiClient.get<Reservation[]>(`/reservations/date/${date}`);
    },
  },

  // Dashboard
  dashboard: {
    getStats: async (params?: {
      dateFrom?: string;
      dateTo?: string;
    }): Promise<DashboardStats> => {
      return apiClient.get<DashboardStats>('/dashboard/stats', { params });
    },

    getRevenueChart: async (period: 'day' | 'week' | 'month' | 'year'): Promise<any[]> => {
      return apiClient.get<any[]>(`/dashboard/revenue-chart`, { params: { period } });
    },

    getOrdersChart: async (period: 'day' | 'week' | 'month' | 'year'): Promise<any[]> => {
      return apiClient.get<any[]>(`/dashboard/orders-chart`, { params: { period } });
    },

    getPopularItems: async (limit = 10): Promise<any[]> => {
      return apiClient.get<any[]>(`/dashboard/popular-items`, { params: { limit } });
    },

    getRecentOrders: async (limit = 10): Promise<Order[]> => {
      return apiClient.get<Order[]>(`/dashboard/recent-orders`, { params: { limit } });
    },
  },

  // Staff
  staff: {
    getAll: async (): Promise<any[]> => {
      return apiClient.get<any[]>('/staff');
    },

    getById: async (id: string): Promise<any> => {
      return apiClient.get<any>(`/staff/${id}`);
    },

    create: async (staffData: any): Promise<any> => {
      return apiClient.post<any>('/staff', staffData);
    },

    update: async (id: string, updates: any): Promise<any> => {
      return apiClient.put<any>(`/staff/${id}`, updates);
    },

    delete: async (id: string): Promise<void> => {
      return apiClient.delete<void>(`/staff/${id}`);
    },
  },
};