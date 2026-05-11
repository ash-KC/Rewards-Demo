# Customer Rewards Program

A Spring Boot REST API that calculates reward points for a retailer's loyalty program based on purchase transactions over a three-month period.

## Problem Statement

A retailer offers a rewards program to its customers, awarding points based on each recorded purchase:

- **2 points** for every dollar spent **over $100** in each transaction
- **1 point** for every dollar spent **between $50 and $100** in each transaction

**Example:** A $120 purchase = 2×$20 + 1×$50 = **90 points**

## Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Language |
| Spring Boot | 3.2.5 | Application framework |
| Spring Data JPA | - | Data access layer |
| H2 Database | - | In-memory database |
| JUnit 5 | - | Unit testing |
| Mockito | - | Mocking framework |
| Maven | 3.6+ | Build tool |

## Project Structure

```
src/
├── main/java/com/retailer/rewards/
│   ├── RewardsApplication.java           # Spring Boot entry point
│   ├── controller/
│   │   └── RewardsController.java        # REST API endpoints
│   ├── dto/
│   │   └── RewardResponse.java           # API response model
│   ├── exception/
│   │   ├── CustomerNotFoundException.java        # 404 exception
│   │   ├── GlobalExceptionHandler.java           # Centralized error handling
│   │   └── InvalidTransactionAmountException.java # 400 exception
│   ├── model/
│   │   ├── Customer.java                 # Customer entity
│   │   └── Transaction.java              # Transaction entity
│   ├── repository/
│   │   ├── CustomerRepository.java       # Customer data access
│   │   └── TransactionRepository.java    # Transaction data access
│   └── service/
│       └── RewardsService.java           # Business logic & points calculation
├── main/resources/
│   ├── application.properties            # App configuration
│   └── data.sql                          # Sample data (3 customers, 17 transactions)
└── test/java/com/retailer/rewards/
    ├── controller/
    │   └── RewardsControllerTest.java    # Integration tests (MockMvc)
    ├── exception/
    │   └── ExceptionTests.java           # Exception unit tests
    └── service/
        └── RewardsServiceTest.java       # Unit tests (Mockito)
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Build & Run

```bash
# Clone the repository
git clone https://github.com/ash-KC/Rewards-Demo.git
cd Rewards-Demo

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application starts on **http://localhost:8080**.

### Run Tests

```bash
# Run all tests
mvn test

# Run with verbose output
mvn test -Dtest=RewardsServiceTest -Dsurefire.useFile=false
```

## API Endpoints

### Get Rewards for All Customers

```
GET /api/rewards
```

**Response:** `200 OK`

```json
[
  {
    "customerId": 1,
    "customerName": "Alice Johnson",
    "monthlyRewards": {
      "January 2026": 115,
      "February 2026": 255,
      "March 2026": 60
    },
    "totalRewards": 430
  }
]
```

### Get Rewards for a Specific Customer

```
GET /api/rewards/{customerId}
```

**Response:** `200 OK`

```json
{
  "customerId": 1,
  "customerName": "Alice Johnson",
  "monthlyRewards": {
    "January 2026": 115,
    "February 2026": 255,
    "March 2026": 60
  },
  "totalRewards": 430
}
```

**Error Response:** `404 Not Found`

```json
{
  "timestamp": "2026-01-15T10:30:00.123",
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with id: 999"
}
```

### List All Customers

```
GET /api/customers
```

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "name": "Alice Johnson"
  },
  {
    "id": 2,
    "name": "Bob Smith"
  },
  {
    "id": 3,
    "name": "Charlie Davis"
  }
]
```

### List All Transactions

```
GET /api/transactions
```

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "customer": {
      "id": 1,
      "name": "Alice Johnson"
    },
    "amount": 120.00,
    "transactionDate": "2026-01-10"
  },
  {
    "id": 2,
    "customer": {
      "id": 1,
      "name": "Alice Johnson"
    },
    "amount": 75.00,
    "transactionDate": "2026-01-25"
  }
]
```

## Error Handling

The application uses a centralized exception handler (`@RestControllerAdvice`) providing consistent JSON error responses:

| HTTP Status | Exception | Scenario |
|-------------|-----------|----------|
| 400 | `MethodArgumentTypeMismatchException` | Non-numeric customer ID (e.g. `/api/rewards/abc`) |
| 400 | `InvalidTransactionAmountException` | Negative transaction amount |
| 404 | `CustomerNotFoundException` | Customer ID does not exist |
| 404 | `NoResourceFoundException` | Unknown endpoint (e.g. `/api/unknown`) |
| 405 | `HttpRequestMethodNotSupportedException` | Wrong HTTP method (e.g. POST to a GET endpoint) |
| 500 | Generic | Unexpected server error |

## Sample Data

The application preloads sample data on startup (via `data.sql`) with:

- **3 customers:** Alice Johnson, Bob Smith, Charlie Davis
- **17 transactions** spanning January–March 2026
- Covers edge cases: amounts below $50, exactly $50, between $50–$100, and over $100

### Expected Reward Totals

| Customer | January | February | March | Total |
|----------|---------|----------|-------|-------|
| Alice Johnson | 115 | 255 | 60 | 430 |
| Bob Smith | 35 | 200 | 450 | 685 |
| Charlie Davis | 110 | 10 | 200 | 320 |

## H2 Database Console

Available at **http://localhost:8080/h2-console** while the application is running.

- **JDBC URL:** `jdbc:h2:mem:rewardsdb`
- **Username:** `sa`
- **Password:** *(empty)*

| Customer       | Month    | Transactions                | Monthly Points |
|----------------|----------|-----------------------------|----------------|
| Alice Johnson  | January  | $120.00, $75.00             | 115            |
| Alice Johnson  | February | $200.00, $55.50             | 255            |
| Alice Johnson  | March    | $45.00, $105.00             | 60             |
| Bob Smith      | January  | $50.00, $85.00              | 35             |
| Bob Smith      | February | $150.00, $100.00            | 200            |
| Bob Smith      | March    | $300.00, $30.00             | 450            |
| Charlie Davis  | January  | $90.00, $110.00             | 110            |
| Charlie Davis  | February | $60.00                      | 10             |
| Charlie Davis  | March    | $175.00, $25.00             | 200            |

## H2 Console

Access the H2 database console at: **http://localhost:8080/h2-console**

- **JDBC URL:** `jdbc:h2:mem:rewardsdb`
- **Username:** `sa`
- **Password:** *(empty)*

## Running Tests

```bash
mvn test
```
