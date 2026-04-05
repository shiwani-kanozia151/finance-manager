# Finance Manager Backend

## Features
- Role-based access control
- Financial record management
- Dashboard analytics

## Tech Stack
- Node.js
- Express
- Prisma
- JWT

## Setup Steps
1. git clone 
2. npm install
3. npx prisma generate
4. npx prisma migrate dev
5. npm start

## API Endpoints

### User Routes
- POST /api/users/register - Register a new user
- POST /api/users/login - Login user

### Record Routes
- POST /api/records - Create a new financial record (requires auth)
- GET /api/records - Get all financial records (requires auth)

### Dashboard Routes
- GET /api/dashboard/summary - Get financial summary (requires auth)

This project is designed with scalability and clean architecture in mind, focusing on separation of concerns and maintainability.