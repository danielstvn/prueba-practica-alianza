# Test Client

En este archivo se encuentra la información de las configuraciones necesarias para levantar los microservicios de la prueba

(recomendación)
### Proyecto Angular v18

Para levantar el micro-frontend de Angular se debe :
 - Entrar a la carpeta : alianza-client-manager-mf
 - Ejecutar el siguiente comando
   
```bash
# npm run dev
```

## Descripción
Es un proyecto de prueba que contiene un microfrontend de gestión de clientes desarrollado en Angular y un servicio backend desarrollado en Java con Gradle y Java versión 17. Implementa conexión con base de datos PostgreSQL, utilizando migraciones para la gestión del esquema de base de datos.

## Requisitos Previos
- Java 17
- Gradle
- PostgreSQL

## Configuración de Base de Datos ( recomendado )
se comparte la carpeta docker-compose, en la cual se encuentra un script que debe ejecutarse para crear el volumen de la base de datos y el contenedor de postgres.

# Ejecutar en una terminal el comando 

```bash
# sudo ./run-docker-compose-svc.sh
```

este script creara el volumen de la base de datos y ejecutara el archivo docker-compose.yml para crear el contendor de postgresql.

### Creación de esquema
se debe crear el siguiente esquema en la base de datos antes de levantar el microservicio backend:

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
cd cm-ms-be

# Compilar el proyecto
gradle clean build
```

3. Configurar Frontend (Angular)
```bash
# Navegar al directorio del frontend
cd alianza-client-manager-mf

# Instalar dependencias
npm install

```

## Ejecución

### Frontend
```bash
# En el directorio alianza-client-manager-mf
npm run dev
```

## Configuración
- La información de conexión a base de datos está en `backend/src/main/resources/application.yml`

## Estructura del Proyecto
```
test-client/
├── backend/           # Proyecto backend Java
├── frontend/          # Proyecto frontend Angular
├── docker-compose/          # Configuración docker 
└── README.md
```

## Tecnologías
- Backend: Java 17, Gradle, Spring Boot
- Frontend: Angular, TypeScript
- Base de Datos: PostgreSQL
- Migración de Datos: Liquibase
- ORM: Hibernate
```
