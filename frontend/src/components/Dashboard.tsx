import React from 'react';
import { motion } from 'framer-motion';
import {
  TrendingUp,
  TrendingDown,
  ShoppingBag,
  DollarSign,
  Users,
  Clock,
  ChefHat,
  Calendar,
} from 'lucide-react';
import { useDashboard } from '../hooks/useRestaurant';
import { formatCurrency, formatRelativeTime } from '../utils';
import Card from './ui/Card';
import Button from './ui/Button';
import {
  LineChart,
  Line,
  AreaChart,
  Area,
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from 'recharts';

const Dashboard: React.FC = () => {
  const { stats, revenueChart, ordersChart, popularItems, isLoading } = useDashboard();

  const statCards = [
    {
      title: 'Total Revenue',
      value: formatCurrency(stats?.totalRevenue || 0),
      change: '+12.5%',
      trend: 'up',
      icon: DollarSign,
      color: 'text-success-600',
      bgColor: 'bg-success-100',
    },
    {
      title: 'Orders Today',
      value: stats?.todayOrders || 0,
      change: '+8.2%',
      trend: 'up',
      icon: ShoppingBag,
      color: 'text-primary-600',
      bgColor: 'bg-primary-100',
    },
    {
      title: 'Active Orders',
      value: stats?.activeOrders || 0,
      change: '-2.1%',
      trend: 'down',
      icon: Clock,
      color: 'text-warning-600',
      bgColor: 'bg-warning-100',
    },
    {
      title: 'Available Tables',
      value: stats?.availableTables || 0,
      change: '+5.3%',
      trend: 'up',
      icon: Calendar,
      color: 'text-blue-600',
      bgColor: 'bg-blue-100',
    },
  ];

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1,
      },
    },
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0 },
  };

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold text-secondary-900 dark:text-white">
              Dashboard
            </h1>
            <p className="text-secondary-600 dark:text-secondary-400">
              Welcome back! Here's what's happening at your restaurant.
            </p>
          </div>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {[...Array(4)].map((_, i) => (
            <Card key={i} className="animate-pulse">
              <div className="h-24 bg-secondary-200 dark:bg-secondary-700 rounded"></div>
            </Card>
          ))}
        </div>
      </div>
    );
  }

  return (
    <motion.div
      className="space-y-6"
      variants={containerVariants}
      initial="hidden"
      animate="visible"
    >
      {/* Header */}
      <motion.div
        className="flex items-center justify-between"
        variants={itemVariants}
      >
        <div>
          <h1 className="text-2xl font-bold text-secondary-900 dark:text-white">
            Dashboard
          </h1>
          <p className="text-secondary-600 dark:text-secondary-400">
            Welcome back! Here's what's happening at your restaurant.
          </p>
        </div>
        <div className="flex gap-3">
          <Button variant="outline" size="sm">
            Export Report
          </Button>
          <Button variant="primary" size="sm">
            View Analytics
          </Button>
        </div>
      </motion.div>

      {/* Stats Cards */}
      <motion.div
        className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6"
        variants={itemVariants}
      >
        {statCards.map((stat, index) => (
          <Card
            key={stat.title}
            variant="elevated"
            hover
            className="relative overflow-hidden"
          >
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-secondary-600 dark:text-secondary-400">
                  {stat.title}
                </p>
                <p className="text-2xl font-bold text-secondary-900 dark:text-white mt-1">
                  {stat.value}
                </p>
                <div className="flex items-center mt-2">
                  {stat.trend === 'up' ? (
                    <TrendingUp className="w-4 h-4 text-success-600 mr-1" />
                  ) : (
                    <TrendingDown className="w-4 h-4 text-error-600 mr-1" />
                  )}
                  <span
                    className={`text-sm font-medium ${
                      stat.trend === 'up' ? 'text-success-600' : 'text-error-600'
                    }`}
                  >
                    {stat.change}
                  </span>
                  <span className="text-sm text-secondary-500 ml-1">
                    vs last week
                  </span>
                </div>
              </div>
              <div className={`p-3 rounded-lg ${stat.bgColor}`}>
                <stat.icon className={`w-6 h-6 ${stat.color}`} />
              </div>
            </div>
            <motion.div
              className="absolute inset-0 bg-gradient-to-r from-transparent to-primary-50 dark:to-primary-900/10 opacity-0"
              whileHover={{ opacity: 1 }}
              transition={{ duration: 0.3 }}
            />
          </Card>
        ))}
      </motion.div>

      {/* Charts Row */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Revenue Chart */}
        <motion.div variants={itemVariants}>
          <Card variant="elevated" padding="lg">
            <Card.Header>
              <Card.Title>Revenue Overview</Card.Title>
              <Card.Description>
                Daily revenue for the past 7 days
              </Card.Description>
            </Card.Header>
            <Card.Content>
              <div className="h-80">
                <ResponsiveContainer width="100%" height="100%">
                  <AreaChart data={revenueChart}>
                    <defs>
                      <linearGradient id="revenueGradient" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="5%" stopColor="#ed7420" stopOpacity={0.3} />
                        <stop offset="95%" stopColor="#ed7420" stopOpacity={0} />
                      </linearGradient>
                    </defs>
                    <CartesianGrid strokeDasharray="3 3" className="opacity-30" />
                    <XAxis dataKey="name" className="text-xs" />
                    <YAxis className="text-xs" />
                    <Tooltip
                      contentStyle={{
                        backgroundColor: 'white',
                        border: '1px solid #e2e8f0',
                        borderRadius: '8px',
                        boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
                      }}
                    />
                    <Area
                      type="monotone"
                      dataKey="value"
                      stroke="#ed7420"
                      strokeWidth={2}
                      fill="url(#revenueGradient)"
                    />
                  </AreaChart>
                </ResponsiveContainer>
              </div>
            </Card.Content>
          </Card>
        </motion.div>

        {/* Orders Chart */}
        <motion.div variants={itemVariants}>
          <Card variant="elevated" padding="lg">
            <Card.Header>
              <Card.Title>Orders Trend</Card.Title>
              <Card.Description>
                Order volume for the past 7 days
              </Card.Description>
            </Card.Header>
            <Card.Content>
              <div className="h-80">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={ordersChart}>
                    <CartesianGrid strokeDasharray="3 3" className="opacity-30" />
                    <XAxis dataKey="name" className="text-xs" />
                    <YAxis className="text-xs" />
                    <Tooltip
                      contentStyle={{
                        backgroundColor: 'white',
                        border: '1px solid #e2e8f0',
                        borderRadius: '8px',
                        boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
                      }}
                    />
                    <Bar dataKey="value" fill="#64748b" radius={[4, 4, 0, 0]} />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </Card.Content>
          </Card>
        </motion.div>
      </div>

      {/* Bottom Row */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Popular Items */}
        <motion.div variants={itemVariants} className="lg:col-span-2">
          <Card variant="elevated" padding="lg">
            <Card.Header>
              <Card.Title>Popular Menu Items</Card.Title>
              <Card.Description>
                Top selling items this week
              </Card.Description>
            </Card.Header>
            <Card.Content>
              <div className="space-y-4">
                {popularItems.map((item, index) => (
                  <div
                    key={item.id}
                    className="flex items-center justify-between p-3 bg-secondary-50 dark:bg-secondary-800 rounded-lg"
                  >
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 bg-primary-100 rounded-lg flex items-center justify-center">
                        <ChefHat className="w-5 h-5 text-primary-600" />
                      </div>
                      <div>
                        <p className="font-medium text-secondary-900 dark:text-white">
                          {item.name}
                        </p>
                        <p className="text-sm text-secondary-600 dark:text-secondary-400">
                          {item.orders} orders
                        </p>
                      </div>
                    </div>
                    <div className="text-right">
                      <p className="font-medium text-secondary-900 dark:text-white">
                        {formatCurrency(item.revenue)}
                      </p>
                      <p className="text-sm text-success-600">
                        +{item.growth}%
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            </Card.Content>
          </Card>
        </motion.div>

        {/* Recent Activity */}
        <motion.div variants={itemVariants}>
          <Card variant="elevated" padding="lg">
            <Card.Header>
              <Card.Title>Recent Activity</Card.Title>
              <Card.Description>
                Latest updates from your restaurant
              </Card.Description>
            </Card.Header>
            <Card.Content>
              <div className="space-y-4">
                {[
                  {
                    type: 'order',
                    message: 'New order #1234 received',
                    time: '2 minutes ago',
                    icon: ShoppingBag,
                    color: 'text-primary-600',
                  },
                  {
                    type: 'table',
                    message: 'Table 5 marked as available',
                    time: '5 minutes ago',
                    icon: Calendar,
                    color: 'text-success-600',
                  },
                  {
                    type: 'customer',
                    message: 'New customer registered',
                    time: '10 minutes ago',
                    icon: Users,
                    color: 'text-blue-600',
                  },
                  {
                    type: 'order',
                    message: 'Order #1233 completed',
                    time: '15 minutes ago',
                    icon: ShoppingBag,
                    color: 'text-success-600',
                  },
                ].map((activity, index) => (
                  <div key={index} className="flex items-start gap-3">
                    <div className={`p-2 rounded-lg bg-secondary-100 dark:bg-secondary-800`}>
                      <activity.icon className={`w-4 h-4 ${activity.color}`} />
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-medium text-secondary-900 dark:text-white">
                        {activity.message}
                      </p>
                      <p className="text-xs text-secondary-500 dark:text-secondary-400">
                        {activity.time}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            </Card.Content>
          </Card>
        </motion.div>
      </div>
    </motion.div>
  );
};

export default Dashboard;