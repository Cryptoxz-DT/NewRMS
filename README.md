# Restaurant Management System (NewRMS)

A comprehensive Restaurant Management System built with Spring Boot, providing complete APIs for managing all aspects of restaurant operations including customers, orders, dishes, staff, ingredients, and reservations.

## 🚀 Features

### Core Management
- **Customer Management**: Complete CRUD operations for customer data with validation
- **Order Management**: Create, track, and manage orders with detailed summaries and status tracking
- **Menu Management**: Manage dishes, categories, and pricing with full ingredient tracking
- **Staff Management**: Handle staff accounts, roles, and authentication with secure password hashing
- **Table Management**: Manage restaurant tables, capacity, and availability
- **Reservation System**: Handle table reservations with time-based booking

### Advanced Features
- **Ingredient Inventory**: Track ingredient stock levels with low-stock alerts
- **Recipe Management**: Link dishes to ingredients with quantity requirements
- **Order Tracking**: Complete order lifecycle from pending to completed
- **Security**: Role-based access control with BCrypt password encryption
- **Audit Trail**: Timestamps for all entities (created_at, updated_at)
- **Data Validation**: Comprehensive input validation with custom error messages

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.5.3
- **Database**: PostgreSQL 17.5
- **Security**: Spring Security with Basic Auth
- **Validation**: Bean Validation (JSR-303)
- **Build Tool**: Maven
- **Java Version**: 21
- **ORM**: JPA/Hibernate with optimized queries
- **Documentation**: OpenAPI 3 (Swagger)

## 📋 Prerequisites

- Java 21 or higher
- PostgreSQL 17+ database
- Maven 3.6+
- Node.js 16+ (for frontend)

## 🔧 Setup Instructions

### 1. Database Setup
```sql
-- Create database
CREATE DATABASE restaurant_db;

-- Run the provided SQL script
\i database/restaurant_db_schema.sql
```

The SQL script includes:
- Complete table structure with indexes
- 10 sample records for each entity
- Useful views for common queries
- Performance optimizations

### 2. Environment Configuration
1. Copy `.env.example` to `.env`
2. Update the database credentials:
```env
DB_URL=jdbc:postgresql://localhost:5432/restaurant_db
DB_USERNAME=postgres
DB_PASSWORD=your_password
```

### 3. Run the Application
```bash
# Install dependencies and run
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8085`

### 4. Access Documentation
- **Swagger UI**: http://localhost:8085/swagger-ui/index.html
- **API Docs**: http://localhost:8085/v3/api-docs

## 📚 API Endpoints

### Authentication & Debug
- `GET /api/auth/user` - Get current user info
- `POST /api/auth/logout` - Logout
- `GET /api/debug/staff` - List all staff (debug)
- `POST /api/debug/verify-password` - Verify credentials (debug)

### Core Entities

#### Staff Management
- `GET /api/staff` - Get all staff
- `GET /api/staff/{id}` - Get staff by ID
- `POST /api/staff` - Create new staff member
- `PUT /api/staff/{id}` - Update staff member
- `DELETE /api/staff/{id}` - Delete staff member

#### Customer Management
- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get customer by ID
- `POST /api/customers` - Create new customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer

#### Order Management
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/summaries` - Get order summaries with totals
- `POST /api/orders` - Create new order
- `DELETE /api/orders/{id}` - Delete order

#### Menu Management
- `GET /api/dishes` - Get all dishes
- `GET /api/categories` - Get all categories
- `POST /api/dishes` - Create new dish
- `PUT /api/dishes/{id}` - Update dish

#### Inventory Management
- `GET /api/ingredients` - Get all ingredients
- `GET /api/ingredients/low-stock` - Get low stock ingredients
- `POST /api/ingredients` - Add new ingredient
- `PATCH /api/ingredients/{id}/stock` - Update stock levels

#### Recipe Management
- `GET /api/dish-ingredients` - Get all dish-ingredient relationships
- `GET /api/dish-ingredients/dish/{dishId}` - Get ingredients for a dish
- `POST /api/dish-ingredients` - Link ingredient to dish

#### Table & Reservation Management
- `GET /api/tables` - Get all tables
- `GET /api/reservations` - Get all reservations
- `POST /api/reservations` - Create new reservation

## 🔐 Authentication

### Default Credentials
All staff members use the password: **"password"**

**Available Users:**
- `admin/password` (Admin)
- `manager1/password` (Manager)  
- `waiter1/password` (Waiter)
- `chef1/password` (Chef)
- `cashier1/password` (Cashier)

### Roles & Permissions
- **ADMIN**: Full system access
- **MANAGER**: Management operations
- **WAITER**: Order and customer management
- **CHEF**: Kitchen and ingredient management
- **CASHIER**: Payment and order completion

## 🗄️ Database Schema

### Core Tables
- **staff** - User accounts and authentication
- **customers** - Customer information
- **category** - Food categories
- **dishes** - Menu items with pricing
- **table_info** - Restaurant tables
- **order_status** - Order workflow states
- **order_table** - Customer orders
- **order_item** - Individual order items
- **ingredient** - Inventory items
- **dish_ingredient** - Recipe relationships
- **reservations** - Table bookings

### Key Features
- **Foreign Key Constraints**: Data integrity maintained
- **Indexes**: Optimized query performance
- **Timestamps**: Audit trail for all changes
- **Validation**: Database-level constraints
- **Views**: Pre-built complex queries

## 🎯 Sample Data

The database comes pre-loaded with:
- **10 Staff members** with different roles
- **10 Customers** with contact information
- **10 Food categories** (Appetizers, Main Course, etc.)
- **10 Dishes** with realistic pricing
- **10 Tables** with varying capacities
- **10 Order statuses** for complete workflow
- **10 Sample orders** with timestamps
- **10 Ingredients** for inventory management
- **10 Reservations** for future dates

## 🚦 Development

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package
```

### Code Quality
- **Validation**: Input validation on all endpoints
- **Error Handling**: Comprehensive exception handling
- **Security**: BCrypt password hashing
- **Performance**: Lazy loading and optimized queries
- **Documentation**: Swagger/OpenAPI integration

## 🔒 Security Features

- **Password Encryption**: BCrypt with salt
- **Input Validation**: Bean Validation (JSR-303)
- **SQL Injection Protection**: JPA/Hibernate parameterized queries
- **Role-Based Access**: Spring Security integration
- **Environment Variables**: Sensitive data externalized
- **CORS Configuration**: Secure cross-origin requests

## 📊 Performance Optimizations

- **Database Indexes**: Strategic indexing for common queries
- **Lazy Loading**: Efficient entity loading
- **Connection Pooling**: HikariCP for database connections
- **Query Optimization**: Custom JPQL queries for complex operations
- **Caching**: Application-level caching where appropriate

## 🐛 Troubleshooting

### Common Issues
1. **Port Already in Use**: Change `server.port` in `application.properties`
2. **Database Connection**: Verify PostgreSQL is running and credentials are correct
3. **Authentication Issues**: Use debug endpoints to verify user credentials
4. **Build Failures**: Ensure Java 21 and Maven are properly installed

### Debug Endpoints
- `/api/debug/staff` - List all users
- `/api/debug/verify-password` - Test authentication
- `/actuator/health` - Application health check

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL community for the robust database
- All contributors who helped improve this project