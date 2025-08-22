import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { NotificationState, ThemeConfig } from '@/types';

interface UIState {
  theme: ThemeConfig;
  sidebarOpen: boolean;
  notifications: NotificationState[];
  isLoading: boolean;
  loadingMessage: string;
  modal: {
    isOpen: boolean;
    type: string | null;
    data: any;
  };
}

interface UIActions {
  toggleSidebar: () => void;
  setSidebarOpen: (open: boolean) => void;
  setTheme: (theme: Partial<ThemeConfig>) => void;
  toggleTheme: () => void;
  addNotification: (notification: Omit<NotificationState, 'id' | 'timestamp'>) => void;
  removeNotification: (id: string) => void;
  clearNotifications: () => void;
  setLoading: (loading: boolean, message?: string) => void;
  openModal: (type: string, data?: any) => void;
  closeModal: () => void;
}

export const useUIStore = create<UIState & UIActions>()(
  persist(
    (set, get) => ({
      // State
      theme: {
        mode: 'light',
        primaryColor: '#ed7420',
        accentColor: '#64748b',
        fontSize: 'medium',
        animations: true,
      },
      sidebarOpen: true,
      notifications: [],
      isLoading: false,
      loadingMessage: '',
      modal: {
        isOpen: false,
        type: null,
        data: null,
      },

      // Actions
      toggleSidebar: () => {
        set((state) => ({ sidebarOpen: !state.sidebarOpen }));
      },

      setSidebarOpen: (open: boolean) => {
        set({ sidebarOpen: open });
      },

      setTheme: (themeUpdate: Partial<ThemeConfig>) => {
        set((state) => ({
          theme: { ...state.theme, ...themeUpdate },
        }));
      },

      toggleTheme: () => {
        set((state) => ({
          theme: {
            ...state.theme,
            mode: state.theme.mode === 'light' ? 'dark' : 'light',
          },
        }));
      },

      addNotification: (notification) => {
        const id = Math.random().toString(36).substr(2, 9);
        const timestamp = new Date().toISOString();
        
        set((state) => ({
          notifications: [
            ...state.notifications,
            { ...notification, id, timestamp },
          ],
        }));

        // Auto-remove notification after duration
        if (notification.duration !== 0) {
          setTimeout(() => {
            get().removeNotification(id);
          }, notification.duration || 5000);
        }
      },

      removeNotification: (id: string) => {
        set((state) => ({
          notifications: state.notifications.filter((n) => n.id !== id),
        }));
      },

      clearNotifications: () => {
        set({ notifications: [] });
      },

      setLoading: (loading: boolean, message = '') => {
        set({ isLoading: loading, loadingMessage: message });
      },

      openModal: (type: string, data = null) => {
        set({
          modal: {
            isOpen: true,
            type,
            data,
          },
        });
      },

      closeModal: () => {
        set({
          modal: {
            isOpen: false,
            type: null,
            data: null,
          },
        });
      },
    }),
    {
      name: 'ui-storage',
      partialize: (state) => ({
        theme: state.theme,
        sidebarOpen: state.sidebarOpen,
      }),
    }
  )
);