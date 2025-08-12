# Restaurant Management System (NewRMS)

A comprehensive Restaurant Management System built with Spring Boot, providing APIs for managing customers, orders, dishes, staff, and reservations.

## Features

- **Customer Management**: CRUD operations for customer data
- **Order Management**: Create and track orders with detailed summaries
- **Menu Management**: Manage dishes, categories, and ingredients
- **Staff Management**: Handle staff information and authentication
- **Table Management**: Manage restaurant tables and reservations
- **Security**: Basic authentication with role-based access control

## Tech Stack

- **Backend**: Spring Boot 3.5.3
- **Database**: PostgreSQL
- **Security**: Spring Security with Basic Auth
- **Validation**: Bean Validation (JSR-303)
- **Build Tool**: Maven
- **Java Version**: 21

## Prerequisites

- Java 21 or higher
- PostgreSQL database
- Maven 3.6+

## Setup Instructions

### 1. Database Setup
```sql
CREATE DATABASE restaurant_db;
```

### 2. Environment Configuration
1. Copy `.env.example` to `.env`
2. Update the database credentials and other configurations:
```env
DB_URL=jdbc:postgresql://localhost:5432/restaurant_db
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

### 3. Run the Application
```bash
# Using Maven
mvn spring-boot:run

# Or using the wrapper
./mvnw spring-boot:run
```

The application will start on `http://localhost:8081`

## API Endpoints

### Authentication
- `GET /api/auth/user` - Get current user info
- `POST /api/auth/logout` - Logout

### Customers
- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get customer by ID
- `POST /api/customers` - Create new customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer

### Orders
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/summaries` - Get order summaries
- `POST /api/orders` - Create new order
- `DELETE /api/orders/{id}` - Delete order

## Authentication

The application uses Basic Authentication. Default credentials:
- **Username**: admin
- **Password**: password

For production, create staff records in the database with proper credentials.

## Database Schema

The application uses JPA/Hibernate for database management with the following main entities:
- Customer
- Order
- OrderItem
- Dish
- Category
- Staff
- TableInfo
- Reservation
- OrderStatus

## Development

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package
```

## Security Considerations

- Database credentials are externalized using environment variables
- Passwords are encrypted using BCrypt
- Input validation is implemented on all endpoints
- Comprehensive error handling with proper HTTP status codes

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.