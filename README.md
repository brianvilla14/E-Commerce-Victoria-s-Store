# Victoria's Store E-Commerce

Proyecto full stack con frontend Vue 3/Vite, backend Spring Boot y persistencia MySQL.

## Requisitos

- Node.js 18+
- Java 17+
- MySQL 8+

## Frontend

```bash
cd frontend
npm ci
npm run dev
```

Variables disponibles:

- `VITE_API_URL`: ruta base usada por el navegador. Por defecto: `/api`.
- `VITE_BACKEND_URL`: destino del proxy local de Vite. Por defecto: `http://localhost:8080`.

## Backend

```bash
cd backend
gradlew.bat bootRun
```

Variables disponibles:

- `DB_URL`: JDBC URL de MySQL. Por defecto: `jdbc:mysql://localhost:3306/practicasii?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`.
- `DB_USERNAME`: usuario de MySQL. Por defecto: `root`.
- `DB_PASSWORD`: password de MySQL. Por defecto: vacío.
- `JPA_DDL_AUTO`: estrategia de Hibernate. Por defecto: `update`.
- `JPA_SHOW_SQL`: logs SQL. Por defecto: `false`.
- `CORS_ALLOWED_ORIGINS`: origenes permitidos separados por coma. Por defecto: `http://localhost:5173,http://127.0.0.1:5173`.

## Base de Datos

El dump incluido es `proyecto.ecomerce.VICTORIASTORE.sql` y corresponde a la base `practicasii`.
