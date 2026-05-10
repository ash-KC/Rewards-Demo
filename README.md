# Customer Rewards Program

A Spring Boot REST API that calculates reward points for a retailer's customers based on their purchase transactions.

## Points Calculation

- **2 points** for every dollar spent **over $100** in each transaction
- **1 point** for every dollar spent **between $50 and $100** in each transaction

**Example:** A $120 purchase = 2×$20 + 1×$50 = **90 points**

## Tech Stack

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- H2 In-Memory Database
- Maven

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+

### Build & Run

```bash
# Clone the repository
git clone https://github.com/<your-username>/rewards-program.git
cd rewards-program

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application starts on **http://localhost:8080**.

## API Endpoints

### Get Rewards for All Customers

```
GET /api/rewards
```

Returns monthly reward points and total for each customer.

**Sample Response:**

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
  },
  {
    "customerId": 2,
    "customerName": "Bob Smith",
    "monthlyRewards": {
      "January 2026": 35,
      "February 2026": 200,
      "March 2026": 450
    },
    "totalRewards": 685
  }
]
```

### Get Rewards for a Specific Customer

```
GET /api/rewards/{customerId}
```

### List All Customers

```
GET /api/customers
```

### List All Transactions

```
GET /api/transactions
```

## Sample Data

The application loads sample data on startup with 3 customers and 17 transactions spanning January–March 2026.

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
