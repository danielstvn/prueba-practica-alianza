# Test Client

En este archivo se encuentra la información de las configuraciones necesarias para levantar los microservicios de la prueba

(recomendación)
### Proyecto Angular v18
- ejecutar con comando ## npm run dev

## Descripción
Es un proyecto de prueba que contiene un microfrontend de gestión de clientes desarrollado en Angular y un servicio backend desarrollado en Java con Gradle y Java versión 17. Implementa conexión con base de datos PostgreSQL, utilizando migraciones para la gestión del esquema de base de datos.

## Requisitos Previos
- Java 17
- Gradle
- PostgreSQL

## Configuración de Base de Datos
Se comparte archivo docke-compose.yml con la configuración incial de la base de datos postgresql :

### Configuración base de datos implementando la base de datos configurada en el microservicio ( recomendado )
se debe crear el siguiente esquema :

```bash
# crear esquema test_cm
```

### Configuración Base de Datos nueva y Esquema ( omitir si se aplico la configuración anterior)
```bash
# Iniciar sesión en PostgreSQL
psql -U postgres

# Crear base de datos
CREATE DATABASE bytedb;

# Conectarse a la base de datos
\c bytedb

# Crear esquema
CREATE SCHEMA test_cm;

# Crear usuario
CREATE USER byte_adm WITH PASSWORD 'b1t3_4dm';

# Asignar privilegios
GRANT ALL PRIVILEGES ON DATABASE bytedb TO byte_adm;
GRANT USAGE, CREATE ON SCHEMA test_cm TO byte_adm;
```

### Detalles de Conexión
- **Base de Datos**: bytedb
- **Esquema**: test_cm
- **Usuario**: byte_adm
- **Puerto**: 5432
- **Host**: localhost

## Instalación
1. Clonar el repositorio
```bash
git clone git@github.com:danielstvn/prueba-practica-alianza.git
cd prueba-practica-alianza
```

2. Configurar Backend (Java)
```bash
# Navegar al directorio del backend
cd backend

# Compilar el proyecto
gradle clean build
```

3. Configurar Frontend (Angular)
```bash
# Navegar al directorio del frontend
cd frontend

# Instalar dependencias
npm install
```

## Ejecución

### Backend
```bash
# En el directorio backend
gradle bootRun
```

### Frontend
```bash
# En el directorio frontend
npm run dev
```

## Configuración
- La información de conexión a base de datos está en `backend/src/main/resources/application.yml`

## Estructura del Proyecto
```
test-client/
├── backend/           # Proyecto backend Java
├── frontend/          # Proyecto frontend Angular
└── README.md
```

## Tecnologías
- Backend: Java 17, Gradle, Spring Boot
- Frontend: Angular, TypeScript
- Base de Datos: PostgreSQL
- Migración de Datos: Liquibase
- ORM: Hibernate
```
