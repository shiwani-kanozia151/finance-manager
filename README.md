# Finance Manager Backend

GitHub repository: https://github.com/shiwani-kanozia151/finance-manager

## Overview

Finance Manager is an Express backend for managing users, role-based access, financial records, and dashboard analytics. It uses Prisma ORM for database access and JWT for authentication.

## Tech Stack

- Node.js
- Express
- Prisma
- JWT
- bcrypt
- PostgreSQL / MySQL / SQLite (via Prisma)

## Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/shiwani-kanozia151/finance-manager.git
   cd finance-manager
   ```

2. Install dependencies:

   ```bash
   npm install
   ```

3. Create a `.env` file from the example:

   ```bash
   copy .env.example .env
   ```

4. Update `.env` with your values:

   - `PORT` — the port the server will use
   - `JWT_SECRET` — a secure secret for signing tokens
   - `DATABASE_URL` — your Prisma database connection string

5. Generate Prisma client files:

   ```bash
   npx prisma generate
   ```

6. Apply database migrations:

   ```bash
   npx prisma migrate dev --name init
   ```

7. Start the application:

   ```bash
   npm start
   ```

The API will be available at `http://localhost:5000` by default.

## API Endpoints

### User Routes

- `POST /api/users/register` — Register a new user
- `POST /api/users/login` — Login user

### Record Routes

- `POST /api/records` — Create a new financial record (requires auth)
- `GET /api/records` — Get all financial records (requires auth)

### Dashboard Routes

- `GET /api/dashboard/summary` — Get financial summary (requires auth)

## Deployment Notes

- Ensure your production database URL is set in `DATABASE_URL`.
- Use a strong value for `JWT_SECRET`.
- On deployment platforms, set environment variables rather than committing secrets.
- If using a cloud provider, run the Prisma migration command after the database is available.

## Project Structure

- `server.js` — app entry point
- `src/controllers` — route controllers
- `src/routes` — route definitions
- `src/middleware` — authentication and role middleware
- `src/config/prisma.js` — Prisma client configuration
- `prisma/schema.prisma` — database schema

## Notes

This project is designed with scalability and clean architecture in mind, focusing on separation of concerns and maintainability.