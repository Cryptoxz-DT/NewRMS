import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { restaurantService } from '@/services/restaurantService';
import { useRestaurantStore } from '@/store/restaurantStore';
import { useUIStore } from '@/store/uiStore';
import { Order, Table, MenuItem, Customer, Reservation } from '@/types';

export const useOrders = (params?: any) => {
  const queryClient = useQueryClient();
  const { addNotification } = useUIStore();
  const { setOrders, setOrdersLoading } = useRestaurantStore();

  // Get all orders
  const {
    data: ordersData,
    isLoading,
    error,
    refetch,
  } = useQuery({
    queryKey: ['orders', params],
    queryFn: () => restaurantService.orders.getAll(params),
    onSuccess: (data) => {
      setOrders(data.data);
    },
    onError: () => {
      addNotification({
        type: 'error',
        title: 'Error',
        message: 'Failed to load orders',
      });
    },
  });

  // Get active orders
  const { data: activeOrders } = useQuery({
    queryKey: ['orders', 'active'],
    queryFn: restaurantService.orders.getActive,
    refetchInterval: 30000, // Refetch every 30 seconds
  });

  // Create order mutation
  const createOrderMutation = useMutation({
    mutationFn: restaurantService.orders.create,
    onSuccess: (newOrder) => {
      queryClient.invalidateQueries({ queryKey: ['orders'] });
      useRestaurantStore.getState().addOrder(newOrder);
      addNotification({
        type: 'success',
        title: 'Order Created',
        message: `Order #${newOrder.orderNumber} has been created`,
      });
    },
    onError: (error: any) => {
      addNotification({
        type: 'error',
        title: 'Failed to Create Order',
        message: error.response?.data?.message || 'Something went wrong',
      });
    },
  });

  // Update order mutation
  const updateOrderMutation = useMutation({
    mutationFn: ({ id, updates }: { id: string; updates: Partial<Order> }) =>
      restaurantService.orders.update(id, updates),
    onSuccess: (updatedOrder) => {
      queryClient.invalidateQueries({ queryKey: ['orders'] });
      useRestaurantStore.getState().updateOrder(updatedOrder.id, updatedOrder);
      addNotification({
        type: 'success',
        title: 'Order Updated',
        message: `Order #${updatedOrder.orderNumber} has been updated`,
      });
    },
    onError: (error: any) => {
      addNotification({
        type: 'error',
        title: 'Failed to Update Order',
        message: error.response?.data?.message || 'Something went wrong',
      });
    },
  });

  // Update order status mutation
  const updateOrderStatusMutation = useMutation({
    mutationFn: ({ id, status }: { id: string; status: string }) =>
      restaurantService.orders.updateStatus(id, status),
    onSuccess: (updatedOrder) => {
      queryClient.invalidateQueries({ queryKey: ['orders'] });
      useRestaurantStore.getState().updateOrder(updatedOrder.id, updatedOrder);
      addNotification({
        type: 'success',
        title: 'Order Status Updated',
        message: `Order #${updatedOrder.orderNumber} is now ${status.toLowerCase()}`,
      });
    },
    onError: (error: any) => {
      addNotification({
        type: 'error',
        title: 'Failed to Update Status',
        message: error.response?.data?.message || 'Something went wrong',
      });
    },
  });

  return {
    orders: ordersData?.data || [],
    activeOrders: activeOrders || [],
    pagination: ordersData?.pagination,
    isLoading,
    error,
    refetch,
    createOrder: createOrderMutation.mutate,
    updateOrder: updateOrderMutation.mutate,
    updateOrderStatus: updateOrderStatusMutation.mutate,
    isCreating: createOrderMutation.isPending,
    isUpdating: updateOrderMutation.isPending,
    isUpdatingStatus: updateOrderStatusMutation.isPending,
  };
};

export const useTables = () => {
  const queryClient = useQueryClient();
  const { addNotification } = useUIStore();
  const { setTables, setTablesLoading } = useRestaurantStore();

  // Get all tables
  const {
    data: tables,
    isLoading,
    error,
    refetch,
  } = useQuery({
    queryKey: ['tables'],
    queryFn: restaurantService.tables.getAll,
    onSuccess: (data) => {
      setTables(data);
    },
    refetchInterval: 60000, // Refetch every minute
  });

  // Update table status mutation
  const updateTableStatusMutation = useMutation({
    mutationFn: ({ id, status }: { id: string; status: string }) =>
      restaurantService.tables.updateStatus(id, status),
    onSuccess: (updatedTable) => {
      queryClient.invalidateQueries({ queryKey: ['tables'] });
      useRestaurantStore.getState().updateTable(updatedTable.id, updatedTable);
      addNotification({
        type: 'success',
        title: 'Table Updated',
        message: `Table ${updatedTable.number} is now ${status.toLowerCase()}`,
      });
    },
    onError: (error: any) => {
      addNotification({
        type: 'error',
        title: 'Failed to Update Table',
        message: error.response?.data?.message || 'Something went wrong',
      });
    },
  });

  return {
    tables: tables || [],
    isLoading,
    error,
    refetch,
    updateTableStatus: updateTableStatusMutation.mutate,
    isUpdatingStatus: updateTableStatusMutation.isPending,
  };
};

export const useMenu = (params?: any) => {
  const queryClient = useQueryClient();
  const { addNotification } = useUIStore();
  const { setMenuItems, setMenuLoading } = useRestaurantStore();

  // Get menu items
  const {
    data: menuItems,
    isLoading,
    error,
    refetch,
  } = useQuery({
    queryKey: ['menu', params],
    queryFn: () => restaurantService.menu.getAll(params),
    onSuccess: (data) => {
      setMenuItems(data);
    },
  });

  // Get categories
  const { data: categories } = useQuery({
    queryKey: ['menu', 'categories'],
    queryFn: restaurantService.menu.getCategories,
  });

  // Create menu item mutation
  const createMenuItemMutation = useMutation({
    mutationFn: restaurantService.menu.create,
    onSuccess: (newItem) => {
      queryClient.invalidateQueries({ queryKey: ['menu'] });
      useRestaurantStore.getState().addMenuItem(newItem);
      addNotification({
        type: 'success',
        title: 'Menu Item Added',
        message: `${newItem.name} has been added to the menu`,
      });
    },
    onError: (error: any) => {
      addNotification({
        type: 'error',
        title: 'Failed to Add Item',
        message: error.response?.data?.message || 'Something went wrong',
      });
    },
  });

  // Update menu item mutation
  const updateMenuItemMutation = useMutation({
    mutationFn: ({ id, updates }: { id: string; updates: Partial<MenuItem> }) =>
      restaurantService.menu.update(id, updates),
    onSuccess: (updatedItem) => {
      queryClient.invalidateQueries({ queryKey: ['menu'] });
      useRestaurantStore.getState().updateMenuItem(updatedItem.id, updatedItem);
      addNotification({
        type: 'success',
        title: 'Menu Item Updated',
        message: `${updatedItem.name} has been updated`,
      });
    },
    onError: (error: any) => {
      addNotification({
        type: 'error',
        title: 'Failed to Update Item',
        message: error.response?.data?.message || 'Something went wrong',
      });
    },
  });

  return {
    menuItems: menuItems || [],
    categories: categories || [],
    isLoading,
    error,
    refetch,
    createMenuItem: createMenuItemMutation.mutate,
    updateMenuItem: updateMenuItemMutation.mutate,
    isCreating: createMenuItemMutation.isPending,
    isUpdating: updateMenuItemMutation.isPending,
  };
};

export const useDashboard = (params?: any) => {
  const { addNotification } = useUIStore();

  // Get dashboard stats
  const {
    data: stats,
    isLoading: isLoadingStats,
    error: statsError,
  } = useQuery({
    queryKey: ['dashboard', 'stats', params],
    queryFn: () => restaurantService.dashboard.getStats(params),
    refetchInterval: 300000, // Refetch every 5 minutes
  });

  // Get revenue chart data
  const {
    data: revenueChart,
    isLoading: isLoadingRevenue,
  } = useQuery({
    queryKey: ['dashboard', 'revenue-chart'],
    queryFn: () => restaurantService.dashboard.getRevenueChart('week'),
  });

  // Get orders chart data
  const {
    data: ordersChart,
    isLoading: isLoadingOrders,
  } = useQuery({
    queryKey: ['dashboard', 'orders-chart'],
    queryFn: () => restaurantService.dashboard.getOrdersChart('week'),
  });

  // Get popular items
  const {
    data: popularItems,
    isLoading: isLoadingPopular,
  } = useQuery({
    queryKey: ['dashboard', 'popular-items'],
    queryFn: () => restaurantService.dashboard.getPopularItems(5),
  });

  return {
    stats,
    revenueChart: revenueChart || [],
    ordersChart: ordersChart || [],
    popularItems: popularItems || [],
    isLoading: isLoadingStats || isLoadingRevenue || isLoadingOrders || isLoadingPopular,
    error: statsError,
  };
};