# Requisites - Giv's Money Management

## Endpoints
### Users and authentication

#### `POST /api/v1/register`

Register a user.

Request body:

```json
{
  "email": "manager@example.com",
  "name": "Alex Manager",
  "password": "a-strong-password"
}
```

Returns `201 Created` with the created user, excluding the password.

#### `POST /api/v1/login`

Authenticate a user.

Request body:

```json
{
  "email": "manager@example.com",
  "password": "a-strong-password"
}
```

Returns `200 OK` with an access token:

```json
{
  "accessToken": "access-token",
  "tokenType": "Bearer"
}
```

Invalid credentials or an inactive user result in `401 Unauthorized`.

### Categories

Category endpoints require authentication. A user can only access their own
categories.

#### `POST /api/v1/categories`

Create a category for the authenticated user.

Request body:

```json
{
  "name": "Salary"
}
```

Returns `201 Created` with the created category. The server assigns `id` and
`userId`.

#### `GET /api/v1/categories`

Returns `200 OK` with an array of the authenticated user's categories.

### Incomes

Income endpoints require authentication. Send the access token in the
`Authorization: Bearer <accessToken>` header. Users can only access their own
incomes.

#### `POST /api/v1/incomes`

Create an income for the authenticated user.

Request body:

```json
{
  "description": "Monthly salary",
  "amount": 5000.00,
  "categoryId": "e12e60b1-52d3-4f34-9192-32c8b23c3a14",
  "type": "FIXED",
  "date": "2026-11-18"
}
```

`categoryId` is optional; when provided, it must identify a category belonging
to the authenticated user. Returns `201 Created` with the created income. The
server assigns `id`, `userId`, `createdAt`, and `updatedAt`.

#### `GET /api/v1/incomes`

Returns `200 OK` with an array of the authenticated user's incomes.

#### `GET /api/v1/incomes/{id}`

Returns `200 OK` with the requested income, or `404 Not Found` if it does not
exist or belongs to another user.

#### `PUT /api/v1/incomes/{id}`

Replace the editable fields of an income belonging to the authenticated user.
The request body has the same fields as the income creation request. Omit
`categoryId` or set it to `null` to remove the category.

Returns `200 OK` with the updated income, or `404 Not Found` if it does not
exist or belongs to another user. The server updates `updatedAt`.

#### `DELETE /api/v1/incomes/{id}`

Delete an income belonging to the authenticated user. Returns `204 No Content`,
or `404 Not Found` if it does not exist or belongs to another user.

### Common behavior

- Protected endpoints require the bearer token returned by login; a missing or
  invalid token results in `401 Unauthorized`.
- Invalid request fields result in `400 Bad Request`.
- Monetary amounts are decimal values, not binary floating-point values.
- Dates use the `YYYY-MM-DD` format. Creation and update timestamps use ISO 8601.

## Entities

Entity fields marked as required must be present in persisted records; fields
assigned by the server are not required in creation requests.

### User

| Property | Type | Required |
| --- | --- | --- |
| id | UUID | Yes |
| name | String | Yes |
| email | String | Yes |
| password | String | Yes |
| createdAt | Date | Yes |
| updatedAt | Date | Yes |
| isActive | Boolean | Yes |

The password is stored as a secure password hash and is never returned in API
responses.

### Income

| Property | Type | Required |
| --- | --- | --- |
| id | UUID | Yes |
| userId | UUID | Yes |
| description | String | Yes |
| amount | Decimal | Yes |
| categoryId | UUID | No |
| type | IncomeType | Yes |
| date | Date | Yes |
| createdAt | Date | Yes |
| updatedAt | Date | Yes |

`IncomeType` accepts `FIXED`, `VARIABLE`, or `EXTRA`.

### Category

| Property | Type | Required |
| --- | --- | --- |
| id | UUID | Yes |
| userId | UUID | Yes |
| name | String | Yes |

Every category belongs to a user. See [AGENTS.md](AGENTS.md) for the income
business rules.

