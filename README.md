# Account Service (Account_Srv_GLCMME)

A Spring Boot microservice that provides account management capabilities — balance validation and account details retrieval — as part of the Mphasis POC fund transfer system.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.6 |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Build | Maven |

---

## Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL running on `localhost:5432`

---

## Configuration

All configuration lives in [`src/main/resources/application.properties`](src/main/resources/application.properties).

| Property | Default |
|---|---|
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/fundtransfer_db` |
| `spring.datasource.username` | `postgres` |
| `spring.datasource.password` | `postgres` |
| `spring.jpa.hibernate.ddl-auto` | `update` |

Create the database before starting:

```sql
CREATE DATABASE fundtransfer_db;
```

---

## Running the Service

```bash
mvn spring-boot:run
```

The service starts on `http://localhost:8080` by default.

Swagger UI is available at:
```
http://localhost:8080/swagger-ui/index.html
```

---

## API Endpoints

### Validate Balance

Checks whether an account has sufficient funds for a transfer. The result is logged to the `validation_log` table.

```
GET /accounts/validate?accountNumber={accountNumber}&amount={amount}
```

| Parameter | Type | Description |
|---|---|---|
| `accountNumber` | String | The account number to validate |
| `amount` | Double | The transfer amount to check against |

**Response: `BalanceResponse`**

```json
{
  "valid": true,
  "message": "Sufficient balance",
  "currentBalance": 5000.00
}
```

---

### Get Account Details

Fetches account and user information for the given user ID.

```
GET /accounts/details/{userId}
```

| Parameter | Type | Description |
|---|---|---|
| `userId` | UUID | The user's unique identifier |

**Response: `AccountDetailsResponse`**

```json
{
  "userId": "...",
  "username": "john_doe",
  "email": "john@example.com",
  "accountNumber": "ACC123456",
  "accountType": "SAVINGS",
  "currentBalance": 5000.00,
  "currency": "SGD",
  "bankName": "Example Bank",
  "ifsc": "EXBK0001234",
  "status": "ACTIVE"
}
```

---

### Get Account Statement

Returns the account's current balance along with all transactions (both debits and credits that touch the account), sorted by time ascending.

```
GET /api/v1/accounts/{account_id}/stmt
```

**Headers**

| Header | Value |
|---|---|
| `Authorization` | `Basic <base64(username:password)>` |

> Note: Spring Security is not yet wired up, so the auth header is currently accepted but not enforced.

| Parameter | Type | Description |
|---|---|---|
| `account_id` | String | The account's unique identifier |

**Example**

```bash
curl -X GET "http://localhost:8080/api/v1/accounts/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11/stmt" \
  -H "Accept: application/json" \
  -u "username:password"
```

**Response: `AccountStatementResponse`**

```json
{
  "currentBalance": 90000.00,
  "sortedByTimeTransactions": [
    {
      "transactionId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "srcAccNo": "AC1001234567",
      "destAccNo": "AC1002345678",
      "type": "CREDIT",
      "amount": 2000.00,
      "netBalance": 86000.00,
      "timeStamp": 1212212.2
    },
    {
      "transactionId": "9c5b94b1-35ad-49bb-b118-8e8fc24abf80",
      "srcAccNo": "AC1001234567",
      "destAccNo": "AC1002345678",
      "type": "DEBIT",
      "amount": 2000.00,
      "netBalance": 84000.00,
      "timeStamp": 1212220.2
    }
  ]
}
```

- `netBalance` is the transaction's `balance_after` value.
- `timeStamp` is the transaction's creation time as epoch seconds.
- `sortedByTimeTransactions` is omitted when the account has no transactions.

---

## Project Structure

```
src/main/java/com/example/poc/
├── PocApplication.java           # Application entry point
├── controller/
│   └── AccountController.java    # REST endpoints
├── service/
│   └── AccountService.java       # Business logic
├── entity/
│   ├── Account.java              # Account JPA entity
│   ├── User.java                 # User JPA entity
│   ├── Transaction.java          # Transaction JPA entity
│   └── ValidationLog.java        # Audit log entity
├── dto/
│   └── response/
│       ├── AccountDetailsResponse.java
│       ├── AccountStatementResponse.java
│       ├── TransactionResponse.java
│       └── BalanceResponse.java
└── repository/
    ├── AccountRepository.java
    ├── UserRepository.java
    ├── TransactionRepository.java
    └── ValidationLogRepository.java
```

---

## Building

```bash
# Run tests
mvn test

# Package as JAR
mvn clean package

# Run the JAR directly
java -jar target/*.jar
```