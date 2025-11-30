# SmartShop - Commercial Management API

> A REST API for B2B commercial management with loyalty system and multi-payment support

## 📋 Project Overview

SmartShop is a backend REST API developed for **MicroTech Maroc**, a B2B distributor of computer equipment in Casablanca. The application manages a portfolio of 650 active clients with an automated loyalty system, progressive discounts, and flexible payment options.

**Key Highlights:**
- 🎯 REST API 
- 👥 650+ active clients management
- 💳 Multi-method partial payments per invoice
- 🏆 Automatic loyalty system with 4 tiers
- 📊 Complete financial traceability
- 🔐 Session-based authentication

## ✨ Features

### 1. Client Management
- ✅ CRUD operations for clients
- ✅ Automatic statistics tracking (orders count, total spent)
- ✅ First and last order date tracking
- ✅ Complete order history view
- ✅ Loyalty level management (BASIC → SILVER → GOLD → PLATINUM)

### 2. Automatic Loyalty System
**Level Calculation:**
- **BASIC**: Default (0 orders)
- **SILVER**: 3+ orders OR 1,000 DH cumulative
- **GOLD**: 10+ orders OR 5,000 DH cumulative
- **PLATINUM**: 20+ orders OR 15,000 DH cumulative

**Discount Application:**
- **SILVER**: 5% discount if order ≥ 500 DH
- **GOLD**: 10% discount if order ≥ 800 DH
- **PLATINUM**: 15% discount if order ≥ 1,200 DH

*Level updates automatically after each confirmed order*

### 3. Product Management
- ✅ Add, update, and delete products
- ✅ Soft delete for products in existing orders
- ✅ Stock management with validation
- ✅ Advanced filtering and pagination

### 4. Order Management
- ✅ Multi-product orders with quantities
- ✅ Stock validation before confirmation
- ✅ Automatic discount calculation (loyalty + promo codes)
- ✅ VAT calculation (20% on amount after discount)
- ✅ Order status workflow: PENDING → CONFIRMED/CANCELED/REJECTED

**Order Calculation Flow:**
```
Subtotal (tax-excluded)
  - Loyalty discount
  - Promo code discount (5%)
  = Amount after discount
  + VAT 20%
  = Total including tax
```

### 5. Multi-Method Payment System
- ✅ 3 payment methods: CASH, CHEQUE, BANK_TRANSFER
- ✅ Partial payments support (multiple payments per order)
- ✅ Payment status tracking: PENDING → PAID/REJECTED
- ✅ CASH payments instantly marked as PAID
- ✅ CHEQUE/BANK_TRANSFER require admin confirmation

**Business Rule:** Order must be fully paid (remaining amount = 0) before confirmation

### 6. Promo Code System
- ✅ Auto-generated promo codes (format: PROMO-XXXX)
- ✅ Fixed 5% discount
- ✅ One-time use validation
- ✅ Applied cumulatively with loyalty discounts

## 🛠️ Tech Stack

### Backend Framework
- **Java 17** - Programming language
- **Spring Boot 4.0.0** - Application framework
- **Spring Data JPA** - Data persistence
- **Hibernate** - ORM

### Database
- **MySQL** - Relational database
- **HikariCP** - Connection pooling

### Testing
- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework
- **Maven Surefire** - Test runner

### Tools & Libraries
- **Lombok** - Reduce boilerplate code
- **MapStruct** - DTO ↔ Entity mapping
- **Maven** - Build tool & dependency management
- **Postman** - API testing

### Development Features
- ✅ Session-based authentication (HTTP Session)
- ✅ Custom exception handling with `@ControllerAdvice`
- ✅ Bean Validation (`@Valid`, `@NotNull`, etc.)
- ✅ Stream API for collections
- ✅ Lambda expressions
- ✅ Builder pattern

## 🏗️ Architecture

### Layered Architecture (MVC Pattern)

```
┌──────────────────────────────────────────────────────┐
│              PRESENTATION LAYER                      │
│          Controllers (REST Endpoints)                │
│                                                      │
│   - AuthController         - ClientController        │
│   - OrderController        - ProductController       │
│   - PaymentController      - PromoCodeController     │
│   - AdminController                                  │
└─────────────────────┬────────────────────────────────┘
                      │
┌─────────────────────▼────────────────────────────────┐
│                 SERVICE LAYER                        │
│          Business Logic & Validations                │
│                                                      │
│   Interfaces:                                        │
│   - AuthService            - ClientService           │
│   - OrderService           - ProductService          │
│   - PaymentService         - PromoCodeService        │
│   - AdminService           - ClientStatisticsService │
│                                                      │
│   Implementations (impl/):                           │
│   - AuthServiceImpl        - ClientServiceImpl       │
│   - OrderServiceImpl       - ProductServiceImpl      │
│   - PaymentServiceImpl     - PromoCodeServiceImpl    │
│   - AdminServiceImpl                                 |
|   - ClientStatisticsServiceImpl                      │
└─────────────────────┬────────────────────────────────┘
                      │
┌─────────────────────▼────────────────────────────────┐
│              PERSISTENCE LAYER                       │
│          Repositories (Spring Data JPA)              │
│                                                      │
│   - ClientRepository       - UserRepository          │
│   - OrderRepository        - OrderItemRepository     │
│   - ProductRepository      - PaymentRepository       │
│   - PromoCodeRepository                              │
└─────────────────────┬────────────────────────────────┘
                      │
┌─────────────────────▼────────────────────────────────┐
│                   DATABASE                           │
│              MySQL (smart shop)                      │
│                                                      │
│   Tables: users, clients, product, orders,           │
│   order_item, payment, promo_code                    │
└──────────────────────────────────────────────────────┘
```

### Project Structure

```
src/
├── main/
│   ├── java/com/smartshop/
│   │   ├── controller/         # REST Controllers
│   │   ├── service/             # Business logic interfaces
│   │   │   └── impl/            # Service implementations
│   │   ├── repository/          # Data access layer
│   │   ├── entity/              # JPA Entities
│   │   │   └── enums/           # Enums (OrderStatus, PaymentType, etc.)
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── request/         # Request DTOs
│   │   │   └── response/        # Response DTOs
│   │   ├── mapper/              # MapStruct mappers
│   │   ├── exception/           # Custom exceptions
│   │   └── util/                # Utility classes
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/smartshop/
        └── service/impl/        # Unit tests
```

### Key Design Patterns
- **Repository Pattern** - Data access abstraction
- **Service Layer Pattern** - Business logic separation
- **DTO Pattern** - Data transfer between layers
- **Builder Pattern** - Object creation (Lombok)
- **Factory Pattern** - Exception handling
- **Strategy Pattern** - Payment type handling

## ⚙️ Configuration / Environment Variables

### application.properties

```properties
# Server Configuration
server.port=8080
spring.application.name=SmartShop

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/smart_shop
spring.datasource.username=your_username_here
spring.datasource.password=your_password_here
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect


# Business Configuration
app.config.tva-rate=0.20
app.config.promo-pattern=PROMO-[A-Z0-9]{4}
app.config.max-cash-payment=20000.0

# Loyalty Tier - Thresholds (To obtain a level)
# Values: number of confirmed orders OR cumulative amount spent
app.loyalty.silver.min-orders=3
app.loyalty.silver.min-amount=1000.0

app.loyalty.gold.min-orders=10
app.loyalty.gold.min-amount=5000.0

app.loyalty.platinum.min-orders=20
app.loyalty.platinum.min-amount=15000.0

# Loyalty Tier - Discounts (To use a level)
# Values: discount percentage (e.g., 0.05 for 5%) AND minimum subtotal required
app.loyalty.silver.discount-rate=0.05
app.loyalty.silver.min-subtotal=500.0

app.loyalty.gold.discount-rate=0.10
app.loyalty.gold.min-subtotal=800.0

app.loyalty.platinum.discount-rate=0.15
app.loyalty.platinum.min-subtotal=1200.0

```

## 📋 Requirements

### Software Requirements
- ☑️ **Java 17+** (JDK 17 or higher)
- ☑️ **Maven 3.6+** (for building the project)
- ☑️ **MySQL 8.0+** (database server)
- ☑️ **Postman** (for API testing)



## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/smartshop.git
cd smartshop
```

### 2. Configure Database
Create MySQL database:
```sql
CREATE DATABASE smartshop_db;
```

Update `src/main/resources/application.properties` with your database credentials.

### 3. Build the Project
```bash
./mvnw clean install
```

### 4. Run the Application
```bash
./mvnw spring-boot:run
```

The API will be available at: `http://localhost:8080/api/v1/smartshop`

### 5. Test with Postman
Import the API endpoints and start testing:
- **Login:** `POST /auth/login`
- **Create Order:** `POST /order`
- **Add Payment:** `POST /payment`
- **Confirm Order:** `PUT /order/{id}/confirm`

## 📊 API Endpoints Overview

### Authentication
- `POST /auth/login` - User login
- `POST /auth/logout` - User logout

### Clients (ADMIN only)
- `GET /clients` - List all clients (paginated)
- `GET /clients/{id}` - Get client details
- `PUT /clients/{id}` - Update client
- `DELETE /clients/{id}` - Delete client
- `GET /clients/{id}/statistics` - Get client statistics
- `GET /clients/personalinfo` - Get own info (CLIENT access)

### Products
- `GET /products` - List products (with filters)
- `POST /products` - Create product (ADMIN)
- `PUT /products/{id}` - Update product (ADMIN)
- `DELETE /products/{id}` - Delete product (ADMIN)
- `GET /products/{id}` - Get product details

### Orders (ADMIN only)
- `POST /order` - Create order
- `GET /order/{id}` - Get order details
- `GET /order` - List all orders (paginated)
- `GET /order/client/{clientId}` - Get client orders
- `GET /order/myorders` - Get own orders (CLIENT access)
- `PUT /order/{id}/confirm` - Confirm order
- `PUT /order/{id}/cancel` - Cancel order

### Payments (ADMIN only)
- `POST /payment` - Add payment
- `PUT /payment/{id}/status` - Update payment status
- `GET /payment/{id}` - Get payment details
- `GET /payment` - List all payments
- `GET /payment/order/{orderId}` - Get order payments

### Promo Codes (ADMIN only)
- `POST /promocode/generate` - Generate new promo code
- `GET /promocode` - List all promo codes (paginated)




## 📝 Business Rules Summary

1. ✅ Stock validation: `quantity ≤ availableStock`
2. ✅ All amounts rounded to 2 decimals
3. ✅ Promo code format: `PROMO-XXXX`
4. ✅ VAT rate: 20% (configurable)
5. ✅ Order must be fully paid before confirmation
6. ✅ Loyalty level updates after each confirmed order
7. ✅ CASH payments instantly PAID
8. ✅ CHEQUE/BANK_TRANSFER require admin confirmation
9. ✅ Soft delete for products in existing orders
10. ✅ Clean error messages with proper HTTP codes

## 🐛 Error Handling

### HTTP Status Codes
- **200** - Success
- **201** - Created
- **204** - No Content
- **400** - Bad Request (validation error)
- **401** - Unauthorized (not authenticated)
- **403** - Forbidden (insufficient permissions)
- **404** - Not Found
- **500** - Internal Server Error

### Error Response Format
```json
{
  "timestamp": "2025-11-30T23:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Order not found",
  "path": "/api/v1/smartshop/order/999"
}
```

## 👨‍💻 Author

**Omar FARAJI**  
MicroTech Maroc - SmartShop Project

## 📄 License

This project is developed as part of a Spring Boot learning curriculum.

---
