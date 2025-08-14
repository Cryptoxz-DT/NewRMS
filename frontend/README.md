# NewRMS Frontend

A modern, minimalistic React frontend for the Restaurant Management System.

## Features

- **Modern UI**: Clean, minimalistic design with a professional look
- **Responsive**: Works on desktop, tablet, and mobile devices
- **Authentication**: Secure login with Basic Auth
- **Dashboard**: Overview of key metrics and recent activity
- **Order Management**: View and manage restaurant orders
- **Customer Management**: Add, edit, and manage customer information
- **Menu Management**: Manage dishes and categories
- **Staff Management**: Handle staff accounts and roles
- **Table Management**: Monitor table status and availability

## Tech Stack

- **React 18**: Modern React with hooks
- **React Router**: Client-side routing
- **Axios**: HTTP client for API calls
- **Lucid React**: Beautiful, customizable icons
- **CSS3**: Modern styling with CSS Grid and Flexbox

## Getting Started

### Prerequisites

- Node.js 16+ and npm
- Backend API running (default: http://localhost:8085)

### Installation

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The application will open at `http://localhost:3000`

### Default Login Credentials

- **Username**: admin
- **Password**: password

## Project Structure

```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   ├── Navbar.js          # Navigation sidebar
│   │   ├── Login.js           # Login form
│   │   ├── Dashboard.js       # Main dashboard
│   │   ├── Orders.js          # Order management
│   │   ├── Customers.js       # Customer management
│   │   ├── Menu.js            # Menu management
│   │   ├── Staff.js           # Staff management
│   │   └── Tables.js          # Table management
│   ├── context/
│   │   └── AuthContext.js     # Authentication context
│   ├── App.js                 # Main app component
│   ├── App.css               # App-specific styles
│   ├── index.js              # Entry point
│   └── index.css             # Global styles
├── package.json
└── README.md
```

## Key Features

### Authentication
- Secure login with username/password
- Persistent authentication state
- Automatic logout functionality

### Dashboard
- Key metrics overview
- Recent orders display
- Quick action buttons

### CRUD Operations
- Create, read, update, delete for all entities
- Form validation
- Confirmation dialogs for destructive actions

### Search & Filter
- Real-time search across all data tables
- Filter by various criteria

### Responsive Design
- Mobile-first approach
- Collapsible sidebar on mobile
- Touch-friendly interface

## API Integration

The frontend communicates with the Spring Boot backend via REST APIs:

- **Authentication**: `/api/auth/*`
- **Orders**: `/api/orders`
- **Customers**: `/api/customers`
- **Staff**: `/api/staff`
- **Menu**: `/api/dishes`, `/api/categories`
- **Tables**: `/api/tables`

## Customization

### Styling
- Modify `src/index.css` for global styles
- Component-specific styles in individual CSS files
- CSS custom properties for easy theming

### Adding New Features
1. Create new component in `src/components/`
2. Add route in `App.js`
3. Add navigation item in `Navbar.js`
4. Implement API calls using axios

## Build for Production

```bash
npm run build
```

This creates an optimized production build in the `build/` directory.

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Contributing

1. Follow the existing code style
2. Add proper error handling
3. Include loading states
4. Test on multiple screen sizes
5. Update documentation as needed